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
    ResponseResult getPage(String pageId);
    ResponseResult updatePage(String pageId, Page page);
    ResponseResult deletePage(String pageId);
}
