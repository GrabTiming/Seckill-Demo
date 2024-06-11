package com.Ljt.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    
    private String password;
    
    
}
