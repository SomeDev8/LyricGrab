package com.rorysoft.lyricgrab.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorysoft.lyricgrab.Lyric;
import com.rorysoft.lyricgrab.LyricsDetail;
import com.rorysoft.lyricgrab.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class LyricAdapter extends RecyclerView.Adapter<LyricAdapter.LyricViewHolder> {

    private Context mContext;
    private ArrayList<Lyric> mLyricsList;
    LyricViewHolder lyricViewHolder;

    public LyricAdapter(Context mContext, ArrayList<Lyric> lyricsList) {
        this.mLyricsList = lyricsList;
        this.mContext = mContext;
    }


    @Override
    public LyricAdapter.LyricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new LyricViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LyricAdapter.LyricViewHolder holder, int position) {
        holder.songName.setText(mLyricsList.get(position).getSongTitle());
        holder.artistName.setText(mLyricsList.get(position).getArtistName());
        holder.albumName.setText(mLyricsList.get(position).getAlbumName());
        Picasso.with(mContext).load(mLyricsList.get(position).getImagePath()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mLyricsList.size();
    }

    public class LyricViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        ImageView imageView;
        TextView songName;
        TextView artistName;
        TextView albumName;


        public LyricViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.album_art);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            albumName = (TextView) itemView.findViewById(R.id.album_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemIndex = getAdapterPosition();
            Lyric lyric = mLyricsList.get(itemIndex);
           // String lyricsPath = lyric.getLyricsPath();
            Intent intent = new Intent(mContext, LyricsDetail.class);
            intent.putExtra("LyricItem", lyric);
            mContext.startActivity(intent);
        }
    }
}
