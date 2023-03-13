package com.easyedit.service;

import com.easyedit.entity.Page;
import com.easyedit.entity.Pagetree;
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

    boolean createPagetree(Page page);
    boolean updatePagetree(Page page);
    boolean deletePagetreeById(Integer pageId);
    boolean deletePagetreeByName(String path, String name);

}
