package com.Ljt.constants;

public class MqConstant {

    public static final String SECKILL_TOPIC = "seckillTopic";
    public static final String ORDER_CREATE_TAG = "orderCreate";
    public static final String ORDER_CANCEL_TAG = "orderCancel";

    //设置消息延迟级别，从1开始，比如说14，对应就是延时10分钟
    // "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
    public static final int DELDAY_LEVEL = 5;

}
