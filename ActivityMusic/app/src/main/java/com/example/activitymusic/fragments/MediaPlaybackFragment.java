package com.example.activitymusic.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import com.example.activitymusic.MainActivity;
import com.example.activitymusic.R;
import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.interfaces.UIMediaUpdate;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.service.MediaPlaybackService;
import com.example.activitymusic.service.SongMedia;

import java.text.SimpleDateFormat;
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
    private MediaPlaybackService musicService;
    private boolean mIsCheckRepeat = false;
    protected boolean mIsShowVertical;
    SharedPreferences sharedPreferencesCurrent;

    public ArrayList<SongItem> getmSongItems() {
        return mSongItems;
    }

    private SongItemAdapter getSongAdapter() {
        return getActivityMusic().getSongAdapter();
    }

    public void setmSongItems(ArrayList<SongItem> mSongItems) {
        this.mSongItems = mSongItems;
    }

    public void setmIsShow(boolean mIsShowVertical) {
        this.mIsShowVertical = mIsShowVertical;
    }

    public void setmMusicService(MediaPlaybackService mMusicService) {
        this.musicService = mMusicService;
    }

    //get activity
    private MainActivity getActivityMusic() {
        if (getActivity() instanceof Activity) {
            return (MainActivity) getActivity();
        }
        return null;
    }

//    //private MediaPlaybackService getMusicService() {
//        //return getActivityMusic().getMusicService();
//    }

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }

    public void setData() {
        //musicService = getMusicService();
        mSongItems = getmSongItems();
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
        updateSeekBar();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        init();
        if(musicService!=null)
        setDataTop();
        //setData();
        updateSeekBar();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onDestroy();
    }


    public void init() {
        mSongName = view.findViewById(R.id.TV_songName_media);
        mSongAuthor = view.findViewById(R.id.TV_song_author_media);
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
        mButtonShowAllSong.setOnClickListener(this);          //set sự kiện button,imageview....
        mLikeMedia.setOnClickListener(this);
        mDisLikeMedia.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mShuffle.setOnClickListener(this);
        mRepeat.setOnClickListener(this);

        if (mIsShowVertical) { // khi dọc
            mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
            if (musicService != null && mSongItems.size() >= 0) {
                update();
            }
        } else {
            //set khi xoay màn hình
            mBackground.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }
        //set seekbar
        if (musicService != null) {
            mSeeBar.setMax(musicService.getDuration());
        }

        mSeeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               // gọi seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (musicService != null && b) {
                    musicService.seekTo(i);
                }
                mSeeBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        if (musicService.isStatusPlay()) {
            mPlayMedia.setImageResource(R.drawable.ic_fab_pause);
        }
        mPlayMedia.setImageResource(R.drawable.ic_fab_play);

        mPlayTime.setText(showTime(String.valueOf(musicService.getCurrentPositionPlay())));
        mEndTime.setText(showTime(mSongItems.get(musicService.getmCurrentPlay()).getmSongTime()));

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
        if (musicService != null & mSongItems.size() > 0 && musicService.isStatusPlay()) {
            int current = musicService.getmCurrentPlay();
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

            if (musicService.isStatusPlay()) {
                mPlayMedia.setImageResource(R.drawable.ic_fab_pause);
            } else mPlayMedia.setImageResource(R.drawable.ic_fab_play);
        }
        update();

    }


    public void updateSeekBar() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicService != null)
                    if (musicService.getmCurrentPlay() >= 0) {
                        //while (musicService.getMedia().getPlayer() != null) {
                        long mCurrent = -1;
                        try {
                            mCurrent = musicService.getCurrentPositionPlay();

                        } catch (IllegalStateException e) {

                        }
                        final long finalCurrent = mCurrent;
                        SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
                        mSeeBar.setMax(musicService.getDuration());
                        mSeeBar.setProgress(musicService.getCurrentPositionPlay());
                        //Log.d("LanNTp", "run: " + musicService.getCurrentPositionPlay());
                        mPlayTime.setText(formatTime.format(finalCurrent));                                 //set time total/ end cho bài hát
                        mEndTime.setText(formatTime.format(musicService.getDuration()));
                        handler.postDelayed(this, 500);
                        //}
                    }
            }
        }, 500);
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


    @Override
    public void mediaUI(int pos) { //override,update UI mediaFragment khi next bài
        setDataTop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_showList: {    // Click show allsongfragment
                if (mIsShowVertical) {
                    getFragmentManager().popBackStack();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                }
                break;
            }

            case R.id.btn_selectMedia: {     //  click select menu
                Toast.makeText(musicService, "Menu chưa làm", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_like_media: {         // click like bài hát
                mLikeMedia.setBackgroundResource(R.drawable.ic_black_like);
                mDisLikeMedia.setBackgroundResource(R.drawable.ic_white_dislike);
                break;
            }
            case R.id.btn_dislike_media: {          // click dislike bài hát
                mDisLikeMedia.setBackgroundResource(R.drawable.ic_black_dislike);
                mLikeMedia.setBackgroundResource(R.drawable.ic_white_like);
                break;
            }
            case R.id.btn_next_media: {// click next bài hát
                if (musicService.getmCurrentPlay() >= 0) {
                    int pos = musicService.getmCurrentPlay();
                    musicService.nextSong(pos);  // Next sang bài sau đó
                    mPosCurrent = musicService.getmCurrentPlay();
                    SongItem mSong_next = mSongItems.get(pos);
                    mSongNameMedia = mSong_next.getmSongName();
                    mSongAuthorMedia = mSong_next.getmSongAuthor();
                    mSongImgMedia = mSong_next.getmSongImg();
                    setDataTop();
                    break;
                }
            }

            case R.id.btn_pre_media: {   // click quay lại bài hát
                if (musicService.getmCurrentPlay() >= 0) {
                    int pos = musicService.getmCurrentPlay();
                    musicService.previousSong(pos);     // quay về bài trước đó
                    mPosCurrent = musicService.getmCurrentPlay();
                    SongItem mSong_pre = mSongItems.get(mPosCurrent);
                    mSongNameMedia = mSong_pre.getmSongName();
                    mSongAuthorMedia = mSong_pre.getmSongAuthor();
                    mSongImgMedia = mSong_pre.getmSongImg();
                    setDataTop();
                    break;
                }
            }
            case R.id.btn_repeat:
                if (!mIsCheckRepeat) {
                    musicService.getPlayer().setLooping(true);
                    mIsCheckRepeat = true;
                    mRepeat.setBackgroundResource(R.drawable.ic_repeat_one);

                } else {
                    musicService.getPlayer().setLooping(false);
                    mIsCheckRepeat = false;
                    mRepeat.setBackgroundResource(R.drawable.ic_repeat_white);
                }

                break;

            case R.id.btn_shuffle:

                break;

            case R.id.btn_play_media: { // click play/pause bài hát
                if (musicService.getmCurrentPlay() >= 0) {
                    if (musicService.isStatusPlay()) {
                        musicService.pauseSong();
                        mPlayMedia.setImageResource(R.drawable.ic_fab_play);

                    } else {
                        musicService.resumeSong();
                        mPlayMedia.setImageResource(R.drawable.ic_fab_pause);
                    }
                }
                break;
            }
        }
    }
}