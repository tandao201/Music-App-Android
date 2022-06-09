package com.example.appnhac.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.DsAlbumActivity;
import com.example.appnhac.Adapters.AlbumAdapter;
import com.example.appnhac.Models.Album;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAlbumToDay extends Fragment {

    View view;
    ArrayList<Album> albums;
    RecyclerView recyclerView;
    TextView xemThemAlbum;
    AlbumAdapter albumAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_today,container,false);
        recyclerView = view.findViewById(R.id.recyclerAlbum);
        xemThemAlbum = view.findViewById(R.id.xemThemAlbum);
        getData();
        xemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DsAlbumActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getData() {
        Dataservice dataservice = APIService.getService(); // cau hinh retrofit
        Call<List<Album>> callback = dataservice.getAlbumToDay();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albums = (ArrayList<Album>) response.body();
                albumAdapter = new AlbumAdapter(getActivity(),albums);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getActivity())); // co nhieu layout nen can layoutmanager
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(albumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}
