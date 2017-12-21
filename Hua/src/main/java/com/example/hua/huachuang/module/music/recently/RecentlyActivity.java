package com.example.hua.huachuang.module.music.recently;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.databinding.ActivityMusicRecentlyBinding;
import com.example.hua.huachuang.module.music.common.FragmentListView;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/25.
 */

public class RecentlyActivity extends BaseActivity<ActivityMusicRecentlyBinding> {
    public static final String ACTION = "com.example.hua.huachuang.module.music.recently.RecentlyActivity";
    //底部布局容器
    private LinearLayout playBarContain;
    //界面内容容器
    private LinearLayout contentContain;
    //无播放记录界面
    private TextView noRecently;
    //临时的List，防止在历史纪录界面播放音乐时listView数据源改变
    private List<Music> tmpLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        //初始化本地变量
        initViews();
        setToolBarTitle(getString(R.string.music_recently));
        showContent(true);
        initPlayBar();
        initListView();
        if(ShareList.recentlyList.size()==0)
            showNoRecently();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,R.id.menu_clear,0,getString(R.string.more_clear));
        menu.findItem(R.id.menu_clear).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_clear:
                ShareList.recentlyList.clear();
                showNoRecently();
                break;
        }
        return true;
    }

    @Override
    public int getContentId() {
        return R.layout.activity_music_recently;
    }

    private void initViews() {
        playBarContain = mDataBinding.recentlyBottom;
        contentContain = mDataBinding.recentlyContent;
        noRecently = mDataBinding.noRecently;
    }

    private void initPlayBar() {
        FragmentPlayBar playBar = new FragmentPlayBar();
        playBar.add(getSupportFragmentManager(),R.id.recently_bottom);
    }

    private void initListView() {
        FragmentListView listView = new FragmentListView();
        listView.add(getSupportFragmentManager(),R.id.recently_list);
        listView.setPlayPos(PlayService.PlayBinder.getCurMusic(),ShareList.recentlyList);
        listView.setListDates(getTempList(ShareList.recentlyList));
        listView.setListType(FragmentListView.TYPE_RECENTLY);
    }

    /**
     * 显示无播放记录界面
     */
    private void showNoRecently() {
        noRecently.setVisibility(View.VISIBLE);
        contentContain.setVisibility(View.GONE);
    }

    /**
     * 获取临时播放记录
     */
    private List<Music> getTempList(List<Music> source) {
        List<Music> lists = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            lists.add(source.get(i));
        }
        return lists;
    }
}
