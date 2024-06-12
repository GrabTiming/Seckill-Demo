package com.Ljt.mq.consumer;

import com.Ljt.constants.MqConstant;

import com.Ljt.mq.message.SeckillMessage;
import com.Ljt.service.SeckillService;

import com.alibaba.fastjson.JSONObject;
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

@Component
@RocketMQMessageListener(consumerGroup = "seckillConsumerGroup", topic = MqConstant.SECKILL_TOPIC,messageModel = MessageModel.BROADCASTING, consumeMode= ConsumeMode.CONCURRENTLY)
@Slf4j
public class SeckillConsumer implements RocketMQListener<String> {

    @Autowired
    private SeckillService seckillService;
    public static final Logger logger = LoggerFactory.getLogger(SeckillConsumer.class);
    @Override
    public void onMessage(String message) {
        logger.info("Received message : "+ message);

        SeckillMessage orderMessage = JSON.parseObject(message, SeckillMessage.class);
        seckillService.executeSeckillForCustomer(orderMessage.getGoodsId(), orderMessage.getUserId());


    }
}
