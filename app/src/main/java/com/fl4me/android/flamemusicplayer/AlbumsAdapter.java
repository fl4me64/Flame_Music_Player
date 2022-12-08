package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.Song.getAlbumImage;
import static com.fl4me.android.flamemusicplayer.Song.setAlbumImage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsViewHolder> {
    private final Context context;
    private final ArrayList<Song> albums;

    public AlbumsAdapter(Context context, ArrayList<Song> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumsAdapter.AlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);

        return new AlbumsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapter.AlbumsViewHolder holder, int position) {
        holder.albumName.setText(albums.get(position).getAlbum());
        byte[] image = getAlbumImage(albums.get(position).getPath());
        setAlbumImage(context, image, holder.albumImage);

        holder.albumImageContainer.setOnClickListener(view -> {
            Intent intent = new Intent(context, AlbumDetails.class);
            intent.putExtra("albumPath", albums.get(position).getPath());
            intent.putExtra("albumName", albums.get(position).getAlbum());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Log.i("Albums", String.valueOf(albums.size()));
        return albums.size();
    }

    public static class AlbumsViewHolder extends RecyclerView.ViewHolder {
        CardView albumImageContainer;
        ImageView albumImage;
        TextView albumName;

        public AlbumsViewHolder(@NonNull View itemView) {
            super(itemView);

            albumImageContainer = itemView.findViewById(R.id.album_tab_album_image_container);
            albumImage = itemView.findViewById(R.id.album_tab_album_image);
            albumName = itemView.findViewById(R.id.album_tab_album_name);
        }
    }
}