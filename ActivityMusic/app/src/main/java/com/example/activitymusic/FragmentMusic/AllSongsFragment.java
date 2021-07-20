package com.example.activitymusic.FragmentMusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

<<<<<<< HEAD
import com.example.activitymusic.AdapterSong.SongItemAdapter;
=======
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
public class AllSongsFragment extends Fragment implements View.OnClickListener {
    public static final String ID_CHANNEL = "21";
    private RecyclerView mRcvSong;
    private ArrayList<SongItem> mSongItems;
    private SongItemAdapter mSongAdapter;
    private RelativeLayout mLlBottom;
    private ImageView mSongArt;
    private int mCurrentPosition;
    private TextView mSongName, mSongAuthor;

    RelativeLayout relativeLayoutBottom ;

=======
    RelativeLayout relativeLayoutBottom;
    private SongItem.Song mSong;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85

    public AllSongsFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

=======
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(SongItem.SONG_ID_KEY)) {
            mSong = SongItem.SONG_ITEMS.get(getArguments()
                    .getInt(SongItem.SONG_ID_KEY));
        }
    }

    void init(ViewGroup view) {
        relativeLayoutBottom = view.findViewById(R.id.bottom_relativeLayout);
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {

<<<<<<< HEAD
        mSongItems = new ArrayList<>();

        mSongArt = view.findViewById(R.id.img_bottomImg);
        mRcvSong = view.findViewById(R.id.recyclerV_Song);
        mSongName = view.findViewById(R.id.textV_bottom_songName);
        mSongAuthor = view.findViewById(R.id.textV_bottom_songAuthor);
        mLlBottom = view.findViewById(R.id.bottom_relativeLayout);

        mLlBottom.setOnClickListener(this);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRcvSong.setLayoutManager(manager);
        mSongAdapter = new SongItemAdapter(mSongItems,getActivity());
        mRcvSong.setAdapter(mSongAdapter);
        mSongAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

=======
//        init(container);
//        relativeLayoutBottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                relativeLayoutBottom.setVisibility(View.GONE);
//            }
//        });
        View rootView = inflater.inflate(R.layout.item_song,
                container, false);
        if ( mSong !=null){
           // ((TextView) rootView.findViewById(R.id.textV_Namesong)).setText(mSong.);
        }
        return rootView;
    }

    public static AllSongsFragment newInstance (int selectedSong) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(SongItem.SONG_ID_KEY, selectedSong);
        fragment.setArguments(arguments);
        return fragment;
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
    }
}