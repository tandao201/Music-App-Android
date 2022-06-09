package com.example.appnhac.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    Context context;
    ArrayList<BaiHat> songs;

    public SearchResultAdapter(Context context, ArrayList<BaiHat> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_search_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = songs.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).into(holder.imgBaiHat);
        holder.tenBaiHatText.setText(baiHat.getTenBaiHat());
        holder.tenCasiText.setText(baiHat.getCasi());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBaiHat;
        TextView tenBaiHatText,tenCasiText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBaiHat = itemView.findViewById(R.id.imageItemSearchResult);
            tenBaiHatText = itemView.findViewById(R.id.textViewTenBaiHatResult);
            tenCasiText = itemView.findViewById(R.id.textViewTenCaSiResult);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("baihat",songs.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
