package com.Ljt.controller;

import com.Ljt.entity.Result;
import com.Ljt.mq.producer.SeckillProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocketmq")
public class MqController {

    @Autowired
    private SeckillProducer producer;

    //用来测试rocketmq是否正常工作
    @PostMapping("/send")
    public Result sendMsg()
    {

        String topic = "SeckillTopic1";
        String msg = "测试rocketmq是否正常运行";
        producer.sendMessage(topic,msg);
        return Result.ok(null);
    }

}
