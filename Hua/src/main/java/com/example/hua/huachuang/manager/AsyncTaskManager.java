package com.example.hua.huachuang.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.LruCache;

import com.example.hua.huachuang.data.DBUtil;
import com.example.hua.huachuang.asyncTask.AsyncTask;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.bean.music.LrcContent;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.ExecutorUtil;
import com.example.hua.huachuang.data.SDUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by hua on 2017/2/23.
 * AsyncTask的实现，处理所有异步任务的通知机制
 */

public class AsyncTaskManager implements AsyncTask {

    private static AsyncTaskManager mAsyncTaskManager;
    //循环发送消息时使用
    private Timer timer;
    //主线程的handler
    private Handler mHandler;
    //线程池引用
    private ThreadPoolExecutor mThreadPoolExecutor;
    //缓存图片
    private LruCache<String,Bitmap> mLruCache;
    private MediaMetadataRetriever mMediaMetadataRetriever;

    private AsyncTaskManager() {
        mHandler = new Handler(Looper.getMainLooper());
        mThreadPoolExecutor = ExecutorUtil.getExecutorService();
        mLruCache = new LruCache<>(10*1024*1024);
        mMediaMetadataRetriever = new MediaMetadataRetriever();
    }

    public static AsyncTaskManager getInstance() {
        if(mAsyncTaskManager == null) {
            mAsyncTaskManager = new AsyncTaskManager();
        }
        return mAsyncTaskManager;
    }

    @Override
    public void scanMusic(final CallBack respond) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DBUtil.getMp3Info(ShareList.localList);
                mHandler.post(new PostRunnable(PostRunnable.SUCCESS, respond, new Result()));
            }
        });
    }

    @Override
    public void scanLrc(final String songName, final CallBack respond) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Result result = new Result();
                List<LrcContent> lists = SDUtil.loadLrc(songName+".lrc");
                if(lists != null) {
                    result.setLrcContents(lists);
                    mHandler.post(new PostRunnable(PostRunnable.SUCCESS,respond,result));
                } else mHandler.post(new PostRunnable(PostRunnable.ERROR,respond,result));
            }
        });
    }

    @Override
    public void setRingStone(Context context, String musicPath){
        ContentResolver resolver = context.getContentResolver();
        Uri contentUri = MediaStore.Audio.Media.getContentUriForPath(musicPath);
        Cursor cursor = resolver.query(contentUri,null,MediaStore.Audio.Media.DATA+"=?",
                new String[] {musicPath},null);//会返回插入到哪张表的哪一行
        if(cursor == null)
            return;
        if(cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            resolver.update(contentUri,values,MediaStore.Audio.Media.DATA+"=?",new String[] {musicPath});
            Uri newUri = ContentUris.withAppendedId(contentUri, Long.valueOf(_id));
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE,newUri);
            cursor.close();
        }
    }

    @Override
    public void downFile(String url, String dirPath, String fileName, CallBack respond) {

    }

    @Override
    public void getMediaThumb(final String url, final CallBack2 callBack) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                bitmap = mLruCache.get(url);
                if(bitmap == null) {
                    mMediaMetadataRetriever.setDataSource(url,new HashMap<String, String>());
                    bitmap = mMediaMetadataRetriever.getFrameAtTime(0);
                }
                final Result result = new Result();
                result.setThumbBitmap(bitmap);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }
}
