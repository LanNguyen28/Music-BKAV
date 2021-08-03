package com.example.activitymusic.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.activitymusic.interfaces.UIMediaUpdate;
import com.example.activitymusic.model.SongItem;

import java.io.IOException;
import java.util.ArrayList;

public class SongMedia {
    private ArrayList<SongItem> mSongItems = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private boolean isStatusPlay = false;
    private int mCurrentPlay = -1;                                        // khởi tạo
    private Context mContext;
    private boolean isPause =false;
    private UIMediaUpdate mUIMediaUpdate;
    private SongItem mSongItem;
    private long mIdPlay;
    private boolean isResume = false;
    SharedPreferences sharedPreferencesCurrent;
    private boolean isFist = true;

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

    public void seekTo(int position) { mMediaPlayer.seekTo(position); }

    public void setMediaUpdateUI(UIMediaUpdate mUIMediaUpdate) {
        this.mUIMediaUpdate = mUIMediaUpdate;
    }

    public  int getCurrentPosition(){
        if(mMediaPlayer!=null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    public long getDuration(){
        if(mMediaPlayer!=null)
            return mMediaPlayer.getDuration();

        return 0;
    }

    public SongMedia(Context mContext) {
        this.mContext = mContext;
        initMediaPlayer();
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
                int current= getmCurrentPlay() +1;
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
        isPause =true;
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

}
