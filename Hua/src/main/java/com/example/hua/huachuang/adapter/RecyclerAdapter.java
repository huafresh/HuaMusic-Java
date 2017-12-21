package com.example.hua.huachuang.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hua on 2017/2/11.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter {

    private List<T> mListDates;
    private int mItemId;
    private Context mContext;
    private MyViewHolder.OnItemClick onItemClick;

    protected RecyclerAdapter(Context context, @LayoutRes int id, List<T> lists) {
        this.mListDates = lists;
        this.mContext = context;
        this.mItemId = id;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),mItemId,parent,false);
        return new MyViewHolder(mContext,binding.getRoot(),onItemClick);
    }

    public abstract void Convert(MyViewHolder viewHolder, T item);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Convert((MyViewHolder) holder,mListDates.get(position));
    }

    public void addOnItemClick(MyViewHolder.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        return mListDates.size();
    }



}
