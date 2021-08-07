package com.example.activitymusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.activitymusic.interfaces.UIMediaUpdate;
import com.example.activitymusic.model.SongItem;

import java.io.IOException;
import java.util.ArrayList;

public class MediaPlaybackService extends Service {

    public static final String ID_CHANNEL = "21";
    private static final CharSequence NAME_MUSIC = "AppMusic";             // khai báo biến

    private MusicBinder mMusicBinder = new MusicBinder();

    public MediaPlaybackService() {
    }

    private ArrayList<SongItem> mSongItems = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private boolean isStatusPlay = false;
    private int mCurrentPlay = -1;
    private Context mContext;
    private boolean isPause = false;
    private UIMediaUpdate mUIMediaUpdate;
    private SongItem mSongItem;
    private long mIdPlay;
    private boolean isResume = false;
    SharedPreferences sharedPreferencesCurrent;
    private boolean isFist = true;


    // khởi tạo

    public boolean isResume() {
        return isResume;
    }

    public long getIdPlay() {
        return mIdPlay;
    }

    public void setIdPlay(long mIdPlay) {
        this.mIdPlay = mIdPlay;
    }

    public void setmListSong(ArrayList<SongItem> mSongItems) {
        this.mSongItems = mSongItems;
    }

    public ArrayList<SongItem> getmSongItems() {
        return mSongItems;
    }

    public int getmCurrentPlay() {
        return mCurrentPlay;
    }
    public boolean isStatusPlay() {
        return isStatusPlay;
    }

    public void setmCurrentPlay(int mCurrentPlay) {
        this.mCurrentPlay = mCurrentPlay;
    }

    public ArrayList<SongItem> getDataMusic() {
        return mSongItems;
    }

    public MediaPlayer getPlayer() {
        return mMediaPlayer;
    }

    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void setMediaUpdateUI(UIMediaUpdate mUIMediaUpdate) {
        this.mUIMediaUpdate = mUIMediaUpdate;
    }

    public boolean isFist() {
        return isFist;
    }


    public int getCurrentPositionPlay() {
        sharedPreferencesCurrent = mContext.getSharedPreferences("DATA_CURRENT_PLAY", Context.MODE_PRIVATE);
        int position = sharedPreferencesCurrent.getInt("DATA_CURRENT_STREAM_POSITION", 0);
        return mMediaPlayer.getCurrentPosition();        //trả về vtri đang phát

    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
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

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("hii", "onCompletion: ");
                mediaPlayer.reset();
                int current = getmCurrentPlay() + 1;
                String pathNext = mSongItems.get(current).getmSongImg();
                setmCurrentPlay(current);
                playSong(pathNext);
                mUIMediaUpdate.mediaUI(current);
            }
        });
    }

    public void playSong(String path) {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            isStatusPlay = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        mMediaPlayer.pause();
        isStatusPlay = false;
        isPause = true;
    }

    public void stopSong() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    public void resumeSong() {
        mMediaPlayer.start();
        isStatusPlay = true;
    }

    public void nextSong(int pos) {
        pos++;
        if (pos > mSongItems.size() - 1) {
            pos = 0;
        }
        mCurrentPlay = pos;
    }

    public void previousSong(int pos) {
        pos--;
        if (pos < 0) {
            pos = mSongItems.size() - 1;
        }
        mCurrentPlay = pos;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}