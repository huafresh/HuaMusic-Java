package com.example.hua.huachuang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.bean.music.RangeInfo;

import java.util.List;

/**
 * Created by hua on 2017/2/19.
 */

public class FragmentListAdapter extends MyBaseAdapter<RangeInfo> {

    public FragmentListAdapter(Context context, List<RangeInfo> lists) {
        super(context,lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            view = View.inflate(getContext(),R.layout.fragment_music_item,null);
        } else view = convertView;
        BaseViewHolder holder = getViewHolder(convertView);


        return null;
    }

    private class MyViewHolder {

    }

}
