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

import com.example.appnhac.Activities.DsBaiHatActivity;
import com.example.appnhac.Models.TheLoai;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DsTheLoaiTheoChuDeAdapter extends RecyclerView.Adapter<DsTheLoaiTheoChuDeAdapter.ViewHolder> {

    Context context;
    ArrayList<TheLoai> theLoais;

    public DsTheLoaiTheoChuDeAdapter(Context context, ArrayList<TheLoai> theLoais) {
        this.context = context;
        this.theLoais = theLoais;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_theloai_theo_chude,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheLoai theLoai = theLoais.get(position);
        Picasso.with(context).load(theLoai.getHinhTheLoai()).into(holder.imageViewTheLoai);
        holder.tenTheLoai.setText(theLoai.getTenTheLoai());
    }

    @Override
    public int getItemCount() {
        return theLoais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewTheLoai;
        TextView tenTheLoai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewTheLoai = itemView.findViewById(R.id.imageTheLoaiTheoChuDe);
            tenTheLoai = itemView.findViewById(R.id.tenTheLoaiTheoChuDe);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DsBaiHatActivity.class);
                    intent.putExtra("idTheLoai",theLoais.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
