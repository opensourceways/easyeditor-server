package com.easyedit.service.impl;

import com.easyedit.entity.Page;
import com.easyedit.mapper.PageMapper;
import com.easyedit.service.PageService;
import com.easyedit.service.PagetreeService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private PagetreeService pagetreeService;

    @Override
    public ResponseResult createPage(Page page) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (page.getIsPublished() == true) {
            page.setPublishStartDate(df.format(LocalDateTime.now()));
        }
        page.setCreatedAt(df.format(LocalDateTime.now()));
        if (page.getFolder().equals("/")) {
            page.setFullName(page.getFolder() + page.getName());
        } else {
            page.setFullName(page.getFolder() + "/" +page.getName());
        }

        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        wrapper.eq("path", page.getPath()).eq("folder", page.getFolder()).eq("name", page.getName());
        Page existPage = pageMapper.selectOne(wrapper);
        if (existPage != null) {
            return ResponseResult.okResult(existPage).HttpCode(HttpServletResponse.SC_OK);
        }

        int r = pageMapper.insert(page);
        if (r <= 0) {
            return ResponseResult.errorResult(
                ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        pagetreeService.createPagetree(page);
        return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getPage(String pageId) {
        Page page = pageMapper.selectById(pageId);
        if (page == null) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult updatePage(String pageId, Page page) {
        page.setId(Integer.parseInt(pageId));

        int r = pageMapper.updateById(page);
        if (r <= 0) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult deletePage(String pageId) {
        int r = pagetreeService.deletePagetree(pageId);
        if (r <= 0) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }

        r = pageMapper.deleteById(pageId);
        if (r <= 0) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        final String DELETE_COMPLETE = "Complete deletion of Page.";
        return ResponseResult.okResult(DELETE_COMPLETE).HttpCode(HttpServletResponse.SC_OK);
    }
}
