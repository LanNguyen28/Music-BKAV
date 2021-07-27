package com.example.activitymusic.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.interfaces.IIClickItems;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.R;
import com.example.activitymusic.service.MediaPlaybackService;
import com.example.activitymusic.service.SongMedia;

import java.util.ArrayList;
import java.util.Collections;

public class AllSongsFragment extends Fragment implements View.OnClickListener, IIClickItems {
    public static final String ID_CHANNEL = "21";
    private RecyclerView mRcvSong;
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongAdapter;
    private RelativeLayout mLlBottom;
    private ImageView mSongImg;
    private Button mBtnPlay;
    private TextView mSongName, mSongAuthor;
    public MediaPlaybackService mMusicService;
    public MediaPlayer mediaPlayer;

    public AllSongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {
        mSongItems = new ArrayList<>();
        // mediaPlayer = new MediaPlayer();

        getSong();
        mSongImg = view.findViewById(R.id.img_bottomImg);
        mRcvSong = view.findViewById(R.id.recyclerV_Song);
        mSongName = view.findViewById(R.id.textV_bottom_songName);
        mSongAuthor = view.findViewById(R.id.textV_bottom_songAuthor);
        mLlBottom = view.findViewById(R.id.bottom_relativeLayout);
        mBtnPlay = view.findViewById(R.id.btn_play);

//        mLlBottom.setOnClickListener(this);
//        mBtnPlay.setOnClickListener(this);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRcvSong.setLayoutManager(manager);
        mSongAdapter = new SongItemAdapter(mSongItems, getActivity(),this);
        mRcvSong.setAdapter(mSongAdapter);
        mSongAdapter.notifyDataSetChanged();
        mLlBottom.setVisibility(View.VISIBLE);

    }

    public void getSong() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);
        //Log.d("Lan", "getSong: "+songCursor.getCount());


        if (songCursor != null && songCursor.moveToFirst()) {
            int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songTime = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songAuthor = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songImg = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                long startId = songCursor.getLong(songID);
                String startName = songCursor.getString(songName);
                String startTime = songCursor.getString(songTime);
                String startAuthor = songCursor.getString(songAuthor);
                String startImg = songCursor.getString(songImg);
                mSongItems.add(new SongItem(startId, startName, startTime, startAuthor, startImg, false));
            } while (songCursor.moveToNext());
            songItemSort(mSongItems);
        }
    }

    public void songItemSort(ArrayList<SongItem> listItem) {
        for (int i = 0; i < listItem.size(); i++) {
            for (int j = i + 1; j < listItem.size(); j++) {
                if (listItem.get(i).getmSongName().compareTo(listItem.get(j).getmSongName()) > 0) {
                    Collections.swap(listItem, i, j);
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play: {
                mBtnPlay.setBackgroundResource(R.drawable.ic_action_dispause);
                break;
            }
            case R.id.bottom: {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void onItemClick(SongItem songItem) {
//        for (int i = 0; i < mSongItems.size(); i++) {
//            mSongItems.get(i).setmIsPlay(false);
//        }
//        mSongItems.get(pos).setmIsPlay(true);
        mBtnPlay.setBackgroundResource(R.drawable.ic_action_subpause);
        mLlBottom.setVisibility(View.VISIBLE);
        Log.d("LanNTp", "onItemClick: "+mLlBottom);
        mSongName.setText(songItem.getmSongName());
        mSongAuthor.setText(songItem.getmSongAuthor());
    }

    @Override
    public void onSongBtnClickListener(ImageButton btnImg, View v, SongItem songItem, int pos) {

    }
}