package com.easyedit.service;

import com.easyedit.entity.Page;
import com.easyedit.entity.Pagetree;
import com.easyedit.util.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhongjun
 * @since 2023-02-28
 */
public interface PagetreeService extends IService<Pagetree> {

    void createPagetree(Page page);
    Integer deletePagetree(String pageId);
    void updatePagetree(Page page);
}
