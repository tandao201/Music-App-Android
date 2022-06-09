package com.example.appnhac.Adapters;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.MainActivity;
import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsBaiHatQcAdapter extends RecyclerView.Adapter<DsBaiHatQcAdapter.ViewHolder> {


    Context context; // noi de tao giao dien
    ArrayList<BaiHat> songs;
    FirebaseAuth firebaseAuth;
    ArrayList<String> favSongsId;

    public DsBaiHatQcAdapter(Context context, ArrayList<BaiHat> songs,ArrayList<String> favSongsId) {
        this.context = context;
        this.songs = songs;
        this.favSongsId = favSongsId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ds_baihat_qc,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = songs.get(position);
        holder.tenBaiHatText.setText(baiHat.getTenBaiHat());
        holder.tenCaSiText.setText(baiHat.getCasi());
        position++;
        holder.indexText.setText(position+"");
        if (favSongsId !=null){
            if (favSongsId.contains(baiHat.getIdBaiHat())){
                holder.imgLuotThich.setImageResource(R.drawable.iconloved);
                holder.imgLuotThich.setEnabled(false);
            }
        }
        for (BaiHat baiHat1: MainActivity.songsOffline){
            if ( baiHat1.getTenBaiHat().equals(baiHat.getTenBaiHat())){
                holder.imgDownload.setVisibility(View.GONE);
            }
        }
        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = baiHat.getLinkBaiHat();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                                DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download");
                request.setDescription(baiHat.getTenBaiHat());
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,baiHat.getTenBaiHat()+".mp3");
//                request.setDestinationInExternalFilesDir(context,".mp3","download");


                // download
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                manager.enqueue(request);

                File musicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), baiHat.getTenBaiHat()+".mp3");
                // retrieve more metadata, duration etc.
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Audio.AudioColumns.DATA, musicFile.getPath());
                contentValues.put(MediaStore.Audio.AudioColumns.TITLE, baiHat.getTenBaiHat());
                contentValues.put(MediaStore.Audio.AudioColumns.ARTIST, baiHat.getCasi());
                contentValues.put(MediaStore.Audio.AudioColumns.DISPLAY_NAME, baiHat.getTenBaiHat());
// more columns should be filled from here
                Uri uri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);

                holder.imgDownload.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView indexText,tenBaiHatText,tenCaSiText;
        ImageView imgLuotThich;
        ImageButton imgDownload;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            indexText = itemView.findViewById(R.id.indexDsTextview);
            tenBaiHatText = itemView.findViewById(R.id.tenbaiHatTextview);
            tenCaSiText = itemView.findViewById(R.id.tenCasiTextView);
            imgLuotThich = itemView.findViewById(R.id.luotThichDsQc);
            imgDownload = itemView.findViewById(R.id.imageDownload);
            firebaseAuth = FirebaseAuth.getInstance();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("baihat",songs.get(getPosition()));
                    context.startActivity(intent);
                }
            });
            FirebaseUser user = firebaseAuth.getCurrentUser();
            imgLuotThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user==null){
                        imgLuotThich.setEnabled(false);
                        Toast.makeText(context, "Bạn phải đăng nhập!", Toast.LENGTH_SHORT).show();
                    } else {
                        imgLuotThich.setEnabled(true);
                        imgLuotThich.setImageResource(R.drawable.iconloved);

                        Dataservice dataservice = APIService.getService();
                        Call<String> callback = dataservice.upDateBaiHatYeuThich("1", songs.get(getPosition()).getIdBaiHat(),user.getEmail());
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                        imgLuotThich.setEnabled(false);
                    }
                }
            });

        }
    }
}
