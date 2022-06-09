package com.example.appnhac.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Activities.PlayMusicActivity;
import com.example.appnhac.Adapters.CommentViewAdapter;
import com.example.appnhac.Models.Comment;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentComment extends Fragment {

    View view;
    RecyclerView recyclerView;
    EditText commentContent;
    Button btComment;
    static ArrayList<Comment> comments = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment_baihat,container,false);
        recyclerView = view.findViewById(R.id.recyclerViewComment);
        commentContent = view.findViewById(R.id.editTextComment);
        btComment = view.findViewById(R.id.buttonComment);
        comments = PlayMusicActivity.comments;
        firebaseAuth = FirebaseAuth.getInstance();
//        getCommentByIdSong();
        if (comments.size()>0){
            CommentViewAdapter commentViewAdapter = new CommentViewAdapter(getActivity(), comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(commentViewAdapter);
            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Nhan", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(getActivity(), "Bạn cần đăng nhập!", Toast.LENGTH_SHORT).show();
                } else {
                    String comment = String.valueOf(commentContent.getText());
                    if (comment.isEmpty()){
                        Toast.makeText(getActivity(), "Bạn chưa nhập gì!", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String username = user.getDisplayName();
                        String id = PlayMusicActivity.songsPlaying.get(PlayMusicActivity.position).getIdBaiHat();
                        String avatar = String.valueOf(user.getPhotoUrl());
                        Dataservice dataservice = APIService.getService();
                        Call<String> callback = dataservice.upLoadComment(username,comment,id,avatar);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                        commentContent.setText("");
                        PlayMusicActivity.comments.add(new Comment(id,username,comment,avatar));
                        Toast.makeText(getActivity(), "Bình luận thành công!", Toast.LENGTH_SHORT).show();
//                        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_commentX");
//                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                        Log.d("comment", "onClick: "+currentFragment);
//                        ft.detach(currentFragment);
//                        ft.attach(currentFragment);
//                        ft.commit();
                    }
                }
            }
        });
        return view;
    }


}
