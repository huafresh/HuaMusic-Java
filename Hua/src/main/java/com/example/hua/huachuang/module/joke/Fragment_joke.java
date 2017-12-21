package com.example.hua.huachuang.module.joke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.adapter.MyFragmentPagerAdapter;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.databinding.FragmentJokeBinding;
import com.example.hua.huachuang.module.joke.child.FragmentJokeRecommend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/17.
 */

public class Fragment_joke extends BaseFragment<FragmentJokeBinding> {

    private List<Fragment> fragmentList;
    private List<String> fragmentTitle;
    public FragmentJokeRecommend jokeRecommend;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContent();
        initFragmentDates();
        fragmentDataBinding.jokeTabLayout.setupWithViewPager(fragmentDataBinding.jokeViewPager);
        fragmentDataBinding.jokeTabLayout.setTabMode(TabLayout.MODE_FIXED);
        fragmentDataBinding.jokeViewPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(),fragmentList,fragmentTitle));
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_joke;
    }

    private void initFragmentDates() {
        fragmentList = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
        jokeRecommend = new FragmentJokeRecommend();
        fragmentList.add(jokeRecommend);
        //fragmentList.add(new FragmentJokePic());
        //fragmentList.add(new FragmentJokeSession());
        fragmentTitle.add("推荐");
        //fragmentTitle.add("图片");
        //fragmentTitle.add("段子");
    }

}
