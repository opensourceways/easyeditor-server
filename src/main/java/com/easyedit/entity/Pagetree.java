package com.easyedit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhongjun
 * @since 2023-02-28
 */
public class Pagetree implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String path;

    private Integer depth;

    private String title;

    private Boolean isEvent;

    private Boolean isPrivate;

    private Boolean isFolder;

    private Integer parent;

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

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
            ", depth = " + depth +
            ", title = " + title +
            ", isEvent = " + isEvent +
            ", isPrivate = " + isPrivate +
            ", isFolder = " + isFolder +
            ", parent = " + parent +
            ", pageId = " + pageId +
        "}";
    }
}
