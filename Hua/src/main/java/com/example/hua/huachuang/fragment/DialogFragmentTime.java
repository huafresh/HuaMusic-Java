package com.example.hua.huachuang.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.databinding.DialogTimeBinding;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hua on 2016/12/24.
 */
public class DialogFragmentTime extends DialogFragment implements AdapterView.OnItemClickListener{
    private ListView listView;
    private String[] dates = {"不开启","10分钟后","20分钟后","30分钟后","45分钟后","60分钟后"};
    private Fragment fragment;
    private DialogTimeBinding mContentView;
    private CommonAdapter adapter;
    private List<String> mListDates;
    private SharedPreferences preferences;
    private int timerPos;
    private boolean isChecked;
    private CheckBox checkBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,0);
        fragment = getTargetFragment();
        preferences = getActivity().getSharedPreferences("cfg", Context.MODE_PRIVATE);
        timerPos = preferences.getInt("timerPos",0);
        isChecked = preferences.getBoolean("isChecked",false);
    }

    @Override
    public void onDestroy() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("timerPos",timerPos);
        editor.putBoolean("isChecked",isChecked);
        editor.apply();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.dialog_time,null,false);
        listView = mContentView.lvTimeList;
        listView.setOnItemClickListener(this);
        mListDates = Arrays.asList(dates);
        adapter = new CommonAdapter<String>(getActivity(),R.layout.dialog_time_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        listView.setAdapter(adapter);
        //处理复选框
        checkBox = mContentView.checkbox;
        checkBox.setChecked(isChecked);
        LinearLayout checkContain = mContentView.checkboxContain;
        checkContain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !checkBox.isChecked();
                checkBox.setChecked(isChecked);
            }
        });
        return mContentView.getRoot();
    }

    private void doConvert(ViewHolder viewHolder, String item, int position) {
        viewHolder.setText(R.id.timer_text,item);
        if(position==timerPos) {
            viewHolder.setVisible(R.id.timer_right,true);
        } else viewHolder.setVisible(R.id.timer_right,false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragment.onActivityResult(0,position,null);
        timerPos = position;
//        adapter.notifyDataSetChanged();
    }
}
