package com.example.appnhac.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appnhac.Activities.DsBaiHatActivity;
import com.example.appnhac.Models.Quangcao;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QcAdapter extends PagerAdapter {

    Context context;
    ArrayList<Quangcao> qc;

    public QcAdapter(Context context, ArrayList<Quangcao> qc) {
        this.context = context;
        this.qc = qc;
    }

    @Override
    public int getCount() {
        return qc.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.banner,null);
        ImageView imgBgBanner = view.findViewById(R.id.imageBgBanner);
        ImageView imgSong = view.findViewById(R.id.imageSong);

        Picasso.with(context).load(qc.get(position).getHinhQc()).into(imgBgBanner);
        Picasso.with(context).load(qc.get(position).getHinhBaiHat()).into(imgSong);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DsBaiHatActivity.class);
                intent.putExtra("banner",qc.get(position));
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
