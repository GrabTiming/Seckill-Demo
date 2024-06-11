package com.Ljt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Ljt.mapper.GoodsMapper;
import com.Ljt.entity.Goods;
import com.Ljt.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Goods)表服务实现类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:25
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {



    @Override
    public List<Goods> getList() {

        return this.baseMapper.selectList(null);

    }
}

