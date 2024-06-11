package com.Ljt.controller;

import com.Ljt.dto.SeckillDTO;
import com.Ljt.entity.Result;
import com.Ljt.service.GoodsService;
import com.Ljt.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill")
public class SeckillController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillService seckillService;

    // 查询所有秒杀商品
    @GetMapping("/getList")
    public Result getList()
    {
        return Result.ok(goodsService.getList());
    }


    // 进行秒杀操作
    @PostMapping("/execute")
    public Result seckillWork(@RequestBody SeckillDTO seckillDTO)
    {
        return seckillService.executeSeckill(seckillDTO);
    }
}
