package com.example.hua.huachuang.service;

import android.media.MediaPlayer;

/**
 * Created by hua on 2017/2/20.
 * 暂时还没做什么
 */

public class MyMediaPlayer extends MediaPlayer {

    private static MyMediaPlayer mMediaPlayer;

    private MyMediaPlayer() {}

    public static MyMediaPlayer getInstance() {
        if(mMediaPlayer ==null) {
            mMediaPlayer = new MyMediaPlayer();
        }
        return mMediaPlayer;
    }

    @Override
    public void release() {
        super.release();
        mMediaPlayer = null;
    }
}
