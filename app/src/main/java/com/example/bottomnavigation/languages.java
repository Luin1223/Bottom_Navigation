package com.example.bottomnavigation;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class languages extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);

        // Assuming you have an array of language options
        String[] language = {"English", "简体中文", "繁體中文"};

        // Create ArrayAdapter and add the language options to it
        ArrayAdapter<String> adapter = new ArrayAdapter<>(languages.this, android.R.layout.simple_list_item_1, language);

        // Get reference to the ListView in LanguageFragment layout
        ListView listView = findViewById(R.id.listview);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // You can add item click listener for LanguageFragment if needed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Resources resources = getResources();
                        DisplayMetrics dm = resources.getDisplayMetrics();
                        Configuration config = resources.getConfiguration();
                        config.locale = Locale.ENGLISH;
                        resources.updateConfiguration(config, dm);
                        break;
                    case 1:
                        Resources resources2 = getResources();
                        DisplayMetrics dm2 = resources2.getDisplayMetrics();
                        Configuration config2 = resources2.getConfiguration();
                        config2.locale = Locale.SIMPLIFIED_CHINESE;
                        resources2.updateConfiguration(config2, dm2);
                        break;
                    case 2:
                        Resources resources3 = getResources();
                        DisplayMetrics dm3 = resources3.getDisplayMetrics();
                        Configuration config3 = resources3.getConfiguration();
                        config3.locale = Locale.TRADITIONAL_CHINESE;
                        resources3.updateConfiguration(config3, dm3);
                        break;
                }
                returnToLibraryFragment();
            }
        });
    }

    private void returnToLibraryFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_con, new LibraryFragment())
                .addToBackStack(null)
                .commit();
        finish();
    }
}
