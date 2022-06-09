package com.example.appnhac.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;

import java.util.ArrayList;
import android.os.Handler;

public class ListPlayingAdapter extends RecyclerView.Adapter<ListPlayingAdapter.ViewHolder> {

    Context context;
    ArrayList<BaiHat> songs;

    public ListPlayingAdapter(Context context, ArrayList<BaiHat> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_play_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = songs.get(position);
        holder.index.setText(position+1+"");
        holder.tenCaSi.setText(baiHat.getCasi());
        holder.tenBaiHat.setText(baiHat.getTenBaiHat());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView index,tenBaiHat,tenCaSi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.indexListPlaying);
            tenBaiHat = itemView.findViewById(R.id.tenbaiHatListPlaying);
            tenCaSi = itemView.findViewById(R.id.tenCaSiListPlaying);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaiHat baiHat = songs.get(getPosition());
                    Toast.makeText(context, baiHat.getTenBaiHat(), Toast.LENGTH_SHORT).show();
                    if(PlayMusicActivity.mediaPlayer.isPlaying()){
                        PlayMusicActivity.mediaPlayer.stop();

                    }
                    PlayMusicActivity.imgPlay.setImageResource(R.drawable.iconpause);
                    PlayMusicActivity.position = songs.indexOf(baiHat);
                    new PlayMusicActivity.PlayMp3().execute(baiHat.getLinkBaiHat());
                    PlayMusicActivity.getCommentByIdSong();
                    PlayMusicActivity.toolbar.setTitle(baiHat.getTenBaiHat());
                    Handler handler = new Handler();
                    // doi anh fragment dia nhac
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (PlayMusicActivity.listPlayingViewPagerAdapter.createFragment(1) != null) { // lay duoc du lieu tu fragment?
                                if (PlayMusicActivity.songsPlaying.size() > 0 && PlayMusicActivity.fragmentDiaNhac.circleImageView != null) {
                                    PlayMusicActivity.fragmentDiaNhac.Playnhac(baiHat.getHinhBaiHat());
                                    handler.removeCallbacks(this);
                                } else {
                                    handler.postDelayed(this, 300); // goi lai funtion nay
                                }
                            }
                        }
                    },500);
                }
            });
        }
    }
}
