package com.example.appnhac.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsBaiHatOfflineAdapter extends RecyclerView.Adapter<DsBaiHatOfflineAdapter.ViewHolder> {


    Context context; // noi de tao giao dien
    ArrayList<BaiHat> songs;
    FirebaseAuth firebaseAuth;
    int pos;

    public DsBaiHatOfflineAdapter(Context context, ArrayList<BaiHat> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_baihat_offline,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = songs.get(position);
        holder.tenBaiHatText.setText(baiHat.getTenBaiHat());
        holder.tenCaSiText.setText(baiHat.getCasi());
//        position++;

//        holder.indexText.setText(position+"");
//        holder.indexText.setVisibility(View.GONE);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDeleteDialog(holder,baiHat);
            }
        });
    }

    private void openDeleteDialog(ViewHolder holder,BaiHat baiHat) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        Window window = dialog.getWindow();
        if (window == null)
            return;
        int gravity = Gravity.CENTER;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity)
            dialog.setCancelable(true);
        else
            dialog.setCancelable(false);

        TextView tenBaiHatDialog;
        Button xoaBtDialog,huyBtDialog;

        tenBaiHatDialog = dialog.findViewById(R.id.tenBaiHatTextViewDialog);
        xoaBtDialog = dialog.findViewById(R.id.xoaBtDialog);
        huyBtDialog = dialog.findViewById(R.id.huyBtDialog);
        tenBaiHatDialog.setText(baiHat.getTenBaiHat());
        xoaBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songs.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),songs.size());
                File musicFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), baiHat.getTenBaiHat()+".mp3");
                Log.e("DELETE", "exist: "+musicFile.exists());
//                musicFile.delete();
                Log.e("DELETE", "onClick: "+musicFile.delete());

                dialog.dismiss();
            }
        });
        huyBtDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView indexText,tenBaiHatText,tenCaSiText;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            indexText = itemView.findViewById(R.id.indexDsTextview);
            tenBaiHatText = itemView.findViewById(R.id.tenbaiHatTextview);
            tenCaSiText = itemView.findViewById(R.id.tenCasiTextView);
            imgDelete = itemView.findViewById(R.id.deleteImage);
            firebaseAuth = FirebaseAuth.getInstance();
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
