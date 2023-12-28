package com.example.bottomnavigation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class UrgentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();  //隐藏ActionBar
            actionBar.show();  //显示ActionBar
        }*/

        String menuTitle = "這邊有點急";
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            TextView textView = actionBar.getCustomView().findViewById(R.id.display_title);
            textView.setText(menuTitle);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}