package com.Ljt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.Ljt.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Goods)表数据库访问层
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:25
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    
    
}

