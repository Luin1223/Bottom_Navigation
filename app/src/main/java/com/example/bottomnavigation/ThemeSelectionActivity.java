package com.example.bottomnavigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ThemeSelectionActivity extends AppCompatActivity {

    private ImageView img1,img2,img3,img4,img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        img1=findViewById(R.id.image1);
        img2=findViewById(R.id.image2);
        img3=findViewById(R.id.image3);
        img4=findViewById(R.id.image4);
        img5=findViewById(R.id.image5);

        /*img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ThemeSelectionActivity", "Image 1 clicked");
                switchTheme(R.style.AppTheme_Theme1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTheme(R.style.AppTheme_Theme2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTheme(R.style.AppTheme_Theme3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTheme(R.style.AppTheme_Theme4);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTheme(R.style.AppTheme_Theme5);
            }
        });*/

    }
   /* private void switchTheme(int themeId) {

        // 设置当前 Activity 主题
        setTheme(themeId);

        // 重启当前 Activity 以应用新主题
        recreate();
    }*/



}

