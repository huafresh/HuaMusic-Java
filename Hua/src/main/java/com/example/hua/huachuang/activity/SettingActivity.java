package com.example.hua.huachuang.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.bean.music.SettingInfo;
import com.example.hua.huachuang.databinding.ActivitySettingBinding;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.List;

/**
 * Created by hua on 2017/3/9.
 */

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    public static final String ACTION = "com.example.hua.huachuang.activity.SettingActivity";
    private SharedPreferences preferences;
    private List<SettingInfo> list;
    private LinearLayout playSetting;

    private TextView title;
    private SwitchCompat playSwitch;
    private TextView playText;
    private LinearLayout downSetting;
    private SwitchCompat downSwitch;
    private TextView downText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        showContent(true);
        setToolBarTitle(getString(R.string.setting));
        preferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        initSetting();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("play",playSwitch.isChecked());
        editor.putBoolean("down",downSwitch.isChecked());
        editor.apply();
        super.onDestroy();
    }


    private void initSetting() {
        playSetting = mDataBinding.settingPlay.settingItem;
        playSwitch = mDataBinding.settingPlay.switchCompat;
        playText = mDataBinding.settingPlay.settingText;
        downSetting = mDataBinding.settingDown.settingItem;
        downSwitch = mDataBinding.settingDown.switchCompat;
        downText = mDataBinding.settingDown.settingText;

        playText.setText(getString(R.string.setting_play));
        playText.setText(getString(R.string.setting_down));
        playSwitch.setChecked(preferences.getBoolean("play",false));
        downSwitch.setChecked(preferences.getBoolean("down",false));

        playSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSwitch.setChecked(!playSwitch.isChecked());
            }
        });
        downSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downSwitch.setChecked(!downSwitch.isChecked());
            }
        });
    }

    @Override
    public int getContentId() {
        return R.layout.activity_setting;
    }
}
