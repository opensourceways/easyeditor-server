package com.easyedit.service.impl;

import com.easyedit.entity.Page;
import com.easyedit.entity.Pagetree;
import com.easyedit.mapper.PageMapper;
import com.easyedit.mapper.PagetreeMapper;
import com.easyedit.service.PagetreeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhongjun
 * @since 2023-02-28
 */
@Service
public class PagetreeServiceImpl extends ServiceImpl<PagetreeMapper, Pagetree> implements PagetreeService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private PagetreeMapper pagetreeMapper;

    @Override
    public void createPagetree(Page page) {
        Pagetree pageTree = new Pagetree();
        pageTree.setIsPrivate(page.getIsPrivate());
        pageTree.setPageId(page.getId());

        if (page.getFolder().equals("/")) {
            pageTree.setDepth(1);
            String ancestors = "/"; 
            pageTree.setAncestors(ancestors);
        } else {
            Integer parentId = findParent(page.getPath(), page.getFolder());
            Pagetree parentTree = pagetreeMapper.selectById(parentId);
            parentTree.setIsFolder(true);
            pagetreeMapper.updateById(parentTree);

            pageTree.setIsFolder(false);
            pageTree.setDepth(parentTree.getDepth() + 1);
            pageTree.setParent(parentId);
            if (parentTree.getAncestors().equals("/")) {
                pageTree.setAncestors(parentTree.getAncestors() + parentId);
            } else {
                pageTree.setAncestors(parentTree.getAncestors() + "/" + parentId);
            }
        }

        pagetreeMapper.insert(pageTree);
    }
 
    private Integer findParent(String path, String folder) {
        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        wrapper.eq("path", path).eq("full_name", folder);
        
        Page parent = pageMapper.selectOne(wrapper);
        return parent.getId();
    }

    @Override
    public boolean deletePagetree(String pageId) {
        Pagetree pagetree = pagetreeMapper.selectById(pageId);
        if (pagetree.getIsFolder() == true) {
            return false;
        }

        Integer parentId = pagetree.getParent();
        pagetreeMapper.deleteById(pageId);
        
        QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
        wrapper.eq("parent", parentId);
        List<Pagetree> sons= pagetreeMapper.selectList(wrapper);
        if (sons.size() == 0) {
            Pagetree parent = new Pagetree();
            parent.setId(parentId);
            parent.setIsFolder(false);
            pagetreeMapper.updateById(parent);
        }
        return true;
    }

    @Override
    public void updatePagetree(Page page) {
    }
}
