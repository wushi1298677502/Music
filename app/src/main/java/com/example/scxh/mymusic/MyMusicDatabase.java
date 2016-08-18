package com.example.scxh.mymusic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scxh on 2016/7/9.
 */
public class MyMusicDatabase extends SQLiteOpenHelper {
    private final static String DATABASENAME = "mymusicsqlite.db";
    private final static  String COLLECT = "create table collectlove(id Integer NOT NULL PRIMARY KEY AUTOINCREMENT,number Integer(20))";
    private final static  String COLLECTLOVE = "create table collect(id Integer NOT NULL PRIMARY KEY )";
    private final static  int DATAVERSION = 2;
    public MyMusicDatabase(Context context) {
        super(context,DATABASENAME,null,DATAVERSION);

    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Logs.e("MyMusicDatabase>>>>>>>>>>>>1");
        sqLiteDatabase.execSQL(COLLECTLOVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int newversion) {
        switch (old){
            case 1:
//                Logs.e("MyMusicDatabase>>>>>>>>>>>>onUpgrade");
//                sqLiteDatabase.execSQL(COLLECTLOVE);
        }
    }
}
