package com.example.hua.huachuang.module.music.online;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.base.OnRefreshListener;
import com.example.hua.huachuang.bean.music.BillBoard;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.databinding.FragmentOnlineScrollBinding;
import com.example.hua.huachuang.databinding.OnlinePreviewItemBinding;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.manager.NetWork_OkHttp;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hua on 2017/3/4.
 */

public class FragmentPreviewScroll extends BaseFragment<FragmentOnlineScrollBinding> {

    private LinearLayout scrollView;
    private NetWork_OkHttp netWork_okHttp;
    //用于排序
    private BillBoard[] billBoards;
    private final int BillBoardSum = 7;
    //用于同步
    private int sum;
    //顺序存储每个榜单对应的数据绑定类
    private List<OnlinePreviewItemBinding> itemBindings;
    //顺序存储榜单类型
    private int[] types;
    private boolean isRefresh = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViews();
        getItemBindings();
        setListener();
        getBillboardDates();
    }

    private void getViews() {
        scrollView = fragmentDataBinding.scroll;
        netWork_okHttp = NetWork_OkHttp.getInstance(getActivity());
        billBoards = new BillBoard[BillBoardSum];
        itemBindings = new ArrayList<>();
        types = new int[BillBoardSum];
    }


    public void add(FragmentManager fm, @IdRes int id) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(id,this);
        ft.commit();
    }

    private void setListener() {
        for (OnlinePreviewItemBinding itemBinding : itemBindings) {
            itemBinding.previewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NetworkUtil.isNetworkConnected(getActivity())) {
                        int order = (int) v.getTag();
                        Intent intent = new Intent(BillBoardActivity.ACTION);
                        intent.putExtra("type",types[order]);
                        startActivity(intent);
                    } else {
                        CommonUtil.toast("无网络连接");
                    }
                }
            });
        }
    }

    private void getBillboardDates() {
        if(!NetworkUtil.isNetworkConnected(getActivity())) {
            showError();
            CommonUtil.toast("无网络连接");
        }
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtil.isNetworkConnected(getActivity())) {
                    isRefresh = true;
                    showLoading();
                    doObtain();
                } else CommonUtil.toast("无网络连接");
            }
        });
        doObtain();
    }

    private void doObtain() {
        sum = 0;
        getBillBoardInfo(fillParams(BillBoard.Type.HOT,3,0,0));
        getBillBoardInfo(fillParams(BillBoard.Type.NEW,4,0,1)); //4才会返回3首歌，fuck
        getBillBoardInfo(fillParams(BillBoard.Type.CHINA,3,0,2));
        getBillBoardInfo(fillParams(BillBoard.Type.EUROPE,3,0,3));
        getBillBoardInfo(fillParams(BillBoard.Type.OLD,3,0,4));
        getBillBoardInfo(fillParams(BillBoard.Type.NET,3,0,5));
        getBillBoardInfo(fillParams(BillBoard.Type.ROCK,3,0,6));
    }

    private HashMap<String,Integer> fillParams(int type, int sum, int offset,int order) {
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("type",type);
        hashMap.put("sum",sum);
        hashMap.put("offset",offset);
        hashMap.put("order",order);
        types[order] = type;
        return hashMap;
    }

    private void getBillBoardInfo(HashMap<String,Integer> param) {
        netWork_okHttp.getBillBoard(param, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                BillBoard billBoard = result.getBillBoard();
                int order = billBoard.getOrder();
                if(order==0) {
                    itemBindings.get(order).setTitle(getString(R.string.title_main));
                    itemBindings.get(order).setIsHaveTitle(true);
                } else if(order==2) {
                    itemBindings.get(order).setTitle(getString(R.string.title_type));
                    itemBindings.get(order).setIsHaveTitle(true);
                }
                itemBindings.get(order).previewItem.setTag(order);
                itemBindings.get(order).setBillBoard(billBoard);
                if(++sum==7) {
                    showContent();
                }
            }
            @Override
            public void onError(Result result) {
                //CommonUtil.toast("服务器异常");
                showError();
            }
        });
    }


    private void getItemBindings() {
        for (int i = 0; i < BillBoardSum; i++) {
            OnlinePreviewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.online_preview_item,null,false);
            if(i==0||i==2) {
                binding.previewTitleContain.setVisibility(View.VISIBLE);
            }
            scrollView.addView(binding.getRoot());
            itemBindings.add(binding);
        }
    }


    @Override
    public int getContentId() {
        return R.layout.fragment_online_scroll;
    }
}
