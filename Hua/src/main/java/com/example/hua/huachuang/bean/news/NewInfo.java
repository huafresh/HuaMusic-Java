package com.example.hua.huachuang.bean.news;

import android.text.Spannable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/3/12.

     <a target="_blank" class="pic" href="http://news.qq.com/a/20170312/000689.htm">
     <img class="picto" src="http://inews.gtimg.com/newsapp_ls/0/1248204728_12088/0">
     </a>

     <a target="_blank" class="linkto" href="http://news.qq.com/a/20170312/000689.htm">
     全国两会上，地方大员给出哪些民生发展承诺？
     </a>

     <span class="from">
     中国新闻网
     </span>

     <span class="pub_time">
     2017-03-12 00:45
     </span>

     <a class="discuzBtn" title="评论数" target="_blank" bosszone="top2newsp" href="http://coral.qq.com/1809159910">
     657
     </a>

     以上为一条文字加图片类型新闻的html，整理自腾讯新闻网。注意评论链接暂时没用，下同。

 /***************************************分 割 线********************************************

     <a target="_blank" class="linkto" href="http://news.qq.com/a/20170312/005313.htm">
     直击贵州从江“牛王争霸”
     </a></em>

     <li class="pic"><a target="_blank" href="http://news.qq.com/a/20170312/005313.htm"><img class="picto" src="http://inews.gtimg.com/newsapp_ls/0/1248607520_207138/0"></a></li>
     <li class="pic"><a target="_blank" href="http://news.qq.com/a/20170312/005313.htm"><img class="picto" src="http://inews.gtimg.com/newsapp_ls/0/1248607573_207138/0"></a></li>
     <li class="pic"><a target="_blank" href="http://news.qq.com/a/20170312/005313.htm"><img class="picto" src="http://inews.gtimg.com/newsapp_ls/0/1248607622_207138/0"></a></li>

     <span class="from">
     腾讯图片
     </span>

     <span class="pub_time">
     2017-03-12 08:07
     </span>

     <a class="discuzBtn" title="评论数" target="_blank" bosszone="top1newsp" href="http://coral.qq.com/1809320831">
     4148
     </a>

    以上为一条图片预览类型新闻的html，整理自腾讯新闻网。
 */

public class NewInfo implements Serializable{
    private String title;
    private String newLink;
    private String picLink;
    //腾讯新闻类型,主要有两种
    private boolean isContent;
    private List<String> picLinks = new ArrayList<>();
    private String from;
    private String pubTime;
    //评论数
    private String disSum;
    //文章内容
    private Spannable spannable;
    //文章中图片的链接
    private List<String> imageList;

    public String getTitle() {
        return title;
    }

    public List<String> getPicLinks() {
        return picLinks;
    }

    public void setPicLinks(List<String> picLinks) {
        this.picLinks = picLinks;
    }

    public Spannable getSpannable() {
        return spannable;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setSpannable(Spannable spannable) {
        this.spannable = spannable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisSum() {
        return disSum;
    }

    public void setDisSum(String disSum) {
        this.disSum = disSum;
    }

    public String getNewLink() {
        return newLink;
    }

    public void setNewLink(String newLink) {
        this.newLink = newLink;
    }

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }

    public boolean isContent() {
        return isContent;
    }

    public void setContent(boolean content) {
        isContent = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
}
