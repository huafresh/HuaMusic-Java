package com.example.hua.huachuang.module.music.down;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.adapter.MyFragmentPagerAdapter;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.databinding.ActivityMusicDownBinding;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/25.
 */

public class DownActivity extends BaseActivity<ActivityMusicDownBinding> {

    public static final String ACTION = "com.example.hua.huachuang.module.music.down.DownActivity";
    //底部布局容器
    private LinearLayout playBarContain;
    //界面内容容器
    private LinearLayout contentContain;
    //临时的List，防止在历史纪录界面播放音乐时listView数据源改变
    private List<Music> tmpLists;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //子View
    private List<Fragment> fragmentList;
    private List<String> fragmentTitle;

    private Fragment_down_down down;
    private Fragment_down_downing downing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        init();
        showContent(true);
    }

    @Override
    public int getContentId() {
        return R.layout.activity_music_down;
    }

    private void  init() {
        //初始化本地变量
        initViews();
        FragmentPlayBar fragment = new FragmentPlayBar();
        fragment.add(getSupportFragmentManager(),R.id.down_playBar_contain);
        setToolBarTitle(getString(R.string.music_down));
        initFragmentDates();
        initTabLayout();
        initViewPager();
    }

    private void initViews() {
        tabLayout = mDataBinding.downTabLayout;
        viewPager = mDataBinding.downViewPager;
        playBarContain = mDataBinding.downPlayBarContain;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,R.id.menu_clear,0,getString(R.string.more_clear));
        menu.findItem(R.id.menu_clear).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initViewPager() {
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,fragmentTitle));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0) {
                    setItemVisible(R.id.menu_clear,true);
                } else setItemVisible(R.id.menu_clear,false);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_clear:
                ShareList.haveDownList.clear();
                down.showNoDown();
                break;
        }
        return true;
    }

    private void initFragmentDates() {
        fragmentList = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
        down = new Fragment_down_down();
        downing = new Fragment_down_downing();
        downing.setTargetFragment(down,0);
        fragmentList.add(down);
        fragmentList.add(downing);
        fragmentTitle.add("已下载");
        fragmentTitle.add("下载中");
    }

    /**
     * 设置已下载列表第一首歌有头
     */
    private void setFirstHaveHeader(List<Music> lists) {
        for (int i = 0; i < lists.size(); i++) {
            if(i==0)
                lists.get(i).setHaveHeader(true);
            else lists.get(i).setHaveHeader(false);
        }
    }

}
