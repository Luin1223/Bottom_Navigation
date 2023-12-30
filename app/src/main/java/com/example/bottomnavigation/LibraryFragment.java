package com.example.bottomnavigation;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayAdapter<String> listview;


    public LibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        String[] view_id = new String[]{
                getString(R.string.language_selection),
                getString(R.string.theme),
                getString(R.string.description),
                getString(R.string.share),
                getString(R.string.privacypolicy),
                getString(R.string.signout)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, view_id);

        ListView listView = view.findViewById(R.id.lv);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LibraryFragment", "Item clicked at position: " + position);

                switch (position) {
                    case 0:
                        Intent intent=new Intent();
                        intent.setClass(getActivity(),languages.class);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:
                        Intent i=new Intent();
                        i.setClass(getActivity(),Description.class);
                        startActivity(i);
                        break;
                    case 3:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this link: https://developer.android.com/training/sharing/");
                        sendIntent.putExtra(Intent.EXTRA_TITLE, "Introducing content previews");
                        Uri contentUri = Uri.parse("https://developer.android.com/training/sharing/");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        sendIntent.setType("text/plain");
                        sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        break;
                    case 4:
                        Intent in=new Intent();
                        in.setClass(getActivity(), Privacypolicy.class);
                        startActivity(in);
                        break;
                    case 5:
                        Log.d("LibraryFragment", "Showing confirmation dialog for logout");
                        new AlertDialog.Builder(requireContext())
                                .setTitle(R.string.confirmation_title)
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(R.string.m)
                                .setPositiveButton(R.string.yes, (dialog, which) -> {
                                    Log.d("LibraryFragment", "User confirmed logout");
                                    Intent loginIntent = new Intent(requireActivity(), LoginActivity.class);
                                    startActivity(loginIntent);
                                    requireActivity().finish();
                                })
                                .setNegativeButton(R.string.no, (dialog, which) -> {
                                    Log.d("LibraryFragment", "User canceled logout");
                                })
                                .show();
                        break;
                }
            }
        });
        return view;
    }
}

