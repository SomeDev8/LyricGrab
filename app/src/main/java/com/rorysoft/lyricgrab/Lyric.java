package com.rorysoft.lyricgrab;

import android.os.Parcel;
import android.os.Parcelable;

public class Lyric implements Parcelable {

    private String songTitle;
    private String artistName;
    private String albumName;
    private String imagePath;
    private String lyricsPath;

    public Lyric(Parcel parcel) {

        songTitle = parcel.readString();
        artistName = parcel.readString();
        albumName = parcel.readString();
        imagePath = parcel.readString();
        lyricsPath = parcel.readString();

    }

    public Lyric() {

    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLyricsPath() {
        return lyricsPath;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(artistName);
        dest.writeString(albumName);
        dest.writeString(imagePath);
        dest.writeString(lyricsPath);
    }

    public static Creator<Lyric> CREATOR = new Creator<Lyric>() {
        @Override
        public Lyric createFromParcel(Parcel source) {
            return new Lyric(source);
        }

        @Override
        public Lyric[] newArray(int size) {
            return new Lyric[size];
        }
    };
}
