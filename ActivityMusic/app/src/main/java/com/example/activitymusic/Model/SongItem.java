package com.example.activitymusic.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SongItem {
<<<<<<< HEAD
    private long mSongID;
    private String mSongName;
    private String mSongTime;
    private String mSongAuthor;
    private String mSongImg;

    public SongItem(long mSongID, String mSongName, String mSongTime, String mSongAuthor, String mSongImg, boolean mIsPlay) {
        this.mSongID = mSongID;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongAuthor = mSongAuthor;
        this.mSongImg = mSongImg;
        this.mIsPlay = mIsPlay;
    }

    private boolean mIsPlay;

    public boolean ismIsPlay() {
        return mIsPlay;
    }

    public void setmIsPlay(boolean mIsPlay) {
        this.mIsPlay = mIsPlay;
    }

    public long getmSongID() {
        return mSongID;
    }

    public void setmSongID(long mSongID) {
        this.mSongID = mSongID;
    }

    public String getmSongName() {
        return mSongName;
    }

    public void setmSongName(String mSongName) {
        this.mSongName = mSongName;
    }

    public String getmSongTime() {
        return mSongTime;
    }

    public void setmSongTime(String mSongTime) {
        this.mSongTime = mSongTime;
    }

    public String getmSongAuthor() {
        return mSongAuthor;
    }

    public void setmSongAuthor(String mSongAuthor) {
        this.mSongAuthor = mSongAuthor;
    }

    public void setmSongImg(String mSongImg) {
        this.mSongImg = mSongImg;
    }

    public String getmSongImg() {
        return mSongImg;
=======

    public static final List<Song> SONG_ITEMS = new ArrayList<>();
    public static final String SONG_ID_KEY = "item_id";

    public static class Song {
        private String mName;
        private String mAuthor;
        private String mTime;

        public Song(String mName, String mAuthor, String mTime) {
            this.mName = mName;
            this.mAuthor = mAuthor;
            this.mTime = mTime;
        }

//        public String getmName() {
//            return mName;
//        }
//
//        public String getmAuthor() {
//            return mAuthor;
//        }
//
//        public String getmTime() {
//            return mTime;
//        }
    }

    private static void addItem(Song item) {
        Log.d("LanNT", "addItem: ");
        SONG_ITEMS.add(item);
    }

    static {
        for (int i = 0; i < 3; i++) {
            addItem(createSongAtPosition(i));
        }
    }

    private static Song createSongAtPosition(int position) {
        String newName;
        String newAuthor;
        String newTime;
        switch (position) {
            case 0:
                newName = "Cry ";
                newAuthor = "Cry\n\nMany ";
                newTime ="3.18";
                break;
            case 1:
                newName = "My Bonnie";
                newAuthor = "My Bonnie\n\nsame session";
                newTime ="2.19";
                break;
            default:
                newName ="hii";
                newAuthor="hhhh";
                newTime="3.2";
                break;
        }
        return new Song(newName, newAuthor,newTime);
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
    }
}

