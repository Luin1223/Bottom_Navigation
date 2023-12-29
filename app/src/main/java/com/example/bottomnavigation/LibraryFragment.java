package com.example.bottomnavigation;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


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

        // 假设你有一个包含字符串的数组
        String[] view_id = new String[]{"語言選擇", "主題", "自訂", "說明", "分享", "登出"};

        // 创建 ArrayAdapter 并将字符串数组添加到它
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, view_id);

        // 获取 ListView 的引用
        ListView listView = view.findViewById(R.id.lv);

        // 设置 Adapter 到 ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("LibraryFragment", "Item clicked at position: " + position);

                // Check which item was clicked and navigate accordingly
                switch (position) {
                    case 0:
                        // "語言選擇" clicked, navigate to LanguageFragment
                        Log.d("LibraryFragment", "Navigating to LanguageFragment");
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new LanguageFragment())
                                .addToBackStack(null)
                                .commit();
                        final Context context=getActivity();
                        Intent LanguageIntent = new Intent(context, LanguageFragment.class);
                        startActivity(LanguageIntent);

                        break;
                    /*case 5:
                        // "登出" clicked, start nologin activity
                        Log.d("LibraryFragment", "Starting nologin activity");
                        Intent nologinIntent = new Intent(requireActivity(), LanguageFragment.class);
                        startActivity(nologinIntent);
                        // Finish the current activity (optional, depending on your app's flow)
                        requireActivity().finish();
                        break;
                    // Add cases for other items as needed
                    */
                }
            }
        });

        return view;
    }
}

