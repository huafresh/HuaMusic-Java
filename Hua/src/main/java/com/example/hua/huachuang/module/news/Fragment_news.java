package com.example.hua.huachuang.module.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.hua.huachuang.adapter.MyFragmentPagerAdapter;
import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.databinding.FragmentNewsBinding;
import com.example.hua.huachuang.module.news.child.Fragment_news_main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/17.
 * 新闻界面，子view有：要闻、科技、本地
 */

public class Fragment_news extends BaseFragment<FragmentNewsBinding> {

    private List<Fragment> fragmentList;
    private List<String> fragmentTitle;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContent();
        initFragmentDates();
        fragmentDataBinding.newsTabLayout.setupWithViewPager(fragmentDataBinding.newsViewPager);
        fragmentDataBinding.newsTabLayout.setTabMode(TabLayout.MODE_FIXED);
        fragmentDataBinding.newsViewPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(),fragmentList,fragmentTitle));
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_news;
    }

    private void initFragmentDates() {
        fragmentList = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
        fragmentList.add(new Fragment_news_main());
        //fragmentList.add(new Fragment_news_technology());
        //fragmentList.add(new Fragment_news_local());
        fragmentTitle.add("要闻");
        //fragmentTitle.add("科技");
        //fragmentTitle.add("本地");
    }
}
