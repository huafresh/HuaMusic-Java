package com.example.hua.huachuang.network;

import android.app.Activity;

import com.example.hua.huachuang.manager.NetWork_OkHttp;

/**
 * Created by hua on 2016/12/27.
 * 返回网络操作的接口，使用工厂模式解耦
 */
public class NetWorkRequestFactory {
    public static NetWork_OkHttp getNetWorkRequest(Activity activity) {
       return NetWork_OkHttp.getInstance(activity);
    }
}
