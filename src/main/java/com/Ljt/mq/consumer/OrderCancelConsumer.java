package com.Ljt.mq.consumer;

import com.Ljt.constants.MqConstant;
import com.Ljt.mq.message.SeckillMessage;
import com.Ljt.service.SeckillService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 负责处理 删除订单
 */
@Component
@RocketMQMessageListener(consumerGroup = "orderCancelConsumerGroup",
        topic = MqConstant.SECKILL_TOPIC,//topic
        selectorExpression = MqConstant.ORDER_CANCEL_TAG,//tag
        messageModel = MessageModel.BROADCASTING, //消息模式
        consumeMode= ConsumeMode.CONCURRENTLY)
@Slf4j
public class OrderCancelConsumer implements RocketMQListener<String> {

    @Autowired
    private SeckillService seckillService;
    public static final Logger logger = LoggerFactory.getLogger(SeckillConsumer.class);
    @Override
    public void onMessage(String message) {

        DateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("接收时间：{},删除订单消息:{}", format.format(new Date()),message);

        SeckillMessage orderMessage = JSON.parseObject(message, SeckillMessage.class);
        seckillService.executeOrderCancel(orderMessage.getGoodsId(), orderMessage.getUserId());

    }
}
