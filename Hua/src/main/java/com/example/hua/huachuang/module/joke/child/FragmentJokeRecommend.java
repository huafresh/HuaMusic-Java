package com.example.hua.huachuang.module.joke.child;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.adapter.MyViewHolder;
import com.example.hua.huachuang.adapter.RecyclerAdapter;
import com.example.hua.huachuang.asyncTask.AsyncTask;
import com.example.hua.huachuang.asyncTask.AsyncTaskFactory;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.base.OnRefreshListener;
import com.example.hua.huachuang.bean.joke.RecommendData;
import com.example.hua.huachuang.bean.joke.RecommendParams;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.databinding.FragmentJokeRecomBinding;
import com.example.hua.huachuang.manager.CallBack;
import com.example.hua.huachuang.manager.CallBack2;
import com.example.hua.huachuang.network.LoadImage;
import com.example.hua.huachuang.network.NetWorkRequest;
import com.example.hua.huachuang.network.NetWorkRequestFactory;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by hua on 2017/2/17.
 */

public class FragmentJokeRecommend extends BaseFragment<FragmentJokeRecomBinding> {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private List<RecommendData.DetailData> mListDates;
    private RecyclerAdapter adapter;
    private NetWorkRequest netWorkRequest;
    private static final String NEW_FROM = "http://news.qq.com/";
    private static String DEFAULT_VIDEO_PATH = "http://ic.snssdk.com/neihan/video/playback/1489235732.1/?video_id=d2a0fc9bb4784a6c983cd4a341df6d76&quality=480p&line=0&is_gif=0&device_platform=android";

    private VideoView mVideoView;
    //正在播放视频的item
    private View mCurrentItem;
    //播放视图中的控件
    private ImageView bagImage;
    private ImageView play;
    private ImageView pause;
    private ImageView restart;
    private MyMediaController controller;
    private AsyncTask asyncTask;
    //当前mVideoView嵌套在哪个item
    private int mPosition;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initVideoView();
        getRecommendInfo();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new MyItemDecoration(this));
    }

    //切换viewPager，停止播放
    public void stopVideoView() {
        removeVideoView(mVideoView);
    }

    private void initViews() {
        refreshLayout = fragmentDataBinding.swipeRefresh;
        TypedValue color = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.huaColorTheme,color,true);
        refreshLayout.setColorSchemeColors(color.data);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtil.isNetworkConnected(getActivity()))
                    getRecommendInfo();
                else {
                    CommonUtil.toast("无网络连接");
                    refreshLayout.setRefreshing(false);
                }
            }
        });
        mRecyclerView = fragmentDataBinding.recyclerListRecommend;
        //如果正在滑动，控制布局立马隐藏
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(controller.isShowing())
                    controller.hide();
                int count = recyclerView.getChildCount();
                View firstChild = recyclerView.getChildAt(0);
                int minPos = recyclerView.getChildAdapterPosition(firstChild);
                View lastChild = recyclerView.getChildAt(count-1);
                int lastPos = recyclerView.getChildAdapterPosition(lastChild);
                if(mPosition<minPos || mPosition>lastPos) {
                    removeVideoView(mVideoView);
                }
            }

        });

        mListDates = new ArrayList<>();
        netWorkRequest = NetWorkRequestFactory.getNetWorkRequest(getActivity());
        asyncTask = AsyncTaskFactory.getAsyncTask();
    }

    private void removeVideoView(VideoView videoView) {
        ViewGroup viewGroup = (ViewGroup) videoView.getTag();
        if(viewGroup!=null) {
            viewGroup.removeView(videoView);
            //重新设置可点击，则可再次播放
            viewGroup.setClickable(true);
        }
    }

    private void addVideoView(View contain, VideoView videoView) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ((ViewGroup)contain).addView(videoView,params);
    }

    private void initVideoView() {
        mVideoView = new VideoView(getActivity());
        controller = new MyMediaController(getActivity(),mVideoView);
        mVideoView.setMediaController(controller);
        mVideoView.requestFocus();
        //视频播放器的准备,此时播放器已经准备好了,此处可以设置一下播放速度,播放位置等等
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //此处设置播放速度为正常速度1
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.toast("click");
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                removeVideoView(mVideoView);
            }
        });
        //设置隐藏与显示控制器时的监听
        controller.setOnHiddenListener(new MediaController.OnHiddenListener() {
            @Override
            public void onHidden() {
                //setPlayingState();
            }
        });
        controller.setOnShownListener(new MediaController.OnShownListener() {
            @Override
            public void onShown() {
//                if(mVideoView.isPlaying()) {
//                    //setPlayState();
//                } else setPauseState();
            }
        });
    }

    private void doConvert(final MyViewHolder myViewHolder, RecommendData.DetailData data) {
        changeWrapContent(myViewHolder.itemView);
        RecommendData.GroupInfo groupInfo = data.getGroup();
        if(groupInfo.getContent().equals(""))
            myViewHolder.setVisible(R.id.user_description,false);
        //内涵段子返回的json数据格式就是这样的，别怪我写这么多get-_-
        String videoUrl = groupInfo.getVideo360p().getUrl_list().get(0).getUrl();
        if(groupInfo.getUrl_list()!=null &&
                groupInfo.getUrl_list().size()!=0 &&
                groupInfo.getUrl_list().get(0).getUrl() != null) {
            String coverUrl = groupInfo.getUrl_list().get(0).getUrl();
            ImageView cover = (ImageView) myViewHolder.itemView.findViewById(R.id.video_bag_image);
            LoadImage.loadImageNormal(cover,coverUrl);
        } else { //根据网络视频路径获取缩略图
            asyncTask.getMediaThumb(videoUrl, new CallBack2() {
                @Override
                public void onSuccess(Result result) {
                    myViewHolder.setImageBitmap(R.id.video_bag_image,result.getThumbBitmap());
                }
            });
        }
        myViewHolder.setText(R.id.user_name,groupInfo.getUser().getName())
                    .setText(R.id.user_description,groupInfo.getContent())
                    .setText(R.id.from,groupInfo.getCategory_name())
                    .setTagUrl(R.id.video,videoUrl)
                    .setOnClickListener(R.id.video, new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            //initCurrent(v);
                            if(NetworkUtil.isNetworkConnected(getActivity())) {
                                if(NetworkUtil.isWifiConnected(getActivity())) {
                                    doPlayVideo(v,myViewHolder.itemView);
                                } else {
                                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                            .setTitle("当前处于移动网络，您确定要播放该视频吗？")
                                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    doPlayVideo(v,myViewHolder.itemView);
                                                }
                                            })
                                            .setNegativeButton("取消",null).create();
                                    dialog.show();
                                }
                            } else CommonUtil.toast("无网络连接");
                        }
                    });
        //为空表示还没获取到缩略图，则自己调用获取。内涵段子返回数据中有
//        if(data.getBitmap()==null) {
//            asyncTask.getMediaThumb(videoUrl, new CallBack2() {
//                @Override
//                public void onSuccess(Result result) {
//                    myViewHolder.setImageBitmap(R.id.video_bag_image,result.getThumbBitmap());
//                }
//            });
//        } else myViewHolder.setImageBitmap(R.id.video_bag_image,data.getBitmap());

        //把videoView设置到第二个item
//        int position = mRecyclerView.getChildLayoutPosition(myViewHolder.itemView);
//        if(position==1) {
//            ViewGroup viewGroup = (ViewGroup) myViewHolder.itemView.findViewById(R.id.video);
//            addVideoView(viewGroup,mVideoView);
//            mVideoView.setVideoPath(videoUrl);
//            //mVideoView.setVisibility(View.INVISIBLE);
//        }
    }

    private void initCurrent(View v) {
        mCurrentItem = v;
        v.setClickable(false);
        bagImage = (ImageView) v.findViewById(R.id.video_bag_image);
        play = (ImageView) v.findViewById(R.id.video_btn_play);
        play.setClickable(true);
        //虽是同一个控件，但响应不一样，需重新设置点击事件
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPauseState();
                //mVideoView.pause();
            }
        });
        pause = (ImageView) v.findViewById(R.id.video_btn_pause);
        pause.setClickable(true);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPlayState();
                //mVideoView.resume();
            }
        });
        //restart = (ImageView) v.findViewById(R.id.);
    }

    //解决一开始item的高度是适配内容的，滑下去，再滑上来，则会变的MATCH_PARENT。
    private void changeWrapContent(View item) {
        ViewGroup.LayoutParams params = item.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        item.setLayoutParams(params);
    }

    private void getRecommendInfo() {
        final RecommendParams params = new RecommendParams();
        params.setName("推荐");
        params.setCount(100);
        params.setScreenInfo("1920*1080");

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkUtil.isNetworkConnected(getActivity())) {
                    CommonUtil.toast("无网络连接");
                    showError();
                } else getDates(params);
            }
        });

        if(!NetworkUtil.isNetworkConnected(getActivity())) {
            CommonUtil.toast("无网络连接");
            showError();
            return;
        }
        getDates(params);
    }

    private void getDates(RecommendParams params) {
        netWorkRequest.getRecommendData(params, new CallBack() {
            @Override
            public void onSuccess(Result result) {
                initRecyclerView(result);
                showContent();
            }
            @Override
            public void onError(Result result) {
                showError();
            }
        });
    }

    private void initRecyclerView(Result result) {
        //刷新时需要清空
        mListDates.clear();
        mListDates = result.getRecommendData().getData().getData();
        removeNullUrl(mListDates);
        //getMediaThumb(mListDates);
        adapter = new RecyclerAdapter<RecommendData.DetailData>(getActivity(),R.layout.item_joke_recommend,mListDates) {
            @Override
            public void Convert(MyViewHolder myViewHolder, RecommendData.DetailData item) {
                doConvert(myViewHolder,item);
            }
        };
        mRecyclerView.setAdapter(adapter);
        refreshLayout.setRefreshing(false);
    }

    private void getMediaThumb(List<RecommendData.DetailData> lists) {
        for (int i = 0; i < lists.size(); i++) {
            final RecommendData.DetailData detailData = lists.get(i);
            String videoUrl = detailData.getGroup().getVideo360p().getUrl_list().get(0).getUrl();
            asyncTask.getMediaThumb(videoUrl, new CallBack2() {
                @Override
                public void onSuccess(Result result) {
                    detailData.setBitmap(result.getThumbBitmap());
                }
            });
        }
    }

    //去掉所有url为空的
    private void removeNullUrl(List<RecommendData.DetailData> lists) {
        List<RecommendData.DetailData> tmpList = new ArrayList<>();
        tmpList.addAll(lists);
        lists.clear();
        for (int i = 0; i < tmpList.size(); i++) {
            RecommendData.GroupInfo groupInfo = tmpList.get(i).getGroup();
            if(groupInfo!=null &&
                    groupInfo.getVideo360p()!=null &&
                    groupInfo.getVideo360p().getUrl_list()!=null &&
                    groupInfo.getVideo360p().getUrl_list().size()!=0 &&
                    groupInfo.getVideo360p().getUrl_list().get(0).getUrl()!=null)
                lists.add(tmpList.get(i));
        }
    }

    //开始播放视频
    private void doPlayVideo(View videoContain, View itemView) {
        videoContain.setClickable(false);
        mPosition = mRecyclerView.getChildLayoutPosition(itemView);
        removeVideoView(mVideoView);
        addVideoView(videoContain,mVideoView);
        mVideoView.setTag(videoContain);
        String url = (String) videoContain.getTag();
        mVideoView.setVideoPath(url);
        //mVideoView.start();
        //setPlayingState();
    }

    //更新播放视频的视图的控件处于播放中的状态
    private void setPlayState() {
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        bagImage.setVisibility(View.INVISIBLE);
    }

    private void setPauseState() {
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        bagImage.setVisibility(View.INVISIBLE);
    }

    private void setRestartState() {

    }

    private void setPlayingState() {
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        bagImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_joke_recom;
    }
}
