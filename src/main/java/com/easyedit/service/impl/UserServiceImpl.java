package com.easyedit.service.impl;

import com.easyedit.entity.User;
import com.easyedit.mapper.UserMapper;
import com.easyedit.service.UserService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserMapper userMapper;
 
    @Override
    public ResponseResult createUser(User user) {
        user.setName(user.getName());
        
        int r = userMapper.insert(user);
        if (r <= 0) {
            return ResponseResult.errorResult(
                ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return ResponseResult.okResult(user).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getUser(String userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userId);

        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return ResponseResult.setAppHttpCodeEnum(
                ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(
                    HttpServletResponse.SC_NOT_FOUND);
        }
        return ResponseResult.okResult(user).HttpCode(HttpServletResponse.SC_OK);
    }

}
