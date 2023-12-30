package com.example.bottomnavigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;

public class Privacypolicy extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pp);

        ListView listView = findViewById(R.id.list);

        // Assuming you have a string array
        String[] textArray = new String[]{
                getString(R.string.long_text),
        };

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textArray);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

    }
}
