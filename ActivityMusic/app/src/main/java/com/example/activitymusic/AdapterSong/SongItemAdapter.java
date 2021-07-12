package com.example.activitymusic.AdapterSong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.SongItemViewHolder> {
    public static final String EXTRA_MESSAGE = "Hello";
    private final ArrayList<SongItem> mSongItems;
    private Context mContext;
    private IIClickItem iiClickItem;

    public SongItemAdapter(ArrayList<SongItem> mSongItems,Context mContext, IIClickItem iiClickItem) {
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
        SongItem songItem = mSongItems.get(position);
        if (songItem != null) {
            holder.mTVName.setText(songItem.getmName());
        }
    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTVName;
        TextView mTextViewAuthor;
        LinearLayout mLinerLayout;
        private IIClickItem miiClickItem;

        public SongItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTVName = itemView.findViewById(R.id.textV_Namesong);

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
