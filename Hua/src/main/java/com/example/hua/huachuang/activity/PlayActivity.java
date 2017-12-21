package com.example.hua.huachuang.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hua.huachuang.network.NetWorkRequest;
import com.example.hua.huachuang.network.NetWorkRequestFactory;
import com.example.hua.huachuang.R;
import com.example.hua.huachuang.asyncTask.AsyncTask;
import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.bean.music.LrcContent;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.custom.LrcTextView;
import com.example.hua.huachuang.databinding.PlayActivityBinding;
import com.example.hua.huachuang.utils.LoopTimer;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.List;
import java.util.Timer;

/**
 * Created by hua on 2016/12/6.
 */
public class PlayActivity extends Activity implements View.OnClickListener{
    //启动action
    public static final String ACTION = "com.example.hua.huachuang.activity.PlayActivity";
    //数据绑定
    private PlayActivityBinding playBinding;

    final private int NEXT = 1;
    final private int PREVIOUS = -1;
    final private int UPDATE_MSG = 1;
    final private int UPDATE_LRC = 2;
    private final int LRC_OK = 3;

    private TextView tv_play_top_name;
    private TextView tv_play_top_author;
    private int curPosition;
    //当seekBar按下时，暂停更新进度
    private boolean isSeekBarPress;
    private TextView tv_play_bottom_current;
    private TextView tv_play_bottom_total;
    //操作服务的对象
    private SeekBar sb_play_bottom_seekBar;
    private ImageView iv_play_bottom_mode;
    private ImageView iv_play_bottom_pre;
    private ImageView iv_play_bottom_play;
    private ImageView iv_play_bottom_next;
    private LinearLayout ll_play_activity_layout;
    private PlayBroadcastReceive broadcastReceive;
    private ImageView iv_play_top;

    //是否更新进度
    private boolean isUpdateProgress;
    //是否更新歌词
    private boolean isUpdateLrc;

    private LrcTextView tv_lrc_textView;
    //存放当前播放歌曲的歌词
    private List<LrcContent> lrcContents;
    //网络操作接口
    private NetWorkRequest netWorkRequest;
    //线程操作接口
    private AsyncTask asyncTask;
    //循环定时器
    private LoopTimer loopTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        playBinding = DataBindingUtil.setContentView(this,R.layout.play_activity);
        initViews();
        initPlayActivity();
        initListener();
    }

    @Override
    protected void onDestroy() {
        isUpdateProgress = false;
        isUpdateLrc = false;
        unregisterReceiver(broadcastReceive);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void initViews(){
        tv_play_top_name = playBinding.top.tvPlayTopName;
        tv_play_top_author = playBinding.top.tvPlayTopAuthor;
        iv_play_top = playBinding.top.ivPlayTop;
        tv_play_bottom_current = playBinding.bottom.tvPlayBottomCurrent;
        tv_play_bottom_total = playBinding.bottom.tvPlayBottomTotal;
        sb_play_bottom_seekBar = playBinding.bottom.sbPlayBottomSeekBar;
        iv_play_bottom_mode = playBinding.bottom.ivPlayBottomMode;
        iv_play_bottom_pre = playBinding.bottom.ivPlayBottomPre;
        iv_play_bottom_play = playBinding.bottom.ivPlayBottomPlay;
        iv_play_bottom_next = playBinding.bottom.ivPlayBottomNext;
        ll_play_activity_layout = playBinding.llPlayActivityLayout;
        tv_lrc_textView = playBinding.tvLrcTextView;
        isSeekBarPress = false;
        netWorkRequest = NetWorkRequestFactory.getNetWorkRequest(this);
        asyncTask = AsyncTaskFactory.getAsyncTask();
        loopTimer = new LoopTimer();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initPlayActivity(){
        PlayService.PlayBinder.setSeekBar(sb_play_bottom_seekBar);
        //注册广播
        broadcastReceive = new PlayBroadcastReceive();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayService.BROADCAST_PREPARE);
        registerReceiver(broadcastReceive,filter);
        //更新界面
        update();
        //调暗播放界面背景
        Bitmap bitmap = black(BitmapFactory.decodeResource(getResources(),R.drawable.play_page_default_bg));
        ll_play_activity_layout.setBackground(new BitmapDrawable(getResources(),bitmap));
        //启动播放进度刷新线程
        startProgressThread();
        //加载歌词
        loadLrc(PlayService.PlayBinder.getCurMusic().getSongName());
    }

    /**
     * 如果是本地音乐，先扫描本地，若无显示无歌词界面让用户选择是否下载；
     * 如果是网络歌曲，先扫描本地，若无则直接下载。
     */
    private void loadLrc(final String songName) {
        asyncTask.scanLrc(songName, new CallBack() {
            @Override
            public void onSuccess(Result respond) { //本地有
                lrcContents = respond.getLrcContents();
                tv_lrc_textView.setLrc(lrcContents);
                tv_lrc_textView.invalidate();
                startLrcThread();
            }
            @Override
            public void onError(Result respond) { //本地无
                //网络获取
                if(PlayService.PlayBinder.getIsOnline()) {

                }
                downLrc(PlayService.PlayBinder.getCurMusic().getLrcUrl(),songName);
            }
        });
    }

    /**
     * 下载歌词
     */
    private void downLrc(String url, String songName) {
        netWorkRequest.DownLrc(url, songName, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                lrcContents = result.getLrcContents();
                tv_lrc_textView.setLrc(lrcContents);
                tv_lrc_textView.invalidate();
                startLrcThread();
            }

            @Override
            public void onError(Result result) {

            }
        });
    }

    /**
     * 每隔500ms刷新一次进度
     */
    private void startProgressThread(){
        isUpdateProgress = true;
        loopTimer.startLoopTimer(500, new LoopTimer.OnTimeUp() {
            @Override
            public void timeUp(Timer timer) {
                if(isUpdateProgress) {
                    //activity退出后有可能还有消息，在这里屏蔽掉
                    if(PlayService.PlayBinder == null) return;
                    if(!isSeekBarPress && PlayService.PlayBinder.isPlay()){
                        tv_play_bottom_current.setText(DensityUtil.valueTimeToString(PlayService.PlayBinder.getCurPosition()));
                        sb_play_bottom_seekBar.setProgress(PlayService.PlayBinder.getCurPosition());
                    }
                } else timer.cancel();
            }
        });
    }

    /**
     * 每隔50ms刷新一次歌词
     */
    private void startLrcThread(){
        isUpdateLrc = true;
        loopTimer.startLoopTimer(50, new LoopTimer.OnTimeUp() {
            @Override
            public void timeUp(Timer timer) {
                if(isUpdateLrc) {
                    int index = getLrcPos(PlayService.PlayBinder.getCurPosition(),lrcContents);
                    index--; // 实测需减1
                    if(tv_lrc_textView.index != index) {
                        tv_lrc_textView.setIndex(index);
                        tv_lrc_textView.invalidate();
                        if(PlayService.PlayBinder.isPlay())
                            tv_lrc_textView.startLrcAnimation();
                    }
                } else timer.cancel();
            }
        });
    }

    /**
     * 获取当前播放的歌词在歌词文本中的位置
     */
    private int getLrcPos(int position, List<LrcContent> lists) {
        int i;
        int len = lists.size();
        for(i = 0; i < len-1; i++){
            int timePre = lists.get(i).getTime();
            int timeNext = lists.get(i+1).getTime();
            if(position < timePre) break;
            else if((position>=timePre) && (position<timeNext)) {
                i++;
                break;
            }
        }
        if(position>=lists.get(len-1).getTime())
            i=len-1;
        return i;
    }

    /**
     * 调暗图片
     */
    private Bitmap black(Bitmap bitmap){
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int brightness = -60;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[] {
                1, 0, 0, 0, brightness,// 红色值
                0, 1, 0, 0, brightness,// 绿色值
                0, 0, 1, 0, brightness,// 蓝色值
                0, 0, 0, 1, 0 // 透明度
        });
        ColorMatrixColorFilter matrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(matrixColorFilter);
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        bitmap.recycle();
        return newBitmap;
    }

    /**
     * 设置监听
     */
    private void initListener(){
        iv_play_bottom_mode.setOnClickListener(this);
        iv_play_bottom_pre.setOnClickListener(this);
        iv_play_bottom_play.setOnClickListener(this);
        iv_play_bottom_next.setOnClickListener(this);
        sb_play_bottom_seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        iv_play_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 进度条监听
     */
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tv_play_bottom_current.setText(DensityUtil.valueTimeToString(progress));
            seekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarPress = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarPress = false;
            int progress = seekBar.getProgress();
            PlayService.PlayBinder.seekTo(progress);
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_bottom_mode:
                changePlayMode();
                update();
                break;
            case R.id.iv_play_bottom_pre:
                PlayService.PlayBinder.previous();
                break;
            case R.id.iv_play_bottom_play:
                if(PlayService.PlayBinder.isPlay())
                    PlayService.PlayBinder.pause();
                else PlayService.PlayBinder.continue_play();
                update();
                break;
            case R.id.iv_play_bottom_next:
                PlayService.PlayBinder.next();
                break;
            default:
                break;
        }
    }

    /**
     * 切换播放模式
     */
    private void changePlayMode() {
        if(PlayService.playMode == PlayService.PLAY_MODE_NORMAL){
            PlayService.playMode = PlayService.PLAY_MODE_RANDOM;
            iv_play_bottom_mode.setImageResource(R.drawable.play_mode_random);
        }
        else if(PlayService.playMode == PlayService.PLAY_MODE_RANDOM){
            PlayService.playMode = PlayService.PLAY_MODE_REPEAT;
            iv_play_bottom_mode.setImageResource(R.drawable.ic_play_btn_one);
        }
        else if(PlayService.playMode == PlayService.PLAY_MODE_REPEAT){
            PlayService.playMode = PlayService.PLAY_MODE_NORMAL;
            iv_play_bottom_mode.setImageResource(R.drawable.play_mode_normal);
        }
    }

    /**
     * 设置播放模式图片
     */
    private void setPlayModeImage(int mode) {
        switch (mode) {
            case PlayService.PLAY_MODE_NORMAL:
                iv_play_bottom_mode.setImageResource(R.drawable.play_mode_normal);
                break;
            case PlayService.PLAY_MODE_RANDOM:
                iv_play_bottom_mode.setImageResource(R.drawable.play_mode_random);
                break;
            case PlayService.PLAY_MODE_REPEAT:
                iv_play_bottom_mode.setImageResource(R.drawable.ic_play_btn_one);
                break;
        }
    }

    /**
     * 切换歌曲或加载播放界面时，更新播放界面
     */
    private void update(){
        Music curMusic = PlayService.PlayBinder.getCurMusic();
        if(curMusic == null) return;
        //顶部布局
        tv_play_top_name.setText(curMusic.getSongName());
        tv_play_top_author.setText(curMusic.getAuthor());
        //底部控制布局
        int current = PlayService.PlayBinder.getCurPosition();
        int duration = PlayService.PlayBinder.getDuration();
        tv_play_bottom_current.setText(DensityUtil.valueTimeToString(current));
        tv_play_bottom_total.setText(DensityUtil.valueTimeToString(duration));
        sb_play_bottom_seekBar.setMax(duration);
        sb_play_bottom_seekBar.setProgress(current);
        if(PlayService.PlayBinder.isPlay()) {
            iv_play_bottom_play.setImageResource(R.drawable.ic_pause);
        }
        else iv_play_bottom_play.setImageResource(R.drawable.ic_play);
        setPlayModeImage(PlayService.playMode);
    }

    /**
     * 接收来自service的广播
     */
    private class PlayBroadcastReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PlayService.BROADCAST_PREPARE:
                    update();
                    break;
            }
        }
    }
}
