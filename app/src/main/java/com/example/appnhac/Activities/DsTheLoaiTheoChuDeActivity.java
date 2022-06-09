package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appnhac.Adapters.DsTheLoaiTheoChuDeAdapter;
import com.example.appnhac.Models.ChuDe;
import com.example.appnhac.Models.TheLoai;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsTheLoaiTheoChuDeActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ChuDe chuDe;
    ArrayList<TheLoai> theLoais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_the_loai_theo_chu_de);
        getIntentData();
        getData(chuDe.getIdChuDe());
        init();

    }

    private void getData(String idchude) {
        Dataservice dataservice = APIService.getService();
        Call<List<TheLoai>> callback = dataservice.getAllTheLoaiTheoChuDe(idchude);
        callback.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                theLoais = (ArrayList<TheLoai>) response.body();
                DsTheLoaiTheoChuDeAdapter dsTheLoaiTheoChuDeAdapter =
                        new DsTheLoaiTheoChuDeAdapter(DsTheLoaiTheoChuDeActivity.this,theLoais);
                recyclerView.setLayoutManager(new GridLayoutManager(DsTheLoaiTheoChuDeActivity.this,2));
                recyclerView.setAdapter(dsTheLoaiTheoChuDeAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });
    }

    private void init(){
        toolbar = findViewById(R.id.toolbarAllTheLoaiTheoChuDe);
        recyclerView = findViewById(R.id.recyclerAllTheLoaiTheoChuDe);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chuDe.getTenChuDe());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent.hasExtra("chude")){
            chuDe = (ChuDe) intent.getSerializableExtra("chude");
        }
    }
}