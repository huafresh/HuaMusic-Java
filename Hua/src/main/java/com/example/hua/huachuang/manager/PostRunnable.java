package com.example.hua.huachuang.manager;

import com.example.hua.huachuang.bean.music.Result;

/**
 * Created by hua on 2017/2/23.
 * 通知主线程
 */

public class PostRunnable implements Runnable{

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;

    //决定执行成功回调，还是执行错误回调
    private int state;
    //回调接口
    private CallBack respond;
    //响应结果封装
    private Result result;

    public PostRunnable(int state, CallBack respond, Result result) {
        this.state = state;
        this.respond = respond;
        this.result = result;
    }

    @Override
    public void run() {
        if(respond!=null) {
            if(state == SUCCESS) {
                respond.onSuccess(result);
            } else {
                respond.onError(result);
            }
        }
    }
}
