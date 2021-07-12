package com.example.activitymusic.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SongItem {

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
    }
}
