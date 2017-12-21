package com.example.hua.huachuang.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.hua.huachuang.base.HuaApplication;

/**
 * Created by hua on 2017/3/11.
 */

public class ShareUtil {

    public static final String SHARE_CFG = "cfg";

    private static ShareUtil mShareUtil;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private static  String mName;


    @SuppressLint("CommitPrefEdits")
    private ShareUtil(String name) {
        Context context = HuaApplication.getInstance();
        mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static ShareUtil getInstance(String name) {
        if(mShareUtil ==null || !mName.equals(name)) {
            mShareUtil = new ShareUtil(name);
            mName = name;
        }
        return mShareUtil;
    }

    public ShareUtil putBoolean(String key, boolean value) {
        mEditor.putBoolean(key,value);
        return this;
    }

    public ShareUtil putInt(String key, int value) {
        mEditor.putInt(key,value);
        return this;
    }

    public int getInt(String key) {
        return mPreferences.getInt(key,0);
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key,false);
    }

    public void apply() {
        mEditor.apply();
    }
}
