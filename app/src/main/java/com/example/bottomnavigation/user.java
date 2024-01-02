package com.example.bottomnavigation;

import android.util.Log;

public class user {

    private static user instance;

    private String user;
    private static String account;

    public user() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static synchronized user getInstance(){
        if(instance == null){
            instance=new user();
        }
        return instance;
    }

    public static String getAccount(){
        return account;
    }

    public void setAccount(String account){
        this.account=account;
    }
}
