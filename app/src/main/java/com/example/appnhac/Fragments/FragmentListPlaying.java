package com.example.appnhac.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Adapters.ListPlayingAdapter;
import com.example.appnhac.R;

public class FragmentListPlaying extends Fragment {

    View view;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_playing,container,false);
        recyclerView = view.findViewById(R.id.recyclerLisyPlaying);
        if (PlayMusicActivity.songsPlaying.size()>0){
            ListPlayingAdapter listPlayingAdapter = new ListPlayingAdapter(getActivity(), PlayMusicActivity.songsPlaying);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(listPlayingAdapter);
            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Nhan", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}
