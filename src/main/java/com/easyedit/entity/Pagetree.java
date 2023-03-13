package com.easyedit.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhongjun
 * @since 2023-02-28
 */
@TableName(autoResultMap = true)
public class Pagetree implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String path;

    private String name;

    private Integer depth;

    private Boolean isEvent;

    private Boolean isPrivate;

    private Boolean isFolder;

    private Integer parent;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> ancestors;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer pageId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Boolean getIsEvent() {
        return isEvent;
    }

    public void setIsEvent(Boolean isEvent) {
        this.isEvent = isEvent;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean isFolder) {
        this.isFolder = isFolder;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public List<Integer> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<Integer> ancestors) {
        this.ancestors = ancestors;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return "Pagetree{" +
            "id = " + id +
            ", path = " + path +
            ", name = " + name +
            ", depth = " + depth +
            ", isEvent = " + isEvent +
            ", isPrivate = " + isPrivate +
            ", isFolder = " + isFolder +
            ", parent = " + parent +
            ", ancestors = " + ancestors +
            ", pageId = " + pageId +
        "}";
    }
}
