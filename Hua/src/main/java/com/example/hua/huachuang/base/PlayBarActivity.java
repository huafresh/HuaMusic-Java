package com.example.hua.huachuang.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.hua.huachuang.share.ShareList;

/**
 * Created by hua on 2017/2/21.
 * 处理底部布局相关逻辑，需调用setContain()来设置底部布局的容器和参数，
 * 并添加底部布局到主视图中.
 * 已弃用。改用fragment管理
 */

public abstract class PlayBarActivity<T extends ViewDataBinding> extends BaseActivity<T>
        implements View.OnClickListener{

    /**
     * 底部布局的数据绑定类
     */
    private AppMainBottomBinding mBottomBinding;

    /**
     * 容器和参数
     */
    private ViewGroup mContain;
    private ViewGroup.LayoutParams mParams;

    /**
     * 底部布局相关对象
     */
    private RelativeLayout bottomContain;
    private ImageView cover;
    private TextView songName;
    private TextView author;
    private ImageView play;
    private ImageView next;
    private ImageView playList;
    //操作服务的对象
    public PlayService.PlayBinder PlayBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.app_main_bottom,null,false);
        initViews();
        setListener();
    }

    private void initViews() {
        bottomContain = mBottomBinding.appMainBottom;
        cover = mBottomBinding.bottomImage;
        songName = mBottomBinding.tvBottomTextName;
        author = mBottomBinding.tvBottomTextAuthor;
        play = mBottomBinding.ivBottomPlay;
        next = mBottomBinding.ivBottomNext;
        playList = mBottomBinding.ivBottomMore;
        PlayBinder = PlayService.PlayBinder;
    }

    private void setListener() {
        bottomContain.setOnClickListener(this);
        cover.setOnClickListener(this);
        playList.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }


    /**
     * 设置容器和参数
     */
    public void setPlayBarContain(ViewGroup viewGroup, ViewGroup.LayoutParams params) {
        this.mContain = viewGroup;
        this.mParams = params;
        commit();
    }

    /**
     * 提交添加操作
     */
    public void commit() {
        mContain.removeAllViews();
        mContain.addView(mBottomBinding.getRoot(),mParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) { //播放界面返回
            update();
        }
    }

    @Override
    public void onClick(View v) {
        if(PlayService.PlayBinder.getCurMusic()==null)
            return;
        switch (v.getId()) {
            case R.id.app_main_bottom:
                Intent intent = new Intent(PlayActivity.ACTION);
                startActivityForResult(intent,0);
                break;
            case R.id.iv_bottom_more:
                break;
            case R.id.iv_bottom_play:
                if(PlayBinder.isPlay())
                    PlayBinder.pause();
                else PlayBinder.continue_play();
                update();
                break;
            case R.id.iv_bottom_next:
                PlayBinder.next();
                update();
                break;
        }
    }

    /**
     * 刷新底部布局
     */
    private void update() {
        if(PlayBinder==null) return;
        Music curMusic = PlayBinder.getCurMusic();
        if(curMusic == null) {
            if(ShareList.localList.size()!=0)
                curMusic = ShareList.localList.get(0);
            else return;
        }
        songName.setText(curMusic.getSongName());
        author.setText(curMusic.getAuthor());
        if(PlayBinder.isPlay())
            play.setImageResource(R.drawable.ic_play_bar_btn_pause);
        else play.setImageResource(R.drawable.ic_play_bar_btn_play);
    }
}
