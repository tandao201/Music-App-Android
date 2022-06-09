package com.example.appnhac.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String email,ten,avatar;

    public User(String email, String ten,String avatar) {
        this.email = email;
        this.ten = ten;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
