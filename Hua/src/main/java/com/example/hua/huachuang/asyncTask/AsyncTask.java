package com.example.hua.huachuang.asyncTask;

import android.content.Context;

import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.manager.CallBack2;

/**
 * Created by hua on 2017/2/23.
 * 线程操作的接口，有耗时任务时在这里添加接口
 */

public interface AsyncTask {
    //扫描本地音乐
    void scanMusic(CallBack respond);
    //扫描本地歌词
    void scanLrc(String songName, CallBack respond);
    //设置铃声，目测不怎么耗时，故没设回调
    void setRingStone(Context context, String musicPath);
    //下载文件
    void downFile(String url, String dirPath, String fileName, CallBack callBack);
    //获取网络视频缩略图
    void getMediaThumb(String url, CallBack2 callBack);

}
