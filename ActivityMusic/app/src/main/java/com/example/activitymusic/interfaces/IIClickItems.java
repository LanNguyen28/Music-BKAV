package com.example.activitymusic.interfaces;

import android.view.View;
import android.widget.ImageButton;

import com.example.activitymusic.model.SongItem;

public interface IIClickItems {
    void onItemClick(SongItem songItem);
    void onSongBtnClickListener(ImageButton btnImg, View v, SongItem songItem, int pos);
}

