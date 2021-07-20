package com.example.activitymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.activitymusic.AdapterSong.SongItemAdapter;
import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IIClickItems {

    private RecyclerView mRcvSongList;
    private ArrayList<SongItem.Song> mSongItems ;
    private SongItemAdapter mSongItemAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
<<<<<<< HEAD
    }


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
=======
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
    }


    public void init(){
        mSongItems = new ArrayList<>();
        mRcvSongList = findViewById(R.id.recyclerV_Song);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRcvSongList.setLayoutManager(linearLayoutManager);
        mSongItemAdapter = new SongItemAdapter(mSongItems, this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        mRcvSongList.addItemDecoration(itemDecoration);
        mRcvSongList.setAdapter(mSongItemAdapter);
    }

    @Override
    public void onItemClick(SongItem song, int pos) {

    }

    @Override
    public void onSongBtnClickListener(ImageButton btn, View v, SongItem song, int pos) {

    }
}