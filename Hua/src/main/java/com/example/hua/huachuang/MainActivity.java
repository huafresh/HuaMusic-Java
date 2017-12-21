package com.example.hua.huachuang;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hua.huachuang.adapter.MyFragmentPagerAdapter;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.custom.MyViewPager;
import com.example.hua.huachuang.custom.SlideMenuLayout;
import com.example.hua.huachuang.data.SDUtil;
import com.example.hua.huachuang.databinding.ActivityMainBinding;
import com.example.hua.huachuang.module.joke.Fragment_joke;
import com.example.hua.huachuang.module.music.FragmentMusic;
import com.example.hua.huachuang.module.news.Fragment_news;
import com.example.hua.huachuang.service.DownService;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.utils.ExecutorUtil;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vov.vitamio.Vitamio;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private MyViewPager mainViewPager;
    private List<Fragment> fragmentList;

    /**
     * toolBar中可点击的ImageView
     */
    private ImageView newsTab;
    private ImageView musicTab;
    private ImageView jokeTab;
    //用于绑定播放service
    private ServiceConnection serviceConnection1;
    //用于绑定下载service
    private ServiceConnection serviceConnection2;
    //数据集中在这里，然后一起写入文件
    private HashMap<String, Object> hashMap;
    //保存记录的文件名
    private final String saveName = "history";
    private FragmentPlayBar playBar;
    public SlideMenuLayout slideMenuLayout;
    private boolean isNight;
    //笑话播放视频时，切换viewPager时应该关闭
    private Fragment_joke fragmentJoke;
    //用于ViewPager判断滑动方向
    private int startX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNight = ShareUtil.getInstance("cfg").getBoolean("isNight");
        if (isNight) setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        //解决Fragment中使用SurfaceView切换时闪屏的问题。
        //SurfaceView在itamio框架播放视频时会被添加。V
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        doRestore();
        showContent(false);
        init();
        initViews();
        initFragmentList();
        setListener();
        initViewPager();
        //解决侧滑菜单图片变灰
        //mDataBinding.navigationView.setItemIconTintList(null);
        addPlayBar();
        //changRedDrawable2White();
    }

    @Override
    public void onBackPressed() {
        if (slideMenuLayout.isMenuOpen()) {
            slideMenuLayout.closeMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (playBar != null) {
            playBar.update();
        }
    }

    @Override
    protected void onDestroy() {
        doSave();
        Intent intent1 = new Intent(this, PlayService.class);
        Intent intent2 = new Intent(this, PlayService.class);
        unbindService(serviceConnection1);
        //stopService(intent1);
        unbindService(serviceConnection2);
        //stopService(intent2);
        ExecutorUtil.stopThreadPool();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        doSave();
    }

    /**
     * 保存记录
     */
    private void doSave() {
        hashMap.clear();
        //播放纪录
        hashMap.put("history", ShareList.recentlyList);
        //下载记录
        hashMap.put("down", ShareList.haveDownList);
        //本地音乐
        hashMap.put("local", ShareList.localList);
        //正在下载的音乐列表
        hashMap.put("downingMusic", ShareList.musicDowningList);
        SDUtil.object2file(this, hashMap, saveName);
    }

    /**
     * 恢复播放记录、下载记录
     */
    private void doRestore() {
        hashMap = DensityUtil.cast(SDUtil.file2object(this, saveName));
        //首次使用时没有记录
        if (hashMap == null) return;
        if (isHaveKey("history")) ShareList.recentlyList = DensityUtil.cast(hashMap.get("history"));
        if (isHaveKey("down")) ShareList.haveDownList = DensityUtil.cast(hashMap.get("down"));
        if (isHaveKey("local")) ShareList.localList = DensityUtil.cast(hashMap.get("local"));
        if (isHaveKey("downingMusic"))
            ShareList.musicDowningList = DensityUtil.cast(hashMap.get("downingMusic"));
    }

    /**
     * 判断是否有该键值
     */
    private boolean isHaveKey(String key) {
        return hashMap.get(key) != null;
    }

    @Override
    public int getContentId() {
        return R.layout.activity_main;
    }


    private void init() {
        //启动播放服务
        Intent intent1 = new Intent(this, PlayService.class);
        startService(intent1);
        serviceConnection1 = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.PlayBinder = (PlayService.PlayBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        bindService(intent1, serviceConnection1, BIND_AUTO_CREATE);
        //启动下载服务
        Intent intent2 = new Intent(this, DownService.class);
        serviceConnection2 = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownService.downBinder = (DownService.DownBinder) service;
                if (playBar != null) playBar.update();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        startService(intent2);
        bindService(intent2, serviceConnection2, BIND_AUTO_CREATE);
        //初始化视频播放框架
        Vitamio.isInitialized(this);
    }

    private void initViews() {
        newsTab = mDataBinding.mainInclude.appMainToolBar.imageNews;
        musicTab = mDataBinding.mainInclude.appMainToolBar.imageMusic;
        jokeTab = mDataBinding.mainInclude.appMainToolBar.imageJoke;
        newsTab.setSelected(true);
        musicTab.setSelected(false);
        jokeTab.setSelected(false);
        mainViewPager = mDataBinding.mainInclude.contentViewPager;
        hashMap = new HashMap<>();
        slideMenuLayout = mDataBinding.slideMenuHua;
    }

    private void initViewPager() {
        mainViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, null));
        mainViewPager.setOffscreenPageLimit(2);
        mainViewPager.setCurrentItem(1);
        mainViewPager.setSlideMenuLayout(slideMenuLayout);
    }

    private void addPlayBar() {
        playBar = new FragmentPlayBar();
        playBar.add(getSupportFragmentManager(), R.id.play_bar_contain);
    }

    private void initFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentJoke = new Fragment_joke();
        fragmentList.add(new Fragment_news());
        fragmentList.add(new FragmentMusic());
        fragmentList.add(fragmentJoke);
    }

    private void setListener() {
        mainViewPager.addOnPageChangeListener(this);
        newsTab.setOnClickListener(this);
        musicTab.setOnClickListener(this);
        jokeTab.setOnClickListener(this);
        if (isNight) {
            newsTab.setImageDrawable(DensityUtil.tintDrawable(this, R.drawable.titlebar_discover_normal));
            musicTab.setImageDrawable(DensityUtil.tintDrawable(this, R.drawable.titlebar_music_normal));
            jokeTab.setImageDrawable(DensityUtil.tintDrawable(this, R.drawable.titlebar_friends_normal));
        }
        mDataBinding.mainInclude.appMainToolBar.slideMenu.setOnClickListener(this);
        mDataBinding.mainInclude.appMainToolBar.search.setOnClickListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                newsTab.setSelected(true);
                musicTab.setSelected(false);
                jokeTab.setSelected(false);
                if (fragmentJoke != null &&
                        fragmentJoke.jokeRecommend != null)
                    fragmentJoke.jokeRecommend.stopVideoView();
                break;
            case 1:
                newsTab.setSelected(false);
                musicTab.setSelected(true);
                jokeTab.setSelected(false);
                if (fragmentJoke != null &&
                        fragmentJoke.jokeRecommend != null)
                    fragmentJoke.jokeRecommend.stopVideoView();
                break;
            case 2:
                newsTab.setSelected(false);
                musicTab.setSelected(false);
                jokeTab.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slide_menu:
                Log.e("hua", "**Toggle*****");
                slideMenuLayout.Toggle();
                break;
            case R.id.image_news:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.image_music:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.image_joke:
                mainViewPager.setCurrentItem(2);
                break;
            case R.id.search:
                break;
        }
    }

    private void setDownLoad() {
        if (ShareList.musicDowningList.size() == 0) {
            Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
            fillDates();
            addToDownList(ShareList.musicDowningList);
        } else {
            DownService.downBinder.cancelAllTask();
            ShareList.musicDowningList.clear();
        }
    }

    private void addToDownList(List<DowningInfo> lists) {
        for (int i = 0; i < lists.size(); i++) {
            DowningInfo info = lists.get(i);
            long id = DownService.downBinder.addDownTask(info);
            info.setId(id);
        }
    }

    private void fillDates() {
        String url = "http://ucdl.25pp.com/fs08/2017/01/20/2/2_87a290b5f041a8b512f0bc51595f839a.apk";
        int sum = ShareList.musicDowningList.size();
        for (int i = 0; i < 5; i++) {
            DowningInfo info = new DowningInfo();
            int j = i + sum;
            fillMusicDowningInfo(info, url, "有何不可" + j);
            ShareList.musicDowningList.add(info);
        }
    }

    private void fillMusicDowningInfo(DowningInfo info, String url, String displayName) {
        info.setUrl(url);
        info.setDirPath(DownService.MUSIC_PATH);
        info.setFileName(displayName + ".mp3");
        info.setDisplayName(displayName);
    }

}
