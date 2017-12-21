package com.example.hua.huachuang.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.databinding.ActivityScanDownBinding;
import com.example.hua.huachuang.utils.ShareUtil;

/**
 * Created by hua on 2017/3/11.
 */

public class ScanDownActivity extends BaseActivity<ActivityScanDownBinding> {
    public static final String ACTION = "com.example.hua.huachuang.activity.ScanDownActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        showContent(true);
        setToolBarTitle(getString(R.string.nav_menu_down));
    }

    @Override
    public int getContentId() {
        return R.layout.activity_scan_down;
    }
}
