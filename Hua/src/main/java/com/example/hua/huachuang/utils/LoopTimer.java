package com.example.hua.huachuang.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hua on 2017/2/24.
 * 用于循环定时
 */

public class LoopTimer {
    private Handler mHandler;
    private Timer timer;

    public LoopTimer() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void startLoopTimer(int cycleTime, final OnTimeUp timeUp) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeUp.timeUp(timer);
                    }
                });
            }
        },0,cycleTime);
    }

    public void startDelayTimer(int delayTime,final OnTimeUp timeUp) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeUp.timeUp(timer);
                    }
                });
            }
        },delayTime);
    }

    public interface OnTimeUp {
        //当定时时间到时调用
        void timeUp(Timer timer);
    }

    public void cancel() {
        timer.cancel();
    }
}
