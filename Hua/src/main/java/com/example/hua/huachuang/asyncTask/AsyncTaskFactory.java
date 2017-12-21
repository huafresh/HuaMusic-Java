package com.example.hua.huachuang.asyncTask;

import com.example.hua.huachuang.manager.AsyncTaskManager;

/**
 * Created by hua on 2017/2/23.
 * 使用工厂模式解耦，以后可以随意更改耗时操作的实现了
 */

public class AsyncTaskFactory {
    public static AsyncTask getAsyncTask() {
        return AsyncTaskManager.getInstance();
    }
}
