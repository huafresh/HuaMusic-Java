package com.example.hua.huachuang.bean.music;

/**
 * Created by huazai on 2016/9/25.
 */
public class LrcContent {
    //歌词内容
    private String content;
    //该句歌词的时间，ms为单位
    private int time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
