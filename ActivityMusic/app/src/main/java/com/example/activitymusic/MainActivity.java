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

public class MainActivity extends AppCompatActivity  {

    public static final int PERMISSION_READ = 0;
    private RecyclerView mRcvSongList;
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongItemAdapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

/*        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);*/

//        if (checkPermission()) {
//           // setAudio();
//        }

    }

//    public boolean checkPermission() {
//        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
//            return false;
//        }
//        return true;
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case  PERMISSION_READ: {
//                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
//                    } else {
//                       // setAudio();
//                    }
//                }
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "hiii", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        mSongItems = new ArrayList<>();

        FragmentManager manager = getSupportFragmentManager();
        AllSongsFragment allSongsFragment = new AllSongsFragment();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.content, allSongsFragment);
        fragmentTransaction.commit();
    }
}