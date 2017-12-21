package com.example.hua.huachuang.bean.music;

import android.graphics.Bitmap;
import android.text.Spannable;

import com.example.hua.huachuang.bean.joke.RecommendData;
import com.example.hua.huachuang.bean.news.NewInfo;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by hua on 2017/2/21.
 * 不管是什么请求，结果都放在这
 */

public class Result {
    //下载歌词使用
    private List<LrcContent> lrcContents = null;
    //歌词存放路径
    private String lrcPath;
    //在线单曲信息
    private Music onlineMusic;
    //获取榜单信息使用
    private BillBoard billBoard;
    private Response response; //okHttp的响应

    //获取新闻预览信息
    private List<NewInfo> newLists;
    //新闻详细信息
    private Spannable spannable;
    //网络缩略图
    private Bitmap thumbBitmap;

    //内涵段子的content_type
    private String listId;
    //内涵段子推荐详细信息
    private RecommendData recommendData;

    //网络访问时的错误码
    private int errorCode;

    public List<LrcContent> getLrcContents() {
        return lrcContents;
    }
    public void setLrcContents(List<LrcContent> lrcContents) {
        this.lrcContents = lrcContents;
    }

    public BillBoard getBillBoard() {
        return billBoard;
    }

    public String getLrcPath() {
        return lrcPath;
    }

    public void setLrcPath(String lrcPath) {
        this.lrcPath = lrcPath;
    }

    public void setBillBoard(BillBoard billBoard) {
        this.billBoard = billBoard;
    }

    public Bitmap getThumbBitmap() {
        return thumbBitmap;
    }

    public void setThumbBitmap(Bitmap thumbBitmap) {
        this.thumbBitmap = thumbBitmap;
    }

    public Music getOnlineMusic() {
        return onlineMusic;
    }

    public List<NewInfo> getNewLists() {
        return newLists;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public RecommendData getRecommendData() {
        return recommendData;
    }

    public void setRecommendData(RecommendData recommendData) {
        this.recommendData = recommendData;
    }

    public void setNewLists(List<NewInfo> newLists) {
        this.newLists = newLists;
    }

    public Spannable getSpannable() {
        return spannable;
    }

    public void setSpannable(Spannable spannable) {
        this.spannable = spannable;
    }

    public void setOnlineMusic(Music onlineMusic) {
        this.onlineMusic = onlineMusic;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
