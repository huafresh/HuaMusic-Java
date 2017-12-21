package com.example.hua.huachuang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hua on 2017/2/19.
 */

public class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> mDataLists;
    private Context context;

    public MyBaseAdapter(Context context, List<T> lists) {
        this.mDataLists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public Context getContext() {
        return context;
    }



    public class BaseViewHolder {

    }

    public BaseViewHolder getViewHolder(View converView) {
        if(converView == null) {
            return new BaseViewHolder();
        } else return null;
    }
}
