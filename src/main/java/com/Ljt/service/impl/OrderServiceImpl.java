package com.Ljt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Ljt.mapper.OrderMapper;
import com.Ljt.entity.Order;
import com.Ljt.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * (Order)表服务实现类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:43
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}

