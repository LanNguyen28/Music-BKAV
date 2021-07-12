package com.example.activitymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.activitymusic.AdapterSong.SongItemAdapter;
import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IIClickItem {

    private RecyclerView mRcvSongList;
    private ArrayList<SongItem> mSongItems ;
    private SongItemAdapter mSongItemAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void createSongList(){
        mSongItems.add(new SongItem("Em là ai","hii","hiii"));
        mSongItems.add(new SongItem("Từng quên đi","hieeii","hee"));
    }


    public void init(){
        mSongItems = new ArrayList<>();
       // createSongList();
        mRcvSongList = findViewById(R.id.recyclerV_Song);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRcvSongList.setLayoutManager(linearLayoutManager);
        mSongItemAdapter = new SongItemAdapter(mSongItems, this, this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        mRcvSongList.addItemDecoration(itemDecoration);
        mRcvSongList.setAdapter(mSongItemAdapter);
    }

    @Override
    public void ItemClick() {

    }
}