package com.easyedit.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyedit.entity.Page;
import com.easyedit.entity.Pagetree;
import com.easyedit.entity.Publish;
import com.easyedit.entity.Version;
import com.easyedit.mapper.PageMapper;
import com.easyedit.mapper.PagetreeMapper;
import com.easyedit.mapper.PublishMapper;
import com.easyedit.mapper.VersionMapper;
import com.easyedit.service.PublishService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangweifeng
 * @since 2023-03-14
 */
@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {
    
    final int MAX_RECCORD_NUM = 2;

    @Autowired
    private PublishMapper publishMapper;
    
    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private PagetreeMapper pagetreeMapper;

    @Autowired
    private VersionMapper versionMapper;

    @Override
    public ResponseResult createPublish(String path) {
        // 获取已有版本信息
        QueryWrapper<Version> wrapperVersion = new QueryWrapper<>();
        wrapperVersion.eq("path", path);
        Version v = versionMapper.selectOne(wrapperVersion);
        if (v == null) {
            v = new Version();
            v.setPath(path);
            v.setNewestVersion(0);
            int r = versionMapper.insert(v);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.ERROR).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        }
        Integer curVersion = v.getNewestVersion() + 1;

        // 从Page表写入到Publish表中
        QueryWrapper<Page> wrapper = new QueryWrapper<>();
        wrapper.eq("path", path);

        List<Page> pages = pageMapper.selectList(wrapper);
        if (pages.size() == 0) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        for (Page page : pages) {
            Publish publish = new Publish();
            publish.setVersion(curVersion);
            publish.setPath(path);
            publish.setName(page.getName());
            publish.setTitle(page.getTitle());
            publish.setDescription(page.getDescription());
            publish.setContent(page.getContent());
            publish.setContentType(page.getContentType());
            publish.setAuthorName(page.getAuthorName());
            publish.setPublisherName("anonymous");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            publish.setPublishAt(df.format(LocalDateTime.now()));

            int r = publishMapper.insert(publish);
            if (r <= 0) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.ERROR).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        }

        v.setPublishVersion(curVersion);
        v.setNewestVersion(curVersion);
        int r = versionMapper.updateById(v);
        if (r <= 0) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.ERROR).HttpCode(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        cleanOldVersion();
        return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult getPublish(String path, Integer version) {
        if (version == null) {
            QueryWrapper<Version> wrapperVersion = new QueryWrapper<>();
            wrapperVersion.eq("path", path);
    
            Version v = versionMapper.selectOne(wrapperVersion);
            if (v == null) {
                return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
            }
            
            version = v.getPublishVersion();
        }
        
        QueryWrapper<Publish> wrapper = new QueryWrapper<>();
        wrapper.eq("path", path).eq("version", version);

        List<Publish> pubs = publishMapper.selectList(wrapper);
        List<Integer> ancestors = new ArrayList<Integer>();
        List<Object> result = genResult(pubs, ancestors);
        return ResponseResult.okResult(result).HttpCode(HttpServletResponse.SC_OK);
    }
    
    @Override
    public ResponseResult getVersions(String path) {
        QueryWrapper<Version> wrapperVersion = new QueryWrapper<>();
        wrapperVersion.eq("path", path);

        Version v = versionMapper.selectOne(wrapperVersion);
        if (v == null) {
            return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.NO_RESULT_FOUND).HttpCode(HttpServletResponse.SC_NOT_FOUND);
        }
        Integer top = v.getNewestVersion() + 1;
        Integer bottom = Math.max(1, top - MAX_RECCORD_NUM);

        List<Integer> versions = IntStream.range(bottom, top).boxed().collect(Collectors.toList());
        return ResponseResult.okResult(versions).HttpCode(HttpServletResponse.SC_OK);
    }

    @Override
    public ResponseResult setVersion(String path, Integer version) {
        return ResponseResult.setAppHttpCodeEnum(ResponseResult.AppHttpCodeEnum.SUCCESS).HttpCode(HttpServletResponse.SC_OK);
    }

    private boolean cleanOldVersion() {
        return true;
    }

    private List<Object> genResult(List<Publish> records, List<Integer> ancestors) {
        List<Object> res = new ArrayList<Object>();
        for (Publish pub : records) {
            QueryWrapper<Pagetree> wrapper = new QueryWrapper<>();
            wrapper.eq("path", pub.getPath()).eq("name", pub.getName());

            Pagetree pt = pagetreeMapper.selectOne(wrapper);
            if (ancestors.equals(pt.getAncestors())) {
                Map<Object, Object> page = new HashMap<Object, Object>();
                page.put("name", pub.getName());
                page.put("title", pub.getTitle());
                page.put("description", pub.getDescription());
                page.put("content", pub.getContent());
                page.put("content_type", pub.getContentType());

                ancestors.add(pt.getId());
                page.put("items", genResult(records, ancestors));
                res.add(page);
                ancestors.remove(pt.getId());
            }
        }
        return res;
    }
}
