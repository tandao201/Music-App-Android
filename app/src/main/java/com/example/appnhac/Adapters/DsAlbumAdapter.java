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
import com.example.appnhac.Models.Album;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DsAlbumAdapter extends RecyclerView.Adapter<DsAlbumAdapter.ViewHolder> {

    Context context;
    ArrayList<Album> albums;

    public DsAlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_album,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albums.get(position);
        Picasso.with(context).load(album.getHinhAlbum()).into(holder.imgAlbum);
        holder.tenAlbum.setText(album.getTenAlbum());
        holder.tenCaSiAlbum.setText(album.getTenCaSiAlbum());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAlbum;
        TextView tenAlbum,tenCaSiAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.imageItemAlbum);
            tenAlbum = itemView.findViewById(R.id.tenItemAlbum);
            tenCaSiAlbum = itemView.findViewById(R.id.tenCaSiAlbum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DsBaiHatActivity.class);
                    intent.putExtra("album",albums.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
