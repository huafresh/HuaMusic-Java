package com.example.hua.huachuang.module.news.child;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.style.ImageSpan;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.activity.ImgPreviewActivity;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.bean.news.NewInfo;
import com.example.hua.huachuang.databinding.ActivityNewDetailBinding;
import com.example.hua.huachuang.htmlspanner.LinkMovementMethodExt;
import com.example.hua.huachuang.htmlspanner.MyImageSpan;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.network.NetWorkRequest;
import com.example.hua.huachuang.network.NetWorkRequestFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/3/12.
 */

public class ActivityNewDetail extends BaseActivity<ActivityNewDetailBinding> {
    public static final String ACTION = "com.example.hua.huachuang.module.news.child.ActivityNewDetail";
    private NewInfo mNewInfo;
    private NetWorkRequest netWorkRequest;
    private List<String> imgList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getDetailInfo();
        //监听图片点击
        mDataBinding.newText.setMovementMethod(LinkMovementMethodExt.getInstance(handler, ImageSpan.class));
    }

    private void init() {
        mNewInfo = (NewInfo) getIntent().getSerializableExtra("newInfo");
        netWorkRequest = NetWorkRequestFactory.getNetWorkRequest(this);
        imgList = new ArrayList<>();
    }

    private void getDetailInfo() {
        netWorkRequest.getMainNewsDetail(mNewInfo, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                imgList.clear();
                imgList = mNewInfo.getImageList();
                mDataBinding.setNewInfo(mNewInfo);
                showContent(true);
                setToolBarTitle(mNewInfo.getTitle());
            }

            @Override
            public void onError(Result result) {
                showError();
            }
        });
    }

    @Override
    public int getContentId() {
        return R.layout.activity_new_detail;
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2://图片点击事件
                    int position=0;
                    MyImageSpan span = (MyImageSpan) msg.obj;
                    for (int i = 0; i < imgList.size(); i++) {
                        if (span.getUrl().equals(imgList.get(i))) {
                            position = i;
                            break;
                        }
                    }
                    Intent intent=new Intent(ActivityNewDetail.this,ImgPreviewActivity.class);
                    Bundle b=new Bundle();
                    b.putInt("position",position);
                    b.putStringArray("imglist",imgList.toArray(new String[]{}));
                    intent.putExtra("b",b);
                    startActivity(intent);
                    break;
            }
        }
    };
}
