package com.example.hua.huachuang.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.databinding.AppMainBottomBinding;
import com.example.hua.huachuang.activity.PlayActivity;
import com.example.hua.huachuang.service.PlayService;

/**
 * Created by hua on 2017/2/28.
 * 处理PlayBar
 */

public class FragmentPlayBar extends Fragment implements View.OnClickListener{

    private AppMainBottomBinding mBottomBinding;
    //底部布局相关对象
    private RelativeLayout bottomContain;
    private ImageView cover;
    private TextView songName;
    private TextView author;
    private ImageView play;
    private ImageView next;
    private ImageView playList;
    private BroadcastReceiver receiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBottomBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.app_main_bottom,null,false);
        initViews();
        setListener();
        update();
        registerReceiver();
        return mBottomBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void add(FragmentManager fm, @IdRes int idRes) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(idRes,this);
        ft.commit();
    }

    private void initViews() {
        bottomContain = mBottomBinding.appMainBottom;
        songName = mBottomBinding.tvBottomTextName;
        author = mBottomBinding.tvBottomTextAuthor;
        cover = mBottomBinding.bottomImage;
        play = mBottomBinding.ivBottomPlay;
        next = mBottomBinding.ivBottomNext;
        playList = mBottomBinding.ivBottomMore;
    }

    private void setListener() {
        bottomContain.setOnClickListener(this);
        playList.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void registerReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                update();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayService.BROADCAST_PREPARE);
        filter.addAction(PlayService.BROADCAST_COMPLETION);
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_main_bottom:
                if(PlayService.PlayBinder.getCurMusic()==null) return;
                Intent intent = new Intent(PlayActivity.ACTION);
                startActivityForResult(intent,0);
                break;
            case R.id.iv_bottom_more:
                break;
            case R.id.iv_bottom_play:
                if(PlayService.PlayBinder.isPlay())
                    PlayService.PlayBinder.pause();
                else PlayService.PlayBinder.continue_play();
                update();
                break;
            case R.id.iv_bottom_next:
                PlayService.PlayBinder.next();
                update();
                break;
        }
    }

    /**
     * 刷新底部布局
     */
    public void update() {
        if(PlayService.PlayBinder==null) return;
        Music curMusic = PlayService.PlayBinder.getCurMusic();
        if(curMusic == null) return;
        songName.setText(curMusic.getSongName());
        author.setText(curMusic.getAuthor());
        if(PlayService.PlayBinder.isPlay())
            play.setImageResource(R.drawable.ic_play_bar_btn_pause);
        else play.setImageResource(R.drawable.ic_play_bar_btn_play);
    }
}
