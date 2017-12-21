package com.example.hua.huachuang.htmlspanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.style.ImageSpan;

/**
 * Created by byl on 2016-12-9.
 */

public class MyImageSpan extends ImageSpan{

    public MyImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public MyImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public MyImageSpan(Drawable d) {
        super(d);
    }

    public MyImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public MyImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public MyImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public MyImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public MyImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public MyImageSpan(Context context, @DrawableRes int resourceId) {
        super(context, resourceId);
    }

    public MyImageSpan(Context context, @DrawableRes int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
