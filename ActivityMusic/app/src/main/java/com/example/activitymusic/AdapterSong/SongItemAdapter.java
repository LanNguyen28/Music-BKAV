package com.example.activitymusic.AdapterSong;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymusic.FragmentMusic.AllSongsFragment;
import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.SongItemViewHolder> {
    public static final String EXTRA_MESSAGE = "Hello";
    private final ArrayList<SongItem.Song> mSongItems;
    private Context mContext;
    private boolean mToCheck = false;

    private IIClickItem iiClickItem;

    public SongItemAdapter(ArrayList<SongItem.Song> mSongItems,Context mContext, IIClickItem iiClickItem) {
        this.mSongItems = mSongItems;
       this.mContext = mContext;
       this.iiClickItem = iiClickItem;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, true);
        SongItemViewHolder songItemViewHolder = new SongItemViewHolder(view);
        songItemViewHolder.setIiClickItem(iiClickItem);
        return songItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemAdapter.SongItemViewHolder holder, int position) {
        holder.mItem = mSongItems.get(position);
        holder.mIdView.setText(String.valueOf(position + 1));
        //holder.mTVName.setText(mSongItems.get(position));
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mToCheck) {
//                    int selectedSong = holder.getAdapterPosition();
//                    AllSongsFragment fragment =
//                            AllSongsFragment.newInstance(selectedSong);
//                    mContext.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.framelayout_full, fragment)
//                            .addToBackStack(null)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context,
//                            .class);
//                    intent.putExtra(SongItem.SONG_ID_KEY,
//                            holder.getAdapterPosition());
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        TextView mTVName;
        TextView mTVAuthor;
        TextView mIdView;
        LinearLayout mLinerLayout;
        SongItem.Song mItem;
        private IIClickItem miiClickItem;

        public SongItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTVName = itemView.findViewById(R.id.textV_Namesong);
            mIdView = itemView.findViewById(R.id.textV_IDsong);
            mView = itemView;

            mLinerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iiClickItem.ItemClick();
                }
            });
        }
        public void setIiClickItem(IIClickItem iiClickItem) {
            miiClickItem = iiClickItem;
        }
    }
}
