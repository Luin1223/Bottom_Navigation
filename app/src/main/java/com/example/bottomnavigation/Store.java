package com.example.bottomnavigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Store {

    private String title;

    public Store(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public Store(){

    }

}
