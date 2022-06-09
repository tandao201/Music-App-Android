package com.example.appnhac.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.appnhac.Adapters.ListPlayingViewPagerAdapter;
import com.example.appnhac.Fragments.FragmentComment;
import com.example.appnhac.Fragments.FragmentDiaNhac;
import com.example.appnhac.Fragments.FragmentListPlaying;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.Models.Comment;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMusicActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    static TextView timeSong, timeSongTotal;
    static SeekBar seekBar;
    public static ImageButton imgPlay, imgRepeat, imgNext, imgPre, imgRandom;
    static ViewPager2 viewPager;
    public static ArrayList<BaiHat> songsPlaying = new ArrayList<>();
    public static ListPlayingViewPagerAdapter listPlayingViewPagerAdapter;
    public static FragmentDiaNhac fragmentDiaNhac;
    public static FragmentListPlaying fragmentListPlaying;
    public static FragmentComment fragmentComment;
    public static MediaPlayer mediaPlayer;
    public static int position = 0;
    static boolean repeat = false;
    static boolean random = false;
    static boolean next = false;
    Intent intent;
    int positionMiniPlayer;
    public static ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy); // loi phat sinh khi su dung mang
        getDataIntent();
        init();
        eventClick();

    }



    public static void getCommentByIdSong(){
        fragmentComment = null;
        fragmentComment = new FragmentComment();
        listPlayingViewPagerAdapter.removeFragment();
        comments.clear();
        String id = songsPlaying.get(position).getIdBaiHat();
        Log.d("PlayingActivity", "getCommentByIdSong: load lai comments "+id);
        Dataservice dataservice = APIService.getService();
        Call<List<Comment>> callback = dataservice.getAllCommentBySongId(id);
        callback.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = (ArrayList<Comment>) response.body();
                for(Comment x:comments)
                    Log.d("comment", "onResponse: "+x.getUsername());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
        listPlayingViewPagerAdapter.addFragment(fragmentComment);
    }

    private void eventClick() {
        Handler handler = new Handler();
        if(!intent.hasExtra("baihatToPlay")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listPlayingViewPagerAdapter.createFragment(1) != null) { // lay duoc du lieu tu fragment?
                        if (songsPlaying.size() > 0 && fragmentDiaNhac.circleImageView != null) {
                            fragmentDiaNhac.Playnhac(songsPlaying.get(position).getHinhBaiHat());
                            handler.removeCallbacks(this);
                        } else {
                            handler.postDelayed(this, 300); // goi lai funtion nay
                        }
                    }
                }
            }, 500);
        }
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    if (fragmentDiaNhac.objectAnimator != null)
                        fragmentDiaNhac.objectAnimator.pause();
                    imgPlay.setImageResource(R.drawable.iconplay);
                } else {
                    mediaPlayer.start();
                    if (fragmentDiaNhac.objectAnimator != null)
                        fragmentDiaNhac.objectAnimator.resume();
                    imgPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });
        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat == false) {
                    if (random == true) {
                        random = false;
                        imgRepeat.setImageResource(R.drawable.iconsyned);
                        imgRandom.setImageResource(R.drawable.iconsuffle);
                    }
                    imgRepeat.setImageResource(R.drawable.iconsyned);
                    repeat = true;
                } else {
                    imgRepeat.setImageResource(R.drawable.iconrepeat);
                    repeat = false;
                }
            }
        });
        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (random == false) {
                    if (repeat == true) {
                        repeat = false;
                        imgRepeat.setImageResource(R.drawable.iconrepeat);
                        imgRandom.setImageResource(R.drawable.iconshuffled);
                    }
                    imgRandom.setImageResource(R.drawable.iconshuffled);
                    random = true;
                } else {
                    imgRandom.setImageResource(R.drawable.iconsuffle);
                    random = false;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
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
                        if (repeat == true) {
                            if (position == 0) {
                                position = songsPlaying.size();
                            }
                            position -= 1;
                        }
                        if (random == true) {
                            Random random = new Random();
                            int rd = random.nextInt(songsPlaying.size());
                            if (rd == position) {
                                position = rd - 1;
                            }
                            position = rd;
                        }
                        if (position > songsPlaying.size() - 1) {
                            position = 0;
                        }
                        BaiHat bh = songsPlaying.get(position);
                        new PlayMp3().execute(bh.getLinkBaiHat());
                        fragmentDiaNhac.Playnhac(bh.getHinhBaiHat());
                        getSupportActionBar().setTitle(bh.getTenBaiHat());
                        updateTime();
                    }
                }
                imgPre.setClickable(false);
                imgNext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 3000);
                getCommentByIdSong();
            }

        });
        imgPre.setOnClickListener(new View.OnClickListener() {
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
                        position -= 1;
                        if (repeat == true) {
                            position += 1;
                        }
                        if (position < 0) {
                            position = songsPlaying.size() - 1;
                        }

                        if (random == true) {
                            Random random = new Random();
                            int rd = random.nextInt(songsPlaying.size());
                            if (rd == position) {
                                position = rd - 1;
                            }
                            position = rd;
                        }

                        BaiHat bh = songsPlaying.get(position);
                        new PlayMp3().execute(bh.getLinkBaiHat());
                        fragmentDiaNhac.Playnhac(bh.getHinhBaiHat());
                        getSupportActionBar().setTitle(bh.getTenBaiHat());
                        updateTime();
                    }
                }
                imgPre.setClickable(false);
                imgNext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 3000);
                getCommentByIdSong();
            }
        });
    }

    private void getDataIntent() {
        intent = getIntent();
        songsPlaying.clear();
        if (intent != null) {
            if (intent.hasExtra("baihat")) {
                BaiHat baiHat = intent.getParcelableExtra("baihat");
                songsPlaying.add(baiHat);
            }
            if (intent.hasExtra("baihats") ) {
                ArrayList<BaiHat> songs = intent.getParcelableArrayListExtra("baihats");
                songsPlaying = songs;
            }

            if (intent.hasExtra("baihatToPlay")){
                ArrayList<BaiHat> songs = intent.getParcelableArrayListExtra("baihatToPlay");
                songsPlaying.clear();
                songsPlaying = songs;
            }
            if (intent.hasExtra("position")){
                positionMiniPlayer = intent.getIntExtra("position",0);
            }
        }
    }

    private void init() {

        toolbar = findViewById(R.id.toolbarPlayMusic);
        timeSong = findViewById(R.id.timeSong);
        timeSongTotal = findViewById(R.id.timeTotalSong);
        seekBar = findViewById(R.id.seekbarSong);
        imgPlay = findViewById(R.id.imageButtonPlay);
        imgRepeat = findViewById(R.id.imageButtonRepeat);
        imgNext = findViewById(R.id.imageButtonNext);
        imgPre = findViewById(R.id.imageButtonPreview);
        imgRandom = findViewById(R.id.imageButtonRandom);
        viewPager = findViewById(R.id.viewpagerPlayMusic);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Music Player");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayMusicActivity.this,MainActivity.class);
                intent.putExtra("baihatMini",songsPlaying.get(position));
                intent.putExtra("songs",songsPlaying);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });
        fragmentDiaNhac = new FragmentDiaNhac();
        fragmentListPlaying = new FragmentListPlaying();
        fragmentComment = new FragmentComment();
        listPlayingViewPagerAdapter = new ListPlayingViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        listPlayingViewPagerAdapter.addFragment(fragmentListPlaying);
        listPlayingViewPagerAdapter.addFragment(fragmentDiaNhac);
        listPlayingViewPagerAdapter.addFragment(fragmentComment);
        viewPager.setAdapter(listPlayingViewPagerAdapter);
        fragmentDiaNhac = (FragmentDiaNhac) listPlayingViewPagerAdapter.createFragment(1);

        if (intent.hasExtra("baihatToPlay") || intent.hasExtra("position")){
            getSupportActionBar().setTitle(songsPlaying.get(positionMiniPlayer).getTenBaiHat());
            Handler handler = new Handler();
            timeSong();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragmentDiaNhac.circleImageView != null) {
                        fragmentDiaNhac.Playnhac(songsPlaying.get(positionMiniPlayer).getHinhBaiHat());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 100); // goi lai funtion nay
                    }
                }
            }, 100);
            imgPlay.setImageResource(R.drawable.iconpause);
            Handler handler1 = new Handler();
            if (mediaPlayer.isPlaying()){
                imgPlay.setImageResource(R.drawable.iconpause);
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentDiaNhac.circleImageView != null) {
                            fragmentDiaNhac.objectAnimator.resume();
                            handler1.removeCallbacks(this);
                        } else {
                            handler1.postDelayed(this, 300); // goi lai funtion nay
                        }
                    }
                }, 500);

            } else {
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // lay duoc du lieu tu fragment?
                        if ( fragmentDiaNhac.circleImageView != null) {
                            fragmentDiaNhac.objectAnimator.pause();
                            handler1.removeCallbacks(this);
                        } else {
                            handler1.postDelayed(this, 300); // goi lai funtion nay
                        }
                    }
                }, 500);
                imgPlay.setImageResource(R.drawable.iconplay);
            }
        }
        else if (songsPlaying.size() > 0) {
            position=0;
            if (mediaPlayer!=null){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
            }

            getSupportActionBar().setTitle(songsPlaying.get(0).getTenBaiHat());
            new PlayMp3().execute(songsPlaying.get(0).getLinkBaiHat());
            imgPlay.setImageResource(R.drawable.iconpause);
            getCommentByIdSong();
        }
    }


    public static class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baiHat) {
            super.onPostExecute(baiHat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // lang nghe khi phat xong 1 bai
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) { // time bi lau qua -> loi
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        if (songsPlaying.size() > 0 && fragmentDiaNhac.circleImageView != null)
                            fragmentDiaNhac.Playnhac(songsPlaying.get(position).getHinhBaiHat());
                        imgPlay.setImageResource(R.drawable.iconpause);
                    }
                });
                mediaPlayer.setDataSource(baiHat);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            timeSong();
            updateTime();
        }
    }

    private static void timeSong() {
        int duration = mediaPlayer.getDuration();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        timeSongTotal.setText(simpleDateFormat.format(duration));
        seekBar.setMax(duration);
    }

    private static final void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    timeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 300);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true) {
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
                            if (repeat == true) {
                                if (position == 0) {
                                    position = songsPlaying.size();
                                }
                                position -= 1;
                            }
                            if (random == true) {
                                Random random = new Random();
                                int rd = random.nextInt(songsPlaying.size());
                                if (rd == position) {
                                    position = rd - 1;
                                }
                                position = rd;
                            }
                            if (position > songsPlaying.size() - 1) {
                                position = 0;
                            }
                            BaiHat bh = songsPlaying.get(position);
                            new PlayMp3().execute(bh.getLinkBaiHat());
                            fragmentDiaNhac.Playnhac(bh.getHinhBaiHat());
                            toolbar.setTitle(bh.getTenBaiHat());
                        }
                    }
                    imgPre.setClickable(false);
                    imgNext.setClickable(false);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgPre.setClickable(true);
                            imgNext.setClickable(true);
                        }
                    }, 3000);
                    next = false;
                    handler1.removeCallbacks(this);
                } else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}