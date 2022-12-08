package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.albumPosition;
import static com.fl4me.android.flamemusicplayer.MainActivity.initializeFullPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;
import static com.fl4me.android.flamemusicplayer.Song.formatSongDuration;
import static com.fl4me.android.flamemusicplayer.Song.getAlbumImage;
import static com.fl4me.android.flamemusicplayer.Song.setAlbumImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
    public static ArrayList<Song> albumSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        ImageView albumDetailsImage = findViewById(R.id.album_details_image);
        String albumPath = getIntent().getStringExtra("albumPath");
        String albumName = getIntent().getStringExtra("albumName");
        TextView albumDetailsInfo = findViewById(R.id.album_details_info);
        int albumDuration = 0;
        RecyclerView albumDetailsRecyclerView = findViewById(R.id.album_details_recyclerView);
        albumDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setAlbumImage(this, getAlbumImage(albumPath), albumDetailsImage);

        int j = 0;
        for(int i = 0; i < songs.size(); i++) {
            if(albumName.equals(songs.get(i).getAlbum())) {
                albumSongs.add(j, songs.get(i));
                j++;
            }
        }

        for(int k = 0; k < albumSongs.size(); k++) {
            albumDuration += albumSongs.get(k).getDuration();
        }

        SongSelectedListener songSelectedListener = (position, albumImage) -> {
            albumPosition = position;
            initializeFullPlayer(this, albumSongs, position, albumImage);

        };

        albumDetailsInfo.setText(albumSongs.get(0).getArtist() + " | " + String.valueOf(albumSongs.size()) + " songs" + " | " + formatSongDuration(albumDuration));

        AlbumDetailsAdapter albumDetailsAdapter = new AlbumDetailsAdapter(getApplicationContext(), albumSongs, songSelectedListener);
        albumDetailsRecyclerView.setAdapter(albumDetailsAdapter);
    }
}