package com.easyedit.service;

import com.easyedit.entity.User;
import com.easyedit.util.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhongjun
 * @since 2023-03-01
 */
public interface UserService extends IService<User> {

    ResponseResult createUser(User user);
    ResponseResult getUser(String userId);
}
