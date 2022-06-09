package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.appnhac.Adapters.DsPlaylistAdapter;
import com.example.appnhac.Models.Playlist;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsPlaylistsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView dsPlaylistRecycler;
    ArrayList<Playlist> playlists;
    DsPlaylistAdapter dsPlaylistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_playlists);
        anhXaView();
        getData();
    }

    private void getData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Playlist>> callback = dataservice.getAllPlaylists();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                dsPlaylistAdapter = new DsPlaylistAdapter(DsPlaylistsActivity.this,playlists);
                dsPlaylistRecycler.setLayoutManager(new GridLayoutManager(DsPlaylistsActivity.this,2));
                dsPlaylistRecycler.setAdapter(dsPlaylistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    private void anhXaView() {
        toolbar = findViewById(R.id.toolBarDsPlaylist);
        dsPlaylistRecycler = findViewById(R.id.recyclerDsPlaylist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PLAYLISTS");
//        getResources().getColor(R.color.name)
        toolbar.setTitleTextColor(Color.BLUE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}