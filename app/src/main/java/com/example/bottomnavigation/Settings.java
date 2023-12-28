package com.example.bottomnavigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private ListView lv;

    private  String[] view_id= new String[]{"設定","主題","分享","登出"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_library);

        lv=findViewById(R.id.lv);

        ArrayAdapter<String> adapter=
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,view_id);

        lv.setAdapter(adapter);

        // 獲取從前一個活動 (LoginActivity 或 MainHome) 傳遞過來的資訊
        /*Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER")) {
            String user = intent.getStringExtra("USER");

            // 在這裡使用 user，例如將其設置給 TextView 顯示在 UI 上
            TextView id = findViewById(R.id.id_name);
            id.setText(user);
        }*/

        lv.setOnItemClickListener(listviewListener);



    }

    private AdapterView.OnItemClickListener listviewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String msg=view_id[position];

        }
    };
}
