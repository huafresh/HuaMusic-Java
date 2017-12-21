package com.example.hua.huachuang.module.joke.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.databinding.FragmentNewsTechnologyBinding;

/**
 * Created by hua on 2017/2/17.
 */

public class FragmentJokeSession extends BaseFragment<FragmentNewsTechnologyBinding> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_news_technology;
    }
}
