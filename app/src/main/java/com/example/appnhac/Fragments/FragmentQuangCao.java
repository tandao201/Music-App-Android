package com.example.appnhac.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.appnhac.Adapters.QcAdapter;
import com.example.appnhac.Models.Quangcao;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentQuangCao extends Fragment {

    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    QcAdapter qcAdapter;
    Runnable runnable;
    Handler handler;
    int curItem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quangcao,container,false);
        viewPager = view.findViewById(R.id.viewPagerQc);
        circleIndicator = view.findViewById(R.id.indicatorQc);
        getData();
        return view;
    }

    private void getData() {
        Dataservice dataservice = APIService.getService();
        Call<List<Quangcao>> callback = dataservice.getQuangCao();
        callback.enqueue(new Callback<List<Quangcao>>() { // lang nghe
            @Override
            public void onResponse(Call<List<Quangcao>> call, Response<List<Quangcao>> response) { // tra ve
                ArrayList<Quangcao> qc = (ArrayList<Quangcao>) response.body(); // lay du lieu trong body
                qcAdapter = new QcAdapter(getActivity(),qc);
                viewPager.setAdapter(qcAdapter);
                circleIndicator.setViewPager(viewPager);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        curItem = viewPager.getCurrentItem();
                        curItem++;
                        if (curItem>=viewPager.getAdapter().getCount()){
                            curItem = 0;
                        }
                        viewPager.setCurrentItem(curItem,true);
                        handler.postDelayed(runnable,3000);
                    }
                };
                handler.postDelayed(runnable,3000);
            }

            @Override
            public void onFailure(Call<List<Quangcao>> call, Throwable t) { // that bai

            }
        });
    }
}
