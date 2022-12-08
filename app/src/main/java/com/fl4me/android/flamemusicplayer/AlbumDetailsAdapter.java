package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.checkMiniPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.playingFromWhere;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;
import static com.fl4me.android.flamemusicplayer.Song.getAlbumImage;
import static com.fl4me.android.flamemusicplayer.Song.isPlaying;
import static com.fl4me.android.flamemusicplayer.Song.playSong;
import static com.fl4me.android.flamemusicplayer.Song.setAlbumImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.AlbumDetailsViewHolder> {
    private final Context context;
    private final ArrayList<Song> albumSongs;
    private final SongSelectedListener songSelectedListener;

    public AlbumDetailsAdapter(Context context, ArrayList<Song> albumSongs, SongSelectedListener songSelectedListener) {
        this.context = context;
        this.albumSongs = albumSongs;
        this.songSelectedListener = songSelectedListener;
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.AlbumDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);

        return new AlbumDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.AlbumDetailsViewHolder holder, int position) {
        setAlbumImage(context, getAlbumImage(albumSongs.get(position).getPath()), holder.albumImage);
        holder.songTitle.setText(albumSongs.get(position).getTitle());
        holder.songDetails.setText(albumSongs.get(position).getArtist());

        holder.songItemLayout.setOnClickListener(view -> {
            isPlaying = true;
            playingFromWhere = "albums";
            setAlbumImage(context, getAlbumImage(albumSongs.get(position).getPath()), holder.albumImage);
            songSelectedListener.transferInformation(position, getAlbumImage(albumSongs.get(position).getPath()));
            playSong(context, albumSongs, position);
            checkMiniPlayer();
            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return albumSongs.size();
    }

    public static class AlbumDetailsViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout songItemLayout;
        ImageView albumImage;
        TextView songTitle, songDetails;

        public AlbumDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            songItemLayout = itemView.findViewById(R.id.song_item_layout);
            albumImage = itemView.findViewById(R.id.song_item_album_image);
            songTitle = itemView.findViewById(R.id.song_item_song_title);
            songDetails = itemView.findViewById(R.id.song_item_song_details);
        }
    }
}