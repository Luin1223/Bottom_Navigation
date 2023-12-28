package com.example.bottomnavigation;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.intellij.lang.annotations.Language;
public class LanguageFragment extends LibraryFragment {

    public LanguageFragment() {
        // Required empty public constructor
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        // Assuming you have an array of language options
        String[] languages = {"English", "简体中文", "繁體中文"};

        // Create ArrayAdapter and add the language options to it
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, languages);

        // Get reference to the ListView in LanguageFragment layout
        ListView listView = view.findViewById(R.id.languageListView);

        listView.setOnClickListener(listViewListener);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        // You can add item click listener for LanguageFragment if needed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Check which item was clicked
                if (position == 0) {
                    // Replace LanguageFragment with LoginActivity
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new LibraryFragment()) // Replace with your actual LoginActivity class
                            .addToBackStack(null)
                            .commit();

                    // Finish the hosting activity (optional, depending on your app's flow)
                    requireActivity().finish();
                }
            }
        });

        return view;
    }
        private View.OnClickListener listViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for the button (assuming it's a ListView item)
                // Perform any necessary actions here

                // Finish the hosting activity
                requireActivity().finish();
            }
        };
}
