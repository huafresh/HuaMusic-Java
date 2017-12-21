package com.example.hua.huachuang.module.music.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.databinding.FragmentListViewBinding;
import com.example.hua.huachuang.manager.AsyncTaskManager;
import com.example.hua.huachuang.manager.NetWork_OkHttp;
import com.example.hua.huachuang.network.NetWorkRequestFactory;
import com.example.hua.huachuang.service.PlayService;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.NetworkUtil;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by hua on 2017/2/28.
 * 动态添加，主要处理音乐ListView的各种点击逻辑
 */

public class FragmentListView extends Fragment {
    //处理的列表的类型
    public static final int TYPE_LOCAL = 0;
    public static final int TYPE_ONLINE = 1;
    public static final int TYPE_RECENTLY = 2;
    public static final int TYPE_DOWN = 3;
    public static final int TYPE_DOWNING = 4;

    private ListView mListView;
    private List<Music> mListDates;
    private FragmentListViewBinding binding;
    public CommonAdapter adapter;
    //处理的是哪个音乐的更多
    private Music mMusic;
    public DialogFragment_more dialogMore;
    //不同子类展示item时有不同的处理
    private OnDisPlay onDisPlay;
    //用于从fragment中返回
    private Fragment fragment;
    private AsyncTaskManager asyncTaskManager;
    //头布局
    private View mHeader;
    //列表的类型
    private int mListType=-1;

    private NetWork_OkHttp netWorkOkHttp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_list_view,null,false);
        mListView = binding.list;
        initListView();
        fragment = this;
        asyncTaskManager = (AsyncTaskManager) AsyncTaskFactory.getAsyncTask();
        netWorkOkHttp = NetWorkRequestFactory.getNetWorkRequest(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        mListView = null;
        mListDates = null;
        adapter = null;
        dialogMore = null;
        asyncTaskManager = null;
        super.onDestroy();
    }

    /**
     * 给listView设置数据源
     */
    public void setListDates(List<Music> listDates) {
        this.mListDates = listDates;
    }

    public void add(FragmentManager fm, @IdRes int idRes) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(idRes,this);
        ft.commit();
    }


    public void setHeader(View view) {
        mHeader = view;
    }

    public void setListType(int type) {
        if(type!=TYPE_LOCAL &&
                type!=TYPE_ONLINE &&
                type!=TYPE_RECENTLY &&
                type!=TYPE_DOWN &&
                type!=TYPE_DOWNING)
            throw new NullPointerException("must set special type!");
        this.mListType = type;
    }

    private void initListView() {
        if(mListDates==null)
            throw new NullPointerException("must set list dates by call setListDates()");
        if(mListType==-1)
            throw new RuntimeException("must set list type by call setListType()");
        adapter = new CommonAdapter<Music>(getActivity(), R.layout.music_item, mListDates) {
            @Override
            protected void convert(ViewHolder viewHolder, Music item, int position) {
                doConvert(viewHolder,item,position);
            }
        };
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doItemClick(position);
            }
        });
        mListView.setAdapter(adapter);
        if(mHeader!=null) mListView.addHeaderView(mHeader);
    }

    public void updatePosition() {
        setSelect(ShareList.localList,getPos(PlayService.PlayBinder.getCurMusic(),ShareList.localList));
        adapter.notifyDataSetChanged();
    }

    private void doConvert(ViewHolder viewHolder, Music item, int position) {
        if(mListDates==null || mListDates.size()==0) return;
        viewHolder.setText(R.id.song_name,item.getSongName());
        viewHolder.setText(R.id.song_author,item.getAuthor());

        if(mListType==TYPE_ONLINE) {
            viewHolder.setVisible(R.id.item_add, false);
            viewHolder.setVisible(R.id.item_digital, true);
            viewHolder.setText(R.id.text_digital,++position+"");
            if(position<4)
                viewHolder.setTextColor(R.id.text_digital,getResources().getColor(R.color.colorTheme));
            else viewHolder.setTextColor(R.id.text_digital,getResources().getColor(R.color.colorContent2));
            //上面的else有必要，防止复用时别的地方也是红色
            viewHolder.setText(R.id.song_author,item.getTitleAndAuthor());
            if(item.isSelect()) {
                viewHolder.setVisible(R.id.song_item_select_online,true);
            } else viewHolder.setVisible(R.id.song_item_select_online,false);
            viewHolder.setVisible(R.id.music_more,false);
        }
        if(mListType==TYPE_LOCAL || mListType==TYPE_RECENTLY) {
            //给more按钮绑定Music对象，当点击时就可以取出来了
            viewHolder.setTag(R.id.music_more,item);
            viewHolder.setOnClickListener(R.id.music_more, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doMoreClick(v);
                }
            });
            if(item.isSelect()) {
                viewHolder.setVisible(R.id.song_item_select,true);
            } else viewHolder.setVisible(R.id.song_item_select,false);
        }

        //做子类特殊处理
        if(onDisPlay!=null)
            onDisPlay.onDisplay(viewHolder,item,position);
    }

    private void doItemClick(final int position) {
        if(mListType==TYPE_LOCAL || mListType==TYPE_RECENTLY) {
            Music music = mListDates.get(position);
            if(music.isOnline()) {
                PlayService.PlayBinder.startOnline(mListDates.get(position),null);
            } else PlayService.PlayBinder.start(mListDates.get(position));
            setSelect(mListDates,position);
            //当点击的歌曲只露一半时这句可让它完全显示
            mListView.smoothScrollToPosition(position);
            mListView.setScrollBarFadeDuration(500);
            adapter.notifyDataSetChanged();
        }
        if(mListType==TYPE_ONLINE) {
            if(NetworkUtil.isNetworkConnected(getActivity())) {
                if(NetworkUtil.isWifiConnected(getActivity())) {
                    doPlayOnline(position);
                } else {
                    android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("当前处于移动网络，您确定要播放该音乐吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doPlayOnline(position);
                                }
                            })
                            .setNegativeButton("取消",null).create();
                    dialog.show();
                }
            } else CommonUtil.toast("无网络连接");
        }
    }

    private void doPlayOnline(int position) {
        setSelect(mListDates,position-1);
        PlayService.PlayBinder.startOnline(mListDates.get(position-1),null);
        adapter.notifyDataSetChanged();
    }

    private void doMoreClick(View v) {
        mMusic = (Music) v.getTag();
        dialogMore = DialogFragment_more.newInstance(mMusic.getSongName());
        dialogMore.setTargetFragment(fragment,0);
        dialogMore.show(getActivity().getSupportFragmentManager(),null);
    }

    /**
     * 获取音乐在给定列表中的位置
     */
    public int getPos(Music music, List<Music> lists) {
        if(lists.size() == 0) return 0;
        if(music==null) return 0;
        int i;
        for (i = 0; i < lists.size(); i++) {
            if(lists.get(i).getId() == music.getId())
                break;
        }
        return i;
    }

    /**
     * 设置播放位置
     */
    public void setPlayPos(Music music,List<Music> lists) {
        setSelect(lists,getPos(music, lists));
    }

    /**
     * 从更多中返回
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!=0) return;
        switch (resultCode) {
            case 0: //分享
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("audio/*");
                File file = new File(mMusic.getPath());
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(intent,"分享"));
                dialogMore.dismiss();
                break;
            case 1: //设为铃声
                asyncTaskManager.setRingStone(getActivity(),mMusic.getPath());
                dialogMore.dismiss();
                Toast.makeText(getActivity(),"设置成功",Toast.LENGTH_SHORT).show();
                break;
            case 2: //查看歌曲信息
                dialogMore.dismiss();
                showDialogFragmentDetail();
                break;
            case 3: //删除
                doDelete();
                break;
        }
    }

    /**
     * 显示详细信息的fragment
     */
    private void showDialogFragmentDetail(){
        DialogFragment_detail fragment_detail = new DialogFragment_detail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("music",mMusic);
        fragment_detail.setArguments(bundle);
        fragment_detail.show(getActivity().getSupportFragmentManager(),null);
    }

    private void doDelete() {
        dialogMore.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除"+ mMusic.getSongName()+ "?");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //暂时就只实现删除列表里的歌曲了
                int position = getPos(mMusic,mListDates);
                mListDates.remove(position);
                adapter.notifyDataSetChanged();
                //当删除的是本地列表里的时候，则要判断该歌曲是否正在播放，如果是则需要切换下一曲
                if(mListDates.equals(ShareList.localList)) {
                    if(getPos(PlayService.PlayBinder.getCurMusic(),mListDates) == position){
                        if(PlayService.PlayBinder.isPlay()) {
                            PlayService.PlayBinder.next();
                            //update();
                        }
                    }
                }
            }
        }).setNegativeButton("取消",null);
        builder.create().show();
    }

    /**
     * 设置列表的position位置为选中状态
     */
    public void setSelect(List<Music> lists, int position) {
        if(lists == null) return;
        if(lists.size()==0) return;
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).setSelect(false);
        }
        lists.get(position).setSelect(true);
    }


    /**
     * 当listView的item项被展示，子类有自己的处理时，需实现此接口
     */
    public interface OnDisPlay {
        void onDisplay(ViewHolder viewHolder, Music item, int position);
    }

    public void setOnDisplay(OnDisPlay onDisplay) {
        this.onDisPlay = onDisplay;
    }

}
