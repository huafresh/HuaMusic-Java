package com.example.hua.huachuang.module.news.child;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.adapter.MyViewHolder;
import com.example.hua.huachuang.adapter.RecyclerAdapter;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.base.OnRefreshListener;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.bean.news.NewInfo;
import com.example.hua.huachuang.databinding.FragmentNewsMainBinding;
import com.example.hua.huachuang.databinding.NewItemBinding;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.network.NetWorkRequest;
import com.example.hua.huachuang.network.NetWorkRequestFactory;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/17.
 */

public class Fragment_news_main extends BaseFragment<FragmentNewsMainBinding> {

    private RecyclerView mRecyclerView;
    private List<NewInfo> mListDates;
    private RecyclerAdapter adapter;
    private NetWorkRequest netWorkRequest;
    private static final String NEW_FROM = "http://news.qq.com/";
    private SwipeRefreshLayout refreshLayout;
    private boolean isRefresh = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        getNewsInfo();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new MyItemDecoration(this));

    }

    private void initViews() {
        refreshLayout = fragmentDataBinding.swipeRefresh;
        TypedValue color = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.huaColorTheme,color,true);
        refreshLayout.setColorSchemeColors(color.data);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtil.isNetworkConnected(getActivity())) {
                    isRefresh = true;
                    getNewsInfo();
                } else {
                    refreshLayout.setRefreshing(false);
                    CommonUtil.toast("无网络连接");
                }
            }
        });

        mRecyclerView = fragmentDataBinding.recyclerListNew;
        mListDates = new ArrayList<>();
        netWorkRequest = NetWorkRequestFactory.getNetWorkRequest(getActivity());
    }

    private void doConvert(MyViewHolder myViewHolder, NewInfo item) {
        myViewHolder.setText(R.id.new_title,item.getTitle())
                    .setImageUrl(R.id.new_image,item.getPicLink());
    }

    private void getNewsInfo() {
        //设置加载失败的监听
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        if(!NetworkUtil.isNetworkConnected(getActivity())) {
            CommonUtil.toast("无网络连接");
            showError();
            return;
        }
        getMainNews();
    }

    private void doRefresh() {
        if(!NetworkUtil.isNetworkConnected(getActivity())) {
            CommonUtil.toast("无网络连接");
            showError();
        } else {
            getMainNews();
        }
    }

    private void getMainNews() {
        netWorkRequest.getMainNewsPreview(NEW_FROM, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                if(!isRefresh)
                    initRecyclerView(result);
                else doRefreshRecycler(result);
                showContent();
            }
            @Override
            public void onError(Result result) {
                showError();
            }
        });
    }

    private void initRecyclerView(Result result) {
        mListDates = result.getNewLists();
        adapter = new RecyclerAdapter<NewInfo>(getActivity(),R.layout.new_item,mListDates) {
            @Override
            public void Convert(MyViewHolder myViewHolder, NewInfo item) {
                doConvert(myViewHolder,item);
            }
        };
        adapter.addOnItemClick(new MyViewHolder.OnItemClick() {
            @Override
            public void onClick(View v, int position) {
                if(NetworkUtil.isNetworkConnected(getActivity())) {
                    openDetail(v,position);
                } else CommonUtil.toast("无网络连接");
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void doRefreshRecycler(Result result) {
        mListDates.clear();
        mListDates.addAll(result.getNewLists());
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        isRefresh = false;
    }

    private void openDetail(View v,int position) {
        NewInfo info = mListDates.get(position);
        Intent intent = new Intent(ActivityNewDetail.ACTION);
        intent.putExtra("newInfo",info);
        startActivity(intent);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_news_main;
    }
}
