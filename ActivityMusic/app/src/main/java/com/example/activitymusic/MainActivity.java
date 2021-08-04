package com.example.activitymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.fragments.AllSongsFragment;
import com.example.activitymusic.fragments.MediaPlaybackFragment;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.service.MediaPlaybackService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 5;
    private RecyclerView mRcvSongList;                                   // khởi tạo biến
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongItemAdapter;
    protected boolean mIsShowVertical;
    public MediaPlaybackService mMusicService;

    public MediaPlaybackService getMusicService() {
        return mMusicService;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // init();

/*        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);*/

        if (checkPermission()) {
            getShowFragment();
        }

    }

    // Kiểm tra quyền truy cập bộ nhớ
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
            return false;
        }
        return true;
    }

    // yêu cầu quyền truy cập bộ nhớ
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getShowFragment();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:                            // Chọn optionmenu search
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getShowFragment() {
        int mOrientation = getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mIsShowVertical = true;
        }
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mIsShowVertical = false;
        }

        if(mIsShowVertical) {  //khi dọc
            FragmentManager manager = getSupportFragmentManager();
            AllSongsFragment allSongsFragment = new AllSongsFragment();                          // Show allSongsFragment
            allSongsFragment.setmIsShow(mIsShowVertical);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.content, allSongsFragment);
            fragmentTransaction.commit();
        }
        else { // khi ngang
            FragmentManager manager = getSupportFragmentManager();
            AllSongsFragment allSongsFragment = new AllSongsFragment();                   // Show allSongsFragment
            allSongsFragment.setmIsShow(mIsShowVertical);

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.content, allSongsFragment);               //get fragment AllSongsFragment vào activity main
            fragmentTransaction.commit();

            MediaPlaybackFragment mMediaPlaybackFragment= new MediaPlaybackFragment();
            mMediaPlaybackFragment.setmMusicService(mMusicService);
            mMediaPlaybackFragment.setmIsShow(mIsShowVertical);
            FragmentTransaction mPlayFragment = manager.beginTransaction();
            mPlayFragment.replace(R.id.fragment_media, mMediaPlaybackFragment);
            mPlayFragment.commit();
        }
    }
}