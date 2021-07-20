package com.example.activitymusic.AdapterSong;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import com.example.activitymusic.IIClickItems;
=======
import com.example.activitymusic.FragmentMusic.AllSongsFragment;
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
import com.example.activitymusic.Interface.IIClickItem;
import com.example.activitymusic.Model.SongItem;
import com.example.activitymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.ViewHolder>{
    public static final String EXTRA_MESSAGE = "Hello";
<<<<<<< HEAD
    private ArrayList<SongItem> mSongItems;
    private Context mContext;
    private IIClickItems iiClickItems;

    public SongItemAdapter(ArrayList<SongItem> mSongItems, Context mContext) {
=======
    private final ArrayList<SongItem.Song> mSongItems;
    private Context mContext;
    private boolean mToCheck = false;

    private IIClickItem iiClickItem;

    public SongItemAdapter(ArrayList<SongItem.Song> mSongItems,Context mContext, IIClickItem iiClickItem) {
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
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
<<<<<<< HEAD
    public void onBindViewHolder(@NonNull SongItemAdapter.ViewHolder holder, int position) {
        holder.binData(mSongItems.get(position), position);

=======
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
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
    }

    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

<<<<<<< HEAD
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSongName;
        private TextView mSongTime;
        private  TextView mSongID;
        private ImageView mImageID;

=======
    class SongItemViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        TextView mTVName;
        TextView mTVAuthor;
        TextView mIdView;
        LinearLayout mLinerLayout;
        SongItem.Song mItem;
        private IIClickItem miiClickItem;
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
<<<<<<< HEAD
            mSongName = itemView.findViewById(R.id.textV_Namesong);
            mSongTime = itemView.findViewById(R.id.textV_Timesong);
            mSongID = itemView.findViewById(R.id.textV_IDsong);

=======
            mTVName = itemView.findViewById(R.id.textV_Namesong);
            mIdView = itemView.findViewById(R.id.textV_IDsong);
            mView = itemView;

            mLinerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iiClickItem.ItemClick();
                }
            });
>>>>>>> 0438f0a05ddeb8aa35d886070a850bc0590b0e85
        }

        public void binData(final SongItem songItem, final int pos) {
            mSongID.setText(String.valueOf(pos + 1));
            mSongName.setText(songItem.getmSongName() + "");
        }
    }


}
