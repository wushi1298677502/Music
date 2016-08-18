package com.example.scxh.mymusic;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * 记住必须要到androidmainifest里进行provider的注册
 *   <provider
 android:authorities="com.example.scxh.mymusic.MusicContentProvider"
 android:name=".MusicContentProvider"/>
 *
 *
 */
public class MusicContentProvider extends ContentProvider {
    public static final String MYMUSICDATABASE = "collectlove";
    public static final String MYMUSICDATA_PATH = "collect";
    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.example.scxh.mymusic.MusicContentProvider";
    public static final String CONTENT_URI_MYMUSIC = SCHEMA+AUTHORITY+"/"+MYMUSICDATABASE;
    public static final String CONTENT_URI_MYMUSICPATH = SCHEMA+AUTHORITY+"/"+MYMUSICDATA_PATH;
    // TODO: 2016/7/8 content:// com.example.scxh.mymusic/collectlove
    public static final int MYMUSICCODE= 1;
    public static final int MYMUSICCODE_PATH= 2;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,MYMUSICDATABASE,MYMUSICCODE);
        uriMatcher.addURI(AUTHORITY,MYMUSICDATA_PATH,MYMUSICCODE_PATH);
    }


    MyMusicDatabase myMusicDatabase;
    SQLiteDatabase sqLiteDatabase;
    public boolean onCreate() {
        myMusicDatabase = new MyMusicDatabase(getContext());
        sqLiteDatabase=myMusicDatabase.getReadableDatabase();
        return false;
    }

    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        int code = uriMatcher.match(uri);
        switch (code){
            case MYMUSICCODE:
                return sqLiteDatabase.query(MYMUSICDATABASE,strings,s,strings1,null,null,s1);
            case MYMUSICCODE_PATH:
                return sqLiteDatabase.query(MYMUSICDATA_PATH,strings,s,strings1,null,null,s1);
        }


        Cursor cursor = sqLiteDatabase.query(MYMUSICDATABASE,strings,s,strings1,null,null,s1);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int code = uriMatcher.match(uri);
        switch (code){
            case MYMUSICCODE:
                return "vnd.android.cursor.dir"+MYMUSICDATABASE;
            case MYMUSICCODE_PATH:
                return "vnd.android.cursor.dir"+MYMUSICDATA_PATH;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int code = uriMatcher.match(uri);
        long id=0;
        switch (code){
            case MYMUSICCODE:
                id = sqLiteDatabase.insert(MYMUSICDATABASE,null,contentValues);
                break;
            case MYMUSICCODE_PATH:
                id = sqLiteDatabase.insert(MYMUSICDATA_PATH,null,contentValues);
                break;
        }
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        switch (code){
            case MYMUSICCODE:
                return  sqLiteDatabase.delete(MYMUSICDATABASE,selection,selectionArgs);

            case MYMUSICCODE_PATH:
                return  sqLiteDatabase.delete(MYMUSICDATA_PATH,selection,selectionArgs);

        }
        return  sqLiteDatabase.delete(MYMUSICDATA_PATH,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        switch (code){
            case MYMUSICCODE:
                return sqLiteDatabase.update(MYMUSICDATABASE,contentValues,selection,selectionArgs);
            case MYMUSICCODE_PATH:
                return sqLiteDatabase.update(MYMUSICDATA_PATH,contentValues,selection,selectionArgs);
        }
        return sqLiteDatabase.update(MYMUSICDATA_PATH,contentValues,selection,selectionArgs);

    }
}
