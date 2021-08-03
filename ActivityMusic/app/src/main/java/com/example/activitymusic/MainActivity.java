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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.activitymusic.adapter.SongItemAdapter;
import com.example.activitymusic.fragments.AllSongsFragment;
import com.example.activitymusic.model.SongItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 5;
    private RecyclerView mRcvSongList;                                   // khởi tạo biến
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongItemAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // init();

/*        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);*/

        if (checkPermission()) {
            init();
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
                init();
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

    public void init() {
        mSongItems = new ArrayList<>();

        FragmentManager manager = getSupportFragmentManager();
        AllSongsFragment allSongsFragment = new AllSongsFragment();                   // Show allSongsFragment
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.content, allSongsFragment);
        fragmentTransaction.commit();
    }
}