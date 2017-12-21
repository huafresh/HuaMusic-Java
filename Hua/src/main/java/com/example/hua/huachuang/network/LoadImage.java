package com.example.hua.huachuang.network;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hua.huachuang.base.HuaApplication;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by hua on 2017/3/4.
 * DataBinding注解的方式加载图片
 */

public class LoadImage {
    private static Context mContext = HuaApplication.getInstance();

    /**
     * 一般的图片加载，非绑定
     */
    public static void loadImageNormal(ImageView imageView,String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    /**
     * 在线音乐预览左边的图
     */
    @BindingAdapter({"previewUrl"})
    public static void loadPreviewImage(ImageView imageView,String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    /**
     * 榜单头部背景图
     */
    @BindingAdapter({"headerBacUrl"})
    public static void loadHeaderBacImage(ImageView imageView,String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                //23是模糊指数，最大25；2是缩放倍数
                .bitmapTransform(new BlurTransformation(mContext,23,2))
                .into(imageView);
    }

    /**
     * 榜单头部左边的图
     */
    @BindingAdapter({"headerUrl"})
    public static void loadHeaderImage(ImageView imageView,String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    /**
     * 新闻预览信息之类型1图片
     */
    @BindingAdapter({"newUrl1"})
    public static void loadNewUrl1(ImageView imageView,String imageUrl) {
        if(imageUrl==null || imageUrl.equals("")) return;
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    /**
     * 新闻预览信息之类型2图片，共三张
     */
    @BindingAdapter({"newUrl2_1"})
    public static void loadNewUrl2_1(ImageView imageView,String imageUrl) {
        if(imageUrl==null || imageUrl.equals("")) return;
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    @BindingAdapter({"newUrl2_2"})
    public static void loadNewUrl2_2(ImageView imageView,String imageUrl) {
        if(imageUrl==null || imageUrl.equals("")) return;
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }

    @BindingAdapter({"newUrl2_3"})
    public static void loadNewUrl2_3(ImageView imageView,String imageUrl) {
        if(imageUrl==null || imageUrl.equals("")) return;
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageView);
    }


}
