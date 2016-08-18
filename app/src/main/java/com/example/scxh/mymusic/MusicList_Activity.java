package com.example.scxh.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicList_Activity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private final static String MP3 = ".mp3";
    private ListView mListView;
    private ArrayList<MusicBean> mDataLists = new ArrayList<>();
    private List<MusicBean> list = new ArrayList<>();
    private MyMusicAdapter mMusicListAdapter;
    public static final int CURRENT_HANDLER_TYPE = 1;//当前时间
    public static int CURRENT_PLAY_MODEL = MusicPlayModel.LOOPING_MODEL;//当前播放模式
    private Button mPlayPauseMusicBtn, mStopMusicBtn, mNextMusicBtn,mMusicModeBtn;
    private TextView mCurrentTimeTxt, mTotalTimeTxt, mMusicNameTxt,mAritist;
    private MediaPlayer mMediaPlayer;
    private String mMusicDir;
    private SeekBar mSeekBar;
    ImageView IvBack,IvPlayer,IvDowload,Ivcollect,IvShare,Ivmodol,IvPre,Ivpap,IvNext,IvMore;
    MusicBean musicBean;
    ImageView imageView;
    TextView MusicArtist;
    TextView MusicName;
//    String MusicName;
    String MusicPath;
    String Artist;
    String Img;
    int Image;
    int Length;
    int Id;
    private boolean isFlag = true;
    private SimpleDateFormat mDataFormat =new SimpleDateFormat("mm:ss");
    private int mCurrentProgress;
    private int mCurrentPostion;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list_layout);

//        mListView = (ListView) findViewById(R.id.MusicList_listView);
//        mListView.setOnItemClickListener(this);
//
//        imageView = (ImageView)findViewById(R.id.mymusic_image);
//        MusicArtist = (TextView)findViewById(R.id.music_listitem_title);
//        MusicName = (TextView)findViewById(R.id.music_listitem_dir);
//
//
//        coursor();
//        init();
//        mMusicListAdapter = new MyMusicAdapter(this);
//        mListView.setAdapter(mMusicListAdapter);
//
//        mMusicListAdapter.setdatalist(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyMusicAdapter adapter = (MyMusicAdapter) parent.getAdapter();
        musicBean = (MusicBean) adapter.getItem(position);

        Intent intent = new Intent(this,MainActivity.class);
        intent.putParcelableArrayListExtra("MUSIC_LIST",mDataLists);
        intent.putExtra("CURRENT_POSITION",position);
        Logs.e("MusicList_Activity");
        startActivity(intent);
    }

    /**
     * 递归遍历获取数据源
     *
     * @param
     */
    public void coursor() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            // 如果不是音乐
            String isMusic = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic != null && isMusic.equals("")){continue;}


            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

            if (isRepeat(title, artist)) continue;

            musicBean = new MusicBean();
            musicBean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            musicBean.setMusicName(title);
            musicBean.setArtist(artist);
            musicBean.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            musicBean.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
            musicBean.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            musicBean.setImg(getAlbumImage(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));

            mDataLists.add(musicBean);
        }
    }

    public void AddItem(String Artist,String MusicName,String Image){
        MusicBean musicBean = new MusicBean(Artist,MusicName,Image);

        list.add(musicBean);
    }




    /* * 根据音乐名称和艺术家来判断是否重复包含了
             *
     * @param title
             * @param artist
             * @return
     */
    private boolean isRepeat(String title, String artist) {
        for (MusicBean music :mDataLists) {
            if (title.equals(music.getMusicName()) && artist.equals(music.getArtist())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据歌曲id获取图片
     *
     * @param albumId
     * @return
     */
    private String getAlbumImage(int albumId) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    Uri.parse("content://media/external/audio/albums/"
                            + albumId), new String[]{"album_art"}, null,
                    null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
                result = cursor.getString(0);
                break;
            }
        } catch (Exception e) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return null == result ? null : result;
    }
    public void init(){
        int i = mDataLists.size();
        for(int g = 0;g<i;g++){
            musicBean = mDataLists.get(g);

            String artistname = musicBean.getArtist();
            String musicname = musicBean.getMusicName();
            String picture = musicBean.getImg();
            Logs.e("musicname>>>>>>>>>"+musicname);
            AddItem(artistname,musicname,picture);

        }

    }










    class MyMusicAdapter extends BaseAdapter {

        public List<MusicBean> MusicList = new ArrayList<>();
        public LayoutInflater layoutInflater;

        public MyMusicAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }
        public void setdatalist(List<MusicBean> list){
            this.MusicList = list;
            notifyDataSetChanged();// TODO: 2016/6/30 当数据改变时通知发送改变

        }
        public int getCount() {
            return MusicList.size();
        }

        @Override
        public Object getItem(int i) {
            return MusicList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                // todo 一级优化  优化View不被重复解析
                view = layoutInflater.inflate(R.layout.activity_music_listitem_layout, null);

                ImageView icon = (ImageView) view.findViewById(R.id.mymusic_image);
                TextView titletxt = (TextView) view.findViewById(R.id.music_listitem_title);
                TextView contenttxt = (TextView) view.findViewById(R.id.music_listitem_dir);

                // todo 二级优化  优化view控件不被重复加载
                holdView = new HoldView();
                holdView.Musicicon = icon;
                holdView.MusicArtist = titletxt;
                holdView.MusicName = contenttxt;
                view.setTag(holdView);// todo setTag里面放的是一个对象，为了view控件不被重复加载，需要再定义一个类放控件
            }

            holdView = (HoldView) view.getTag();
            // todo 从View对象中获取控件实例
            MusicBean item = (MusicBean) getItem(i);

            Bitmap icon  = BitmapFactory.decodeFile(item.getImg());
            Logs.e("icon>>>>>>>>>>"+item.getImg());
            Logs.e("icon>>>>>>>>>>"+icon);
            holdView.Musicicon.setImageBitmap(icon  == null ? BitmapFactory.decodeResource(getResources(), R.drawable.migu) : icon );
            holdView.MusicArtist.setText(item.getArtist());
            holdView.MusicName.setText(item.getMusicName());
            return view;
        }
    }

    class HoldView{
        ImageView Musicicon;
        TextView MusicArtist;
        TextView MusicName;
    }

}
