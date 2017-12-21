package com.example.hua.huachuang.network;

import com.example.hua.huachuang.bean.joke.RecommendParams;
import com.example.hua.huachuang.bean.news.NewInfo;
import com.example.hua.huachuang.manager.CallBack;

import java.util.HashMap;


/**
 * Created by hua on 2016/12/27.
 * 此接口由NetWorkRequestManager实现，供网络访问者使用
 */
public interface NetWorkRequest {
    //获取榜单数据
    void getBillBoard(HashMap<String, Integer> data, CallBack callBack);
    //获取网络歌曲链接
    void getOnlineMusicLink(String songId, CallBack callBack);
    //下载歌词
    void DownLrc(String url, String songName, CallBack callBack);

    //获取腾讯要闻概述
    void getMainNewsPreview(String url, CallBack callBack);
    //获取腾讯要闻详细信息
    void getMainNewsDetail(NewInfo newInfo, CallBack callBack);

    //获取内涵段子推荐信息
    void getRecommendData(RecommendParams params, CallBack callBack);

//    void BillBoardRequestSync(BillBoard billBoard, BillBoardRequestData data);
//    void LoadImage(String urlString, NetWorkCallBack<Bitmap> callBack);
//    void LoadImageSync(String urlString, ImageView imageView);
//    void DownMusicLink(String songId, NetWorkCallBack<String> callBack);
//    void DownLrc(String urlString, String musicName, NetWorkCallBack<String> callBack);
//    void getWeather(String urlString, NetWorkCallBack<Weather> callBack);


}
