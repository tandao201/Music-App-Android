package com.example.appnhac.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("IdBaiHat")
    @Expose
    private String idBaiHat;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Comment(String idBaiHat, String username, String comment, String avatar) {
        this.idBaiHat = idBaiHat;
        this.username = username;
        this.comment = comment;
        this.avatar = avatar;
    }
}
