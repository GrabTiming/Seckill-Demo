package com.Ljt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.Ljt.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:53
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    
}

