package com.example.hua.huachuang.base;

import android.app.Application;

/**
 * Created by hua on 2017/2/17.
 */

public class HuaApplication extends Application {
    private static HuaApplication context;
    public static HuaApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
