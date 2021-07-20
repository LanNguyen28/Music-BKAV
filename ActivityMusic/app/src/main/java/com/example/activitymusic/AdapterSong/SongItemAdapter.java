package com.example.activitymusic.AdapterSong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymusic.IIClickItems;
import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.ViewHolder>{
    public static final String EXTRA_MESSAGE = "Hello";
    private ArrayList<SongItem> mSongItems;
    private Context mContext;
    private IIClickItems iiClickItems;

    public SongItemAdapter(ArrayList<SongItem> mSongItems, Context mContext) {
        this.mSongItems = mSongItems;
        this.mContext = mContext;
    }

    public void setSongAdapter(IIClickItems iiClickItems) {
        this.iiClickItems = iiClickItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (mSongItems.get(position).ismIsPlay()) return 1;
        return 0;
    }

    @NonNull
    @Override
    public SongItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemAdapter.ViewHolder holder, int position) {
        holder.binData(mSongItems.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSongName;
        private TextView mSongTime;
        private  TextView mSongID;
        private ImageView mImageID;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSongName = itemView.findViewById(R.id.textV_Namesong);
            mSongTime = itemView.findViewById(R.id.textV_Timesong);
            mSongID = itemView.findViewById(R.id.textV_IDsong);

        }

        public void binData(final SongItem songItem, final int pos) {
            mSongID.setText(String.valueOf(pos + 1));
            mSongName.setText(songItem.getmSongName() + "");
        }
    }


}
