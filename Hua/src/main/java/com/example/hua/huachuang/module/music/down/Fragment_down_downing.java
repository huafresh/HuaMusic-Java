package com.example.hua.huachuang.module.music.down;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.asyncTask.AsyncTask;
import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.databinding.FragmentDownDowningBinding;
import com.example.hua.huachuang.download.DownloadManager;
import com.example.hua.huachuang.utils.LoopTimer;
import com.example.hua.huachuang.service.DownService;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hua on 2017/2/28.
 */

public class Fragment_down_downing extends BaseFragment<FragmentDownDowningBinding> {

    private ListView downingList;
    private CommonAdapter adapter;
    private List<DowningInfo> mListDates;
    //标题
    private LinearLayout startAll;
    private LinearLayout clear;
    private TextView downState;
    private ImageView downStateImage;
    private AsyncTask asyncTask;
    //用于显隐界面
    private LinearLayout downingContent;
    private TextView noDowning;
    private LoopTimer loopTimer;
    private HashMap<Long,DowningInfo> hashMap;
    //接收实时下载信息
    private DownBroadcastReceive receive;
    //已下载界面
    private Fragment_down_down fragment;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setListener();
        initListView();
        showContent();
        if(ShareList.musicDowningList.size()==0)
            showNoDowning();
        else showDowning();
        fragment = (Fragment_down_down) getTargetFragment();
    }

    @Override
    public void onDetach() {
        try{
            getActivity().unregisterReceiver(receive);
        }catch(IllegalArgumentException e){
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
        super.onDetach();
    }

    @SuppressLint("UseSparseArrays")
    private void initViews() {
        downingList = fragmentDataBinding.downingList;
        startAll = fragmentDataBinding.title.titleStartAll;
        clear = fragmentDataBinding.title.titleClear;
        downState = fragmentDataBinding.title.downState;
        downStateImage = fragmentDataBinding.title.downStateImage;
        downingContent = fragmentDataBinding.downingContent;
        noDowning = fragmentDataBinding.noDowning;
        asyncTask = AsyncTaskFactory.getAsyncTask();
        hashMap = new HashMap<>();
        loopTimer = new LoopTimer();
        receive = new DownBroadcastReceive();
    }

    private boolean isHaveDowning(List<DowningInfo> lists) {
        for (int i = 0; i < lists.size(); i++) {
            DowningInfo info = lists.get(i);
            if(info.getStatus()==DownloadManager.STATUS_RUNNING)
                return true;
        }
        return false;
    }


    private void setListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownService.BROADCAST_QUERY);
        getActivity().registerReceiver(receive,filter);

        //全部开始或者暂停
        startAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartAll();
            }
        });
        //清空下载
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownService.downBinder.cancelAllTask();
                mListDates.clear();
                adapter.notifyDataSetChanged();
                showNoDowning();
            }
        });
    }



    private void doStartAll() {
        //DownService.downBinder.addDownTasks();
        if(downState.getText().toString().equals(getString(R.string.down_start_all))) {
            setPauseAll();
            continueAllTask(mListDates);
        } else if(downState.getText().toString().equals(getString(R.string.down_pause_all))){
            setStartAll();
            stopAllTask(mListDates);
        }
    }

    private void continueAllTask(List<DowningInfo> lists) {
        for (int i = 0; i < lists.size(); i++) {
            DowningInfo info = lists.get(i);
            if(info.getStatus() == DownloadManager.STATUS_PAUSED) {
                DownService.downBinder.resumeTask(info.getId());
            }
        }
    }

    private void stopAllTask(List<DowningInfo> lists) {
        for (int i = 0; i < lists.size(); i++) {
            DowningInfo info = lists.get(i);
            if(info.getStatus() == DownloadManager.STATUS_RUNNING) {
                DownService.downBinder.pauseTask(info.getId());
            }
        }
    }

    /**
     * 提取所给列表中的id
     */
    private long[] getIds(List<DowningInfo> lists) {
        List<Long> ids = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            ids.add(lists.get(i).getId());
        }
        long[] tmp = new long[ids.size()];
        for (int j = 0; j < ids.size(); j++) {
            tmp[j] = ids.get(j);
        }
        return tmp;
    }

    private void initListView() {
        //使用全局列表，可实现任务中断后的断点保存
        mListDates = ShareList.musicDowningList;
        adapter = new CommonAdapter<DowningInfo>(getActivity(), R.layout.downing_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, DowningInfo item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        downingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doItemClick(position);
            }
        });
        downingList.setAdapter(adapter);
    }

    private void doItemClick(int position) {
        DowningInfo info = mListDates.get(position);
        if(info.getStatus()==DownloadManager.STATUS_RUNNING) {
            DownService.downBinder.pauseTask(info.getId());
            info.setStatus(DownloadManager.STATUS_PAUSED);
        } else if(info.getStatus()==DownloadManager.STATUS_PAUSED) {
            DownService.downBinder.resumeTask(info.getId());
            info.setStatus(DownloadManager.STATUS_RUNNING);
        }
    }

    private void doConvert(ViewHolder viewHolder, DowningInfo item, int position) {
        viewHolder.setText(R.id.name,item.getDisplayName());
        if(item.getStatus()== DownloadManager.STATUS_RUNNING) {
            showProgressBar(viewHolder);
            updateProgressBar(viewHolder,item);
        }
        else if(item.getStatus()== DownloadManager.STATUS_PENDING){
            hideProgressBar(viewHolder);
            viewHolder.setText(R.id.notice,getString(R.string.down_wait_downing));
        } else if(item.getStatus()== DownloadManager.STATUS_PAUSED){
            hideProgressBar(viewHolder);
            viewHolder.setText(R.id.notice,getString(R.string.down_pause));
        } else if(item.getStatus()== DownloadManager.STATUS_PAUSED){
            hideProgressBar(viewHolder);
            viewHolder.setText(R.id.notice,getString(R.string.down_pause));
        }
        //给取消按钮绑定列表位置
        LinearLayout close = (LinearLayout) viewHolder.getConvertView().findViewById(R.id.close);
        close.setTag(position);
        //点击取消
        viewHolder.setOnClickListener(R.id.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DowningInfo info = mListDates.get((int) v.getTag());
                DownService.downBinder.cancelTask(info.getId());
                mListDates.remove(info);
            }
        });
    }

    private void showProgressBar(ViewHolder viewHolder) {
        viewHolder.setVisible(R.id.notice,false);
        viewHolder.setVisible(R.id.progress_contain,true);
    }

    private void hideProgressBar(ViewHolder viewHolder) {
        viewHolder.setVisible(R.id.notice,true);
        viewHolder.setVisible(R.id.progress_contain,false);
    }

    private void updateProgressBar(ViewHolder viewHolder, DowningInfo item) {
        long current = item.getCurrent();
        long total = item.getTotal();
        if(total <= 0) return;
        int progress = (int) ((current*100)/total);
        viewHolder.setProgress(R.id.progress_bar,progress);
        viewHolder.setText(R.id.current, DensityUtil.valueToSize(current));
        viewHolder.setText(R.id.total, DensityUtil.valueToSize(total));
    }

    private void setStartAll() {
        downStateImage.setImageResource(R.drawable.down_start);
        downState.setText(getString(R.string.down_start_all));
    }

    private void setPauseAll() {
        downStateImage.setImageResource(R.drawable.down_state_pause);
        downState.setText(getString(R.string.down_pause_all));
    }

    private void showNoDowning() {
        noDowning.setVisibility(View.VISIBLE);
        downingContent.setVisibility(View.GONE);
    }

    private void showDowning() {
        noDowning.setVisibility(View.GONE);
        downingContent.setVisibility(View.VISIBLE);
        if(isHaveDowning(mListDates))
            setPauseAll();
        else setStartAll();
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_down_downing;
    }

    class DownBroadcastReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownService.BROADCAST_QUERY)) {
                Bundle bundle = intent.getExtras();
                updateListDates(bundle);
                adapter.notifyDataSetChanged();
                if(mListDates.size()==0)
                    showNoDowning();
            }
        }
    }

    private void updateListDates(Bundle bundle) {
        //用查询得到的下载信息替换listView中绑定的
        for (int j = 0; j < mListDates.size(); j++) {
            Long id = mListDates.get(j).getId();
            Log.i("onReceive",id+"");
            DowningInfo info = (DowningInfo) bundle.get(String.valueOf(id));
            if(info!=null) {
                Log.i("onReceive",info.getStatus()+"");
                if(info.getStatus()== DownloadManager.STATUS_SUCCESSFUL ||
                        info.getStatus()== DownService.STATUS_EXIST) {
                    Music music = new Music();
                    music.setSongName(mListDates.get(j).getDisplayName());
                    music.setAuthor("许嵩");
                    //ShareList.haveDownList.add(music);
                    //fragment.updateList();
                    mListDates.remove(j);
                }
                else copyInfo(mListDates.get(j),info);
            }
        }
    }

    private void copyInfo(DowningInfo info1,DowningInfo info2) {
        info1.setCurrent(info2.getCurrent());
        info1.setTotal(info2.getTotal());
        info1.setStatus(info2.getStatus());
    }

}
