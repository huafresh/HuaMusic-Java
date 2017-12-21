package com.example.hua.huachuang.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hua on 2017/3/19.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory c, int version) {
        super(context,name,c,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String sql = "create table diary"+
                        "("+
                        "_id integer primary autoincrement"+
                        "topic varchar(100)"+
                        "content varchar(1000)"+
                        ")";
            db.execSQL(sql);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table diary";
        db.execSQL(sql);
        this.onCreate(db);
        }
}
