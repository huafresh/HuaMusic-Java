package com.example.hua.huachuang.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;


/**
 * Created by hua on 2017/3/25.
 * 实现了一个侧滑菜单容器，使用时需注意格式
 * 菜单布局用容器封装，内容布局也用容器封装
 * 最后菜单布局和内容布局使用水平方向的LinearLayout封装
 */

public class SlideMenuLayout extends HorizontalScrollView {

    private int mScreenWidth;
    private ViewGroup mLeftMenu;
    private ViewGroup mContentLayout;
    private int mLeftPadding;
    private int mMenuWidth;
    //处理滑动事件冲突时，需要知道菜单是否打开，故此使用全局变量，这样耦合性太高，后续想别的办法
    public static boolean isOpen;
    //标记是否拦截滑动事件
    private boolean isIntercept;
    //用于计算滑动速度
    private VelocityTracker mVelocityTracker;
    private int startX;
    private ViewPager mViewPager;

    public SlideMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = getScreenWidth(context);
        mLeftPadding = 200;
    }


    public SlideMenuLayout(Context context) {
        this(context, null);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    //确定子view以及自身的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout mMenu = (LinearLayout) getChildAt(0);
        mLeftMenu = (ViewGroup) mMenu.getChildAt(0);
        mContentLayout = (ViewGroup) mMenu.getChildAt(1);
        mMenuWidth = mScreenWidth - mLeftPadding;
        mLeftMenu.getLayoutParams().width = mMenuWidth;
        mContentLayout.getLayoutParams().width = mScreenWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        createVelocityTracker(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                int deltaX = (int) (ev.getRawX()-startX);
                if (isToggleMenu(scrollX,deltaX)) {
                    Toggle();
                } else { //如果不要改变状态，则回弹
                    if(isOpen) openMenu();
                    else closeMenu();
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //l就是整个自定义HorizontalScrollView的最左边到屏幕左边的横向距离
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        ViewHelper.setTranslationX(mLeftMenu, l * 0.6f);
        float scale = l * 1.0f / mMenuWidth; //1-0
        //各种动画系数
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 1.0f - scale * 0.6f;
        //设置主内容缩放中心点
        ViewHelper.setPivotX(mContentLayout, 0);
        ViewHelper.setPivotY(mContentLayout, mContentLayout.getHeight() / 2);
        ViewHelper.setScaleX(mContentLayout, rightScale);
        ViewHelper.setScaleY(mContentLayout, rightScale);
        ViewHelper.setScaleX(mLeftMenu, leftScale);
        ViewHelper.setScaleY(mLeftMenu, leftScale);
        ViewHelper.setAlpha(mLeftMenu, leftAlpha);
    }

    public void openMenu() {
        smoothScrollTo(0, 0);
        isOpen = true;
    }

    public void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * 转换菜单状态
     */
    public void Toggle() {
        if (isOpen) closeMenu();
        else openMenu();
    }

    public boolean isMenuOpen() {
        return isOpen;
    }

    public void setIntercept(boolean b) {
        this.isIntercept = b;
    }

    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 判断是否需要改变Menu的开关状态
     */
    private boolean isToggleMenu(float distance,int deltaX) {
        return judgeBySpeed(deltaX) || judgeByDistance(distance);
    }

    /**
     * 根据滑动速度判断是否改变状态
     */
    private boolean judgeBySpeed(int deltaX) {
        return isOpen && (deltaX < 0) && (getXSpeed() > 200) ||
                !isOpen && (deltaX > 0) && (getXSpeed() > 200);
    }

    /**
     * 根据隐藏view的大小来判断
     */
    private boolean judgeByDistance(float distance){
        Log.i("distance",distance+"");
        Log.i("half",mScreenWidth / 2.0+"");
        return (isOpen && isMoreHalf(distance)) || (!isOpen && !isMoreHalf(distance));
    }

    /**
     * 判断view左边缘到屏幕左侧的距离是否超过菜单的一半
     */
    private boolean isMoreHalf(float distance) {
        double half = (mScreenWidth-mLeftPadding) / 2.0;
        return distance > half;
    }

    /**
     * 获取水平滑动速度，单位是像素/s
     */
    private int getXSpeed() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int speed = (int) mVelocityTracker.getXVelocity();
        return Math.abs(speed);
    }

}
