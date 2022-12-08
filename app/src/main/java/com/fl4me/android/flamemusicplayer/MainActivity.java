package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.AlbumDetails.albumSongs;
import static com.fl4me.android.flamemusicplayer.Song.formatSongDuration;
import static com.fl4me.android.flamemusicplayer.Song.getAlbumImage;
import static com.fl4me.android.flamemusicplayer.Song.isPlaying;
import static com.fl4me.android.flamemusicplayer.Song.playSong;
import static com.fl4me.android.flamemusicplayer.Song.setAlbumImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
1. Bottom navigation DONE
2. Requesting permission DONE
3. Add songs to RecyclerView and show song name and album art DONE
4. Full player screen
5. Play song DONE
6. Button functionality DONE
 */

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static ArrayList<Song> songs;
    public static MediaPlayer mediaPlayer;
    public static ImageView miniAlbumImage, miniPreviousBtn, miniNextBtn, miniPlayPauseBtn, fullAlbumImage, fullPreviousBtn, fullNextBtn, fullPlayPauseBtn;
    public static TextView miniSongTitle, miniArtistName, fullSongTitle, fullArtistName, startTime, endTime;
    public static SeekBar seekBar;
    public static BottomSheetBehavior<View> fullPlayerBehavior;
    public static int songPosition, albumPosition;
    public static Timer timer = new Timer();
    public static ArrayList<Song> albums = new ArrayList<>();
    public static String playingFromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        // Initialize views
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        miniAlbumImage = findViewById(R.id.mini_album_image);
        miniPreviousBtn = findViewById(R.id.mini_previous_btn);
        miniNextBtn = findViewById(R.id.mini_next_btn);
        miniPlayPauseBtn = findViewById(R.id.mini_play_pause_btn);
        fullAlbumImage = findViewById(R.id.full_album_image);
        fullPreviousBtn = findViewById(R.id.full_previous_btn);
        fullNextBtn = findViewById(R.id.full_next_btn);
        fullPlayPauseBtn = findViewById(R.id.full_play_pause_btn);
        miniSongTitle = findViewById(R.id.mini_song_title);
        miniArtistName = findViewById(R.id.mini_artist_name);
        fullSongTitle = findViewById(R.id.full_song_title);
        fullArtistName = findViewById(R.id.full_artist_name);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        View fullPlayer = findViewById(R.id.full_player);
        fullPlayerBehavior = BottomSheetBehavior.from(fullPlayer);
        fullPlayerBehavior.setHideable(true);
        CardView albumImageContainer = findViewById(R.id.album_image_container);
        seekBar = findViewById(R.id.seekBar);

        // Set up screen
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_fragment, new SongsFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if(item.getItemId() == R.id.songs) {
                selectedFragment = new SongsFragment();
            } else {
                selectedFragment = new AlbumsFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_fragment, selectedFragment).commit();

            return true;
        });

        checkMiniPlayer();

        fullPlayer.setOnClickListener(view -> {
            fullPlayerBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        fullPlayerBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        miniAlbumImage.setVisibility(View.VISIBLE);
                        miniSongTitle.setVisibility(View.VISIBLE);
                        miniArtistName.setVisibility(View.VISIBLE);
                        miniPreviousBtn.setVisibility(View.VISIBLE);
                        miniPlayPauseBtn.setVisibility(View.VISIBLE);
                        miniNextBtn.setVisibility(View.VISIBLE);
                        albumImageContainer.setVisibility(View.INVISIBLE);
                        fullAlbumImage.setVisibility(View.INVISIBLE);
                        fullSongTitle.setVisibility(View.INVISIBLE);
                        fullArtistName.setVisibility(View.INVISIBLE);
                        seekBar.setVisibility(View.INVISIBLE);
                        startTime.setVisibility(View.INVISIBLE);
                        endTime.setVisibility(View.INVISIBLE);
                        fullPreviousBtn.setVisibility(View.INVISIBLE);
                        fullPlayPauseBtn.setVisibility(View.INVISIBLE);
                        fullNextBtn.setVisibility(View.INVISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        miniAlbumImage.setVisibility(View.INVISIBLE);
                        miniSongTitle.setVisibility(View.INVISIBLE);
                        miniArtistName.setVisibility(View.INVISIBLE);
                        miniPreviousBtn.setVisibility(View.INVISIBLE);
                        miniPlayPauseBtn.setVisibility(View.INVISIBLE);
                        miniNextBtn.setVisibility(View.INVISIBLE);
                        albumImageContainer.setVisibility(View.VISIBLE);
                        fullAlbumImage.setVisibility(View.VISIBLE);
                        fullSongTitle.setVisibility(View.VISIBLE);
                        fullArtistName.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.VISIBLE);
                        startTime.setVisibility(View.VISIBLE);
                        endTime.setVisibility(View.VISIBLE);
                        fullPreviousBtn.setVisibility(View.VISIBLE);
                        fullPlayPauseBtn.setVisibility(View.VISIBLE);
                        fullNextBtn.setVisibility(View.VISIBLE);
                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:
                        isPlaying = false;
                        mediaPlayer.stop();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    mediaPlayer.seekTo(i);
                }

                startTime.setText(formatSongDuration(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(mediaPlayer != null) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                });
            }
        }, 0, 1000);

        miniPreviousBtn.setOnClickListener(view -> {
            if(playingFromWhere.equals("songs")) {
                playPreviousSong("songs");
            } else {
                playPreviousSong("albums");
            }
        });

        miniPlayPauseBtn.setOnClickListener(view -> {
            togglePlaying(isPlaying);
            isPlaying = !isPlaying;
        });

        miniNextBtn.setOnClickListener(view -> {
            if(playingFromWhere.equals("songs")) {
                playNextSong("songs");
            } else {
                playNextSong("albums");
            }
        });

        albumImageContainer.setOnClickListener(view -> {
            togglePlaying(isPlaying);
            isPlaying = !isPlaying;
        });

        fullPreviousBtn.setOnClickListener(view -> {
            if(playingFromWhere.equals("songs")) {
                playPreviousSong("songs");
            } else {
                playPreviousSong("albums");
            }
        });

        fullPlayPauseBtn.setOnClickListener(view -> {
            togglePlaying(isPlaying);
            isPlaying = !isPlaying;
        });

        fullNextBtn.setOnClickListener(view -> {
            if(playingFromWhere.equals("songs")) {
                playNextSong("songs");
            } else {
                playNextSong("albums");
            }
        });
    }

    // Requesting external storage permission
    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission");
                builder.setMessage("Permission needed");
                builder.setPositiveButton("OK", ((dialogInterface, i) -> {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }));
                builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(this, "Permission was not granted :(", Toast.LENGTH_SHORT).show();
                }));
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            songs = getSongs(this);
        }
    }

    // Callback for permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                songs = getSongs(this);
            } else {
                Toast.makeText(this, "Permission was not granted :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Retrieve songs
    public ArrayList<Song> getSongs(Context context) {
        ArrayList<String> duplicate = new ArrayList<>();
        ArrayList<Song> songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA // path
        };
        String selection = MediaStore.Audio.Media.DATA + " like ? ";
        String[] selectionArgs = new String[]{"%Music%"};

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                String album = cursor.getString(2);
                int duration = cursor.getInt(3);
                String path = cursor.getString(4);

                Song song = new Song(title, artist, album, duration, path, false);

                if(!(duplicate.contains(album))) {
                    albums.add(song);
                    duplicate.add(album);
                }

                songs.add(song);
            }
            cursor.close();
        }
        return songs;
    }

    public static void checkMiniPlayer() {
        if(isPlaying) {
            fullPlayerBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            miniPlayPauseBtn.setImageResource(R.drawable.pause_icon);
            fullPlayPauseBtn.setImageResource(R.drawable.pause_icon);
        } else {
            fullPlayerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            miniPlayPauseBtn.setImageResource(R.drawable.play_icon);
            fullPlayPauseBtn.setImageResource(R.drawable.play_icon);
        }
    }

    public static void initializeFullPlayer(Context context, ArrayList<Song> list, int position, byte[] albumImage) {
        if(list.get(position).getArtist().equals("<unknown>")) {
            miniArtistName.setText(R.string.unknown_artist);
            fullArtistName.setText(R.string.unknown_artist);
        } else {
            miniArtistName.setText(list.get(position).getArtist());
            fullArtistName.setText(list.get(position).getArtist());
        }

        setAlbumImage(context, albumImage, miniAlbumImage);

        miniSongTitle.setText(list.get(position).getTitle());

        setAlbumImage(context, albumImage, fullAlbumImage);

        fullSongTitle.setText(list.get(position).getTitle());
        endTime.setText(formatSongDuration(list.get(position).getDuration()));
    }

    private void togglePlaying(boolean isPlaying) {
        if(isPlaying) {
            mediaPlayer.pause();
            miniPlayPauseBtn.setImageResource(R.drawable.play_icon);
            fullPlayPauseBtn.setImageResource(R.drawable.play_icon);
        } else {
            mediaPlayer.start();
            miniPlayPauseBtn.setImageResource(R.drawable.pause_icon);
            fullPlayPauseBtn.setImageResource(R.drawable.pause_icon);
        }
    }

    private void playPreviousSong(String playingFromWhere) {
        if(mediaPlayer.getCurrentPosition() > 0 && mediaPlayer.getCurrentPosition() < 6000) {
            if(playingFromWhere.equals("songs")) {
                if(songPosition == 0) {
                    songPosition = songs.size() - 1;
                } else {
                    --songPosition;
                }

                initializeFullPlayer(this, songs, songPosition, getAlbumImage(songs.get(songPosition).getPath()));
                playSong(this, songs, songPosition);

                miniPlayPauseBtn.setImageResource(R.drawable.pause_icon);
                fullPlayPauseBtn.setImageResource(R.drawable.pause_icon);
            } else {
                if(albumPosition == 0) {
                    albumPosition = albumSongs.size() - 1;
                } else {
                    --albumPosition;
                }

                Toast.makeText(this, String.valueOf(albumPosition), Toast.LENGTH_SHORT).show();

                initializeFullPlayer(this, albumSongs, albumPosition, getAlbumImage(albumSongs.get(albumPosition).getPath()));
                playSong(this, albumSongs, albumPosition);
            }
        } else {
            mediaPlayer.seekTo(0);
        }
    }

    private void playNextSong(String playingFromWhere) {
        if(playingFromWhere.equals("songs")) {
            if(songPosition == songs.size() - 1) {
                songPosition = 0;
            } else {
                ++songPosition;
            }

            initializeFullPlayer(this, songs, songPosition, getAlbumImage(songs.get(songPosition).getPath()));
            playSong(this, songs, songPosition);

            miniPlayPauseBtn.setImageResource(R.drawable.pause_icon);
            fullPlayPauseBtn.setImageResource(R.drawable.pause_icon);
        } else {
            if(albumPosition == albumSongs.size() - 1) {
                albumPosition = 0;
            } else {
                ++albumPosition;
            }

            Toast.makeText(this, String.valueOf(albumPosition), Toast.LENGTH_SHORT).show();

            initializeFullPlayer(this, albumSongs, albumPosition, getAlbumImage(albumSongs.get(albumPosition).getPath()));
            playSong(this, albumSongs, albumPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if(fullPlayerBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            fullPlayerBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}