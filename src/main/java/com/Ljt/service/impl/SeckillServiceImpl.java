package com.Ljt.service.impl;

import com.Ljt.constants.SeckillConstant;
import com.Ljt.dto.SeckillDTO;
import com.Ljt.entity.Goods;
import com.Ljt.entity.Order;
import com.Ljt.entity.Result;
import com.Ljt.service.GoodsService;
import com.Ljt.service.OrderService;
import com.Ljt.service.SeckillService;
import com.Ljt.util.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedissonClient redissonClient;

    @Transactional
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

        //尝试 扣减库存 + 创建订单
        RLock lock = redissonClient.getLock(SeckillConstant.SECKILL_LOCK+ userId);
        boolean isLock = false;
        try {
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
        return Result.error(416,"秒杀失败");
        //return Result.ok("秒杀成功，请在10分钟内支付订单",null);
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
        if(redisCache.containsCacheSet(SeckillConstant.SECKILL_SUCCESS_USER+goodsId,userId)){
            throw new RuntimeException(SeckillConstant.REPEATE_ORDER);
        }

        //3. 尝试扣减库存
        boolean flag = goodsService.update(new LambdaUpdateWrapper<Goods>()
                .eq(Goods::getId,goodsId)
                .gt(Goods::getStock,0)
                .setSql("stock = stock -1"));
        if(!flag) return Result.error(415,"秒杀失败");

        redisCache.setCacheSet(SeckillConstant.SECKILL_SUCCESS_USER+goodsId,userId);
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setGoodsId(goodsId);
        newOrder.setStatus(0);
        newOrder.setCreateTime(new Date());
        orderService.save(newOrder);
        return Result.ok("秒杀成功,请在10分钟内支付订单",null);
    }

}
