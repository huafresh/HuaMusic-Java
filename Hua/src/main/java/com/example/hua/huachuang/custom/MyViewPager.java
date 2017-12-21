package com.example.hua.huachuang.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hua on 2017/3/25.
 */

public class MyViewPager extends ViewPager {

    //用于ViewPager判断滑动方向
    private int startX1;
    private int startX2;
    private SlideMenuLayout mSlideMenuLayout;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int currentItem = this.getCurrentItem();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX1 = (int) event.getX();
                if (SlideMenuLayout.isOpen) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    //菜单打开时，不能让viewPager处理事件。
                    return false;
                } else getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) event.getX() - startX1;
                //这个20主要是确保用户是向右滑的
                if (currentItem == 0 && deltaX > 200) {//此时让父view拦截剩余的滑动事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void setSlideMenuLayout(SlideMenuLayout menu) {
        this.mSlideMenuLayout = menu;
    }

}
