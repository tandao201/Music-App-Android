package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.appnhac.Adapters.DsBaiHatQcAdapter;
import com.example.appnhac.Models.Album;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.Models.Playlist;
import com.example.appnhac.Models.Quangcao;
import com.example.appnhac.Models.TheLoai;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsBaiHatActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerViewDsBaiHat;
    FloatingActionButton floatingActionButton;
    Quangcao quangcao = null;
    ImageView imageViewDs;
    ArrayList<BaiHat> songs;
    DsBaiHatQcAdapter dsBaiHatQcAdapter;
    Playlist playlist;
    TheLoai theLoai;
    Album album;
    FirebaseAuth firebaseAuth;
    public static ArrayList<String> favSongsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_bai_hat);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy); // loi phat sinh khi su dung mang
        anhXaView();
        dataIntent();
        init();
        if(quangcao != null && !quangcao.getTenBaiHat().isEmpty()){
            setValueInView(quangcao.getTenBaiHat(),quangcao.getHinhBaiHat());
            getDataQc(quangcao.getIdQuangCao());
        }
        if (playlist != null && !playlist.getTen().isEmpty()){
            setValueInView(playlist.getTen(),playlist.getHinhnen());
            getDataPlaylist(playlist.getIdPlaylist());
        }
        if (theLoai !=null && !theLoai.getTenTheLoai().isEmpty()){
            setValueInView(theLoai.getTenTheLoai(),theLoai.getHinhTheLoai());
            getDataTheLoai(theLoai.getIdTheLoai());
        }
        if(album!=null && !album.getTenAlbum().isEmpty()){
            setValueInView(album.getTenAlbum(),album.getHinhAlbum());
            getDataAlbum(album.getIdAlbum());
        }
        getFavSong();

    }

    private void getFavSong() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        favSongsId = new ArrayList<>();
        if (user != null){
            Dataservice dataservice = APIService.getService();
            Call<List<BaiHat>> callback = dataservice.baiHatYeuThichTheoUser(user.getEmail());
            callback.enqueue(new Callback<List<BaiHat>>() {
                @Override
                public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                    ArrayList<BaiHat> favSongs = (ArrayList<BaiHat>) response.body();
                    for (BaiHat song :favSongs){
                        favSongsId.add(song.getIdBaiHat());
                        Log.d("Bai hat yeu thich", "onResponse: "+song.getIdBaiHat());
                    }

                }
                @Override
                public void onFailure(Call<List<BaiHat>> call, Throwable t) {

                }
            });
        }
    }

    private void getDataAlbum(String idAlbum) {
        Dataservice dataservice = APIService.getService();
        Call<List<BaiHat>> callback = dataservice.getDsBaiHatTheoAlbum(idAlbum);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                songs = (ArrayList<BaiHat>) response.body();
                dsBaiHatQcAdapter = new DsBaiHatQcAdapter(DsBaiHatActivity.this,songs,favSongsId);
                recyclerViewDsBaiHat.setLayoutManager(new LinearLayoutManager(DsBaiHatActivity.this));
                recyclerViewDsBaiHat.setAdapter(dsBaiHatQcAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataTheLoai(String idTheLoai) {
        Dataservice dataservice = APIService.getService();
        Call<List<BaiHat>> callback = dataservice.getDsBaiHatTheoTheLoai(idTheLoai);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                songs = (ArrayList<BaiHat>) response.body();
                dsBaiHatQcAdapter = new DsBaiHatQcAdapter(DsBaiHatActivity.this,songs,favSongsId);
                recyclerViewDsBaiHat.setLayoutManager(new LinearLayoutManager(DsBaiHatActivity.this));
                recyclerViewDsBaiHat.setAdapter(dsBaiHatQcAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataPlaylist(String idPlaylist) {
        Dataservice dataservice = APIService.getService();
        Call<List<BaiHat>> callback = dataservice.getDSBaiHatTheoPlaylist(idPlaylist);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                songs = (ArrayList<BaiHat>) response.body();
                dsBaiHatQcAdapter = new DsBaiHatQcAdapter(DsBaiHatActivity.this,songs,favSongsId);
                recyclerViewDsBaiHat.setLayoutManager(new LinearLayoutManager(DsBaiHatActivity.this));
                recyclerViewDsBaiHat.setAdapter(dsBaiHatQcAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataQc(String idQuangcao) {
        Dataservice dataservice = APIService.getService();
        Call<List<BaiHat>> callback = dataservice.getDsBaiHatTheoQc(idQuangcao);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                songs = (ArrayList<BaiHat>) response.body();
                dsBaiHatQcAdapter = new DsBaiHatQcAdapter(DsBaiHatActivity.this,songs,favSongsId);
                recyclerViewDsBaiHat.setLayoutManager(new LinearLayoutManager(DsBaiHatActivity.this));
                recyclerViewDsBaiHat.setAdapter(dsBaiHatQcAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void setValueInView(String ten, String imgBg) {
        collapsingToolbarLayout.setTitle(ten);
        try {
            URL url = new URL(imgBg);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
            collapsingToolbarLayout.setBackground(bitmapDrawable);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Picasso.with(this).load(imgBg).into(imageViewDs);
    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        floatingActionButton.setEnabled(false);
    }

    private void anhXaView() {
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolBar);
        toolbar = findViewById(R.id.toolbarDs);
        recyclerViewDsBaiHat = findViewById(R.id.recyclerDsBaiHat);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        imageViewDs = findViewById(R.id.imageDsBaiHat);
    }

    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("banner")){
                quangcao = (Quangcao) intent.getSerializableExtra("banner");
            }

            if(intent.hasExtra("dsPl")){
                playlist = (Playlist) intent.getSerializableExtra("dsPl");
            }
            if(intent.hasExtra("idTheLoai")){
                theLoai = (TheLoai) intent.getSerializableExtra("idTheLoai");
            }
            if(intent.hasExtra("album")){
                album = (Album) intent.getSerializableExtra("album");
            }
        }
    }
    private void eventClick(){
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DsBaiHatActivity.this,PlayMusicActivity.class);
                intent.putExtra("baihats", songs);
                startActivity(intent);
            }
        });
    }
}