package com.example.hua.huachuang.module.music.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.asyncTask.AsyncTask;
import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
import com.example.hua.huachuang.base.BaseActivity;
import com.example.hua.huachuang.base.FragmentPlayBar;
import com.example.hua.huachuang.databinding.ActivityMusicLocalBinding;
import com.example.hua.huachuang.module.music.common.FragmentListView;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.ShareUtil;


/**
 * Created by hua on 2017/2/19.
 */

public class LocalActivity extends BaseActivity<ActivityMusicLocalBinding> {
    public static final String ACTION = "com.example.hua.huachuang.module.music.range.LocalActivity";
    //底部布局的容器
    private LinearLayout playBarContain;
    //提示无本地音乐
    private TextView noLocal;
    //有音乐时的内容容器
    private LinearLayout musicContent;
    private AsyncTask asyncTask;
    private FragmentPlayBar playBar;
    private FragmentListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(ShareUtil.getInstance("cfg").getBoolean("isNight"))
            setTheme(R.style.AppTheme_Night);
        super.onCreate(savedInstanceState);
        //初始化本地变量
        initViews();
        setToolBarTitle(getString(R.string.music_local));
        //获取本地歌曲
        getLocalMusic();
    }

    /**
     * 本地音乐扫描成功后执行
     */
    private void doOnSuccess() {
        showContent(true);
        if(ShareList.localList.size() == 0) {
            showNoLocal();
            return;
        }
        //有时候服务的Music对象会恢复失败
        if(PlayService.PlayBinder.getCurMusic()==null)
            PlayService.PlayBinder.prepare(ShareList.localList.get(0));
        initPlayBar();
        initFragmentListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(playBar!=null) {
            playBar.update();
        }
        if(listView!=null) {
            listView.updatePosition();
        }
    }

    /**
     * 初始化本地变量
     */
    private void initViews() {
        playBarContain = mDataBinding.localBottom;
        noLocal = mDataBinding.noLocal;
        musicContent = mDataBinding.localContent;
        asyncTask = AsyncTaskFactory.getAsyncTask();
    }

    @Override
    public int getContentId() {
        return R.layout.activity_music_local;
    }

    private void initPlayBar() {
        playBar = new FragmentPlayBar();
        playBar.add(getSupportFragmentManager(),R.id.local_bottom);
    }

    private void initFragmentListView() {
        listView = new FragmentListView();
        listView.add(getSupportFragmentManager(),R.id.local_list);
        listView.setListDates(ShareList.localList);
        listView.setListType(FragmentListView.TYPE_LOCAL);
        if(!PlayService.PlayBinder.getCurMusic().isOnline())
            listView.setPlayPos(PlayService.PlayBinder.getCurMusic(),ShareList.localList);
        else listView.setPlayPos(ShareList.localList.get(0),ShareList.localList);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.local_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                CommonUtil.toast("search");
                break;
            case R.id.scan:
                CommonUtil.toast("scan");
                break;
            case R.id.order:
                CommonUtil.toast("order");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLocalMusic() {
        if(ShareList.localList.size()!=0) {
            doOnSuccess();
            return;
        }
        noLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                doRefreshScan();
            }
        });
        doScan();

    }

    private void doScan() {
        asyncTask.scanMusic(new CallBack() {
            @Override
            public void onSuccess(Result respond) {
                doOnSuccess();
            }
            @Override
            public void onError(Result respond) {
                //showError();
            }
        });
    }

    private void doRefreshScan() {
        asyncTask.scanMusic(new CallBack() {
            @Override
            public void onSuccess(Result respond) {
                CommonUtil.toast("刷新成功");
                if(ShareList.localList.size() == 0) {
                    showNoLocal();
                    return;
                }
                //有时候服务的Music对象会恢复失败
                if(PlayService.PlayBinder.getCurMusic()==null)
                    PlayService.PlayBinder.prepare(ShareList.localList.get(0));
                initPlayBar();
                initFragmentListView();
            }
            @Override
            public void onError(Result respond) {
                //showError();
            }
        });
    }

    /**
     * 显示无本地歌曲的界面
     */
    private void showNoLocal() {
        noLocal.setVisibility(View.VISIBLE);
        musicContent.setVisibility(View.GONE);
    }

}
