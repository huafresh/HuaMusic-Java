package com.example.hua.huachuang.module.music.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.databinding.ActivityOnlinePreviewBinding;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.ShareUtil;

/**
 * Created by hua on 2017/3/4.
 */

public class PreviewActivity extends BaseActivity<ActivityOnlinePreviewBinding> {

    public static final String ACTION = "com.example.hua.huachuang.module.music.online.PreviewActivity";
    private FragmentPlayBar playBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        showContent(true);
        setToolBarTitle(getString(R.string.music_online));
        addPlayBar();
        addScrollFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,R.id.menu_search,0,getString(R.string.menu_search));
        menu.findItem(R.id.menu_search).setIcon(R.drawable.actionbar_search);
        menu.findItem(R.id.menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                CommonUtil.toast("search");
                break;
        }
        return true;
    }

    private void addPlayBar() {
        playBar = new FragmentPlayBar();
        playBar.add(getSupportFragmentManager(),R.id.preview_playBar_contain);
    }

    private void addScrollFragment() {
        FragmentPreviewScroll scrollFragment = new FragmentPreviewScroll();
        scrollFragment.add(getSupportFragmentManager(),R.id.preview_scroll);
    }

    @Override
    public int getContentId() {
        return R.layout.activity_online_preview;
    }
}
