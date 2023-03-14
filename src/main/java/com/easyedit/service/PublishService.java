package com.easyedit.service;

import com.easyedit.entity.Publish;
import com.easyedit.util.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangweifeng
 * @since 2023-03-14
 */
public interface PublishService extends IService<Publish> {
    
    ResponseResult createPublish(String path);
    ResponseResult getPublish(String path, Integer version);
    ResponseResult getVersions(String path);
    ResponseResult setVersion(String path, Integer version);

}
