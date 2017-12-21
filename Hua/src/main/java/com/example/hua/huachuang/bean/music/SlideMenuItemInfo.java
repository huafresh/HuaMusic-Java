package com.example.hua.huachuang.bean.music;

import android.graphics.drawable.Drawable;

/**
 * Created by hua on 2017/3/10.
 */

public class SlideMenuItemInfo {
    private int drawableId;
    private String title;
    private Drawable drawable;

    public SlideMenuItemInfo(Drawable drawable, String title) {
        this.drawable = drawable;
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
