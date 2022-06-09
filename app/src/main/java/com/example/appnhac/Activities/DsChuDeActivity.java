package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.appnhac.Adapters.DsChuDeAdapter;
import com.example.appnhac.Models.ChuDe;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsChuDeActivity extends AppCompatActivity {

    Toolbar toolbarChuDe;
    RecyclerView recyclerViewAllChuDe;
    ArrayList<ChuDe> chuDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_chu_de);
        init();
        getData();

    }

    private void getData() {
        Dataservice dataservice = APIService.getService();
        Call<List<ChuDe>> callback = dataservice.getAllChuDe();
        callback.enqueue(new Callback<List<ChuDe>>() {
            @Override
            public void onResponse(Call<List<ChuDe>> call, Response<List<ChuDe>> response) {
                chuDes = (ArrayList<ChuDe>) response.body();
                DsChuDeAdapter dsChuDeAdapter = new DsChuDeAdapter(DsChuDeActivity.this,chuDes);
                recyclerViewAllChuDe.setLayoutManager(new GridLayoutManager(DsChuDeActivity.this,1));
                recyclerViewAllChuDe.setAdapter(dsChuDeAdapter);
            }

            @Override
            public void onFailure(Call<List<ChuDe>> call, Throwable t) {

            }
        });
    }

    private void init(){
        toolbarChuDe = findViewById(R.id.toolbarAllChuDe);
        recyclerViewAllChuDe = findViewById(R.id.recyclerAllChuDe);

        setSupportActionBar(toolbarChuDe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề");
        toolbarChuDe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}