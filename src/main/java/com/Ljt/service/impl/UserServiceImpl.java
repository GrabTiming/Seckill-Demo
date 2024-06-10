package com.Ljt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Ljt.mapper.UserMapper;
import com.Ljt.entity.User;
import com.Ljt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author Liang2003
 * @since 2024-06-10 16:49:53
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

