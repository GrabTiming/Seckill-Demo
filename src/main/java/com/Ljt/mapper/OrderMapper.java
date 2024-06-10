package com.Ljt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.Ljt.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Order)表数据库访问层
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:43
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    
    
}

