package com.example.hua.huachuang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.databinding.ActivityAboutBinding;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hua on 2016/12/25.
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    public static final String ACTION = "com.example.hua.huachuang.activity.AboutActivity";
    private ListView mListView;
    private TextView neteaseService;
    private TextView neteaseRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        showContent(true);
        setToolBarTitle(getString(R.string.about_title));
        mListView = mDataBinding.aboutList;
        List<String> lists = new ArrayList<>();
        lists.add(getString(R.string.about_update));
        lists.add(getString(R.string.about_history));
        lists.add(getString(R.string.about_star));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.about_item,
                R.id.about_item_text,lists);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doOnItemClick(position);

            }
        });
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setAdapter(adapter);

        neteaseService = mDataBinding.neteaseService;
        String serviceText = neteaseService.getText().toString();
        SpannableString serviceSpanned = new SpannableString(serviceText);
        serviceSpanned.setSpan(new URLSpan("http://music.163.com/html/web2/service.html"),0,serviceText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        serviceSpanned.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorContent)),0,serviceSpanned.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        neteaseService.setText("《");
        neteaseService.append(serviceSpanned);
        neteaseService.append("》");

        neteaseRule = mDataBinding.neteaseRule;
        String ruleText = neteaseRule.getText().toString();
        SpannableString ruleSpanned = new SpannableString(ruleText);
        ruleSpanned.setSpan(new URLSpan("http://music.163.com/about"),0,ruleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ruleSpanned.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorContent)),0,ruleSpanned.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        neteaseRule.setText("《");
        neteaseRule.append(ruleSpanned);
        neteaseRule.append("》");

        neteaseService.setMovementMethod(LinkMovementMethod.getInstance());
        neteaseRule.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void doOnItemClick(int position) {
        switch (position) {
            case 0: //新版更新
                CommonUtil.toast("当前已是最新版本");
                break;
            case 1: //更新日志
                CommonUtil.toast("暂无更新日志");
                break;
            case 2: //start
                String url = "http://www.baidu.com";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_about;
    }
}
