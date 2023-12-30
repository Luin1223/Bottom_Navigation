package com.example.bottomnavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class languages extends AppCompatActivity {
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);

        // Assuming you have an array of language options
        String[] language = {"English", "简体中文", "繁體中文"};

        // Create ArrayAdapter and add the language options to it
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, language);

        // Get reference to the ListView in LanguageFragment layout
        ListView listView = findViewById(R.id.listview);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // You can add item click listener for LanguageFragment if needed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Replace LanguageFragment with LibraryFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new LibraryFragment()) // Replace with your actual LibraryFragment class
                            .addToBackStack(null)
                            .commit();
                    finish();

                }
            }
        });
    }
}
