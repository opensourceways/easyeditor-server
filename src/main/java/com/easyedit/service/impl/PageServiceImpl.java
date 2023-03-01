package com.easyedit.service.impl;

import com.easyedit.entity.Page;
import com.easyedit.mapper.PageMapper;
import com.easyedit.service.PageService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Override
    public ResponseResult createPage(Page page) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Boolean isPrivate = true;
        page.setId(1);
        page.setTitle(page.getTitle());
        page.setPath("test");
        page.setHash("test");
        page.setDescription("test");
        page.setIsPrivate(isPrivate);
        page.setIsPublished(isPrivate);
        page.setPublishEndDate("test");
        page.setPublishStartDate("tes");
        page.setContent("test");
        page.setContentType("test");
        page.setUpdatedAt("dfd");
        page.setAuthorName("ddd");
        page.setCreatorName("333");;
        page.setCreatedAt(df.format(LocalDateTime.now()));
        
        int r = pageMapper.insert(page);
        if (r <= 0) {
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getPage(String pageId) {
        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        wrapper.eq("hash", "ndd");

        Page page = pageMapper.selectOne(wrapper);
        if (page == null) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
    }
}
