package com.example.activitymusic.FragmentMusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

public class AllSongsFragment extends Fragment {

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

    public AllSongsFragment() {
        // Required empty public constructor
    }

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
    }
}