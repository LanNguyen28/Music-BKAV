package com.example.activitymusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MediaPlaybackService extends Service {

    public static final String ID_CHANNEL = "21";
    private static final CharSequence NAME_MUSIC ="AppMusic" ;             // khai báo biến

    private SongMedia mSongMedia;


    private MusicBinder mMusicBinder = new MusicBinder();

    public SongMedia getMedia() {
        return mSongMedia;
    }

    public MediaPlaybackService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    public class MusicBinder extends Binder {
        public MediaPlaybackService getMusicService() {
            return MediaPlaybackService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSongMedia = new SongMedia(this);
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}