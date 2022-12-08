package com.fl4me.android.flamemusicplayer;

import static com.fl4me.android.flamemusicplayer.MainActivity.albums;
import static com.fl4me.android.flamemusicplayer.MainActivity.songs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_albums, container, false);

        RecyclerView albumsRecyclerView = view.findViewById(R.id.albums_recyclerView);
        albumsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if(albums.size() >= 1) {
            AlbumsAdapter albumsAdapter = new AlbumsAdapter(getActivity(), albums);
            albumsRecyclerView.setAdapter(albumsAdapter);
        }

        return view;
    }
}