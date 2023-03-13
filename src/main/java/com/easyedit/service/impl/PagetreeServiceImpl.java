package com.easyedit.service.impl;

import com.easyedit.entity.Page;
import com.easyedit.entity.Pagetree;
import com.easyedit.mapper.PagetreeMapper;
import com.easyedit.service.PagetreeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
    private PagetreeMapper pagetreeMapper;

    @Override
    public boolean createPagetree(Page page) {
        QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
        String path = page.getPath();
        String[] allNames = page.getName().split("/");
        final String GLUECHAR = "/";
        String varName = "";
        Integer depth = 1;
        Integer parent = -1;
        List<Integer> ancestors = new ArrayList<Integer>();
        for (String name : allNames) {
            varName = varName + name;
            wrapper.eq("path", path).eq("name", varName);
            Pagetree node = pagetreeMapper.selectOne(wrapper);
            if (node == null) { // 不存在则新建
                Pagetree nodeNoPage = new Pagetree();
                nodeNoPage.setPath(path);
                nodeNoPage.setName(varName);
                nodeNoPage.setDepth(depth);
                nodeNoPage.setIsPrivate(false);
                if (parent > 0) {
                    nodeNoPage.setParent(parent);
                }
                nodeNoPage.setAncestors(ancestors);
                if (page.getName().equals(varName)) { // 设置pageId
                    nodeNoPage.setIsPrivate(page.getIsPrivate());
                    nodeNoPage.setIsFolder(false);
                    nodeNoPage.setPageId(page.getId());
                } else {
                    nodeNoPage.setIsFolder(true);
                }
                
                int r = pagetreeMapper.insert(nodeNoPage);
                if (r <= 0) {
                    return false;
                }
                parent = nodeNoPage.getId();
                ancestors.add(nodeNoPage.getId());
            } else {
                if (page.getName().equals(varName)) { // 设置pageId
                    node.setIsPrivate(page.getIsPrivate());
                    node.setPageId(page.getId());

                    int r = pagetreeMapper.updateById(node);
                    if (r <= 0) {
                        return false;
                    }
                } else {
                    node.setIsFolder(true);
                    int r = pagetreeMapper.updateById(node);
                    if (r <= 0) {
                        return false;
                    }
                }

                parent = node.getId();
                ancestors.add(node.getId());
            }
            depth += 1;
            wrapper.clear();
            varName = varName + GLUECHAR;
        }
        return true;
    }

    @Override
    public boolean updatePagetree(Page page) {
        // 删除旧节点
        Integer pageId = page.getId();
        boolean isDeleted = deletePagetreeById(pageId);
        if (!isDeleted) {
            return false;
        }
        
        // 建立新节点
        boolean isCreated = createPagetree(page);
        if (!isCreated) {
            return false;
        }

        return true;
    }

    @Override
    public boolean deletePagetreeById(Integer pageId) {
        QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
        wrapper.eq("page_id", pageId);
        
        Pagetree node = pagetreeMapper.selectOne(wrapper);
        if (node == null) {
            return false;
        }

        return deletePagetree(node);
    }

    @Override
    public boolean deletePagetreeByName(String path, String name) {
        QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
        wrapper.eq("path", path).eq("name", name);
        
        Pagetree node = pagetreeMapper.selectOne(wrapper);
        if (node == null) {
            return false;
        }

        return deletePagetree(node);
    }

    private boolean deletePagetree(Pagetree node) {
        if (node.getIsFolder() == true) {
            node.setPageId(null);
            node.setIsPrivate(false);

            int r = pagetreeMapper.updateById(node);
            if (r <= 0) {
                return false;
            }
            return true;
        } else {
            List<Integer> ancestors = node.getAncestors();
            int r = pagetreeMapper.deleteById(node);
            if (r <= 0) {
                return false;
            }
            if (deleteEmptyNodes(ancestors) == false) {
                return false;
            }
            return true;
        }
    }

    private boolean deleteEmptyNodes(List<Integer> ancestors) {
        Collections.reverse(ancestors);
        for (Integer nodeId : ancestors) {
            QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
            wrapper.eq("parent", nodeId);
        
            Pagetree son = pagetreeMapper.selectOne(wrapper);
            Pagetree curNode = pagetreeMapper.selectById(nodeId);
            if (curNode == null) {
                return false;
            }
            Integer pageId = curNode.getPageId();
            if (son == null && pageId == null) {
                int r = pagetreeMapper.deleteById(nodeId);
                if (r <= 0) {
                    return false;
                }
            } else if (son == null && pageId != null) {
                curNode.setIsFolder(false);
                int r = pagetreeMapper.updateById(curNode);
                if (r <= 0) {
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }
}
