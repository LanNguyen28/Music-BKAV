package com.example.activitymusic.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitymusic.R;
import com.example.activitymusic.interfaces.IIClickItems;
import com.example.activitymusic.model.SongItem;

import java.util.ArrayList;

//import eu.gsottbauer.equalizerview.EqualizerView;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.ViewHolder> {
    private ArrayList<SongItem> mSongItems;
    private Context mContext;                       // khai báo biến
    private IIClickItems mIiClickItems;

    public SongItemAdapter(ArrayList<SongItem> mSongItems, Context mContext, IIClickItems mIiClickItems) {
        this.mSongItems = mSongItems;
        this.mContext = mContext;                                //khởi tạo biến
        this.mIiClickItems = mIiClickItems;

    }


    @Override
    public int getItemViewType(int position) {
        if (mSongItems.get(position).ismIsPlay()) return 1;
        return 0;
    }

    @NonNull
    @Override
    public SongItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song, parent, false);
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

    // xác định thời gian bài hát
    private String getDuration(String time) {
        long duration = Long.parseLong(time);
        int minutes = (int) (duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) % 60);
        if (seconds < 10) {
            String seconds2 = "0" + seconds;
            return minutes + ":" + seconds2;
        }
        return minutes + ":" + seconds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSongName;
        private TextView mSongTime;
        private TextView mSongID;                    // khai báo biến
        private ImageView mImageID;
        private ImageButton mSelectSong;
        private RelativeLayout mLayoutClick;
       // private EqualizerView mEqualizer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSongName = itemView.findViewById(R.id.textV_Namesong);
            mSongTime = itemView.findViewById(R.id.textV_Timesong);
            mSongID = itemView.findViewById(R.id.textV_IDsong);                     //ánh xạ
            mImageID = itemView.findViewById(R.id.ImgV_IDSong);
            //mEqualizer = itemView.findViewById(R.id.EquaV_IDSong);
            mSelectSong = itemView.findViewById(R.id.Img_SongSelect);
            mLayoutClick = itemView.findViewById(R.id.LayoutClick);
        }

        public void binData(final SongItem songItem, final int pos) {
            mSongID.setText(String.valueOf(pos + 1));
            mSongName.setText(songItem.getmSongName() + "");
            mSongTime.setText(getDuration(songItem.getmSongTime()));

            if(songItem.ismIsPlay())
            {
                mSongID.setVisibility(View.INVISIBLE);
                mImageID.setVisibility(View.VISIBLE);                                 // set item về chữ đậm, có đổi stt thành icon chơi nhạc
                //mEqualizer.animateBars();
                mSongName.setTypeface(null, Typeface.BOLD);
            }
            else
            {
               // mEqualizer.stopBars();
                mSongID.setVisibility(View.VISIBLE);
                mImageID.setVisibility(View.INVISIBLE);                           // set item về stt,chữ thường,
                mSongName.setTypeface(null, Typeface.NORMAL);
            }

            mLayoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIiClickItems.onItemClick(songItem,pos);                   //click item bài hát show ra thông tin vắn tắt bài hát đang phát
                }
            });
            mSelectSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIiClickItems != null) {
                        mIiClickItems.onSongBtnClickListener(mSelectSong, view, songItem , pos);    // click dấu ... của bài hát
                    }
                }
            });
        }
    }
}

