package com.example.hua.huachuang.module.music.down;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.databinding.FragmentDownDownBinding;
import com.example.hua.huachuang.module.music.common.FragmentListView;
import com.example.hua.huachuang.share.ShareList;

/**
 * Created by hua on 2017/2/28.
 */

public class Fragment_down_down extends BaseFragment<FragmentDownDownBinding> {

    private LinearLayout listContain;
    private TextView noDown;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListViewFragment();
        showContent();
        updateList();
    }

    private void initViews() {
        noDown = fragmentDataBinding.noDown;
        listContain = fragmentDataBinding.listContain;
    }

    private void initListViewFragment() {
        FragmentListView fragment = new FragmentListView();
        fragment.add(getActivity().getSupportFragmentManager(),R.id.list_contain);
        fragment.setListDates(ShareList.haveDownList);
        fragment.setListType(FragmentListView.TYPE_DOWN);
    }

    public void showNoDown() {
        noDown.setVisibility(View.VISIBLE);
        listContain.setVisibility(View.GONE);
    }

    private void showDown() {
        noDown.setVisibility(View.GONE);
        listContain.setVisibility(View.VISIBLE);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_down_down;
    }

    public void updateList() {
        if(ShareList.haveDownList.size()==0)
            showNoDown();
        else showDown();
    }
}
