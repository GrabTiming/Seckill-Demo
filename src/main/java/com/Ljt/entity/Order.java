package com.Ljt.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Order)表实体类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order")
public class Order  {
    
    @TableId
    private Long id;

    private Long userId;
    
    private Long goodsId;
    
    //0未支付，1已支付，2已发货，3已收货，4已退款
    private Integer status;
    
    //收获地址
    private String address;
    
    private Date createTime;
    
    
}
