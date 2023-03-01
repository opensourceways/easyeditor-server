package com.easyedit.service.impl;

import com.easyedit.entity.User;
import com.easyedit.mapper.UserMapper;
import com.easyedit.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhongjun
 * @since 2023-03-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
