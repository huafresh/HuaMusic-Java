package com.example.hua.huachuang.bean.music;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**  百度返回的一首单曲信息
     {"songinfo":
         {"special_type":0,
         "pic_huge":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_1000",
         "resource_type":"0",
         "pic_premium":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_500",
         "havehigh":2,
         "author":"张信哲",
         "toneid":"0",
         "has_mv":0,
         "song_id":"533357685",
         "piao_id":"0",
         "artist_id":"166",
         "lrclink":"http:\/\/musicdata.baidu.com\/data2\/lrc\/e156f9f097cb5cc929b1774dd58c807e\/533358233\/533358233.lrc",
         "relate_status":"1",
         "learn":0,
         "pic_big":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_150",
         "play_type":0,
         "album_id":"533357576",
         "album_title":"夏夜星空海",
         "bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}",
         "song_source":"web",
         "all_artist_id":"166",
         "all_artist_ting_uid":"1118",
         "all_rate":"64,128,256,320,flac",
         "charge":0,"copy_type":"0",
         "is_first_publish":0,
         "korean_bb_song":"0",
         "pic_radio":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_300",
         "has_mv_mobile":0,
         "title":"夏夜星空海",
         "pic_small":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_90",
         "album_no":"0",
         "resource_type_ext":"0",
         "ting_uid":"1118"},
         "error_code":22000,
         "bitrate":{"show_link":"http:\/\/zhangmenshiting.baidu.com\/data2\/music\/34e6fb21e745abf77d4ce97ec74dbd59\/533357704\/533357704.mp3?xcode=d1595e5412a180ee9d88bb873c3ec0e5",
         "free":1,
         "song_file_id":533357704,
         "file_size":1781791,
         "file_extension":"mp3",
         "file_duration":223,
         "file_bitrate":64,
         "file_link":"http:\/\/yinyueshiting.baidu.com\/data2\/music\/34e6fb21e745abf77d4ce97ec74dbd59\/533357704\/533357704.mp3?xcode=d1595e5412a180ee9d88bb873c3ec0e5",
         "hash":"d349dfde206a1bfbde8d745c1daf5ff1fae807ed"}
     }
 */

public class Music implements Serializable{
    private String songName;
    //文件名，注意与上面的区别
    private String fileName;
    private String author;
    private int duration;
    private int fileSize;
    private String path;
    private String folderPath;
    //唯一标识歌曲，从0开始递增
    private int id;
    //在自建数据库中的添加日期，这里简化为整形数，主要用于排序
    private int date;
    //存放歌手拼音首字母
    private String authorFirstC;
    private String albumName;
    //是否是当前在播放
    private boolean isSelect;
    //是否是在线的
    private boolean isOnline;
    //所属音乐列表，掩码方式
    private int listType;
    //在列表中显示时，是否显示头
    private boolean isHaveHeader;
    //网络地址
    private String downLoadUrl;
    //歌词下载地址
    private String lrcUrl;
    //头像下载地址
    private String coverUrl;
    //歌曲下载链接
    private String musicLink;
    private String titleAndAuthor;

    //以下来自onlineMp3
    final public static String SONG_COVER = "song_cover";

    private Bitmap bitmapCover;

    private String cover_image_key;
    private String artist;
    private String albumTitle;

    private String LrcOnlinePath;
    private List<LrcContent> lrcContents;

    private String picSmallUrl;
    private String picBigUrl;

    private String song_id;

    public Music() {
        lrcContents = new ArrayList<>();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitleAndAuthor() {
        return titleAndAuthor;
    }

    public void setTitleAndAuthor(String titleAndAuthor) {
        this.titleAndAuthor = titleAndAuthor;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public String getMusicLink() {
        return musicLink;
    }

    public void setMusicLink(String musicLink) {
        this.musicLink = musicLink;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getListType() {
        return listType;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorFirstC() {
        return authorFirstC;
    }

    public void setAuthorFirstC(String authorFirstC) {
        this.authorFirstC = authorFirstC;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    //以下来自onlineMp3
    public Bitmap getBitmapCover() {
        return bitmapCover;
    }

    public void setBitmapCover(Bitmap bitmapCover) {
        this.bitmapCover = bitmapCover;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getPicSmallUrl() {
        return picSmallUrl;
    }

    public void setPicSmallUrl(String picSmallUrl) {
        this.picSmallUrl = picSmallUrl;
    }

    public String getPicBigUrl() {
        return picBigUrl;
    }

    public void setPicBigUrl(String picBigUrl) {
        this.picBigUrl = picBigUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getLrcOnlinePath() {
        return LrcOnlinePath;
    }

    public void setLrcOnlinePath(String lrcOnlinePath) {
        LrcOnlinePath = lrcOnlinePath;
    }

    public List<LrcContent> getLrcContents() {
        return lrcContents;
    }

    public void setLrcContents(List<LrcContent> lrcContents) {
        this.lrcContents = lrcContents;
    }

    public String getCover_image_key() {
        return cover_image_key;
    }

    public void setCover_image_key(String cover_image_key) {
        this.cover_image_key = cover_image_key;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public boolean isHaveHeader() {
        return isHaveHeader;
    }

    public void setHaveHeader(boolean haveHeader) {
        isHaveHeader = haveHeader;
    }


}

