package com.easyedit.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangweifeng
 * @since 2023-03-14
 */
public class Version implements Serializable {
 
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String path;

    private Integer publishVersion;

    private Integer newestVersion;
    

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

    public Integer getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(Integer publishVersion) {
        this.publishVersion = publishVersion;
    }

    public Integer getNewestVersion() {
        return newestVersion;
    }

    public void setNewestVersion(Integer newestVersion) {
        this.newestVersion = newestVersion;
    }

    @Override
    public String toString() {
        return "Page{" +
            "id = " + id +
            ", path = " + path +
            ", publishVersion = " + publishVersion +
            ", newestVersion = " + newestVersion +
        "}";
    }
}
