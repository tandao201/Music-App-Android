package com.example.appnhac.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.DsBaiHatActivity;
import com.example.appnhac.Activities.MainActivity;
import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Adapters.DsBaiHatOfflineAdapter;
import com.example.appnhac.Adapters.DsBaiHatQcAdapter;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentBaiHatOffline extends Fragment {
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerViewDsBaiHat;
    FloatingActionButton floatingActionButton;
    ArrayList<BaiHat> songs;
    DsBaiHatOfflineAdapter dsBaiHatOfflineAdapter;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_baihat_offline,container,false);

        anhxa();
        init();
        getData();
        if (songs.size()>0)
            eventClick();
        return view;
    }

    private void getData() {
        songs = (ArrayList<BaiHat>) getAllAudioFromDevice(getActivity());
//        Log.d("OFFLINE", "getData: "+songs.get(0).getTenBaiHat());
        dsBaiHatOfflineAdapter = new DsBaiHatOfflineAdapter(getContext(),songs);
        recyclerViewDsBaiHat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDsBaiHat.setAdapter(dsBaiHatOfflineAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        init();
    }

    public List<BaiHat> getAllAudioFromDevice(Context context) {
        final List<BaiHat> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, // for path
                                MediaStore.Audio.AudioColumns.TITLE ,
                                MediaStore.Audio.AudioColumns.ALBUM,
                                MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri,
                                projection, MediaStore.Audio.Media.DATA+" LIKE?", new String[]{"%.mp3%"}, null);

        if (c != null) {
            while (c.moveToNext()) {
                // Create a model object.
                BaiHat audioModel = new BaiHat();

                String path = c.getString(0);   // Retrieve path.
                String name = c.getString(1);   // Retrieve name.
                String album = c.getString(2);  // Retrieve album name.
                String artist = c.getString(3); // Retrieve artist name.
                File musicFile = new File(path);
                if (musicFile.exists()){
//                    Set data to the model object.
                    audioModel.setTenBaiHat(name);
//                audioModel.setaAlbum(album);
                    audioModel.setCasi(artist);
                    audioModel.setLinkBaiHat(path);

                    Log.e("Name :" + name, " Album :" + album);
                    Log.e("Path :" + path, " Artist :" + artist);
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    try {
                        retriever.setDataSource(path);
                        byte[] art = retriever.getEmbeddedPicture();
                        retriever.release();
                        if ( art!= null){
                            audioModel.setHinhBaiHat(String.valueOf(art));
                        } else {
                            audioModel.setHinhBaiHat(String.valueOf(R.drawable.icon_offline));
                        }
                    } catch (Exception e){

                    }
                    // Add the model object to the list .
                    tempAudioList.add(audioModel);
                }
            }
            c.close();
        } else if (!c.moveToNext()){
            Toast.makeText(context, "No music found!", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(context, "Some thing wrong!", Toast.LENGTH_SHORT).show();
        }

        // Return the list.
        return tempAudioList;
    }

    private void anhxa() {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayoutOffline);
        collapsingToolbarLayout = view.findViewById(R.id.collapsingToolBarOffline);
        toolbar = view.findViewById(R.id.toolbarDsOffline);
        recyclerViewDsBaiHat = view.findViewById(R.id.recyclerDsBaiHatOffline);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonOffline);

    }

    private void eventClick() {
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                intent.putExtra("baihats", songs);
                startActivity(intent);
            }
        });
    }

    private void init() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle(null);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        floatingActionButton.setEnabled(false);

    }
}
