package com.example.hua.huachuang.share;

import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.bean.music.MenuInfo;
import com.example.hua.huachuang.bean.music.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/22.
 */

public class ShareList {
    //存放所有扫描出来的本地歌曲
    public static List<Music> localList = new ArrayList<>();
    //动态存放搜索出来的网络歌曲
    public static List<Music> onlineList = new ArrayList<>();
    //已下载列表
    public static List<Music> haveDownList = new ArrayList<>();
    //最近播放列表
    public static List<Music> recentlyList = new ArrayList<>();
    //所有下载的任务列表
    public static List<DowningInfo> downingList = new ArrayList<>();
    //音乐下载队列
    public static List<DowningInfo> musicDowningList = new ArrayList<>();
    //创建的歌单列表
    public static List<MenuInfo> menuInfoList = new ArrayList<>();

    /**
     * 添加一首歌到播放记录列表，最近添加的在最上面
     * 如果以前就存在，则删除之前的
     */
    public static void AddMusicToRecentPlay(Music music) {
        for (int i = 0; i < recentlyList.size(); i++) {
            if(music.getId() == recentlyList.get(i).getId()) {
                recentlyList.remove(i);
            }
        }
        recentlyList.add(0,music);
    }

    /**
     * 添加一首歌到已下载列表，最近添加的在最上面
     * 如果以前就存在，什么也不做
     */
    public static void AddMusicToHaveDown(Music music) {
        for (int i = 0; i < haveDownList.size(); i++) {
            if(music.getId() == haveDownList.get(i).getId()) {
                return;
            }
        }
        haveDownList.add(music);
    }

    /**
     * 添加一首歌到已下载列表，最近添加的在最上面
     * 如果以前就存在，什么也不做
     */
    public static void RemoveMusicFromHaveDown(Music music) {
        if(music.getId()==haveDownList.get(0).getId())  //删除的是第一首
            haveDownList.get(1).setHaveHeader(true);
        haveDownList.remove(music);
    }

    /**
     * 判断新建的下载任务是否之前有记录，有返回记录的位置，无返回-1
     */
    public static int getDownFilePos(String url) {
        return -1;
    }

}
