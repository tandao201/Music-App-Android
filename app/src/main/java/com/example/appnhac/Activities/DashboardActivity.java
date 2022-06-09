package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.appnhac.Adapters.DsBaiHatQcAdapter;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.Models.User;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView userName,logout,favMusic;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    RecyclerView recycler;
    ArrayList<BaiHat> songs;
    DsBaiHatQcAdapter dsBaiHatQcAdapter;
    User user;
    ArrayList<String> favSongsId;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        eventClick();
        getFavSong();
    }

    private void init(){
        if (PlayMusicActivity.mediaPlayer != null){
            if (PlayMusicActivity.mediaPlayer.isPlaying()){
                PlayMusicActivity.mediaPlayer.stop();
            }
        }
        firebaseAuth = FirebaseAuth.getInstance();
        favMusic = findViewById(R.id.textviewFavoriteMusic);
        logout = findViewById(R.id.textviewDangXuat);
        circleImageView = findViewById(R.id.imageAcc);
        userName = findViewById(R.id.userNameText);
        toolbar = findViewById(R.id.toolbarDashboard);
        floatingActionButton = findViewById(R.id.floatingActionButtonDashboard);
        floatingActionButton.setEnabled(false);
        recycler = findViewById(R.id.recyclerDsBaiHat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tài khoản");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("user")){
                user = (User) intent.getSerializableExtra("user");
                Picasso.with(DashboardActivity.this).load(user.getAvatar()).into(circleImageView);
                userName.setText(user.getTen());
            }
        }
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

    private void eventClick(){
        if (songs!=null)
            floatingActionButtonClick();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.firebaseAuth!=null){
                    if (MainActivity.firebaseAuth.getCurrentUser()!=null){
                        Log.e("dashboard",MainActivity.firebaseAuth.toString());
                        MainActivity.firebaseAuth.signOut();
                        Intent intent = new Intent(DashboardActivity.this,DangNhapActivity.class);
                        startActivity(intent);
                    }
                } else if (DangNhapActivity.mAuth!=null){
                    if (DangNhapActivity.mAuth.getCurrentUser()!=null){
                        DangNhapActivity.mAuth.signOut();
                        Intent intent = new Intent(DashboardActivity.this,DangNhapActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
        favMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dataservice dataservice = APIService.getService();
                Call<List<BaiHat>> callback = dataservice.baiHatYeuThichTheoUser(user.getEmail());
                callback.enqueue(new Callback<List<BaiHat>>() {
                    @Override
                    public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                        songs = (ArrayList<BaiHat>) response.body();
                        dsBaiHatQcAdapter = new DsBaiHatQcAdapter(DashboardActivity.this,songs,favSongsId);
                        recycler.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                        recycler.setAdapter(dsBaiHatQcAdapter);
                        floatingActionButtonClick();
                    }

                    @Override
                    public void onFailure(Call<List<BaiHat>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void floatingActionButtonClick(){
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,PlayMusicActivity.class);
                intent.putExtra("baihats", songs);
                startActivity(intent);
            }
        });
    }
}