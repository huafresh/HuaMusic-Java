package com.example.hua.huachuang.manager;

import com.example.hua.huachuang.bean.music.Result;

/**
 * Created by hua on 2017/2/21.
 */

public interface CallBack {
    void onSuccess(Result result);
    void onError(Result result);
}

