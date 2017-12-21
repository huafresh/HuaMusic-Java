package com.example.hua.huachuang.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hua on 2017/2/28.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String LOCAL_TABLE = "local";
    public static final String RECENT_TABLE = "recently";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public DBHelper(Context context, String name,int version) {
        super(context, name, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //本地音乐表
        //编号	歌曲名	歌手	 专辑名	添加日期	 所属列表	时长 	文件大小 	存储路径
        //_id	songName	author	albumName	addDate	listType	duration	fileSize	path
        //                                  (即添加顺序，不是真正的日期)
        String sqlLoacal = "create table "+LOCAL_TABLE+"( "+
                "_id integer,songName text,author text,albumName text," +
                "addDate integer,listType integer,duration integer,fileSize integer,path text)";
        db.execSQL(sqlLoacal);

        //播放记录表
        //歌曲id 歌曲名  歌手
        //_id   songName author
        String sqlRecently = "create table "+RECENT_TABLE+"(_id integer,songName text,author text)";
        db.execSQL(sqlRecently);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    interface BaseColumn {
        String ID = "_id";
        String NAME = "_songName";
        String AUTHOR = "_author";
    }

    //本地音乐表列名
    interface LocalColumn extends BaseColumn{
        String ALBUM = "_albumName";
        String DATE = "_addDate";
        String TYPE = "_listType";
        String DURATION = "_duration";
        String SIZE = "_fileSize";
        String PATH  = "_path";
    }

}
