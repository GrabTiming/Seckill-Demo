package com.Ljt.util;

import com.Ljt.constants.SeckillConstant;

public class LockUtil {

    private LockUtil(){

    }

    //秒杀商品的时候需要用到的锁，由用户id和商品id唯一确定
    public static String getSeckillLock(Long goodsId,Long userId)
    {
        return SeckillConstant.SECKILL_LOCK+goodsId+":"+userId;
    }
}
