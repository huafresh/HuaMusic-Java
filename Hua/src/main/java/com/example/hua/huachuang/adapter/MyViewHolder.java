package com.example.hua.huachuang.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hua.huachuang.network.LoadImage;
import com.example.hua.huachuang.utils.DensityUtil;

/**
 * Created by hua on 2017/3/14.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding mBinding;
    private SparseArray<View> mViews;
    private Context mContext;
    private OnItemClick onItemClick;

    MyViewHolder(Context context,View itemView, OnItemClick onItemClick1) {
        super(itemView);
        this.onItemClick = onItemClick1;
        this.mContext = context;
        mViews = new SparseArray<>();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null)
                    onItemClick.onClick(v,getLayoutPosition());
            }
        });
    }

    public interface OnItemClick {
        void onClick(View v, int position);
    }

    //以下拷自鸿洋的万能适配器。后期RecyclerView用熟了用他的RecyclerView兼容版
    /**
     * 通过viewId获取控件
     */
    private <S extends View> S getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return DensityUtil.cast(view);
    }

    /****以下为辅助方法*****/

    public MyViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public MyViewHolder setVisible(int viewId, boolean visible)
    {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    //view设置url链接，针对播放视频
    public MyViewHolder setTagUrl(int viewId, String url) {
        View v = getView(viewId);
        v.setTag(url);
        return this;
    }

    public MyViewHolder setSelect(int viewId, boolean b) {
        View v = getView(viewId);
        v.setSelected(b);
        return this;
    }

    public boolean isSelect(int viewId) {
        View v = getView(viewId);
        return v.isSelected();
    }

    public MyViewHolder setAnimation(int viewId, Animation animation) {
        View v = getView(viewId);
        v.startAnimation(animation);
        return this;
    }
    public MyViewHolder setImageResource(int viewId, int resId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public MyViewHolder setImageBitmap(int viewId, Bitmap bitmap)
    {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public MyViewHolder setImageDrawable(int viewId, Drawable drawable)
    {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public MyViewHolder setImageUrl(int viewId, String url)
    {
        ImageView view = getView(viewId);
        LoadImage.loadImageNormal(view,url);
        return this;
    }

    public MyViewHolder setBackgroundColor(int viewId, int color)
    {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public MyViewHolder setBackgroundRes(int viewId, int backgroundRes)
    {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public MyViewHolder setTextColor(int viewId, int textColor)
    {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public MyViewHolder setTextColorRes(int viewId, int textColorRes)
    {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * 关于事件的
     */
    public MyViewHolder setOnClickListener(int viewId,
                                           View.OnClickListener listener)
    {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public MyViewHolder setOnTouchListener(int viewId,
                                           View.OnTouchListener listener)
    {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public MyViewHolder setOnLongClickListener(int viewId,
                                               View.OnLongClickListener listener)
    {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
