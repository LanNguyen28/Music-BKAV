package com.example.activitymusic.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.activitymusic.R;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.service.SongMedia;

import java.util.ArrayList;


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener {

    private static final String SONG_NAME = "name";
    private static final String SONG_ARTIST = "author";
    private static final String SONG_IMG = "img";
    private static final String CURRENT_POSITION = "posCurrent";

    private TextView mSongName, mSongAuthor;
    private ImageButton mButtonReturnAllSong;
    private String mSongNameMedia;
    private String mSongAuthorMedia;
    private String mSongImgMedia;
    private int mPosCurrent;
    private ImageView mImgMedia;
    private ImageView mBackground;
    private TextView mPlayTime, mEndTime;
    private ImageButton mPlayMedia, mPreMedia, mNextMedia, mLikeMedia, mDisLikeMedia, mMenu;
    private SongMedia mSongMedia;
    private SeekBar mSeeBar;
    private ArrayList<SongItem> mSongList = new ArrayList<>();
    private View view;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        init();
        return view;
    }

    public void init() {
        mSongName = view.findViewById(R.id.TV_songName_media);
        mSongAuthor = view.findViewById(R.id.TV_songAuthormedia);
        mImgMedia = view.findViewById(R.id.ImgV_Media);
        mBackground = view.findViewById(R.id.img_background);
        mPlayMedia = view.findViewById(R.id.btn_play_media);
        mPreMedia = view.findViewById(R.id.btn_pre_media);
        mNextMedia = view.findViewById(R.id.btn_next_media);
        mButtonReturnAllSong = view.findViewById(R.id.btn_showList);
        mPlayTime = view.findViewById(R.id.play_time);
        mEndTime = view.findViewById(R.id.end_time);
        mSeeBar = view.findViewById(R.id.media_seekBar);
        mLikeMedia = view.findViewById(R.id.btn_like_media);
        mDisLikeMedia = view.findViewById(R.id.btn_dislike_media);
        mMenu = view.findViewById(R.id.btn_selectMedia);

        mPlayMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);
        mPreMedia.setOnClickListener(this);
        mButtonReturnAllSong.setOnClickListener(this);
        mLikeMedia.setOnClickListener(this);
        mDisLikeMedia.setOnClickListener(this);
        mMenu.setOnClickListener(this);
    }

    public static MediaPlaybackFragment newInstance(String songName, String songArtist, String songImg, int mPosCurrent) {
        MediaPlaybackFragment fragment = new MediaPlaybackFragment();
        Bundle args = new Bundle();
        args.putString(SONG_NAME, songName);
        args.putString(SONG_ARTIST, songArtist);
        args.putString(SONG_IMG, songImg);
        args.putInt(CURRENT_POSITION, mPosCurrent);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onClick(View v) {

    }
}