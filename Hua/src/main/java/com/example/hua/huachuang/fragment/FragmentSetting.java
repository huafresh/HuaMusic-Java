package com.example.hua.huachuang.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.bean.music.SettingInfo;
import com.example.hua.huachuang.databinding.FragmentSettingBinding;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.List;

/**
 * Created by hua on 2017/3/9.
 */

public class FragmentSetting extends Fragment{

    private ListView mListView;
    private FragmentSettingBinding mContentView;
    private CommonAdapter adapter;
    private List<SettingInfo> mListDates;
    private TextView mTitleTextView;
    private String mTitle;
    private SwitchCompat switchCompat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_setting,null,false);
        initViews();
        initListView();
        return mContentView.getRoot();
    }

    private void initViews() {
        mListView = mContentView.settingList;
        mTitleTextView = mContentView.settingTitle;
    }

    private int dip2px(float value) {
        return DensityUtil.dip2px(getActivity(),value);
    }

    private float getDimen(int resId) {
        return getActivity().getResources().getDimension(resId);
    }

    private void initListView() {
        if(mTitle!=null)
            mTitleTextView.setText(mTitle);
        else mTitleTextView.setText("无标题");
        if(mListDates==null)
            throw new NullPointerException("必须调用setListDates设置数据源");
        adapter = new CommonAdapter<SettingInfo>(getActivity(), R.layout.fragment_setting_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, SettingInfo item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonUtil.toast("item");
            }
        });
    }

    public void add(FragmentManager fm, @IdRes int idRes) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(idRes,this);
        ft.commit();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setListDates(List<SettingInfo> listDates) {
        mListDates = listDates;
    }

    private void doConvert(ViewHolder viewHolder, SettingInfo item, int position) {
        viewHolder.setText(R.id.setting_text,item.getText());
        if(item.getSubText()!=null) {
            viewHolder.setVisible(R.id.setting_sub_text,true);
            viewHolder.setText(R.id.setting_sub_text,item.getSubText());
        }
        if(item.isSelect()) {
            viewHolder.setChecked2(R.id.switch_compat,true);
        } else viewHolder.setChecked2(R.id.switch_compat,false);

        if(item.isNoDivider())
            viewHolder.setVisible(R.id.setting_divider,false);
    }

    private void doSelectClick(View v) {
        SettingInfo info = (SettingInfo) v.getTag();
        SwitchCompat compat = (SwitchCompat) v.findViewById(R.id.switch_compat);
        if(!info.isSelect()) {
            compat.setChecked(true);
            info.setSelect(true);
        } else {
            compat.setChecked(false);
            info.setSelect(false);
        }
        //网络设置第一个设置项
//        if(info.getFlag().equals(SettingInfo.flags.NET_FIRST)) {
//            SettingInfo info2 = mListDates.get(1);
//            SettingInfo info3 = mListDates.get(2);
//            if(info.isSelect()) {
//                info2.setCanClick(false);
//                info3.setCanClick(false);
//            } else {
//                info2.setCanClick(true);
//                info3.setCanClick(true);
//            }
//            adapter.notifyDataSetChanged();
//        }
    }


}
