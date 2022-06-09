package com.example.appnhac.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.DsTheLoaiTheoChuDeActivity;
import com.example.appnhac.Models.ChuDe;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DsChuDeAdapter extends RecyclerView.Adapter<DsChuDeAdapter.ViewHolder> {


    Context context;
    ArrayList<ChuDe> chuDes;

    public DsChuDeAdapter(Context context, ArrayList<ChuDe> chuDes) {
        this.context = context;
        this.chuDes = chuDes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_chu_de,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuDe chuDe = chuDes.get(position);
        Picasso.with(context).load(chuDe.getHinhChuDe()).into(holder.imgChuDe);
    }

    @Override
    public int getItemCount() {
        return chuDes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgChuDe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChuDe = itemView.findViewById(R.id.imageItemChuDe);
            imgChuDe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DsTheLoaiTheoChuDeActivity.class);
                    intent.putExtra("chude",chuDes.get(getPosition()));
                    context.startActivity(intent); //
                }
            });
        }
    }
}
