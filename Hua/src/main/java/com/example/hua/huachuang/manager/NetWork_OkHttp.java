package com.example.hua.huachuang.manager;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.hua.huachuang.bean.joke.RecommendData;
import com.example.hua.huachuang.bean.joke.RecommendParams;
import com.example.hua.huachuang.bean.joke.TabType;
import com.example.hua.huachuang.bean.news.NewInfo;
import com.example.hua.huachuang.htmlspanner.HtmlSpanner;
import com.example.hua.huachuang.network.NetWorkRequest;
import com.example.hua.huachuang.bean.music.BillBoard;
import com.example.hua.huachuang.bean.music.Result;
import com.example.hua.huachuang.utils.ExecutorUtil;
import com.example.hua.huachuang.utils.JSoupUtil;
import com.example.hua.huachuang.utils.LrcUtil;
import com.example.hua.huachuang.utils.UrlUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hua on 2017/2/26.
 * 使用OKHttp框架
 */

public class NetWork_OkHttp implements NetWorkRequest {
    //执行各种各样的网络请求
    private static NetWork_OkHttp mNetWorkRequestManager;
    //由这个发出网络请求
    private OkHttpClient okHttpClient;
    //用于切换回主线程
    private Handler mHandler;
    private ThreadPoolExecutor threadPoolExecutor;
    //解析html文本
    private HtmlSpanner htmlSpanner;
    private List<String> imgUrlList;



    private NetWork_OkHttp(Activity context) {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mHandler = new Handler(context.getMainLooper());
        threadPoolExecutor = ExecutorUtil.getExecutorService();
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        imgUrlList = new ArrayList<>();
        Handler htmlHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    String url = (String) msg.obj;
                    imgUrlList.add(url);
                }
            }
        };
        htmlSpanner = new HtmlSpanner(context, dm.widthPixels, htmlHandler);
    }

    public static NetWork_OkHttp getInstance(Activity context) {
        if (mNetWorkRequestManager == null) {
            mNetWorkRequestManager = new NetWork_OkHttp(context);
            return mNetWorkRequestManager;
        }
        return mNetWorkRequestManager;
    }

    @Override
    public void getBillBoard(HashMap<String, Integer> data, final CallBack callBack) {
        int type = data.get("type");
        int sum = data.get("sum");
        int offset = data.get("offset");
        final int order = data.get("order");
        String urlString = UrlUtil.MakeBillBoardUrl("json", type, sum, offset);
        Log.i("huazai3", urlString);
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new PostRunnable(PostRunnable.ERROR, callBack, new Result()));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonString = response.body().string();
                Log.i("huazai3", jsonString);
                Gson gson = new Gson();
                BillBoard billBoard = null;
                try {
                    billBoard = gson.fromJson(jsonString, BillBoard.class);
                    billBoard.setOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new PostRunnable(PostRunnable.ERROR, callBack, new Result()));
                    return;
                }
                final Result result = new Result();
                result.setBillBoard(billBoard);
                mPostSuccess(result, callBack);
            }
        });
    }

    @Override
    public void getOnlineMusicLink(String songId, final CallBack callBack) {
        String url = UrlUtil.MakeMp3DownLoadUrl(songId);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onError(new Result());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonString = response.body().string();
                Gson gson = new Gson();
                BillBoard board = gson.fromJson(jsonString, BillBoard.class);
                final Result result = new Result();
                result.setBillBoard(board);
                mPostSuccess(result, callBack);
            }
        });
    }

    @Override
    public void DownLrc(String url, final String songName, final CallBack callBack) {
        if (url == null) return;
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File file = new File(externalPath + "/" + LrcUtil.FOLDER_NAME + "/" + songName + ".lrc");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    String s = new String(response.body().bytes(), "UTF-8");
                    outputStream.write(s.getBytes());
                    final Result result = new Result();
                    result.setLrcContents(LrcUtil.ResolveLrc(file.getAbsolutePath()));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getMainNewsPreview(final String url, final CallBack callBack) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<NewInfo> infoList = JSoupUtil.obtainNewPreInfo(url);
                Result result = new Result();
                result.setNewLists(infoList);
                if (infoList.size() != 0)
                    mHandler.post(new PostRunnable(PostRunnable.SUCCESS, callBack, result));
                else mHandler.post(new PostRunnable(PostRunnable.ERROR, callBack, result));
            }
        });
    }

    @Override
    public void getMainNewsDetail(final NewInfo newInfo, final CallBack callBack) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String article = JSoupUtil.obtainNewDetailInfo(newInfo.getNewLink(), newInfo);
                imgUrlList.clear();
                if (article == null) {
                    mHandler.post(new PostRunnable(PostRunnable.ERROR, callBack, new Result()));
                }
                Spannable spannable = htmlSpanner.fromHtml(article);
                newInfo.setSpannable(spannable);
                newInfo.setImageList(imgUrlList);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(new Result());
                    }
                });
            }
        });
    }


    private String getContentType(final String name) {
        String url = UrlUtil.getContentTypeUrl();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String jsonString = response.body().string();
            Gson gson = new Gson();
            TabType tabType = gson.fromJson(jsonString, TabType.class);
            List<TabType.TabInfo> tabs = tabType.getData();
            for (int i = 0; i < tabs.size(); i++) {
                TabType.TabInfo info = tabs.get(i);
                if (info.getName().equals(name)) {
                    return info.getList_id();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getRecommendData(final RecommendParams params, final CallBack callBack) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String contentType = getContentType(params.getName());
                if (contentType == null) {
                    callBack.onError(new Result());
                    return;
                }
                String url = UrlUtil.MakeJokeUrl(contentType, params.getCount(), params.getScreenInfo());
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        callBack.onError(new Result());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        RecommendData data = gson.fromJson(jsonString, RecommendData.class);
                        final Result result = new Result();
                        result.setRecommendData(data);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });
                    }
                });
            }
        });

    }


    private void mPostSuccess(final Result result, final CallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });
    }

}
