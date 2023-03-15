package com.easyedit.service.impl;

import com.easyedit.entity.Page;
import com.easyedit.mapper.PageMapper;
import com.easyedit.service.PageService;
import com.easyedit.service.PagetreeService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        String currentTime = df.format(LocalDateTime.now());
        page.setCreatedAt(currentTime);
        page.setUpdatedAt(currentTime);

        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        wrapper.eq("path", page.getPath()).eq("name", page.getName());
        Page existPage = pageMapper.selectOne(wrapper);
        if (existPage != null) {
            return ResponseResult.errorResult(existPage).HttpCode(HttpServletResponse.SC_CONFLICT);
        }

        int r = pageMapper.insert(page);
        if (r <= 0) {
            return ResponseResult.errorResult(
                ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        boolean isCreated = pagetreeService.createPagetree(page);
        if (isCreated) {
            return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
        } else {
            pageMapper.deleteById(page);
            return ResponseResult.errorResult(
                ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_INSERT).HttpCode(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ResponseResult getPage(Integer pageId, String path, String name) {
        if (pageId > 0 && path.equals("") && name.equals("")) {
            Page page = pageMapper.selectById(pageId);
            if (page == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }
            return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
        }

        if (pageId <= 0 && !path.equals("") && !name.equals("")) {
            QueryWrapper<Page> wrapper = new QueryWrapper<>();
            wrapper.eq("path", path).eq("name", name);
            Page page = pageMapper.selectOne(wrapper);
            if (page == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }
            return ResponseResult.okResult(page).HttpCode(HttpServletResponse.SC_OK);
        }

        return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.BAD_REQUEST).HttpCode(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ResponseResult getProfile(List<String> siteNames) {
        List<Object> editablePages = new ArrayList<Object>();
        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        for (String siteName : siteNames) {
            wrapper.clear();
            wrapper.like("path", siteName).eq("name", "profile");

            Page page = pageMapper.selectOne(wrapper);
            if (page == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }

            Map<Object, Object> editablePage = new HashMap<Object, Object>();
            editablePage.put("siteName", siteName);
            editablePage.put("type", page.getType());
            editablePage.put("path", page.getPath());
            editablePage.put("locale", page.getLocale());
            editablePage.put("updated_at", page.getUpdatedAt());
            editablePages.add(editablePage);
        }
        return ResponseResult.okResult(editablePages).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getAllByPath(String path) {
        return ResponseResult.okResult(path).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult updatePage(Integer pageId, String path, String name, Page page) {
        if (pageId > 0 && path.equals("") && name.equals("")) {
            Page oldPage = pageMapper.selectById(pageId);
            if (oldPage == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }

            String oldPath = oldPage.getPath();
            String oldName = oldPage.getName();
            page.setId(pageId);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            page.setUpdatedAt(df.format(LocalDateTime.now()));
            int r = pageMapper.updateById(page);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_UPDATE).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
            if ((page.getPath() != null && !oldPath.equals(page.getPath())) || 
              (page.getName() != null && !oldName.equals(page.getName()))) {
                Page newPage = pageMapper.selectById(pageId);
                boolean isUpdated = pagetreeService.updatePagetree(newPage);
                if (!isUpdated) {
                    pageMapper.updateById(oldPage);
                    return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_UPDATE).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
            }
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
        }

        if (pageId <= 0 && !path.equals("") && !name.equals("")) {
            QueryWrapper<Page> wrapper = new QueryWrapper<>();
            wrapper.eq("path", path).eq("name", name);

            Page oldPage = pageMapper.selectOne(wrapper);
            if (oldPage == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            page.setUpdatedAt(df.format(LocalDateTime.now()));
            int r = pageMapper.update(page, wrapper);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_UPDATE).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
            if ((page.getPath() != null && !path.equals(page.getPath())) || 
              (page.getName() != null && !name.equals(page.getName()))) {
                Page newPage = pageMapper.selectById(oldPage.getId());
                boolean isUpdated = pagetreeService.updatePagetree(newPage);
                if (!isUpdated) {
                    pageMapper.updateById(oldPage);
                    return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_UPDATE).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
            }
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
        }

        return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.BAD_REQUEST).HttpCode(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ResponseResult deletePage(Integer pageId, String path, String name) {
        if (pageId > 0 && path.equals("") && name.equals("")) {
            int r = pageMapper.deleteById(pageId);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_DELETED).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
            pagetreeService.deletePagetreeById(pageId);
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
        }

        if (pageId <= 0 && !path.equals("") && !name.equals("")) {
            QueryWrapper<Page> wrapper = new QueryWrapper<>();
            wrapper.eq("path", path).eq("name", name);

            int r = pageMapper.delete(wrapper);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_DATA_WAS_DELETED).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
            pagetreeService.deletePagetreeByName(path, name);
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
        }

        return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.BAD_REQUEST).HttpCode(HttpServletResponse.SC_BAD_REQUEST);
    }

    
}
