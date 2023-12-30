package com.example.bottomnavigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class languages extends AppCompatActivity {
    ListView listView;
    int[] resIds=new int[]{R.drawable.us,R.drawable.china,R.drawable.tw};
    String[] language = {"English", "简体中文", "繁體中文"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);

        listView = findViewById(R.id.listview);

        MyAdapter adapter=new MyAdapter(this);

        listView.setAdapter(adapter);

        // You can add item click listener for LanguageFragment if needed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new AlertDialog.Builder(languages.this)
                                .setTitle(R.string.confirmation_title)
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(R.string.confirmation_message)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(languages.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User clicked Cancel, do nothing
                                    }
                                })
                                .show();
                        Resources resources = getResources();
                        DisplayMetrics dm = resources.getDisplayMetrics();
                        Configuration config = resources.getConfiguration();
                        config.locale = Locale.ENGLISH;
                        resources.updateConfiguration(config, dm);
                        break;
                    case 1:
                        new AlertDialog.Builder(languages.this)
                                .setTitle(R.string.confirmation_title)
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(R.string.confirmation_message)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(languages.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User clicked Cancel, do nothing
                                    }
                                })
                                .show();
                        Resources resources2 = getResources();
                        DisplayMetrics dm2 = resources2.getDisplayMetrics();
                        Configuration config2 = resources2.getConfiguration();
                        config2.locale = Locale.SIMPLIFIED_CHINESE;
                        resources2.updateConfiguration(config2, dm2);
                        break;
                    case 2:
                        new AlertDialog.Builder(languages.this)
                                .setTitle(R.string.confirmation_title)
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(R.string.confirmation_message)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(languages.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User clicked Cancel, do nothing
                                    }
                                })
                                .show();
                        Resources resources3 = getResources();
                        DisplayMetrics dm3 = resources3.getDisplayMetrics();
                        Configuration config3 = resources3.getConfiguration();
                        config3.locale = Locale.TRADITIONAL_CHINESE;
                        resources3.updateConfiguration(config3, dm3);
                        break;
                }
            }
        });
    }
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;
        public MyAdapter(Context c){myInflater= LayoutInflater.from(c);}
        @Override
        public int getCount(){return language.length;}
        @Override
        public Object getItem(int position){return language[position];}
        @Override
        public long getItemId(int position){return position;}
        @Override
        public View getView(int position, View converView, ViewGroup parent){
            converView=myInflater.inflate(R.layout.languagelayout,null);
            ImageView image=converView.findViewById(R.id.image);
            TextView txt1=converView.findViewById(R.id.txt1);
            image.setImageResource(resIds[position]);
            txt1.setText(language[position]);
            return converView;
        }
    }
}
