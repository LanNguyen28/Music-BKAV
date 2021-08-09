package com.example.activitymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.fragments.AllSongsFragment;
import com.example.activitymusic.fragments.MediaPlaybackFragment;
import com.example.activitymusic.model.SongItem;
import com.example.activitymusic.service.MediaPlaybackService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 5;
    private ArrayList<SongItem> mSongItems;
    protected boolean mIsShowVertical;
    public MediaPlaybackService musicService;
    private SongItemAdapter mSongAdapter;
    SharedPreferences mSharedPreferences;
    private SearchView mSearchView;
    private AllSongsFragment mAllSongsFragment;
    private MediaPlaybackFragment mMediaPlaybackFragment;

    public MediaPlaybackService getMusicService() {
        return musicService;
    }

    public void setmMusicService(MediaPlaybackService mMusicService) {
        this.musicService = mMusicService;
    }

    public ArrayList<SongItem> getmSongItems() {
        return mSongItems;
    }

    public SongItemAdapter getSongAdapter() {
        return mSongAdapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("DATA_PLAY_MEDIA", MODE_PRIVATE);


        if (checkPermission()) {
            getShowFragment();
        }
    }

//    public void getData() {
//        mSongItems = new ArrayList<>();
//        SongMedia.getSong(this, mSongItems);   //set List song cho activity
//
//    }

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
                musicService.setmListSong(mSongItems);
                getShowFragment();
            } else {
                finish();
            }
        }
    }

    // gọi service
    private void setService() {
        Intent intent = new Intent(this, MediaPlaybackService.class);
        intent.setAction("");
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
            musicService = binder.getMusicService();
            musicService.setmListSong(mSongItems);
            mAllSongsFragment.setmMusicService(musicService);
            int mOrientation = getResources().getConfiguration().orientation;
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d("LanNTp", "onServiceConnected: "+mMediaPlaybackFragment+":"+musicService);
                mMediaPlaybackFragment.setmMusicService(musicService);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    @Override
    protected void onStart() {
        setService();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setmMusicService(musicService);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicService != null) {
            unbindService(serviceConnection);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        mSearchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        mSearchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                mSongAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                mSongAdapter.getFilter().filter(query);
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        /*if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return;
        }*/
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:                            // Chọn optionmenu search
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void initView() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fullLayout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.app_name,
//                R.string.app_name);
//
//        if (drawer != null) {
//            drawer.addDrawerListener(toggle);
//
//        }
//        toggle.setDrawerIndicatorEnabled(true);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
//        }
//
//    }

    public void getShowFragment() {
        int mOrientation = getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mIsShowVertical = true;
        }
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mIsShowVertical = false;
        }

        if (mIsShowVertical) {  //khi dọc
            FragmentManager manager = getSupportFragmentManager();
            mAllSongsFragment = new AllSongsFragment();                          // Show allSongsFragment
            mAllSongsFragment.setmIsShow(mIsShowVertical);
            Log.d("LanNTp", "getShowFragment: "+musicService);
            mAllSongsFragment.setmMusicService(musicService);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.content, mAllSongsFragment);
            fragmentTransaction.commit();

        } else { // khi ngang
            FragmentManager manager = getSupportFragmentManager();
            mAllSongsFragment = new AllSongsFragment();                   // Show allSongsFragment
            mAllSongsFragment.setmIsShow(mIsShowVertical);
            mAllSongsFragment.setmMusicService(musicService);

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.content, mAllSongsFragment);               //get fragment AllSongsFragment vào activity main
            fragmentTransaction.commit();

            mMediaPlaybackFragment = new MediaPlaybackFragment();
            mMediaPlaybackFragment.setmMusicService(musicService);
            mMediaPlaybackFragment.setmIsShow(mIsShowVertical);
            FragmentTransaction mPlayFragment = manager.beginTransaction();
            mPlayFragment.replace(R.id.fragment_media, mMediaPlaybackFragment);
            mPlayFragment.commit();
        }
        //set navigation drawer
//        if (mIsShowVertical) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fullLayout);
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    this, drawer, R.string.app_name,
//                    R.string.app_name);
//
//            if (drawer != null) {
//                drawer.addDrawerListener(toggle);
//
//            }
//            toggle.setDrawerIndicatorEnabled(true);
//            toggle.syncState();
//
//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);        //show button navigationView
//            if (navigationView != null) {
//                navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
//            }
//
//        }
    }

    // navigation drawer
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fullLayout);
//        switch (item.getItemId()) {
//            case R.id.nav_listen:
//                if (mIsShowVertical) {
//                    drawer.closeDrawer(GravityCompat.START);
//                    getSupportActionBar().setTitle("Music");
//                    getSupportActionBar().show();
//                    AllSongsFragment allSongsFragment = new AllSongsFragment();
//                    allSongsFragment.setmIsShow(mIsShowVertical);
//                    FragmentManager manager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                    fragmentTransaction.replace(R.id.content, allSongsFragment);
//                    fragmentTransaction.commit();
//
//                    Toast.makeText(this, "listen now", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            case R.id.nav_recent:
//
//                Toast.makeText(this, "recent", Toast.LENGTH_SHORT).show();
//
//                return true;
//
//            case R.id.nav_setting:
//                drawer.closeDrawer(GravityCompat.START);
//                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
//                return true;
//
//            case R.id.nav_help_feedback:
//                drawer.closeDrawer(GravityCompat.START);
//                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return false;
//        }
//    }
}
