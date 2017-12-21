package com.example.hua.huachuang.module.music.online;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.bean.music.BillBoard;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.databinding.ActivityBillboardBinding;
import com.example.hua.huachuang.databinding.BillboardHeaderBinding;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.manager.NetWork_OkHttp;
import com.example.hua.huachuang.module.music.common.FragmentListView;
import com.example.hua.huachuang.network.NetWorkRequestFactory;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.LoopTimer;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

/**
 * Created by hua on 2017/3/6.
 */

public class BillBoardActivity extends BaseActivity<ActivityBillboardBinding> {
    public static final String ACTION = "com.example.hua.huachuang.module.music.online.BillBoardActivity";
    private BillboardHeaderBinding headerBinding;
    private int billBoardType;
    private NetWork_OkHttp netWork_okHttp;
    private List<Music> mDataLists;
    private FragmentListView fragmentListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        getViews();
        getListDates();
        setToolBarTitle(getString(R.string.music_online));
    }

    private void getViews() {
        billBoardType = getIntent().getIntExtra("type",0);
        headerBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.billboard_header,null,false);
        netWork_okHttp = NetWorkRequestFactory.getNetWorkRequest(this);
        mDataLists = new ArrayList<>();
    }

    private void addListView() {
        fragmentListView = new FragmentListView();
        fragmentListView.add(getSupportFragmentManager(),R.id.board_list_contain);
        fragmentListView.setHeader(headerBinding.getRoot());
        fragmentListView.setListType(FragmentListView.TYPE_ONLINE);
        fragmentListView.setListDates(mDataLists);
    }

    private void getListDates() {
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("type",billBoardType);
        hashMap.put("sum",50);
        hashMap.put("offset",0);
        hashMap.put("order",0);
        getBillBoardInfo(hashMap);
    }

    private void getBillBoardInfo(HashMap<String,Integer> param) {
        //获取榜单信息
        netWork_okHttp.getBillBoard(param, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                doOnSuccess(result);
            }
            @Override
            public void onError(Result result) {
                //CommonUtil.toast("服务器异常");
                showError();
            }
        });
    }

    private void doOnSuccess(Result result) {
        obtainMusicInfo(mDataLists,result.getBillBoard().getSong_list());
        headerBinding.setBill(result.getBillBoard());
        showContent(true);
        addListView();
        addPlayBar();
        getMusicLink();
        LoopTimer loopTimer = new LoopTimer();
        //等listView加载完再隐藏
        loopTimer.startDelayTimer(200, new LoopTimer.OnTimeUp() {
            @Override
            public void timeUp(Timer timer) {
                mDataBinding.viewTmp.setVisibility(View.GONE);
            }
        });
    }

    private void getMusicLink() {
        //获取歌曲下载链接
        for (final Music music : mDataLists) {
            netWork_okHttp.getOnlineMusicLink(music.getSong_id(),new CallBack() {
                @Override
                public void onSuccess(Result result) {
                    BillBoard billBoard = result.getBillBoard();
                    music.setMusicLink(billBoard.getBitrate().getFile_link());
                }
                @Override
                public void onError(Result result) {
                }
            });
        }
    }

    private void obtainMusicInfo(List<Music> musicLists, List<BillBoard.musicInfo> lists) {
        for (BillBoard.musicInfo info : lists) {
            Music music = new Music();
            music.setSongName(info.getTitle());
            music.setAuthor(info.getAuthor());
            music.setTitleAndAuthor(info.getAuthor()+" - "+info.getTitle());
            music.setCoverUrl(info.getPic_small());
            music.setLrcUrl(info.getLrclink());
            music.setSong_id(info.getSong_id());
            musicLists.add(music);
        }
    }

    private void addPlayBar() {
        FragmentPlayBar fragmentPlayBar = new FragmentPlayBar();
        fragmentPlayBar.add(getSupportFragmentManager(),R.id.board_play_bar_contain);
    }

    @Override
    public int getContentId() {
        return R.layout.activity_billboard;
    }
}
