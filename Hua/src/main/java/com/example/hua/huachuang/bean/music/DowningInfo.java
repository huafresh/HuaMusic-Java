package com.example.hua.huachuang.bean.music;

import java.io.Serializable;

/**
 * Created by hua on 2017/2/28.
 */

public class DowningInfo implements Serializable{
    private long id;
    //存储名称
    private String fileName;
    //存储路径
    private String dirPath;
    //展示名称，针对下载音乐
    private String displayName;
    //下载地址
    private String url;
    //下载进度
    private long current;
    private long total;
    //当前状态
    private int status;
    //暂停或者失败原因
    private String reason;
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
