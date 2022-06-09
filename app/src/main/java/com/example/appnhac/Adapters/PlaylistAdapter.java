package com.example.appnhac.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appnhac.Models.Playlist;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
        super(context, resource, objects);
    }
    class ViewHolder {
        TextView tenPlaylistTextView;
        ImageView imgIconImageView,imgBgImageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){ // chua ton tai j -> converview=null
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.playlist,null);
            viewHolder = new ViewHolder();
            viewHolder.tenPlaylistTextView = convertView.findViewById(R.id.tenPlayList);
            viewHolder.imgBgImageView = convertView.findViewById(R.id.imgBgPlaylist);
            viewHolder.imgIconImageView = convertView.findViewById(R.id.imgIconPlaylist);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Playlist playlist = getItem(position);

        Picasso.with(getContext()).load(playlist.getHinhnen()).into(viewHolder.imgBgImageView);
        Picasso.with(getContext()).load(playlist.getHinhicon()).into(viewHolder.imgIconImageView);
        viewHolder.tenPlaylistTextView.setText(playlist.getTen());
        return convertView;
    }
}
