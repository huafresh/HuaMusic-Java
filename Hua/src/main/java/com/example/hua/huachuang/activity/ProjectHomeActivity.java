package com.example.hua.huachuang.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.databinding.ActivityProjectHomeBinding;
import com.example.hua.huachuang.utils.ShareUtil;

/**
 * Created by hua on 2017/3/15.
 */

public class ProjectHomeActivity extends BaseActivity<ActivityProjectHomeBinding> {

    public static final String ACTION = "com.example.hua.huachuang.activity.ProjectHomeActivity";
    private TextView homeIntroduce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        showContent(true);
        setToolBarTitle(getString(R.string.nav_menu_home));
//        homeIntroduce = mDataBinding.homeIntroduce;
//        homeIntroduce.setAutoLinkMask(Linkify.ALL);
//        homeIntroduce.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getContentId() {
        return R.layout.activity_project_home;
    }
}
