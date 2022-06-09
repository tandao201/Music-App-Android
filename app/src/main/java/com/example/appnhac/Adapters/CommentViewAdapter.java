package com.example.appnhac.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Models.Comment;
import com.example.appnhac.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentViewAdapter extends RecyclerView.Adapter<CommentViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Comment> comments;

    public CommentViewAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_item,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Picasso.with(context).load(comment.getAvatar()).into(holder.imgAvatar);
        holder.accountName.setText(comment.getUsername());
        holder.content.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView avatar;
        ImageView imgAvatar;
        TextView accountName,content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.cardViewAccountImageComment);
            accountName = itemView.findViewById(R.id.textviewAccountName);
            content = itemView.findViewById(R.id.textviewContentOfComment);
            imgAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }
}
