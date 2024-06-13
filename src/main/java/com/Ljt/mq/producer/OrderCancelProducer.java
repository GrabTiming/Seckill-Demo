package com.Ljt.mq.producer;

import com.Ljt.constants.MqConstant;
import com.Ljt.mq.message.OrderCancelMessage;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送延时消息的订单
 */
@Component
@Slf4j
public class OrderCancelProducer {

    private static Logger logger = LoggerFactory.getLogger(OrderCancelProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    //发送普通消息的示例

    @Value("${seckill.topic}")
    private String topic ;


    public void sendDelayMessage(OrderCancelMessage message,int delayLevel)
    {

        //设置消息延迟级别，从1开始，我这里设置14，对应就是延时10分钟
        // "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        DateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("发送时间：{}", format.format(new Date()));
        rocketMQTemplate.syncSend(topic+":"+ MqConstant.ORDER_CANCEL_TAG, MessageBuilder.withPayload(message).build(), 2000, delayLevel);

        /*
         * 自定义延长时间，需要rocketmq版本在5.0及以上，然后rocketmq-starter依赖要在2.2以上。
         */
        //按秒，参数: 字符串"topic:tag",消息内容，延迟时间
//        rocketMQTemplate.syncSendDelayTimeSeconds(topic,message,600);
        //还有各按毫秒的 syncSendDelayTimeMills ，用法相同
    }

}
