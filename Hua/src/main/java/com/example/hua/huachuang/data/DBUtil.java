package com.example.hua.huachuang.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import com.example.hua.huachuang.base.HuaApplication;
import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.download.DownloadManager;
import com.example.hua.huachuang.share.Constant;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/27.
 * 操作数据库
 */

public class DBUtil {

    /**
     * 从自建数据库中读取音乐信息，若数据库不存在，则从MediaStore中读取
     * 扫描结果存放在全局音乐列表里
     * 自建数据库感觉没有必要，暂放
     */
    public static void getMusicInfo() {
        SQLiteDatabase db = openDb();
        queryLocal(db);
        queryRecently(db);
    }

    private static void queryLocal(SQLiteDatabase db) {
        String sqlLocal = "select * from local";
        Cursor cursor = db.rawQuery(sqlLocal,null);
        if(cursor==null || cursor.getCount()==0) {
            getMp3Info(ShareList.localList);
            return;
        }
        while (cursor.moveToNext()) {
            Music mp3Info = new Music();
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.ID));
            mp3Info.setId(id);
            String songName = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.NAME));
            mp3Info.setSongName(songName);
            String author = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.AUTHOR));
            mp3Info.setAuthor(author);
            String album = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.ALBUM));
            mp3Info.setAlbumName(album);
            int duration = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.DURATION));
            mp3Info.setDuration(duration);
            int fileSize = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.SIZE));
            mp3Info.setFileSize(fileSize);
            String path = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.PATH));
            mp3Info.setPath(path);
            mp3Info.setFolderPath(DensityUtil.getFolderPath(path));
            int date = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.DATE));
            mp3Info.setDate(date);
//            int type = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.TYPE));
//            ShareList.localList.add(mp3Info);
//            if((type&Constant.recentlyPlayID) != 0)
//                ShareList.recentlyList.add(mp3Info);
//            else if((type&Constant.haveDownListID) != 0)
//                ShareList.haveDownList.add(mp3Info);
//            else if((type&Constant.downingListID) != 0)
//                ShareList.downingList.add(mp3Info);
            mp3Info.setSelect(false);
            mp3Info.setHaveHeader(false);
            ShareList.localList.add(mp3Info);
        }
        cursor.close();
    }

    public static void insertLocal(Music music) {
        SQLiteDatabase db = openDb();
        ContentValues values = new ContentValues();
        values.put(DBHelper.LocalColumn.ID,music.getId());
        values.put(DBHelper.LocalColumn.NAME,music.getId());
        values.put(DBHelper.LocalColumn.AUTHOR,music.getId());
        values.put(DBHelper.LocalColumn.ALBUM,music.getId());
        values.put(DBHelper.LocalColumn.DURATION,music.getId());
        values.put(DBHelper.LocalColumn.SIZE,music.getId());
        values.put(DBHelper.LocalColumn.PATH,music.getId());
        values.put(DBHelper.LocalColumn.ID,music.getId());
        //db.insert(DBHelper.LOCAL_TABLE,null,)
    }

    private static void queryRecently(SQLiteDatabase db) {
        String sqlRecent = "select * from recently";
        Cursor cursor = db.rawQuery(sqlRecent,null);
        if(cursor==null) return;
        while (cursor.moveToNext()) {
            Music music = new Music();
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.LocalColumn.ID));
            music.setId(id);
            String songName = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.NAME));
            music.setSongName(songName);
            String author = cursor.getString(cursor.getColumnIndex(DBHelper.LocalColumn.AUTHOR));
            music.setAuthor(author);
            ShareList.AddMusicToRecentPlay(music);
        }
        cursor.close();
    }

    private static SQLiteDatabase openDb() {
        DBHelper helper = new DBHelper(HuaApplication.getInstance(),"music.db",1);
        return helper.getWritableDatabase();
    }

    /**
     * 从MediaStore中读取歌曲信息
     */
    public static void getMp3Info(List<Music> lists){
        if(lists!=null) {
            lists.clear();
        } else return;
        ContentResolver resolver = HuaApplication.getInstance().getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        assert cursor != null;
        if(cursor.getCount() == 0) {
            return;
        }
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic != 0)
            {
                Music mp3Info = new Music();
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                mp3Info.setSongName(title);
                String author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                mp3Info.setAuthor(author);
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                mp3Info.setAlbumName(album);
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                mp3Info.setDuration(duration);
                int fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                mp3Info.setFileSize(fileSize);
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                mp3Info.setPath(path);
                mp3Info.setFolderPath(DensityUtil.getFolderPath(path));
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                mp3Info.setFileName(filePath);
                mp3Info.setSelect(false);
                mp3Info.setHaveHeader(false);
                mp3Info.setOnline(false);
                mp3Info.setListType(Constant.localListID);
                lists.add(mp3Info);
//                String c = CharactersUtil.getFirstLetter(author.charAt(0));
//                mp3Info.setAuthorFirstC(c);
            }
        }
//        Collections.sort(localList);  //对列表按拼音首字母排序
        for(int i = 0; i < lists.size(); i++)
            lists.get(i).setId(i);
        cursor.close();
    }

    /**
     * 获取一个下载任务信息
     */
    public static DowningInfo getDownInfo(long id,DownloadManager dm) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = dm.query(query);
        if(cursor==null) return null;
        DowningInfo downInfo = new DowningInfo();
        if(cursor.moveToFirst()) {
            String current = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            String total = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
            String status = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String reason = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
            Log.i("status",status);
            downInfo.setCurrent(Integer.parseInt(current));
            downInfo.setTotal(Integer.parseInt(total));
            downInfo.setUrl(url);
            downInfo.setStatus(Integer.parseInt(status));
            downInfo.setId(id);
            downInfo.setReason(reason);
        }
        cursor.close();
        return downInfo;
    }

    /**
     * 获取多个下载任务信息
     */
    public static List<DowningInfo> getDownTasks(List<Long> ids,DownloadManager dm) {
        List<DowningInfo> lists = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Long id = ids.get(i);
            DowningInfo info = getDownInfo(id,dm);
            lists.add(info);
        }
        return lists;
    }
}
