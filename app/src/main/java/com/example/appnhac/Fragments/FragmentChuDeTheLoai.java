package com.example.appnhac.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.appnhac.Activities.DsBaiHatActivity;
import com.example.appnhac.Activities.DsChuDeActivity;
import com.example.appnhac.Activities.DsTheLoaiTheoChuDeActivity;
import com.example.appnhac.Models.ChuDe;
import com.example.appnhac.Models.ChuDeTheLoaiToDay;
import com.example.appnhac.Models.TheLoai;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChuDeTheLoai extends Fragment {

    View view;
    ChuDeTheLoaiToDay chuDeTheLoaiToDay;
    HorizontalScrollView horizontalScrollView;
    TextView xemThemTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chude_theloai,container,false);
        horizontalScrollView = view.findViewById(R.id.horizotalChuDeTheLoai);
        xemThemTextView = view.findViewById(R.id.xemThemChudeTL);
        xemThemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DsChuDeActivity.class);
                startActivity(intent);
            }
        });
        getData();
        return view;
    }

    private void getData() {
        Dataservice dataservice = APIService.getService();
        Call<ChuDeTheLoaiToDay> callback = dataservice.getChuDeTheLoaiToDay();
        callback.enqueue(new Callback<ChuDeTheLoaiToDay>() {
            @Override
            public void onResponse(Call<ChuDeTheLoaiToDay> call, Response<ChuDeTheLoaiToDay> response) {
                chuDeTheLoaiToDay = (ChuDeTheLoaiToDay) response.body();
                final ArrayList<ChuDe> chudes = (ArrayList<ChuDe>) chuDeTheLoaiToDay.getChuDe();
                final ArrayList<TheLoai> theloais = (ArrayList<TheLoai>) chuDeTheLoaiToDay.getTheLoai();
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(580,250);
                layout.setMargins(10,20,10,30);  // xet lai kich thuoc

                for (int i=0 ; i<chudes.size() ; i++){
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    String image = chudes.get(i).getHinhChuDe();
                    if (image !=null){
                        Picasso.with(getActivity()).load(image).into(imageView);
                    }
                    cardView.setLayoutParams(layout); // xet kich thuoc de tranh hong anh
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                    int finalI = i;
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DsTheLoaiTheoChuDeActivity.class);
                            intent.putExtra("chude",chudes.get(finalI));
                            startActivity(intent);
                        }
                    });
                }

                for (int i=0 ; i<theloais.size() ; i++){
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    String image = theloais.get(i).getHinhTheLoai();
                    if (image !=null){
                        Picasso.with(getActivity()).load(image).into(imageView);
                    }
                    cardView.setLayoutParams(layout); // xet kich thuoc de tranh hong anh
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                    int finalI = i;
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DsBaiHatActivity.class);
                            intent.putExtra("idTheLoai",theloais.get(finalI));
                            startActivity(intent);
                        }
                    });
                }
                horizontalScrollView.addView(linearLayout);
            }

            @Override
            public void onFailure(Call<ChuDeTheLoaiToDay> call, Throwable t) {

            }
        });
    }
}
