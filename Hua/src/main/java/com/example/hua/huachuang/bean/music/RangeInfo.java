package com.example.hua.huachuang.bean.music;

import android.graphics.drawable.Drawable;

/**
 * Created by hua on 2017/2/19.
 * 分类信息，如本地音乐、最近播放等
 */

public class RangeInfo {
    private int imageId;
    private String title;
    private String stringSum;
    private Drawable drawable;

    public RangeInfo(Drawable drawable, String title, String sum) {
        setDrawable(drawable);
        setTitle(title);
        setStringSum(sum);
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStringSum() {
        return stringSum;
    }

    public void setStringSum(String stringSum) {
        this.stringSum = stringSum;
    }
}
