package com.example.hua.huachuang.bean.music;

/**
 * Created by hua on 2016/12/27.
 */
public class BillBoardRequestData {
    int type;
    int sum;
    int offset;


    final public static String NEW_KEY = "new";
    final public static String HOT_KEY = "hot";
    final public static String CHINA_KEY = "china";
    final public static String ROCK_KEY = "rock";
    final public static String EUROPE_KEY = "europe";
    final public static String OLD_KEY = "old";
    final public static String NET_KEY = "net";

    /**
     * 网络操作缓存的图片对应使用的key，使用时需要加上相应的榜单类型
     */
    final public static String PREVIEW = "preview";
    final public static String HEADER = "header";
    final public static String HEADER_BAC = "header_bac";
    final public static String SONG_COVER = "song_cover";

    public BillBoardRequestData(int type, int sum, int offset) {
        this.type = type;
        this.sum = sum;
        this.offset = offset;
    }

    public int getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public int getSum() {
        return sum;
    }
}
