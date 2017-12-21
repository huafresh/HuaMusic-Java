package com.example.hua.huachuang.utils;

import android.widget.Toast;

import com.example.hua.huachuang.base.HuaApplication;

/**
 * Created by hua on 2017/2/25.
 * 通用
 */

public class CommonUtil {

    //打印字符串
    public static void toast(String s) {
        Toast.makeText(HuaApplication.getInstance(),s,Toast.LENGTH_SHORT).show();
    }

    //打印整数
    public static void toast(int s) {
        Toast.makeText(HuaApplication.getInstance(),s+"",Toast.LENGTH_SHORT).show();
    }


}
