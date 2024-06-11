package com.Ljt.service;

import com.Ljt.dto.SeckillDTO;
import com.Ljt.entity.Result;
import org.springframework.stereotype.Service;

public interface SeckillService {

    Result executeSeckill(SeckillDTO seckillDTO);


    Result createOrder(Long userId,Long goodsId);

}