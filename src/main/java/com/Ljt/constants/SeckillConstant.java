package com.Ljt.constants;

public class SeckillConstant {

    public static final String SECKILL_SUCCESS_USER = "seckill_access_user:";

    public static final String SECKILL_BEFORE = "活动还未开始";
    public static final String SECKILL_AFTER = "活动已经结束";

    public static final String REPEATE_ORDER = "不允许重复下单";

    public static final String SECKILL_LOCK = "seckill_lock:";

    //订单未支付状态
    public static final int ORDER_UNPAID = 0;
    //订单已支付状态
    public static final int ORDER_PAID = 1;
    //订单已发货状态
    public static final int ORDER_SEND_GOODS = 2;
    //订单已收货状态
    public static final int ORDER_RECEIVED_GOODS = 3;

}
