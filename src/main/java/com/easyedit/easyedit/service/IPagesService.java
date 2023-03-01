package com.easyedit.easyedit.service;

import com.easyedit.easyedit.entity.Pages;
import com.easyedit.easyedit.util.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhongjun
 * @since 2023-02-28
 */
public interface IPagesService extends IService<Pages> {

    ResponseResult createPages(Pages pages);
    ResponseResult getPage(String pageId);

}
