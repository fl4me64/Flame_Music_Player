package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.initializeFullPlayer;
import static com.fl4me.android.flamemusicplayer.MainActivity.songPosition;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_songs, container, false);

        RecyclerView songsRecyclerView = view.findViewById(R.id.songs_recyclerView);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SongSelectedListener songSelectedListener = (position, albumImage) -> {
            songPosition = position;
            initializeFullPlayer(getActivity().getApplicationContext(), songs, position, albumImage);
        };

        if(songs.size() >= 1) {
            SongsAdapter songsAdapter = new SongsAdapter(getActivity(), songSelectedListener);
            songsRecyclerView.setAdapter(songsAdapter);
        }

        return view;
    }
}