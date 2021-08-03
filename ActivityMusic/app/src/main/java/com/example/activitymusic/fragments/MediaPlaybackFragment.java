package com.example.activitymusic.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.activitymusic.R;
import com.example.activitymusic.interfaces.UIMediaUpdate;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.service.MediaPlaybackService;
import com.example.activitymusic.service.SongMedia;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener, UIMediaUpdate {

    private static final String SONG_NAME = "name";
    private static final String SONG_ARTIST = "author";
    private static final String SONG_IMG = "img";
    private static final String CURRENT_POSITION = "posCurrent";

    private TextView mSongName, mSongAuthor;
    private ImageButton mButtonShowAllSong;
    private String mSongNameMedia;                                                      //khai báo biến
    private String mSongAuthorMedia;
    private String mSongImgMedia;
    private int mPosCurrent;
    private ImageView mImgMedia;
    private ImageView mBackground;
    private TextView mPlayTime, mEndTime;
    private ImageButton mPlayMedia, mPreMedia, mNextMedia, mLikeMedia, mDisLikeMedia, mMenu;
    private ImageButton mRepeat, mShuffle;
    private SeekBar mSeeBar;
    private ArrayList<SongItem> mSongItems = new ArrayList<>();
    private View view;
    private MediaPlaybackService mMusicService;
    private UpdateSeekBarThread mUpdateSeekBarThread;
    private boolean mIsCheckRepeat = false;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }

    // khởi tạo MediaPlaybackFragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSongNameMedia = getArguments().getString(SONG_NAME);
            mSongAuthorMedia = getArguments().getString(SONG_ARTIST);
            mSongImgMedia = getArguments().getString(SONG_IMG);
            mPosCurrent = getArguments().getInt(CURRENT_POSITION);
        }

        mUpdateSeekBarThread = new UpdateSeekBarThread();
        mUpdateSeekBarThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        init();
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

    @Override
    public void onDestroy() {
        mUpdateSeekBarThread.exit();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onDestroy();
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
            mSongItems = mMusicService.getMedia().getmSongItems();
            mMusicService.getMedia().setMediaUpdateUI(MediaPlaybackFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

    public void init() {
        mSongName = view.findViewById(R.id.TV_songName_media);
        mSongAuthor = view.findViewById(R.id.TV_songAuthormedia);
        mImgMedia = view.findViewById(R.id.ImgV_Media);
        mBackground = view.findViewById(R.id.img_background);
        mPlayMedia = view.findViewById(R.id.btn_play_media);
        mPreMedia = view.findViewById(R.id.btn_pre_media);                         //ánh xạ
        mNextMedia = view.findViewById(R.id.btn_next_media);
        mButtonShowAllSong = view.findViewById(R.id.btn_showList);
        mPlayTime = view.findViewById(R.id.play_time);
        mEndTime = view.findViewById(R.id.end_time);
        mSeeBar = view.findViewById(R.id.media_seekBar);
        mLikeMedia = view.findViewById(R.id.btn_like_media);
        mDisLikeMedia = view.findViewById(R.id.btn_dislike_media);
        mMenu = view.findViewById(R.id.btn_selectMedia);
        mRepeat = view.findViewById(R.id.btn_repeat);
        mShuffle = view.findViewById(R.id.btn_shuffle);

        mPlayMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);
        mPreMedia.setOnClickListener(this);
        mButtonShowAllSong.setOnClickListener(this);
        mLikeMedia.setOnClickListener(this);
        mDisLikeMedia.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mShuffle.setOnClickListener(this);
        mRepeat.setOnClickListener(this);

        mSeeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mMusicService != null && b) {
                    mMusicService.getMedia().seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        update();

    }

    //request dữ liệu khi khởi tạo
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

    //cập nhật dữ liệu bài hát
    public void update() {
        mSongName.setText(mSongNameMedia);
        mSongAuthor.setText(mSongAuthorMedia);
        byte[] AlbumImg = getAlbumImg(mSongImgMedia);
        Glide.with(view.getContext()).asBitmap()
                .load(AlbumImg)
                .error(R.drawable.backgroundmusic)
                .into(mImgMedia);
        Glide.with(view.getContext()).asBitmap()
                .load(AlbumImg)
                .error(R.drawable.backgroundmusic)
                .into(mBackground);
        mPlayMedia.setBackgroundResource(R.drawable.ic_white_play);
    }

    // Chuyển đường dẫn ảnh thành ảnh bài
    public static byte[] getAlbumImg(String uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] albumArt = mediaMetadataRetriever.getEmbeddedPicture();
        mediaMetadataRetriever.release();
        return albumArt;
    }


    //lấy dữ liệu tóm tắt bài hát lên trên đầu khi đang chơi nhạc
    private void setDataTop() {
        if (mMusicService != null) {
            int current = mMusicService.getMedia().getmCurrentPlay();
            mSongName.setText(mSongItems.get(current).getmSongName());
            mSongAuthor.setText(mSongItems.get(current).getmSongAuthor());
            mEndTime.setText(showTime(mSongItems.get(current).getmSongTime()));
            byte[] AlbumImg = getAlbumImg(mSongItems.get(current).getmSongImg());
            Glide.with(view.getContext()).asBitmap()
                    .load(AlbumImg)
                    .error(R.drawable.backgroundmusic)
                    .into(mImgMedia);
            Glide.with(view.getContext()).asBitmap()
                    .load(AlbumImg)
                    .error(R.drawable.backgroundmusic)
                    .into(mBackground);

            if (mMusicService.getMedia().isStatusPlay()) {
                mPlayMedia.setBackgroundResource(R.drawable.ic_white_play);
            } else mPlayMedia.setBackgroundResource(R.drawable.ic_white_pause);
        }


    }

    //  gọi seekbar
    public class UpdateSeekBarThread extends Thread {
        private Handler handler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        }

        public void updateSeekBar() {
            if (mMusicService != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mMusicService.getMedia().getmCurrentPlay() >= 0) {
                            while (mMusicService.getMedia().getPlayer() != null) {
                                try {
                                    long current = -1;
                                    try {

                                    } catch (IllegalStateException e) {

                                    }
                                    if (getActivity() != null && mSongItems.size() > 0) {
                                        final long finalCurrent = current;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSeeBar.setMax((int) (mMusicService.getMedia().getDuration()));
                                                mSeeBar.setProgress((int) (finalCurrent));
                                                mPlayTime.setText(showTime(String.valueOf(finalCurrent)));                                 //set time start/ end cho bài hát
                                                mEndTime.setText(showTime(mSongItems.get(mMusicService.getMedia().getmCurrentPlay()).getmSongTime()));
                                            }
                                        });
                                    }
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                });
            }
        }

        public void exit() {
            handler.getLooper().quit();
        }
    }

    // xác định thời gian bài hát
    private String showTime(String time) {
        long duration = Long.parseLong(time);
        int minutes = (int) (duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) % 60);
        if (seconds < 10) {
            String seconds2 = "0" + seconds;
            return minutes + ":" + seconds2;
        }
        return minutes + ":" + seconds;
    }

    private void updateUI() {
        if (mMusicService != null) {
            mUpdateSeekBarThread.updateSeekBar();
        }
    }

    @Override
    public void mediaUI(int pos) { //override,update UI mediaFragment khi next bài
        setDataTop();
        updateUI();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_showList:     // Click show allsongfragment
                getFragmentManager().popBackStack();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                break;

            case R.id.btn_selectMedia:      //  click select menu
                Toast.makeText(mMusicService, "Menu chưa làm", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_like_media:         // click like bài hát
                mLikeMedia.setBackgroundResource(R.drawable.ic_black_like);
                mDisLikeMedia.setBackgroundResource(R.drawable.ic_white_dislike);
                break;

            case R.id.btn_dislike_media:          // click dislike bài hát
                mDisLikeMedia.setBackgroundResource(R.drawable.ic_black_dislike);
                mLikeMedia.setBackgroundResource(R.drawable.ic_white_like);
                break;

            case R.id.btn_next_media: {            // click next bài hát
                mMusicService.getMedia().nextSong(mPosCurrent);  // Next sang bài sau đó
                mPosCurrent = mMusicService.getMedia().getmCurrentPlay();
                SongItem mSong_next = mSongItems.get(mPosCurrent);
                mSongNameMedia = mSong_next.getmSongName();
                mSongAuthorMedia = mSong_next.getmSongAuthor();
                mSongImgMedia = mSong_next.getmSongImg();
                mMusicService.getMedia().playSong(mSong_next.getmSongImg());
                setDataTop();
                break;
            }

            case R.id.btn_pre_media: {   // click quay lại bài hát
                mMusicService.getMedia().previousSong(mPosCurrent);     // quay về bài trước đó
                mPosCurrent = mMusicService.getMedia().getmCurrentPlay();
                SongItem mSong_pre = mSongItems.get(mPosCurrent);
                mSongNameMedia = mSong_pre.getmSongName();
                mSongAuthorMedia = mSong_pre.getmSongAuthor();
                mSongImgMedia = mSong_pre.getmSongImg();
                mMusicService.getMedia().playSong(mSong_pre.getmSongImg());
                setDataTop();
                break;
            }
            case R.id.btn_repeat:
                if (!mIsCheckRepeat) {
                    mMusicService.getMedia().getPlayer().setLooping(true);
                    mIsCheckRepeat = true;
                    mRepeat.setBackgroundResource(R.drawable.ic_repeat_one);

                } else {
                    mMusicService.getMedia().getPlayer().setLooping(false);
                    mIsCheckRepeat = false;
                    mRepeat.setBackgroundResource(R.drawable.ic_repeat_white);
                }

                break;

            case R.id.btn_shuffle:

                break;

            case R.id.btn_play_media: { // click play/pause bài hát
                if (mMusicService.getMedia().isStatusPlay()) {
                    mMusicService.getMedia().pauseSong();
                    mPlayMedia.setBackgroundResource(R.drawable.ic_white_play);

                } else {
                    mMusicService.getMedia().resumeSong();
                    mPlayMedia.setBackgroundResource(R.drawable.ic_white_pause);
                }
                break;
            }
        }
    }
}