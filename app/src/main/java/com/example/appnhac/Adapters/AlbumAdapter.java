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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    Context context;
    ArrayList<Album> albums;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // gan gia tri
        Album album = albums.get(position);
        holder.tenCaSiText.setText(album.getTenCaSiAlbum());
        holder.tenAlbumText.setText(album.getTenAlbum());

        Picasso.with(context).load(album.getHinhAlbum()).into(holder.imgHinhAlbum);
    }

    @Override
    public int getItemCount() {  // hien thi bao nhieu item
        return albums.size();
    }

    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinhAlbum;
        TextView tenAlbumText;
        TextView tenCaSiText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhAlbum = itemView.findViewById(R.id.imageViewAlbum);
            tenAlbumText = itemView.findViewById(R.id.tenAlbumText);
            tenCaSiText = itemView.findViewById(R.id.tenCaSiText);
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
