package com.example.activitymusic.fragments;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import android.os.IBinder;
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
import android.widget.Toast;

import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.interfaces.IIClickItems;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.R;
import com.example.activitymusic.service.MediaPlaybackService;
import com.example.activitymusic.service.SongMedia;

import java.util.ArrayList;
import java.util.Collections;
import static android.content.Context.BIND_AUTO_CREATE;

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
    private SongMedia mSongMedia;
    private int mPosCurrent;

    public SongMedia getMedia() {
        return mSongMedia;
    }

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

    @Override
    public void onStart() {
        setService();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMusicService != null) {
            getActivity().unbindService(serviceConnection);
        }
    }

    private void setService() {
        Intent intent = new Intent(getActivity(), MediaPlaybackService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
            mMusicService = binder.getMusicService();

           mMusicService.getMedia().setmListSong(mSongItems);
            getDataBottom();
            mSongAdapter.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

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
        mLlBottom.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRcvSong.setLayoutManager(manager);
        mSongAdapter = new SongItemAdapter(mSongItems, getActivity(), this);
        mRcvSong.setAdapter(mSongAdapter);
        getDataBottom();

        mSongAdapter.notifyDataSetChanged();

        if (mMusicService != null) {
            mLlBottom.setVisibility(View.VISIBLE);
            mSongName.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongName());
            mSongAuthor.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongAuthor());
        }

        if (!mMusicService.getMedia().isStatusPlay()) {
            mBtnPlay.setBackgroundResource(R.drawable.ic_action_subpause);
        }

    }

    public void getSong() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);


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

    public void getDataBottom() {
        if (mMusicService != null && mMusicService.getMedia().getmCurrentPlay() >= 0) {
            mLlBottom.setVisibility(View.VISIBLE);

            mSongName.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongName());
            mSongAuthor.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongAuthor());

            if (mMusicService.getMedia().isStatusPlay()) {
                mBtnPlay.setBackgroundResource(R.drawable.ic_action_subpause);
            } else {
                mBtnPlay.setBackgroundResource(R.drawable.ic_action_dispause);
            }

            for (int i = 0; i < mSongItems.size(); i++) {
                mSongItems.get(i).setmIsPlay(false);
            }
            mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).setmIsPlay(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play: {
                Toast.makeText(getActivity(), "pausemusic", Toast.LENGTH_SHORT).show();
                if (mMusicService.getMedia().isStatusPlay()) {
                    mMusicService.getMedia().pauseSong();
                    mBtnPlay.setBackgroundResource(R.drawable.ic_action_dispause);

                } else {
                    Toast.makeText(getActivity(), "playmusic", Toast.LENGTH_SHORT).show();
                    mMusicService.getMedia().resumeSong();
                    mBtnPlay.setBackgroundResource(R.drawable.ic_action_subpause);
                }
                break;
            }
            case R.id.bottom: {
                mPosCurrent = mMusicService.getMedia().getmCurrentPlay();
                SongItem mSongItem = mSongItems.get(mPosCurrent);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MediaPlaybackFragment mediaPlaybackFragment = MediaPlaybackFragment.newInstance(
                        mSongItem.getmSongName(), mSongItem.getmSongAuthor(), mSongItem.getmSongImg(), mPosCurrent);
                mediaPlaybackFragment.getActivity();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                fragmentTransaction.replace(R.id.content, mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Log.d("LanNTp", "onClick: ");
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void onItemClick(SongItem songItem, int pos) {
        for (int i = 0; i < mSongItems.size(); i++) {
            mSongItems.get(i).setmIsPlay(false);
        }
        mSongItems.get(pos).setmIsPlay(true);

        if (mMusicService != null) {
            mMusicService.getMedia().playSong(songItem.getmSongImg());
            mMusicService.getMedia().setmCurrentPlay(pos);
        }
        mBtnPlay.setBackgroundResource(R.drawable.ic_action_subpause);

        mLlBottom.setVisibility(View.VISIBLE);
        Log.d("LanNTp", "onItemClick: " + mLlBottom);
        mSongName.setText(songItem.getmSongName());
        mSongAuthor.setText(songItem.getmSongAuthor());
        // mPosCurrent = pos;

    }

    @Override
    public void onSongBtnClickListener(ImageButton btnImg, View v, SongItem songItem, int pos) {

    }
}