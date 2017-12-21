package com.example.hua.huachuang.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.MainActivity;
import com.example.hua.huachuang.R;
import com.example.hua.huachuang.activity.AboutActivity;
import com.example.hua.huachuang.activity.ProjectHomeActivity;
import com.example.hua.huachuang.activity.ScanDownActivity;
import com.example.hua.huachuang.activity.SettingActivity;
import com.example.hua.huachuang.bean.music.SlideMenuItemInfo;
import com.example.hua.huachuang.databinding.FragmentSlideBinding;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.utils.LoopTimer;
import com.example.hua.huachuang.utils.ShareUtil;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by hua on 2017/3/10.
 */

public class FragmentSlide extends Fragment {
    private FragmentSlideBinding mContentView;
    private CommonAdapter adapter;
    private ListView mListView;
    private TextView setting;
    private TextView exit;
    private List<SlideMenuItemInfo> mListDates;
    private DialogFragmentTime dialogTime;
    //定时时间，单位分钟
    private int[] times = {-1,10,20,30,45,60,90};
    private String[] notices = {"不开启","10分钟后","20分钟后","30分钟后","45分钟后","60分钟后"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_slide,null,false);
        initViews();
        setListener();
        initDates();
        addListView();
        return mContentView.getRoot();
    }

    private void initViews() {
        mListView = mContentView.slideList;
        setting = mContentView.slideSetting;
        exit = mContentView.slideExit;
    }

    private void setListener() {
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SettingActivity.ACTION);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).slideMenuLayout.closeMenu();
                getActivity().finish();
            }
        });
    }

    private void initDates() {
        mListDates = new ArrayList<>();
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_home),
                getString(R.string.nav_menu_home)));
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_scan),
                getString(R.string.nav_menu_down)));
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_night),
                getString(R.string.nav_menu_night)));
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_time),
                getString(R.string.nav_menu_time)));
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_deedback),
                getString(R.string.nav_menu_back)));
        mListDates.add(new SlideMenuItemInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.ic_nav_about),
                getString(R.string.nav_menu_about)));
    }

    private void addListView() {
        adapter = new CommonAdapter<SlideMenuItemInfo>(getActivity(),R.layout.slide_menu_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, SlideMenuItemInfo item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        View header = View.inflate(getActivity(),R.layout.slide_menu_header,null);
        mListView.addHeaderView(header);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doOnItemClick(position);
            }
        });
        mListView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialogTime.dismiss();
        final int time = times[resultCode];
        LoopTimer loopTimer = new LoopTimer();
        if(time>0) {
            loopTimer.startDelayTimer(time * 60 * 1000, new LoopTimer.OnTimeUp() {
                @Override
                public void timeUp(Timer timer) {
                    PlayService.PlayBinder.pause();
                }
            });
            CommonUtil.toast("设置成功，将在"+time+"分钟后停止播放");
        } else CommonUtil.toast("定时停止播放已关闭");
    }

    private void doOnItemClick(int position) {
        switch (position) { //注意position要算上头部
            case 1: //项目主页
                Intent intent2 = new Intent(ProjectHomeActivity.ACTION);
                startActivity(intent2);
                break;
            case 2: //扫码下载
                ((MainActivity)getActivity()).slideMenuLayout.closeMenu();
                Intent intent = new Intent(ScanDownActivity.ACTION);
                startActivity(intent);
                break;
            case 3: //夜间模式
                Activity activity = getActivity();
                if(!(activity instanceof MainActivity)) return;
                final MainActivity mainActivity = (MainActivity) activity;
                //mainActivity.s.closeDrawer(Gravity.LEFT);
                ShareUtil shareUtil = ShareUtil.getInstance("cfg");
                boolean isNight = shareUtil.getBoolean("isNight");
                if(isNight) {
                    shareUtil.putBoolean("isNight",false).apply();
                } else shareUtil.putBoolean("isNight",true).apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                View view = View.inflate(mainActivity,R.layout.alert_dialog,null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                LoopTimer loopTimer = new LoopTimer();
                //给点dialog显示出来的时间
                loopTimer.startDelayTimer(200, new LoopTimer.OnTimeUp() {
                    @Override
                    public void timeUp(Timer timer) {
                        mainActivity.recreate();
                    }
                });
                //切换完后关闭dialog
                loopTimer.startDelayTimer(1500, new LoopTimer.OnTimeUp() {
                    @Override
                    public void timeUp(Timer timer) {
                        dialog.dismiss();
                    }
                });
                break;
            case 4: //定时停止播放
                ((MainActivity)getActivity()).slideMenuLayout.closeMenu();
                dialogTime = new DialogFragmentTime();
                dialogTime.setTargetFragment(this,0);
                dialogTime.show(getFragmentManager(),null);
                break;
            case 5: //问题反馈
                CommonUtil.toast("暂时无法反馈问题，可直接与作者联系");
                break;
            case 6: //关于华创音乐
                Intent about = new Intent(AboutActivity.ACTION);
                startActivity(about);
                break;
        }
    }

    private void doConvert(ViewHolder viewHolder, SlideMenuItemInfo item, int position) {
        viewHolder.setImageDrawable(R.id.slide_item_image,item.getDrawable());
        viewHolder.setText(R.id.slide_item_text,item.getTitle());
    }
}
