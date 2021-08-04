package com.example.activitymusic.fragments;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
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

import com.bumptech.glide.Glide;
import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.interfaces.IIClickItems;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.R;
import com.example.activitymusic.service.MediaPlaybackService;
import com.example.activitymusic.service.SongMedia;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.BIND_AUTO_CREATE;

public class AllSongsFragment extends Fragment implements View.OnClickListener, IIClickItems {
    public static final String ID_CHANNEL = "21";
    private RecyclerView mRcvSong;
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongAdapter;                  // khai báo biến
    private RelativeLayout mLlBottom;
    private ImageView mSongImg;
    private ImageButton mBtnPlay;
    private TextView mSongName, mSongAuthor;
    public MediaPlaybackService mMusicService;
    private int mPosCurrent;
    private View mViewBottom;
    protected boolean mIsShowVertical;

    public void setmIsShow(boolean mIsShowVertical) {
        this.mIsShowVertical = mIsShowVertical;
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

    // gọi service
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
        mLlBottom = view.findViewById(R.id.bottom_relativeLayout);             // ánh xạ
        mBtnPlay = view.findViewById(R.id.btn_play);
        mViewBottom = view.findViewById(R.id.view_bottom);
        mLlBottom.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());           // set hiển thị cho recyclerview
        manager.setOrientation(RecyclerView.VERTICAL);
        mRcvSong.setLayoutManager(manager);
        mSongAdapter = new SongItemAdapter(mSongItems, getActivity(), this);
        mRcvSong.setAdapter(mSongAdapter);
        getDataBottom();


        if (mMusicService != null) {

            mLlBottom.setVisibility(View.VISIBLE);
            mViewBottom.setVisibility(View.VISIBLE);
            mSongName.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongName());
            mSongAuthor.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongAuthor());        // show data vắn tắt bài hát
            if (!mMusicService.getMedia().isStatusPlay()) {
                mBtnPlay.setBackgroundResource(R.drawable.ic_black_pause);

                byte[] songArt = getAlbumImg(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongImg());
                Glide.with(view.getContext()).asBitmap()
                        .error(R.drawable.backgroundmusic)
                        .load(songArt)
                        .into(mSongImg);
            }
        }
    }

    // lấy bài hát trong thiết bị
    public void getSong() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=?";
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor songCursor = musicResolver.query(songUri, null, selection, new String[]{String.valueOf(1)}, null);

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

    // sắp xếp bài hát a-z
    public void songItemSort(ArrayList<SongItem> listItem) {
        for (int i = 0; i < listItem.size(); i++) {
            for (int j = i + 1; j < listItem.size(); j++) {
                if (listItem.get(i).getmSongName().compareTo(listItem.get(j).getmSongName()) > 0) {
                    Collections.swap(listItem, i, j);
                }
            }
        }

    }

    //truyền dữ liệu bài hát vào thông tin vắn tắt bài hát
    public void getDataBottom() {
        if (mMusicService != null && mMusicService.getMedia().getmCurrentPlay() >= 0) {
            if (mIsShowVertical) {
                mLlBottom.setVisibility(View.VISIBLE);
                mViewBottom.setVisibility(View.VISIBLE);
            } else {
                mLlBottom.setVisibility(View.GONE);
                mViewBottom.setVisibility(View.GONE);
            }

            mSongName.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongName());
            mSongAuthor.setText(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongAuthor());
            byte[] songImg = getAlbumImg(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongImg());
            Glide.with(getContext()).asBitmap()
                    .error(R.drawable.backgroundmusic)
                    .load(songImg)
                    .into(mSongImg);
            if (mMusicService.getMedia().isStatusPlay()) {
                mBtnPlay.setBackgroundResource(R.drawable.ic_black_pause);
            } else {
                mBtnPlay.setBackgroundResource(R.drawable.ic_black_play);
            }

            for (int i = 0; i < mSongItems.size(); i++) {
                mSongItems.get(i).setmIsPlay(false);
            }
            mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).setmIsPlay(true);
        }
    }

    // chuyển đường dẫn file ảnh bài hát về ảnh
    public static byte[] getAlbumImg(String uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] albumImg = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return albumImg;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play: {              // click button play/pause ở thông tin vắn tắt bài hát
                if (mMusicService.getMedia().isStatusPlay()) {
                    mMusicService.getMedia().pauseSong();
                    mBtnPlay.setBackgroundResource(R.drawable.ic_black_pause);

                } else {
                    mMusicService.getMedia().resumeSong();
                    mBtnPlay.setBackgroundResource(R.drawable.ic_black_play);
                }
                break;
            }
            case R.id.bottom_relativeLayout: {                  // click thông tin vắn tắt bài hát show đến giao diện mediaPlaybackFragment
                mPosCurrent = mMusicService.getMedia().getmCurrentPlay();
                Log.d("LanNTp", "onClick: "+mPosCurrent);
                SongItem mSongItem = mSongItems.get(mPosCurrent);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MediaPlaybackFragment mediaPlaybackFragment = MediaPlaybackFragment.newInstance(
                        mSongItem.getmSongName(), mSongItem.getmSongAuthor(), mSongItem.getmSongImg(), mPosCurrent);
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();                   // ẩn actionbar
                mediaPlaybackFragment.setmIsShow(mIsShowVertical);
                mediaPlaybackFragment.setmMusicService(mMusicService);
                mediaPlaybackFragment.setmSongItems(mSongItems);
                fragmentTransaction.replace(R.id.content, mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
            default: {
            }
        }
    }


    // click item bài hát
    @Override
    public void onItemClick(SongItem songItem, int pos) {
        if (mIsShowVertical) { // khi doc
            mLlBottom.setVisibility(View.VISIBLE);
            mViewBottom.setVisibility(View.VISIBLE);

            for (int i = 0; i < mSongItems.size(); i++) {
                mSongItems.get(i).setmIsPlay(false);
            }
            mSongItems.get(pos).setmIsPlay(true);

            if (mMusicService != null) {
                mMusicService.getMedia().playSong(songItem.getmSongImg());
                mMusicService.getMedia().setmCurrentPlay(pos);
            }
            mBtnPlay.setBackgroundResource(R.drawable.ic_black_pause);

            mSongName.setText(songItem.getmSongName());
            mSongAuthor.setText(songItem.getmSongAuthor());
            byte[] songImg = getAlbumImg(mSongItems.get(pos).getmSongImg());
            Glide.with(getContext()).asBitmap()
                    .error(R.drawable.backgroundmusic)
                    .load(songImg)
                    .into(mSongImg);

        } else { //khi ngang

            if (mMusicService != null) {
                mMusicService.getMedia().setmCurrentPlay(pos);
                mMusicService.getMedia().playSong(songItem.getmSongImg());       //play nhac
            }
            mLlBottom.setVisibility(View.GONE);
            mViewBottom.setVisibility(View.GONE);
            mSongName.setText(songItem.getmSongName());
            mSongAuthor.setText(songItem.getmSongAuthor());
            byte[] songImg = getAlbumImg(mSongItems.get(pos).getmSongImg());
            Glide.with(getContext()).asBitmap()
                    .error(R.drawable.backgroundmusic)
                    .load(songImg)
                    .into(mSongImg);
        }
        mPosCurrent = pos;

    }

    // click dấu ... ở item bài hát
    @Override
    public void onSongBtnClickListener(ImageButton btnImg, View v, SongItem songItem, int pos) {

    }
}