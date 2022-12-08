package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.checkMiniPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.playingFromWhere;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;
import static com.fl4me.android.flamemusicplayer.Song.formatSongDuration;
import static com.fl4me.android.flamemusicplayer.Song.getAlbumImage;
import static com.fl4me.android.flamemusicplayer.Song.isPlaying;
import static com.fl4me.android.flamemusicplayer.Song.playSong;
import static com.fl4me.android.flamemusicplayer.Song.setAlbumImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {
    private final Context context;
    private final SongSelectedListener songSelectedListener;

    public SongsAdapter(Context context, SongSelectedListener songSelectedListener) {
        this.context = context;
        this.songSelectedListener = songSelectedListener;
    }

    @NonNull
    @Override
    public SongsAdapter.SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.SongsViewHolder holder, int position) {
        String artist;

        holder.songTitle.setText(songs.get(position).getTitle());

        if(songs.get(position).getArtist().equals("<unknown>")) {
            artist = "Unknown Artist";
        } else {
            artist = songs.get(position).getArtist();
        }
        holder.songDetails.setText(context.getString(R.string.song_details, artist, formatSongDuration(songs.get(position).getDuration())));

        byte[] albumImage = getAlbumImage(songs.get(position).getPath());

        setAlbumImage(context, albumImage, holder.albumImage);

        holder.songItemLayout.setOnClickListener(view -> {
            isPlaying = true;
            playingFromWhere = "songs";
            songSelectedListener.transferInformation(position, albumImage);
            playSong(context, songs, position);
            checkMiniPlayer();
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongsViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout songItemLayout;
        ImageView albumImage;
        TextView songTitle, songDetails;

        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);

            songItemLayout = itemView.findViewById(R.id.song_item_layout);
            albumImage = itemView.findViewById(R.id.song_item_album_image);
            songTitle = itemView.findViewById(R.id.song_item_song_title);
            songDetails = itemView.findViewById(R.id.song_item_song_details);
        }
    }
}