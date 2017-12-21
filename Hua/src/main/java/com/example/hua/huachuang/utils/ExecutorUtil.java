package com.example.hua.huachuang.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hua on 2017/2/26.
 * 线程池
 */

public class ExecutorUtil {
    //全局使用的线程池
    private static ExecutorService mExecutorService;
    //最大线程数
    private static int MAX_THREAD = Runtime.getRuntime().availableProcessors();

    public static ThreadPoolExecutor getExecutorService() {
        if(mExecutorService==null) {
            synchronized (ExecutorUtil.class) {
                if(mExecutorService==null) {
                    BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
                    mExecutorService = new ThreadPoolExecutor(
                            0, //最小线程数
                            MAX_THREAD, //最大线程数
                            1, //允许空闲时间
                            TimeUnit.SECONDS, //允许空闲时间单位
                            queue); //缓冲队列
                }
            }
        }
        return (ThreadPoolExecutor) mExecutorService;
    }

    //关闭线程池
    public static void stopThreadPool() {
        //暂时不关闭，以后优化
        //mExecutorService.shutdown();
        //mExecutorService = null;
    }

}
