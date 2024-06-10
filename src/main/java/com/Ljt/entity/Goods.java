package com.Ljt.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Goods)表实体类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods  {
    
    @TableId
    private Long id;

    private String goodsName;
    
    private Double price;
    
    private Integer stock;
    
    
}
