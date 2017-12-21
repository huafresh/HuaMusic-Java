package com.example.hua.huachuang.module.music.common;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.utils.LoopTimer;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

/**
 * Created by hua on 2017/3/8.
 */

public class PopupWindowManager {

    private Activity mContext;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private List<String> mListDates;
    private int[] mResIds;
    private TextView mTitleView;
    private ListView mListView;
    private CommonAdapter adapter;
    private OnItemClick mOnItemClick;

    public PopupWindowManager(Activity context) {
        this.mContext = context;
        mContentView = View.inflate(context,R.layout.popup_window,null);
        mTitleView = (TextView) mContentView.findViewById(R.id.popup_title);
        mTitleView.setText("请在此添加标题");
        initListView();
        initPopupWindow();
    }

    private void initListView() {
        mListView = (ListView) mContentView.findViewById(R.id.popup_list);
        mListDates = new ArrayList<>();
        mListDates.add("请在此添加内容");
        adapter = new CommonAdapter<String>(mContext, R.layout.popup_window_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        mListView.setAdapter(adapter);
    }

    private void doConvert(ViewHolder viewHolder, String item, int position) {
        Log.i("convert",item);
        viewHolder.setText(R.id.popup_text,item);
        viewHolder.setImageDrawable(R.id.popup_image, DensityUtil.changeDrawableColor(mContext,mResIds[position]));
        //借用图标打tag，因为item布局已经被用了
        ImageView image = (ImageView) viewHolder.getConvertView().findViewById(R.id.popup_image);
        image.setTag(position);
        viewHolder.setOnClickListener(R.id.popup_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) v.findViewById(R.id.popup_image);
                doItemClick((Integer) image.getTag());
            }
        });
    }

    private void doItemClick(int position) {
        if(mOnItemClick!=null) {
            mOnItemClick.onItemClick(position);
            mPopupWindow.dismiss();
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //半透明颜色
        ColorDrawable cd = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.popup);
        mPopupWindow.setContentView(mContentView);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void setListDates(String[] texts,@DrawableRes int[] resIds) {
        mListDates.clear();
        Collections.addAll(mListDates, texts);
        mResIds = resIds;
        adapter.notifyDataSetChanged();
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
        adapter.notifyDataSetChanged();
    }

    public void show() {
        mPopupWindow.showAtLocation(mTitleView, Gravity.BOTTOM, 0,0);
        LoopTimer loopTimer = new LoopTimer();
        loopTimer.startDelayTimer(200, new LoopTimer.OnTimeUp() {
            @Override
            public void timeUp(Timer timer) {
                setBackgroundAlpha(0.5f);
            }
        });
    }

    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = alpha;
        mContext.getWindow().setAttributes(lp);
    }

}
