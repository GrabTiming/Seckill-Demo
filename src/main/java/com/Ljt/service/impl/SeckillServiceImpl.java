package com.Ljt.service.impl;

import com.Ljt.constants.MqConstant;
import com.Ljt.constants.SeckillConstant;
import com.Ljt.dto.SeckillDTO;
import com.Ljt.entity.Goods;
import com.Ljt.entity.Order;
import com.Ljt.entity.Result;
import com.Ljt.mq.message.OrderCancelMessage;
import com.Ljt.mq.message.SeckillMessage;
import com.Ljt.mq.producer.OrderCancelProducer;
import com.Ljt.mq.producer.SeckillProducer;
import com.Ljt.service.GoodsService;
import com.Ljt.service.OrderService;
import com.Ljt.service.SeckillService;
import com.Ljt.util.LockUtil;
import com.Ljt.util.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    public static final Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SeckillProducer producer;

    @Autowired
    private OrderCancelProducer orderCancelProducer;



    @Override
    public Result executeSeckill(SeckillDTO seckillDTO) {

        Long goodsId = seckillDTO.getGoodsId();
        Long userId = seckillDTO.getUserId();
        //1. 判断是否在活动范围内

        Goods goods = goodsService.getById(goodsId);
        Date now = new Date();
        //未开始
        if(goods.getStartTime().after(now)){
            return Result.error(414,SeckillConstant.SECKILL_BEFORE);
        }
        //已结束
        if(goods.getEndTime().before(now)){
            return Result.error(414,SeckillConstant.SECKILL_AFTER);
        }

        //2. 判断是否重复下单
        if(redisCache.containsCacheSet(SeckillConstant.SECKILL_SUCCESS_USER+goodsId.toString(),userId))
        {
            return Result.error(414,SeckillConstant.REPEATE_ORDER);
        }

        //-----------------------扔进消息队列异步处理------------------------//
        //等启动好RocketMQ后回来
        //跑通RocketMQ

        //发送消息
        SeckillMessage seckillMessage = new SeckillMessage(userId, goodsId);
        producer.sendMessage(seckillMessage);

        return Result.ok(null);

    }

    @Transactional
    public Result executeSeckillForCustomer(Long goodsId,Long userId)
    {
        //尝试 扣减库存 + 创建订单
        RLock lock = redissonClient.getLock(LockUtil.getSeckillLock(goodsId,userId));
        boolean isLock = false;
        try {
            //3秒都给多了，秒杀3秒钟都没了。。。
            isLock = lock.tryLock(3, TimeUnit.SECONDS);
            if(isLock){
                try{

                    SeckillService proxy = (SeckillService) AopContext.currentProxy();
                    return proxy.createOrder(userId,goodsId);

                }finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.ok(null);
    }

    @Transactional
    public Result createOrder(Long userId,Long goodsId)
    {

        //判断是否已经下过单
//        Long count = orderService.count(new LambdaQueryWrapper< Order >()
//                .eq(Order::getUserId,userId)
//                .eq(Order::getGoodsId,goodsId));
//        if(count>=1) {
//            throw new RuntimeException("已经下单");
//        }
        //改用redis判断是否下过单
        if(redisCache.containsCacheSet(SeckillConstant.SECKILL_SUCCESS_USER+goodsId,userId)){
            throw new RuntimeException(SeckillConstant.REPEATE_ORDER);
        }

        //3. 尝试扣减库存
        boolean flag = goodsService.update(new LambdaUpdateWrapper<Goods>()
                .eq(Goods::getId,goodsId)
                .gt(Goods::getStock,0)
                .setSql("stock = stock -1"));
        if(!flag)
        {
            logger.info("秒杀失败");
            return Result.error(415,"秒杀失败");
        }

        redisCache.setCacheSet(SeckillConstant.SECKILL_SUCCESS_USER+goodsId,userId);

        //创建订单并保存
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setGoodsId(goodsId);
        newOrder.setStatus(0);
        newOrder.setCreateTime(new Date());
        orderService.save(newOrder);
        //发送延时消息
        orderCancelProducer.sendDelayMessage(new OrderCancelMessage(userId,goodsId), MqConstant.DELDAY_LEVEL);
        logger.info("秒杀成功");
        return Result.ok("秒杀成功,请在10分钟内支付订单",null);
    }


    @Override
    public Result executeOrderCancel(Long goodsId, Long userId) {

        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        //删除订单
        queryWrapper.eq(Order::getUserId,userId)
                .eq(Order::getGoodsId,goodsId)
                .eq(Order::getStatus,SeckillConstant.ORDER_UNPAID);
        int isDelete = orderService.getBaseMapper().delete(queryWrapper);

        if(isDelete>0)
        {
            //删除下单标记
            redisCache.deleteCacheSetValue(SeckillConstant.SECKILL_SUCCESS_USER+goodsId,userId);

            //库存+1
            boolean flag = goodsService.update(new LambdaUpdateWrapper<Goods>()
                    .eq(Goods::getId,goodsId)
                    .gt(Goods::getStock,0)
                    .setSql("stock = stock +1"));

            return Result.ok("订单超时未支付，已取消订单",null);
        }
        return Result.ok(null);
    }

}
