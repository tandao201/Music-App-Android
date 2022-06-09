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
import com.example.appnhac.Models.Playlist;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DsPlaylistAdapter extends RecyclerView.Adapter<DsPlaylistAdapter.ViewHolder> {

    Context context;
    ArrayList<Playlist> playlists;

    public DsPlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.tenItemPlaylist.setText(playlist.getTen());
        Picasso.with(context).load(playlist.getHinhnen()).into(holder.imgItemPlaylist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imgItemPlaylist;
        TextView tenItemPlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemPlaylist = itemView.findViewById(R.id.imageItemPlaylist);
            tenItemPlaylist = itemView.findViewById(R.id.tenItemPlaylist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DsBaiHatActivity.class);
                    intent.putExtra("dsPl",playlists.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
