package com.example.hua.huachuang.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.data.DBUtil;
import com.example.hua.huachuang.download.DownloadManager;
import com.example.hua.huachuang.share.ShareList;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hua on 2017/2/26.
 */

public class DownService extends Service {
    public static final String ACTION = "com.example.hua.huachuang.service.DownService";

    public static final String BROADCAST_QUERY = "com.example.BROADCAST_QUERY";
    //文件存在时，实测status值为16
    public static final int STATUS_EXIST = 16;

    //存储音乐的路径
    public static final String MUSIC_PATH = Environment.DIRECTORY_DOWNLOADS+"/music";
    //存储歌词的路径
    public static final String LRC_PATH = Environment.DIRECTORY_DOWNLOADS+"/lrc";
    //用于暴露服务方法
    public static DownBinder downBinder;
    //系统下载管理器
    private DownloadManager downloadManager;
    //定时查询
    private Timer timer;


    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = DownloadManager.getInstance(getContentResolver(),getPackageName());
        timer = new Timer();
        timer.schedule(new queryTask(),0,1000);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        downBinder = new DownBinder();
        return downBinder;
    }

    /**
     * 下载到..files/Download/目录使用Environment.DIRECTORY_DOWNLOADS
     * 下载到SD卡，dirPath是路径如/hua/Download
     */
    public long mAddDownTask(DowningInfo info) {
        Uri uri = Uri.parse(info.getUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(this,info.getDirPath(),info.getFileName());
        request.setAllowedOverRoaming(false);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        return downloadManager.enqueue(request);
    }

    public List<Long> mAddDownTasks(List<DowningInfo> lists) {
        List<Long> longs = new ArrayList<>();
        if(lists==null) return null;
        if(lists.size()==0) return longs;
        for (int i = 0; i < lists.size(); i++) {
            longs.add(mAddDownTask(lists.get(i)));
        }
        return longs;
    }

    /**
     * 暂停一个下载任务
     */
    public void mPauseTask(long id) {
        downloadManager.pauseDownload(id);
    }

    /**
     * 暂停多个任务
     */
    public void mPauseTasks(long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            downloadManager.pauseDownload(ids[i]);
        }
    }

    /**
     * 恢复一个下载任务
     */
    public void mResumeTask(long id) {
        downloadManager.resumeDownload(id);
    }

    /**
     * 恢复多个任务
     */
    public void mResumeTasks(long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            downloadManager.resumeDownload(ids[i]);
        }
    }

    /**
     * 取消下载任务
     */
    public void mCancel(long id) {
        downloadManager.remove(id);
    }

    /**
     * 取消多个任务
     */
    public void mCancelAll() {
        for (int i = 0; i < ShareList.musicDowningList.size(); i++) {
            long id = ShareList.musicDowningList.get(i).getId();
            downloadManager.remove(id);
        }
    }

    /**
     * 获取一个下载任务信息
     */
    public DowningInfo mGetDownInfo(long id) {
        return DBUtil.getDownInfo(id,downloadManager);
    }

    /**
     * 获取多个下载任务信息
     */
    public List<DowningInfo> mGetDownTasks(List<Long> ids) {
        return DBUtil.getDownTasks(ids,downloadManager);
    }


    private class queryTask extends TimerTask {
        @Override
        public void run() {
            List<Long> ids = getIds(ShareList.musicDowningList);
            if(ids.size()!=0) {
                List<DowningInfo> lists = mGetDownTasks(ids);
                mSendBroadcast(lists);
            }
        }
    }

    private void mSendBroadcast(List<DowningInfo> lists) {
        if(lists.size()==0) return;
        Intent intent = new Intent(BROADCAST_QUERY);
        Bundle bundle = new Bundle();
        for (int i = 0; i < lists.size(); i++) {
            DowningInfo info = lists.get(i);
            if(info==null) continue;
            bundle.putSerializable(String.valueOf(info.getId()),info);
        }
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    /**
     * 提取列表中的id
     */
    private List<Long> getIds(List<DowningInfo> lists) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            ids.add(lists.get(i).getId());
        }
        return ids;
    }

    public class DownBinder extends Binder {
        /**
         * 下载到..files/Download/目录使用 MUSIC_PATH
         * 下载到SD卡，dirPath是路径如/hua/Download
         */
        public Long addDownTask(DowningInfo info) {
            return mAddDownTask(info);
        }
        //添加多个下载任务
        public List<Long> addDownTasks(List<DowningInfo> lists) {
            return mAddDownTasks(lists);
        }
        //取消指定下载任务
        public void cancelTask(long id) {
            mCancel(id);
        }
        //取消多个下载任务
        public void cancelAllTask() {
            mCancelAll();
        }

        public void pauseTask(long id) {
            mPauseTask(id);
        }

        public void pauseTasks(long[] ids) {
            mPauseTasks(ids);
        }

        public void resumeTask(long id) {
            mResumeTask(id);
        }

        public void resumeTasks(long[] ids) {
            mPauseTasks(ids);
        }

        //获取下载信息
        public DowningInfo getDownInfo(long id) {
            return mGetDownInfo(id);
        }

        //获取多个下载信息
        public List<DowningInfo> getDownInfos(List<Long> ids) {
            return mGetDownTasks(ids);
        }
    }
}
