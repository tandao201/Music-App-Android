package com.example.appnhac.Activities;

import static com.example.appnhac.Activities.PlayMusicActivity.mediaPlayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appnhac.Adapters.MainViewPagerAdapter;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.Models.User;
import com.example.appnhac.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static ViewPager2 viewPager;
    SeekBar seekBar;
    public static CircleImageView circleImageView;
    public static TextView tenBaiHat,tenCaSi;
    ImageButton imgPlay,imgNext;
    public static LinearLayout linearLayout;
    BaiHat baiHat;
    ObjectAnimator objectAnimator;
    public static int position=PlayMusicActivity.position;
    public static FirebaseAuth firebaseAuth;
    public static ArrayList<BaiHat> songsPlaying = new ArrayList<>();
    public static ArrayList<BaiHat> songsOffline = new ArrayList<>();
    public static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions();
        init();
        change();
        process();
        evenClick();
    }

    private void permissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            },REQUEST_CODE);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length>0){

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                },REQUEST_CODE);
            }
        }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (songsPlaying.size()>0)
            outState.putParcelableArrayList("songs",songsPlaying);
        Log.e("inSave", String.valueOf(songsPlaying.size()));
        outState.putInt("position",position);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if (PlayMusicActivity.songsPlaying.size()<=0)
            songsPlaying = savedInstanceState.getParcelableArrayList("songs");
        position = savedInstanceState.getInt("position");
        Log.e("onRestore", String.valueOf(position));
            Log.e("onResstore",String.valueOf(PlayMusicActivity.songsPlaying.size()));
        process();
        evenClick();
    }

    private void loadBaiHat(BaiHat baiHat){
        Picasso.with(MainActivity.this).load(baiHat.getHinhBaiHat()).into(circleImageView);
        tenBaiHat.setText(baiHat.getTenBaiHat());
        tenCaSi.setText(baiHat.getCasi());
    }

    public void process() {
        Intent intent = getIntent();
        if (intent != null){

            if (intent.hasExtra("baihatMini")){
                linearLayout.setVisibility(View.VISIBLE);
                baiHat = intent.getParcelableExtra("baihatMini");
                if (intent.hasExtra("index"))
                    position = intent.getIntExtra("index",0);
                loadBaiHat(baiHat);
                imgPlay.setImageResource(R.drawable.iconpause);
                objectAnimator = ObjectAnimator.ofFloat(circleImageView,"rotation",0f,360f);
                objectAnimator.setDuration(10000);
                objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator.setRepeatMode(ValueAnimator.RESTART);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.start();
                seekBar.setMax(mediaPlayer.getDuration());
                if (mediaPlayer.isPlaying()){
                    imgPlay.setImageResource(R.drawable.iconpause);
                } else {
                    imgPlay.setImageResource(R.drawable.iconplay);
                    objectAnimator.pause();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        position++;
                        if(position> songsPlaying.size()-1)
                            position=0;
                        Log.e("complete", String.valueOf(position));
                        Log.e("sóng",songsPlaying.get(position).getTenBaiHat());
                        loadBaiHat(songsPlaying.get(position));
                    }
                });
                upDate();
            }
        }
    }

    private void upDate(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null)
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 300);
            }
        },300);
    }

    private void evenClick() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PlayMusicActivity.class);
                intent.putExtra("baihatToPlay",songsPlaying);
                Log.e("eventClick", String.valueOf(songsPlaying.size()));
                for(BaiHat x:songsPlaying)
                    Log.e("evenClick",x.getTenBaiHat());
                intent.putExtra("position",position);
                PlayMusicActivity.position = position;
                startActivity(intent);
            }
        });
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    objectAnimator.pause();
                    imgPlay.setImageResource(R.drawable.iconplay);
                } else {
                    mediaPlayer.start();
                    objectAnimator.resume();
                    imgPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songsPlaying.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release(); //dong bo lai
                        mediaPlayer = null;
                        seekBar.setProgress(0);
                    }
                    if (position < songsPlaying.size()) {
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position++;

                        if (position > songsPlaying.size() - 1) {
                            position = 0;
                        }
                        BaiHat bh = songsPlaying.get(position);
                        loadBaiHat(bh);
                        new PlayMusicActivity.PlayMp3().execute(bh.getLinkBaiHat());
                        upDate();

                    }
                }
                // set delay time cho nut next
                imgNext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgNext.setClickable(true);
                    }
                }, 3000);
            }

        });
    }

    private void init(){
        viewPager = findViewById(R.id.MainViewPager);
        bottomNavigationView = findViewById(R.id.MainBottomNav);
        seekBar = findViewById(R.id.seekbarMiniPlayer);
        circleImageView = findViewById(R.id.imageMiniPlayer);
        tenBaiHat = findViewById(R.id.tenBaiHatMiniPlayer);
        tenCaSi = findViewById(R.id.tenCaSiMiniPlayer);
        imgPlay = findViewById(R.id.iconPlayMiniPlayer);
        imgNext = findViewById(R.id.iconNextMiniPlayer);
        linearLayout = findViewById(R.id.miniPlayer);
        linearLayout.setVisibility(View.GONE);
        seekBar.setEnabled(false);
        seekBar.setBackground(null);
        firebaseAuth = FirebaseAuth.getInstance();
        songsOffline = (ArrayList<BaiHat>) getAllAudioFromDevice(MainActivity.this);
        if (PlayMusicActivity.songsPlaying.size()>0){
            Log.e("gan lai tu play->main", String.valueOf(PlayMusicActivity.songsPlaying.size()));
            songsPlaying = PlayMusicActivity.songsPlaying;
        }

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager.setAdapter(mainViewPagerAdapter);
    }

    private void change() {

        // fragment thay doi->bottomNav thay doi

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab1).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab2).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab4).setChecked(true);
                        break;
                }
            }
        });

        // bottomNav thay doi->fragment thay doi
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_tab1:
                        viewPager.setCurrentItem(0,true);
                        break;
                    case R.id.menu_tab2:
                        viewPager.setCurrentItem(1,true);
                        break;
                    case R.id.menu_tab3:
                        Intent intent;
                        if (firebaseAuth.getCurrentUser()==null)
                            intent = new Intent(MainActivity.this, DangNhapActivity.class);
                        else {
                            intent = new Intent(MainActivity.this,DashboardActivity.class);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            User user1 = new User(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString());
                            Log.e("user",user.getEmail());
                            if (user != null){
                                intent.putExtra("user",user1);
                            }
                        }

                        startActivity(intent);
                        break;
                    case R.id.menu_tab4:
                        viewPager.setCurrentItem(2,true);
                        break;
                }

                return true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (songsPlaying!=null)
            songsPlaying.clear();
        super.onDestroy();
    }


}