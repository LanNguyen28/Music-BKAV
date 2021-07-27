package com.example.activitymusic.model;

public class SongItem {
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
    }
}

