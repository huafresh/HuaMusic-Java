package com.example.hua.huachuang.module.music.common;//package com.example.hua.huachuang.module.music.common;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.databinding.DataBindingUtil;
//import android.databinding.ViewDataBinding;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.example.hua.huachuang.R;
//import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
//import com.example.hua.huachuang.base.PlayBarActivity;
//import com.example.hua.huachuang.bean.Music;
//import com.example.hua.huachuang.databinding.MusicItemBinding;
//import com.example.hua.huachuang.manager.AsyncTaskManager;
//import com.example.hua.huachuang.service.PlayService;
//import com.example.hua.huachuang.share.ShareList;
//import com.example.hua.huachuang.zhy.CommonAdapter;
//import com.example.hua.huachuang.zhy.ViewHolder;
//
//import java.io.File;
//import java.util.List;
//
///**
// * Created by hua on 2017/2/25.
// * 当界面中有使用listView展示歌曲的时候，可以继承此activity处理大部分listView的点击事件
// * 已启用，改用fragment管理
// */
//
//public abstract class MusicCommonActivity<T extends ViewDataBinding>
//        extends PlayBarActivity<T> implements OnDialogFragmentResult {
//    //点击更多弹出的fragment
//    public DialogFragment_more dialogMore;
//    //点击更多的item项位置
//    private int morePosition;
//    //异步操作实现类
//    public AsyncTaskManager asyncTaskManager;
//    //处理的是哪个音乐的更多
//    private Music mMusic;
//    //广播接收者
//    private LocalBroadCastReceiver receiver;
//    //列表项数据绑定
//    private MusicItemBinding itemBinding;
//    //listView相关
//    public CommonAdapter adapter;
//    public ListView mListView;
//    private List<Music> mLists;
//    //不同子类展示item时有不同的处理
//    private OnDisPlay onDisPlay;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //初始化公共变量
//        initViews();
//        initListView();
//        registerReceiver(receiver,new IntentFilter(PlayService.BROADCAST_PREPARE));
//    }
//
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(receiver);
//        super.onDestroy();
//    }
//
//    /**
//     * 每次重新加载时，都设置一下音乐选中状态
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //扫描歌曲是异步的，这里会先运行
//        if(mLists.size() != 0) {
//            setSelect(mLists,getPos(PlayBinder.getCurMusic(),mLists));
//        }
//    }
//
//    private void initViews() {
//        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.music_item,null,false);
//        asyncTaskManager = (AsyncTaskManager) AsyncTaskFactory.getAsyncTask();
//        receiver = new LocalBroadCastReceiver();
//        //一次初始化，避免动态获取
//        mListView = setListView();
//        mLists = setListDates();
//    }
//
//    private void initListView() {
//        adapter = new CommonAdapter<Music>(this, R.layout.music_item, mLists) {
//            @Override
//            protected void convert(ViewHolder viewHolder, Music item, int position) {
//                if(mLists.size()==0) return;
//
//                viewHolder.setText(R.id.song_name,item.getSongName());
//                viewHolder.setText(R.id.song_author,item.getAuthor());
//
//                if(item.isSelect())
//                    viewHolder.setImageResource(R.id.song_item_select, R.drawable.ic_music_list_item_select);
//                else
//                    viewHolder.setImageResource(R.id.song_item_select, R.drawable.ic_music_list_item_no_select);
//
//                viewHolder.setOnClickListener(R.id.song_menu_more, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mMusic = (Music) v.getTag();
//                        dialogMore = DialogFragment_more.newInstance(mMusic.getSongName());
//                        //dialogMore.setTargetFragment(fragment,0);
//                        dialogMore.show(getSupportFragmentManager(),null);
//                    }
//                });
//                //给more按钮绑定Music对象，当点击时就可以取出来了
//                LinearLayout more = (LinearLayout) viewHolder.getConvertView().findViewById(R.id.song_menu_more);
//                if(more != null)
//                    more.setTag(mLists.get(position));
//                //做子类特殊处理
//                if(onDisPlay!=null)
//                    onDisPlay.onDisplay(viewHolder,item,position);
//            }
//        };
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PlayBinder.start(mLists.get(position));
//                setSelect(mLists,position);
//                //当点击的歌曲只露一半时这句可让它完全显示
//                mListView.smoothScrollToPosition(position);
//                mListView.setScrollBarFadeDuration(500);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        mListView.setAdapter(adapter);
//    }
//
//    /**
//     * 给父类设置ListView
//     */
//    public abstract ListView setListView();
//
//    /**
//     * 给listView设置数据源
//     */
//    public abstract List<Music> setListDates();
//
//    /**
//     * 当listView的item项被展示，子类有自己的处理时，需实现此接口
//     */
//    public interface OnDisPlay {
//        void onDisplay(ViewHolder viewHolder, Music item, int position);
//    }
//
//    public void setOnDisplay(OnDisPlay onDisplay) {
//        this.onDisPlay = onDisplay;
//    }
//
//    /**
//     * 点击更多时，从fragment中返回时调用
//     */
//    @Override
//    public void onResult(int pos) {
//        switch (pos){
//            case 0: //分享
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("audio/*");
//                File file = new File(mMusic.getPath());
//                Uri uri = Uri.fromFile(file);
//                intent.putExtra(Intent.EXTRA_STREAM,uri);
//                startActivity(Intent.createChooser(intent,"分享"));
//                dialogMore.dismiss();
//                break;
//            case 1: //设为铃声
//                asyncTaskManager.setRingStone(this,mMusic.getPath());
//                dialogMore.dismiss();
//                Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
//                break;
//            case 2: //查看歌曲信息
//                dialogMore.dismiss();
//                showDialogFragmentDetail();
//                break;
//            case 3: //删除
//                doDelete();
//                break;
//        }
//    }
//
//    /**
//     * 显示详细信息的fragment
//     */
//    private void showDialogFragmentDetail(){
//        DialogFragment_detail fragment_detail = new DialogFragment_detail();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("music",mMusic);
//        fragment_detail.setArguments(bundle);
//        fragment_detail.show(getSupportFragmentManager(),null);
//    }
//
//
//
//    /**
//     * 设置列表的position位置为选中状态
//     */
//    public void setSelect(List<Music> lists, int position) {
//        for (int i = 0; i < lists.size(); i++) {
//            lists.get(i).setSelect(false);
//        }
//        lists.get(position).setSelect(true);
//    }
//
//    /**
//     * 接受来自服务的广播，如准备ok
//     */
//    private class LocalBroadCastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            switch (action) {
//                case PlayService.BROADCAST_PREPARE:
//                    update();
//                    break;
//            }
//        }
//    }
//
//    private void doDelete() {
//        dialogMore.dismiss();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("确认删除"+ mMusic.getSongName()+ "?");
//        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //暂时就只实现删除列表里的歌曲了
//                int position = getPos(mMusic,mLists);
//                mLists.remove(position);
//                adapter.notifyDataSetChanged();
//                //当删除的是本地列表里的时候，则要判断该歌曲是否正在播放，如果是则需要切换下一曲
//                if(mLists.equals(ShareList.localList)) {
//                    if(getPos(PlayBinder.getCurMusic(),mLists) == position){
//                        if(PlayBinder.isPlay()) {
//                            PlayBinder.next();
//                            update();
//                        }
//                    }
//                }
//            }
//        }).setNegativeButton("取消",null);
//        builder.create().show();
//    }
//
//}
