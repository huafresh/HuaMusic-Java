package com.example.hua.huachuang.module.joke.child;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.ShareUtil;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by hua on 2017/3/14.
 */

public class MyMediaController extends MediaController {

    private View mContentView;
    private ImageButton mSound;
    private ImageButton mFullScreen;
    private boolean isSound;
    private Context mContext;
    private VideoView mVideoView;
    private AudioManager audioManager;
    private ShareUtil shareUtil;


    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyMediaController(Context context, VideoView videoView) {
        super(context);
        init(context);
        mVideoView = videoView;
    }

    private void init(Context context) {
        mContext = context;
        isSound = true;
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        shareUtil = ShareUtil.getInstance(ShareUtil.SHARE_CFG);
    }

    private void setListener() {
        mSound = (ImageButton) mContentView.findViewById(R.id.mediacontroller_sound);
        mFullScreen = (ImageButton) mContentView.findViewById(R.id.mediacontroller_full_screen);
        mSound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVolume();
                updateSoundView();
            }
        });
        mFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.toast("全屏");
            }
        });
    }


    private void setVolume() {
        if(isSound) {
            isSound = false;
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            shareUtil.putInt("lastVolume",volume);
        } else {
            isSound = true;
            int lastVolume = shareUtil.getInt("lastVolume");
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,lastVolume,0);
        }
    }

    private void updateSoundView() {
        if(isSound)
            mSound.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.btn_mute_open));
        else mSound.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.btn_mute_mute));
    }

    @Override
    protected View makeControllerView() {
        mContentView = super.makeControllerView();
        setListener();
        updateSoundView();
        return mContentView;
    }
}
