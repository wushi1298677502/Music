package com.example.scxh.mymusic;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView IvBack,IvPlayer,IvDowload,IvShare,Ivmodol,IvPre,Ivpap,IvNext,IvMore,Ivcollect;
    ImageView mimageview1,mimageview2;
    boolean back = false;
    boolean checked = true;

    TextView titlename,artist,Album;
    public static final int CURRENT_HANDLER_TYPE = 1;//当前时间
    public static int CURRENT_PLAY_MODEL = 1;//当前播放模式
    private Button mPlayPauseMusicBtn, mStopMusicBtn, mNextMusicBtn,mMusicModeBtn;
    private TextView mCurrentTimeTxt, mTotalTimeTxt, mMusicNameTxt,mabulm;
    private MediaPlayer mMediaPlayer;
    private String mMusicDir;
    private SeekBar mSeekBar;
//    CheckBox Ivcollect;
    private boolean isFlag = true;
    private SimpleDateFormat mDataFormat =new SimpleDateFormat("mm:ss");
    private int mCurrentProgress = 1;
    private int mCurrentPostion;
    ViewPager mViewPager;
    ListView listview;
    String MusciName;
    String ArtistName;
    String img;//图片
    String album;//专辑
    int Image;
    int Length;
    int Id;
    private ArrayList<String> listarraylist = new ArrayList<>();
    private ArrayList<MusicBean> mDataLists = new ArrayList<>();
    private MyMusicAdapter mMusicListAdapter;
    MusicBean musicBean;
    ImageView imageView;
    TextView MusicArtist;
    TextView MusicName;

    SharedPreferences.Editor sharedPreference;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case CURRENT_HANDLER_TYPE:
                    int currentPostion = msg.arg2;
                    mSeekBar.setProgress(currentPostion);
                    mCurrentTimeTxt.setText(mDataFormat.format(new Date(currentPostion)));
                    break;
            }

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IvBack = (ImageView) findViewById(R.id.mymusic_back);
        IvPlayer = (ImageView) findViewById(R.id.mymusic_player);
        IvDowload = (ImageView) findViewById(R.id.mymusic_dowload);
        Ivcollect = (ImageView) findViewById(R.id.mymusic_favorite);
        IvShare = (ImageView) findViewById(R.id.mymusic_share);
        Ivmodol = (ImageView) findViewById(R.id.mymusic_modole);
        IvPre = (ImageView) findViewById(R.id.mymusic_last);
        Ivpap = (ImageView) findViewById(R.id.mymusic_playandpause);
        IvNext = (ImageView) findViewById(R.id.mymusic_next);
        IvMore = (ImageView) findViewById(R.id.mymusic_more);
        mimageview1 = (ImageView) findViewById(R.id.mymusic_pageone);
        mimageview2 = (ImageView) findViewById(R.id.mymusic_pagetwo);

        mSeekBar = (SeekBar) findViewById(R.id.mymusic_seekbar);
        mCurrentTimeTxt = (TextView) findViewById(R.id.mymusic_currenttime);
        mTotalTimeTxt = (TextView) findViewById(R.id.mymusic_totaltime);
        mabulm = (TextView) findViewById(R.id.mymusic_anblum);
        mViewPager = (ViewPager) findViewById(R.id.mymusic_imageview);

        titlename = (TextView) findViewById(R.id.mymusic_titlename);
        artist = (TextView) findViewById(R.id.mymusic_ritist);
        mSeekBar = (SeekBar) findViewById(R.id.mymusic_seekbar);
        Album = (TextView) findViewById(R.id.mymusic_anblum);

        IvBack.setOnClickListener(this);
        IvPlayer.setOnClickListener(this);
        IvDowload.setOnClickListener(this);
        Ivcollect.setOnClickListener(this);
        IvShare.setOnClickListener(this);
        Ivmodol.setOnClickListener(this);
        IvPre.setOnClickListener(this);
        Ivpap.setOnClickListener(this);
        IvNext.setOnClickListener(this);
        IvMore.setOnClickListener(this);


        imageView = (ImageView)findViewById(R.id.mymusic_image);
        MusicArtist = (TextView)findViewById(R.id.music_listitem_title);
        MusicName = (TextView)findViewById(R.id.music_listitem_dir);
        /**
         * 调用contentprovider遍历文件的音乐
         */

        new UpdateUIThread().start();
        sharedPreference = getSharedPreferences("com.example.scxh.myapp.login",MODE_PRIVATE).edit();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.scxh.myapp.login",MODE_PRIVATE);

        if(sharedPreferences!=null) {
            int id = sharedPreferences.getInt("CURRENT_PLAY_MODEL",CURRENT_PLAY_MODEL);
            int currentprogress = sharedPreferences.getInt("CURRENTPROGRESS",mCurrentProgress);
            int currentposition = sharedPreferences.getInt("CURRENTPOSITION",mCurrentPostion);
             String artistname = sharedPreferences.getString("ARTIST","");
             String albums = sharedPreferences.getString("ALBUM","");
             String musicnames = sharedPreferences.getString("MUSICNAME","");

            titlename.setText(musicnames);
            Album.setText(albums);
            artist.setText(artistname);
            mCurrentProgress = currentprogress;
            mCurrentPostion = currentposition;
            Logs.e("id>>>>>>>>>"+id);
            Logs.e("ArtistName>>>>>>>>>"+musicnames);
            Logs.e("album>>>>>>>>>"+albums);
            Logs.e("MusciName>>>>>>>>>"+artistname);
            Logs.e("currentprogress>>>>>>>>>"+currentprogress);
            Logs.e("currentposition>>>>>>>>>"+currentposition);

            CURRENT_PLAY_MODEL = id;
            switch (CURRENT_PLAY_MODEL){
                case MusicPlayModel.LOOPING_MODEL:
                    CURRENT_PLAY_MODEL = MusicPlayModel.LOOPING_MODEL;
                    Ivmodol.setImageResource(R.drawable.bg_button_player_mode_single_nor);
                    break;
                case MusicPlayModel.ORDERING_MODEL:
                    CURRENT_PLAY_MODEL = MusicPlayModel.ORDERING_MODEL;
                    Ivmodol.setImageResource(R.drawable.bg_button_player_mode_order_nor);
                    break;
                case MusicPlayModel.ALL_LOOPING_MODEL:
                    CURRENT_PLAY_MODEL = MusicPlayModel.ALL_LOOPING_MODEL;
                    Ivmodol.setImageResource(R.drawable.bg_button_player_mode_cycle_nor);

                    break;
                case MusicPlayModel.RANDOM_MODEL:
                    CURRENT_PLAY_MODEL =MusicPlayModel.RANDOM_MODEL;
                    Ivmodol.setImageResource(R.drawable.bg_button_player_mode_random_p);
                    break;
            }

        }
        /**
         * 实现viewpager的各个页面
         */
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view1 = layoutInflater.inflate(R.layout.activity_listview_demo_layout,null);
        View view2 = layoutInflater.inflate(R.layout.activity_lyric_demo_layout,null);
        List<View> addView = new ArrayList<>();
        addView.add(view1);
        addView.add(view2);

        MyMusicViewPager myMusicViewPager = new MyMusicViewPager(addView);
        mViewPager.setAdapter(myMusicViewPager);
        /**
         * 必须加view1 因为当前页面已经被viewpager给解析了
         */
        listview = (ListView) view1.findViewById(R.id.listview_demo);
        coursor();
        mMusicListAdapter = new MyMusicAdapter(MainActivity.this);
        listview.setAdapter(mMusicListAdapter);
        Logs.e("mDataLists>>>>>>>>>>>>>>."+mDataLists);
        mMusicListAdapter.setdatalist(mDataLists);
        listview.setOnItemClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logs.e("onPageScrolled>>>>>>>>>>>>>>."+position);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mimageview1.setImageResource(R.drawable.page);
                        mimageview2.setImageResource(R.drawable.page_now);
                        break;
                    case 1:
                        mimageview1.setImageResource(R.drawable.page_now);
                        mimageview2.setImageResource(R.drawable.page);
                        break;

                }
                Logs.e("onPageSelected>>>>>>>>>>>>>>."+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

                playerCurrentMusic();
                 startmyserver();

    }

    /**
     * 插入数据保存喜好
     * @param
     */


    public void InsertData(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",Id);
        Logs.e("insertdata>>>>>"+Id);
        Uri uri = getContentResolver().insert(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),contentValues);
        Logs.e("insertdata>>>>>"+uri);
        Ivcollect.setImageResource(R.drawable.bg_button_player_collected_n);
    }

    /**
     * 点击取消时删除收藏
     * @param
     */
    public void DeletData(){
        String whereClause1 ="id"+" = ?";
        String[] whereArgs1 = {""+Id};
        Logs.e("DeletData>>>>>Id"+Id);
         int id = getContentResolver().delete(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),whereClause1,whereArgs1);
        Logs.e("DeletData>>>>>"+id);
        Ivcollect.setImageResource(R.drawable.bg_button_player_collect_p);
    }
    public void querydata(){
        Logs.e("querydata>>>>>");

        Cursor cursor = getContentResolver().query(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),null,null,null,null);
        while (cursor.moveToNext()){
            int idComumnIndex = cursor.getColumnIndex("id"); //id字段序号

            int id = cursor.getInt(idComumnIndex);

            Logs.e(id +"");

        }
        cursor.close();// TODO: 2016/7/7 不要忘记关闭cursor
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mymusic_playandpause:
                playStopMusic();
                break;
            case R.id.mymusic_last:
                precMusic();
                break;
            case R.id.mymusic_next:
                nextMusic();
                break;
            case R.id.mymusic_modole:
                setPlayMode();
                break;
            case R.id.mymusic_share:
                sharemusic();
                break;
            case R.id.mymusic_back:
                onBackPressed();
                break;
            case R.id.mymusic_dowload:
                querydata();
            case R.id.mymusic_favorite:
                favorite();
                break;
        }
    }
    public void favorite(){
        if(checked){
            InsertData();
            checked = false;
        }else {
            DeletData();
            checked = true;
        }
    }
    /**
     * 启动服务
     */
    public void startmyserver(){
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }
    /**
     * 设置音乐分享
     */
    public void sharemusic(){
        Dialog shareActivity = new ShareActivity(MainActivity.this);
        Window dialogWindow = shareActivity.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        shareActivity.show();
    }
    /**
     * 设置音乐播放模式
     */
    public void setPlayMode(){
        switch (CURRENT_PLAY_MODEL){
            case MusicPlayModel.LOOPING_MODEL:
                CURRENT_PLAY_MODEL = MusicPlayModel.ORDERING_MODEL;
                Toast.makeText(this,"顺序播放",Toast.LENGTH_SHORT).show();
                Ivmodol.setImageResource(R.drawable.bg_button_player_mode_order_nor);
                break;
            case MusicPlayModel.ORDERING_MODEL:
                CURRENT_PLAY_MODEL = MusicPlayModel.ALL_LOOPING_MODEL;
                Toast.makeText(this,"循环播放",Toast.LENGTH_SHORT).show();
                Ivmodol.setImageResource(R.drawable.bg_button_player_mode_cycle_nor);
                break;
            case MusicPlayModel.ALL_LOOPING_MODEL:
                CURRENT_PLAY_MODEL =MusicPlayModel.RANDOM_MODEL;
                Toast.makeText(this,"随机播放",Toast.LENGTH_SHORT).show();
                Ivmodol.setImageResource(R.drawable.bg_button_player_mode_random_p);
                break;
            case MusicPlayModel.RANDOM_MODEL:
                CURRENT_PLAY_MODEL = MusicPlayModel.LOOPING_MODEL;
                Toast.makeText(this,"单曲播放",Toast.LENGTH_SHORT).show();
                Ivmodol.setImageResource(R.drawable.bg_button_player_mode_single_nor);
                break;
        }
    }

    /**
     * 设定退出方式方法
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            sharedPreference.putInt("CURRENT_PLAY_MODEL",CURRENT_PLAY_MODEL);
            sharedPreference.putInt("CURRENTPROGRESS",mCurrentProgress);
            sharedPreference.putInt("CURRENTPOSITION",mCurrentPostion);
            sharedPreference.putString("ARTIST",ArtistName);
            sharedPreference.putString("ALBUM",album);
            sharedPreference.putString("MUSICNAME",MusciName);
            sharedPreference.commit();
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            if(back==true){
                finish();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    back=true;
                    SystemClock.sleep(2000);
                    back=false;
                }
            }).start();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void nextMusic() {
        if (++mCurrentPostion == mDataLists.size()) {
            Toast.makeText(MainActivity.this, "已是最后一首", Toast.LENGTH_SHORT).show();
            mCurrentPostion = mDataLists.size()-1;
        } else {
            playerCurrentMusic();
        }
//        Ivcollect.setBackgroundResource(R.drawable.actionbar_favorite);
//        Cursor cursor = getContentResolver().query(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),null,null,null,null);
//        while (cursor.moveToNext()){
//            int idComumnIndex = cursor.getColumnIndex("id"); //id字段序号
//
//            int id = cursor.getInt(idComumnIndex);
//            if(id == Id){
//                Ivcollect.setImageResource(R.drawable.bg_button_player_collected_n);
//                checked = false;
//                return;
//            }else {
//                Ivcollect.setImageResource(R.drawable.bg_button_player_collect_p);
//
//            }
//
//        }


    }

    public void precMusic(){
        if(--mCurrentPostion < 0 ){
            Toast.makeText(MainActivity.this, "已是第一首", Toast.LENGTH_SHORT).show();
            mCurrentPostion = 0;
        }else{
            playerCurrentMusic();
        }
//        Ivcollect.setBackgroundResource(R.drawable.actionbar_favorite);
//        Cursor cursor = getContentResolver().query(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),null,null,null,null);
//        while (cursor.moveToNext()){
//            int idComumnIndex = cursor.getColumnIndex("id"); //id字段序号
//            int id = cursor.getInt(idComumnIndex);
//            Logs.e("cursor>>>>>>>>"+id);
//            if(id == Id){
//                Ivcollect.setImageResource(R.drawable.bg_button_player_collected_n);
//                checked = false;
//                return;
//            }else {
//                Ivcollect.setImageResource(R.drawable.bg_button_player_collect_p);
//
//            }
//    }
    }

    /**
     * 设置当前音乐资源并播放
     */
    public void playerCurrentMusic() {
        Logs.e("playerCurrentMusic"+mCurrentPostion);
        Logs.e("playerCurrentMusic mDataLists"+mDataLists);
        musicBean = mDataLists.get(mCurrentPostion);

        MusciName = musicBean.getMusicName();
        titlename.setText(MusciName);
        mMusicDir = musicBean.getMusicPath();
        ArtistName = musicBean.getArtist();
        Length = musicBean.getLength();
        Id = musicBean.getId();
        album = musicBean.getAlbum();
        img = musicBean.getImg();

        inintMusic();
        playStopMusic();
    }

    /**
     * 初始化音乐
     */
    private void inintMusic() {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource("file://" + mMusicDir);
            mMediaPlayer.prepare();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    singleModel();
                    orderModel();
                    allLoopingModel();
                    randomModel();
                }
            });

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        mCurrentProgress = progress;
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mMediaPlayer.seekTo(mCurrentProgress);
                }
            });

            int duration = Length;
            mSeekBar.setMax(Length);
            mTotalTimeTxt.setText(mDataFormat.format(new Date(duration)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 播放，暂停
     */
    public void playStopMusic() {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        artist.setText(ArtistName);
        Album.setText(album);
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            Logs.e("pause >>>>");
            Ivpap.setImageResource(R.drawable.bg_button_player_play_p);
        } else {
            mMediaPlayer.start();
            Logs.e("start >>>>Duration ：" + mMediaPlayer.getDuration() + " currentPostion ：" + mMediaPlayer.getCurrentPosition());
            Ivpap.setImageResource(R.drawable.bg_button_player_pause_p);
        }
        Cursor cursor = getContentResolver().query(Uri.parse(MusicContentProvider.CONTENT_URI_MYMUSICPATH),null,null,null,null);
        while (cursor.moveToNext()){
            int idComumnIndex = cursor.getColumnIndex("id"); //id字段序号
            int id = cursor.getInt(idComumnIndex);
            Logs.e("cursor>>>>>>>>"+id);
            if(id == Id){
                Ivcollect.setImageResource(R.drawable.bg_button_player_collected_n);
                checked = false;
                return;
            }else {
                Ivcollect.setImageResource(R.drawable.bg_button_player_collect_p);

            }
        }


    }

    /**
     * 单曲播放
     */
    public void singleModel(){
        if(CURRENT_PLAY_MODEL == MusicPlayModel.LOOPING_MODEL) {
            if (mMediaPlayer != null) {
                mMediaPlayer.setLooping(true); //单曲循环
            }
            playerCurrentMusic();
        }else {
            if (mMediaPlayer != null) {
                mMediaPlayer.setLooping(false); //单曲循环
            }
        }
    }

    /**
     * 顺序播放
     */
    public void orderModel(){
        if(CURRENT_PLAY_MODEL == MusicPlayModel.ORDERING_MODEL){
            nextMusic();
        }
    }

    /**
     * 全部循环
     */
    public void allLoopingModel(){
        if(CURRENT_PLAY_MODEL == MusicPlayModel.ALL_LOOPING_MODEL) {
            if (++mCurrentPostion == mDataLists.size()) {
                mCurrentPostion = 0;
            }
            playerCurrentMusic();
        }
    }
    /**
     * 随机播放
     */
    public void randomModel(){
        if(CURRENT_PLAY_MODEL == MusicPlayModel.RANDOM_MODEL) {
            int number = mDataLists.size();
            Random random = new Random();
            mCurrentPostion = random.nextInt(number);
            playerCurrentMusic();
        }
    }


    /**
    * 点击播放新音乐
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyMusicAdapter adapter = (MyMusicAdapter) parent.getAdapter();
         musicBean = (MusicBean) adapter.getItem(position);
        Logs.e("onItemClick"+position);
        mCurrentPostion = position;
        playerCurrentMusic();

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


    /**
     * 根据音乐名称和艺术家来判断是否重复包含了
     *
     * @param
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
        //三目运算符
        return null == result ? null : result;
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
            holdView.Musicicon.setImageBitmap(icon  == null ? BitmapFactory.decodeResource(getResources(), R.drawable.genre_1) : icon );
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


    /**
     * 重置，初始化音乐
     */
    public void stopResetMusic() {
        mMediaPlayer.reset();
        Logs.e("reset >>>>");
        inintMusic();
//        mPlayPauseMusicBtn.setText("播放");
    }

    /**
     * 更新播放进度
     */
    class UpdateUIThread extends Thread {
        @Override
        public void run() {
            while (isFlag) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    int currentPostion = mMediaPlayer.getCurrentPosition();
                    Message message = Message.obtain();
                    message.arg1 = CURRENT_HANDLER_TYPE;
                    message.arg2 = currentPostion;
                    mHandler.sendMessage(message);

                    SystemClock.sleep(1000);
                }
            }
        }
    }

    /**
     * 设置viewpager的适配器
     */
    class MyMusicViewPager extends PagerAdapter{
        List<View> addlistview = new ArrayList<>();
        public MyMusicViewPager(List<View> list){
            this.addlistview = list;
        }
        @Override
        public int getCount() {
            return addlistview.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        public Object instantiateItem(ViewGroup container, int position) {
            View view = addlistview.get(position);
            container.addView(view);
            return view;
        }
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = addlistview.get(position);
            container.removeView(view);
        }

    }

    class ShareActivity extends AlertDialog {
        Activity context;
        protected ShareActivity(Activity context) {
            super(context);
            this.context =  context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_share_layout);

            Button weixin = (Button) findViewById(R.id.share_weixin);
            Button qq = (Button) findViewById(R.id.share_qq);

            weixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }
    public void onBackPressed(){

        Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
        if(back==true){
            sharedPreference.putInt("CURRENT_PLAY_MODEL",CURRENT_PLAY_MODEL);
            sharedPreference.putInt("CURRENTPROGRESS",mCurrentProgress);
            sharedPreference.putInt("CURRENTPOSITION",mCurrentPostion);
            sharedPreference.putString("ARTIST",ArtistName);
            sharedPreference.putString("ALBUM",album);
            sharedPreference.putString("MUSICNAME",MusciName);

            sharedPreference.commit();
            finish();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                back=true;
                SystemClock.sleep(2000);
                back=false;
            }
        }).start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 释放音乐资源
         */
        isFlag = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;

        }
    }
}
