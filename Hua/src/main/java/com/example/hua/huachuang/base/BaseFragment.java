package com.example.hua.huachuang.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.databinding.FragmentBaseBinding;


/**
 * Created by hua on 2017/2/17.
 * 处理布局加载
 */

public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {

    /**
     * 相应子fragment的binding
     */
    public DB fragmentDataBinding;

    private FragmentBaseBinding baseBinding;

    /**
     * base布局相关对象
     */
    private LinearLayout baseContain;
    private LinearLayout baseLoading;
    private LinearLayout llLoading;
    private LinearLayout llRetry;

    private AnimationDrawable mAnimationDrawable;
    private ImageView loadingImageView;

    private OnRefreshListener mRefreshListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDataBinding = DataBindingUtil.inflate(inflater,getContentId(),null,false);
        baseBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_base,null,false);

        initViews();
        setListener();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        baseContain.addView(fragmentDataBinding.getRoot(),params);
        fragmentDataBinding.getRoot().setVisibility(View.GONE);

        //开启加载中时的图片动画
        mAnimationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        if(!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }

        return baseBinding.getRoot();
    }

    private void initViews() {
        baseContain = baseBinding.baseFragmentContain;
        loadingImageView = baseBinding.fragmentBaseLoading.ivLoadImage;
        baseLoading = baseBinding.baseFragmentLoading;
        llLoading = baseBinding.fragmentBaseLoading.llLoading;
        llRetry = baseBinding.fragmentBaseError.llErrorRefresh;
    }

    private void setListener() {
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRefreshListener != null) {
                    showLoading();
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * @return 内容布局资源id
     */
    public abstract int getContentId();

    /**
     * 默认显示的都是加载中的视图，需要调用此方法来显示你自己的视图
     */
    public void showContent() {
        //显示实际内容
        baseLoading.setVisibility(View.GONE);
        fragmentDataBinding.getRoot().setVisibility(View.VISIBLE);

        //停止动画
        if(mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
        llRetry.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    public void showError() {
        llLoading.setVisibility(View.GONE);
        llRetry.setVisibility(View.VISIBLE);
    }

    /**
     * 当加载失败时，设置此监听实现点击屏幕重新加载
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }
}
