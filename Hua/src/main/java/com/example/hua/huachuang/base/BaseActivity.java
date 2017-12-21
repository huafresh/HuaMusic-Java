package com.example.hua.huachuang.base;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.databinding.ActivityBaseBinding;
import com.example.hua.huachuang.utils.DensityUtil;

import java.lang.reflect.Method;

/**
 * Created by hua on 2017/2/17.
 * 主要处理：toolbar的添加，显示加载中、加载失败视图
 */

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    /**
     * 主视图的数据绑定类
     */
    public DB mDataBinding;

    /**
     * baseActivity的数据绑定类
     */
    private ActivityBaseBinding baseBinding;

    /**
     * base布局中的相关对象
     */
    private LinearLayout baseContain;
    private LinearLayout baseLoading;
    private LinearLayout llLoading;
    private LinearLayout llRetry;

    private AnimationDrawable mAnimationDrawable;
    private ImageView loadingImageView;

    private OnRefreshListener mRefreshListener;

    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        baseBinding = DataBindingUtil.setContentView(this,R.layout.activity_base);
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this),getContentId(),null,false);

        initViews();
        setListener();

        initToolbar();

        //启动时都启动加载中动画
        mAnimationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        if(!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }

    private void initViews() {
        loadingImageView = baseBinding.activityBaseLoading.ivLoadImage;
        baseLoading = baseBinding.baseLoading;
        llLoading = baseBinding.activityBaseLoading.llLoading;
        llRetry = baseBinding.activityBaseError.llErrorRefresh;
        baseContain = baseBinding.baseActivityContain;
        toolbar = baseBinding.toolBar;
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

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        if(Build.VERSION.SDK_INT>=19) {
            //设置toolbar的高度为加上状态栏高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = params.height + DensityUtil.dip2px(this,25);
            toolbar.setLayoutParams(params);
        }
        toolbar.setOverflowIcon(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(),
                R.drawable.actionbar_more)));
        //点击返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * @return 当你调用showContent方法时展示的内容的布局资源id
     */
    public abstract int getContentId();

    /**
     * 默认显示的都是加载中的视图，需要调用此方法来显示你自己的视图
     * isToolBar表示是否加载默认的toolbar，使用默认的toolbar你可以编写menu文件进行定制
     */
    public void showContent(boolean isToolBar) {
        if(isToolBar) {
            baseContain.removeView(baseLoading);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            baseContain.addView(mDataBinding.getRoot().getRootView(),params);
        }
        else getWindow().setContentView(mDataBinding.getRoot());
        //停止动画
        if(mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
    }

    /**
     * 显示或者隐藏某个菜单项
     */
    public void setItemVisible(@IdRes int id, boolean stat) {
        toolbar.getMenu().findItem(id).setVisible(stat);
    }

    /**
     * 设置菜单项的图标
     */
    public void setItemIcon(@IdRes int id, @DrawableRes int icon) {
        toolbar.getMenu().findItem(id).setIcon(icon);
    }

    /**
     * 设置菜单项的标题
     */
    public void setItemTitle(@IdRes int id, String title) {
        toolbar.getMenu().findItem(id).setTitle(title);
    }

    /**
     * 设置toolbar标题
     */
    public void setToolBarTitle(String title) {
        toolbar.setTitle(title);

        toolbar.setSubtitle(null);
    }

    public void setToolbarRightMargin(int rightMargin) {
        toolbar.setTitleMarginEnd(rightMargin);
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

    /**
     * 反射设置菜单icon是否有效
     */
    public void setIconEnable(Menu menu, boolean enable) {

        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
