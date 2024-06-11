package com.Ljt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.Ljt.entity.Goods;

import java.util.List;

/**
 * (Goods)表服务接口
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:25
 */
public interface GoodsService extends IService<Goods> {

    List<Goods> getList();
}

