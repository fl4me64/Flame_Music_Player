package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.fullPlayPauseBtn;
import static com.fl4me.android.flamemusicplayer.MainActivity.initializeFullPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.mediaPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.miniPlayPauseBtn;
import static com.fl4me.android.flamemusicplayer.MainActivity.seekBar;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;
import static com.fl4me.android.flamemusicplayer.MainActivity.timer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Song {
    public String title;
    public String artist;
    public String album;
    public int duration;
    public String path;
    public static boolean isPlaying;

    public Song(String title, String artist, String album, int duration, String path, boolean isPlaying) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        Song.isPlaying = isPlaying;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public static String formatSongDuration(int duration) {
        int hours, minutes, seconds;
        String hoursString, minutesString, secondsString;

        hours = duration / 3600000;
        minutes = (duration / 60000) - (hours * 60);
        seconds = (duration / 1000) - (hours * 3600 + minutes * 60);

        if(hours >= 10) {
            hoursString = Integer.toString(hours);
        } else {
            hoursString = "0" + hours;
        }

        if(minutes >= 0 && minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = Integer.toString(minutes);
        }

        if(seconds >= 0  && seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = Integer.toString(seconds);
        }

        if(hoursString.equals("00")) {
            return minutesString + ":" + secondsString;
        } else {
            return hoursString + ":" + minutesString + ":" + secondsString;
        }
    }

    public static byte[] getAlbumImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] albumArt = retriever.getEmbeddedPicture();
        retriever.release();
        return albumArt;
    }

    public static void setAlbumImage(Context context, byte[] albumImage, ImageView albumImageHolder) {
        if(albumImage != null) {
            Glide.with(context).asBitmap()
                    .load(albumImage)
                    .into(albumImageHolder);
        } else {
            Glide.with(context).asBitmap()
                    .load(R.drawable.song_icon)
                    .into(albumImageHolder);
        }
    }

    public static void playSong(Context context, ArrayList<Song> list, int position) {
        isPlaying = true;

        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, Uri.parse(list.get(position).getPath()));
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();

        int nextPosition = ++position;
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            byte[] nextAlbumImage = getAlbumImage(list.get(nextPosition).getPath());
            initializeFullPlayer(context, list, nextPosition, nextAlbumImage);
            playSong(context, list, nextPosition);
        });
    }
}