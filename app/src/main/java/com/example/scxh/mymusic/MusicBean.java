package com.example.scxh.mymusic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scxh on 2016/7/5.
 */
public  class MusicBean implements Parcelable{
    String MusicName;
    String MusicPath;
    String Artist;
    String Img;
    String Album;
    int Image;
    int Length;
    int Id;
    public MusicBean(){

    }

    public MusicBean(String Artist,String MusicName,String Img){
        this.MusicName = MusicName;
        this.Artist = Artist;
        this.Img = Img;

    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public String getMusicPath() {
        return MusicPath;
    }

    public void setMusicPath(String musicPath) {
        MusicPath = musicPath;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    protected MusicBean(Parcel in) {
        MusicName = in.readString();
        MusicPath = in.readString();
        Artist = in.readString();
        Img = in.readString();
        Album = in.readString();
        Image = in.readInt();
        Length = in.readInt();
        Id = in.readInt();
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel in) {
            return new MusicBean(in);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MusicName);
        parcel.writeString(MusicPath);
        parcel.writeString(Artist);
        parcel.writeString(Img);
        parcel.writeString(Album);
        parcel.writeInt(Image);
        parcel.writeInt(Length);
        parcel.writeInt(Id);
    }
}
