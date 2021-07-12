package com.example.activitymusic.Model;

public class SongItem {
    private String mName;
    private String mAuthor;
    private String mTime;

    public SongItem( String mName, String mAuthor,String mTime){
        this.mName = mName;
        this.mAuthor = mAuthor;
        this.mTime = mTime;
    }

    public String getmName() {
        return mName;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmTime() {
        return mTime;
    }
}
