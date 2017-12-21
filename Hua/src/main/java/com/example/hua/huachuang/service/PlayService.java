package com.example.hua.huachuang.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.data.SDUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huazai on 2016/9/10.
 * 处理播放模块
 */
public class PlayService extends Service {
    //操作mediaPlayer的句柄
    public static PlayBinder PlayBinder;
    //next的flag
    private final int NEXT = 1;
    private final int PREVIOUS = -1;
    //播放模式
    final public static int PLAY_MODE_NORMAL = 1;
    final public static int PLAY_MODE_REPEAT = 2;
    final public static int PLAY_MODE_RANDOM = 3;
    //当前播放模式
    public static int playMode = PLAY_MODE_NORMAL;
    //正在播放的本地音乐|在线音乐
    private Music curMusic;
    //当前播放的歌曲是否是在线的
    private boolean isOnline = false;
    //播放列表，默认等于本地列表
    private List<Music> playList;
    //播放位置
    private int curPosition = 0;
    //服务action
    public static final String ACTION = "com.example.hua.huachuang.service.PlayService";
    //广播action
    public static final String BROADCAST_COMPLETION = "com.example.BROADCAST_COMPLETION";
    public static final String BROADCAST_PREPARE = "com.example.BROADCAST_PREPARE";
    public static final String BROADCAST_UPDATE = "com.example.BROADCAST_UPDATE";
    //封装的播放类
    private MyMediaPlayer mMediaPlayer;
    //用于更新在线缓冲进度
    private SeekBar seekBar;
    //保存记录的文件名
    private static final String saveName = "service";
    //用于恢复播放进度
    private int hisProgress;
    //用于保存播放记录
    private HashMap<String, Object> hashMap;

    //暂时用于网络歌曲id，从2000开始增
    private int onlineId = 2000;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MyMediaPlayer.getInstance();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(BROADCAST_COMPLETION);
                sendBroadcast(intent);
                mStart(getNextMusic(NEXT));
            }
        });
        doRestore();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    @Override
    public void onDestroy() {
        isOnline = false;
        doSave();
        this.mStop();
        //这是大坑，如果不置为null，主界面和服务退出后，改值还存在内存中！！
        //以后要注意，全局使用的变量，退出要置null
        PlayService.PlayBinder = null;
        Log.i("hua", "diao yong onDestroy");
        super.onDestroy();
    }

    public void mPrepare(Music music) {
        if (music == null) return;
        try {
            mMediaPlayer.reset();
            if (!music.isOnline())
                mMediaPlayer.setDataSource(music.getPath());
            else mMediaPlayer.setDataSource(music.getMusicLink());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //当启动时，主界面的playBar优先于服务的doRestore()代码，故需要准备好后通知更新
        Intent intent = new Intent(BROADCAST_PREPARE);
        sendBroadcast(intent);
        //针对从本地列表调用此函数
        curMusic = music;
    }

    public void mSetPlayList(List<Music> playList) {
        this.playList = playList;
    }

    //改为传Music对象，这样就不会依赖具体的播放列表了
    public void mStart(Music music) {
        if (music == null) return;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(music.getPath());
            mMediaPlayer.setOnPreparedListener(new prepareListener());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.curMusic = music;
        //从哪启动的，播放列表要更新
        this.playList = ShareList.localList;
        //添加一条播放记录
        ShareList.AddMusicToRecentPlay(music);
    }

    /**
     * 获取下一首播放的歌曲
     */
    private Music getNextMusic(int flag) {
        if (playList == null) {
            if (ShareList.localList.size() != 0)
                playList = ShareList.localList;
            else return null;
        }
        curPosition = getCurPos(curMusic, playList);
        switch (playMode) {
            case PLAY_MODE_NORMAL:
                if (flag == NEXT) {
                    if (curPosition == (playList.size() - 1))
                        curPosition = 0;
                    else curPosition++;
                } else if (flag == PREVIOUS) {
                    if (curPosition == 0)
                        curPosition = playList.size() - 1;
                    else curPosition--;
                }
                break;
            case PLAY_MODE_RANDOM:
                curPosition = (int) (Math.random() * (playList.size()));
                break;
            case PLAY_MODE_REPEAT:
                break;
        }
        return playList.get(curPosition);
    }

    /**
     * 得到当前歌曲在播放列表中的位置
     */
    private int getCurPos(Music music, List<Music> lists) {
        if (lists == null || music == null) return 0;
        int i;
        for (i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == music.getId())
                break;
        }
        return i;
    }

    /**
     * 在线播放
     */
    public void mPlayOnline(Music music, SeekBar seekBar) {
        this.seekBar = seekBar;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(music.getMusicLink());
            mMediaPlayer.setOnBufferingUpdateListener(new BufferUpdateListener());
            mMediaPlayer.setOnPreparedListener(new prepareListener());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        music.setOnline(true);
        music.setId(onlineId++);
        this.curMusic = music;
        //从哪启动的，播放列表要更新
        this.playList = ShareList.localList;
        //添加一条播放记录
        ShareList.AddMusicToRecentPlay(music);
    }

    private void mSeekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void mPause() {
        mMediaPlayer.pause();
    }

    public void mContinue_play() {
        mMediaPlayer.start();
    }

    public boolean mIsPlay() {
        return mMediaPlayer.isPlaying();
    }

    public void mStop() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    public void mNext(int flag) {
        mStart(getNextMusic(flag));
    }

    public int mGetCurPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int mGetDuration() {
        return mMediaPlayer.getDuration();
    }

    private final class prepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            Intent intent = new Intent(BROADCAST_PREPARE);
            sendBroadcast(intent);
        }
    }

    private class BufferUpdateListener implements MediaPlayer.OnBufferingUpdateListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (seekBar != null) {
                int curPosition = mGetCurPosition() * percent;
                seekBar.setSecondaryProgress(curPosition);
            }
        }
    }

    /**
     * 保存播放记录，播放位置等数据
     */
    private void doSave() {
        HashMap<String, Object> hashMap = new HashMap<>();
        //当前播放的歌曲
        hashMap.put("music", curMusic);
        //播放进度
        hashMap.put("curProgress", mGetCurPosition());
        //播放模式
        hashMap.put("playMode", playMode);
        SDUtil.object2file(this, hashMap, saveName);
    }

    /**
     * 恢复播放记录
     */
    private void doRestore() {
        hashMap = DensityUtil.cast(SDUtil.file2object(this, saveName));
        //首次使用时没有记录
        if (hashMap == null) return;
        if (isHaveKey("music")) {
            curMusic = (Music) hashMap.get("music");
            mPrepare(curMusic);
            //播放列表暂时使用本地音乐
            playList = ShareList.localList;
            curPosition = getCurPos(curMusic, playList);
        } else { //没有保存记录，则预加载本地列表第一首歌
            if (ShareList.localList.size() != 0) {
                curMusic = ShareList.localList.get(0);
                curMusic.setOnline(false);
                mPrepare(curMusic);
            }
        }
        if (isHaveKey("curProgress")) {
            hisProgress = (int) hashMap.get("curProgress");
            mMediaPlayer.seekTo(hisProgress);
        }
        if (isHaveKey("playMode")) playMode = (int) hashMap.get("playMode");
    }

    private boolean isHaveKey(String key) {
        return hashMap.get(key) != null;
    }

    public class PlayBinder extends Binder {
        //播放音乐
        public void start(Music music) {
            mStart(music);
        }

        //播放网络音乐
        public void startOnline(Music music, SeekBar seekBar) {
            mPlayOnline(music, seekBar);
        }

        //暂停播放
        public void pause() {
            mPause();
        }

        //继续播放
        public void continue_play() {
            mContinue_play();
        }

        //下一曲
        public void next() {
            mNext(NEXT);
        }

        //上一曲
        public void previous() {
            mNext(PREVIOUS);
        }

        //判断是否正在播放
        public boolean isPlay() {
            return mIsPlay();
        }

        //获取当前播放位置
        public int getCurPosition() {
            return mGetCurPosition();
        }

        //获取时长
        public int getDuration() {
            return mGetDuration();
        }

        //准备一首歌，但不播放
        public void prepare(Music music) {
            mPrepare(music);
        }

        //定位
        public void seekTo(int position) {
            mSeekTo(position);
        }

        //设置进度条，用于播放网络歌曲时更新缓冲条
        public void setSeekBar(SeekBar bar) {
            seekBar = bar;
        }

        //获取当前播放的音乐
        public Music getCurMusic() {
            return curMusic;
        }

        //设置当前播放的是否是网络的
        public void setIsOnline(boolean b) {
            isOnline = b;
        }

        //只为兼容
        public Music getOnlineMp3() {
            return null;
        }

        //判断是否是播放网络音乐
        public boolean getIsOnline() {
            return isOnline;
        }

        //设置播放列表
        public void setPlayList(List<Music> list) {
            mSetPlayList(list);
        }
    }

    private void mSetLocalMp3(Music localMp3) {
        this.curMusic = localMp3;
    }
}
