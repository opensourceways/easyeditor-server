package com.easyedit.easyedit.service.impl;

import com.easyedit.easyedit.entity.Pages;
import com.easyedit.easyedit.mapper.PagesMapper;
import com.easyedit.easyedit.service.IPagesService;
import com.easyedit.easyedit.util.ResponseResult;

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
 * @since 2023-02-28
 */
@Service
public class PagesServiceImpl extends ServiceImpl<PagesMapper, Pages> implements IPagesService {

    @Autowired
    private PagesMapper pagesMapper;

    @Override
    public ResponseResult createPages(Pages pages) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // pages.setId(2);
        Boolean isEvent = true;
        Boolean isPrivate = true;
        pages.setId(1);
        pages.setName(pages.getName());
        pages.setTitle(pages.getTitle());
        pages.setPath("test");
        pages.setHash("test");
        pages.setDescription("test");
        pages.setIsEvent(isEvent);
        pages.setIsPrivate(isPrivate);
        pages.setIsPublished(isPrivate);
        pages.setPublishEndDate("test");
        pages.setPublishStartDate("tes");
        pages.setText("test");
        pages.setTextType("test");
        pages.setUpdatedAt("dfd");
        pages.setAuthorName("ddd");
        pages.setCreatorName("333");;
        pages.setCreatedAt(df.format(LocalDateTime.now()));
        
        int r = pagesMapper.insert(pages);
        if (r <= 0) {
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return ResponseResult.okResult(pages).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getPage(String pageId) {
        QueryWrapper<Pages> wrapper = new QueryWrapper<>();
        wrapper.eq("hash", "title");

        Pages pages = pagesMapper.selectOne(wrapper);
        if (pages == null) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        return ResponseResult.okResult(pages).HttpCode(HttpServletResponse.SC_OK);
    }

}
