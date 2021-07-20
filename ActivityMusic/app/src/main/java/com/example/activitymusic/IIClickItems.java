package com.example.activitymusic;

import android.view.View;
import android.widget.ImageButton;

import com.example.activitymusic.Model.SongItem;

public interface IIClickItems {
    void onItemClick(SongItem song, int pos);
    void onSongBtnClickListener(ImageButton btn, View v, SongItem song, int pos);
}

