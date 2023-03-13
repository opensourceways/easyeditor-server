package com.easyedit.service;

import com.easyedit.entity.Page;
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
public interface PageService extends IService<Page> {

    ResponseResult createPage(Page page);
    ResponseResult getPage(Integer pageId, String path, String name);
    ResponseResult updatePage(Integer pageId, String path, String name, Page page);
    ResponseResult deletePage(Integer pageId, String path, String name);

}
