package com.example.bottomnavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainHome extends AppCompatActivity {

    FloatingActionButton fab;

    DrawerLayout drawerLayout;

    BottomNavigationView bottomNavigationView;

    FirebaseDatabase database,database1;
    DatabaseReference reference,reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        fab=findViewById(R.id.tab);


        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            }  else if (itemId == R.id.nav_settings) {
                replaceFragment(new LibraryFragment());
            }

            return true;

        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        } );

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        String[] Goal = new String[]{"一周","兩周","三周","一個月","三個月","半年","一年"};

        final  Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottonsheetlayout);

        ArrayAdapter<String>adapterGoals = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,Goal);

        adapterGoals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LinearLayout addnewtask = dialog.findViewById(R.id.Addnewtask);
        //LinearLayout addnewgoal = dialog.findViewById(R.id.Addnewgoal);
        ImageView cancelButton = dialog.findViewById(R.id.cancelbtn);



        addnewtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.addtask);

                ImageView cancelbtn = dialog.findViewById(R.id.cancelbtn);
                TextView create=dialog.findViewById(R.id.createText);
                EditText Title=dialog.findViewById(R.id.edt_title);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = Title.getText().toString();


                        // 将数据写入 Firebase 数据库
                        writeToFirebase(title);


                        // 啟動另一個頁面
                        dialog.dismiss();
                    }
                });



                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        /*addnewgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.addgoal);

                ImageView cancelbtn = dialog.findViewById(R.id.cancelbtn);
                Spinner spn = dialog.findViewById(R.id.spn);
                EditText edt = dialog.findViewById(R.id.edt);
                TextView createText = dialog.findViewById(R.id.createText);

                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                spn.setAdapter(adapterGoals);
                spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String date_text = parent.getSelectedItem().toString();
                        String goal_text = edt.getText().toString();

                        // 將目標和日期合併成一個字串，使用逗號分隔
                        String combinedString = goal_text + "," + date_text;

                        createText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 調用修改後的 writeToFirebase1 方法
                                //writeToFirebase1(combinedString);

                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }
        });*/


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    // 写入数据到 Firebase 数据库的方法
    private void writeToFirebase(String title) {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("tasks");

        Store store = new Store(title);

        reference.child(title).setValue(store);

    }

    /*private void writeToFirebase1(String combinedString) {
        database1 = FirebaseDatabase.getInstance();
        reference1 = database1.getReference("goals");

        // 將合併的字串拆分成目標和日期
        String[] parts = combinedString.split(",");
        String goal = parts[0];
        String date = parts[1];

        Target target = new Target(goal, date);

        // 將目標和日期以物件形式寫入 Firebase
        reference1.push().setValue(target);
    }*/


}

        // 获取当前用户的 UID
        /*String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 获取 Firebase 实时数据库引用
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("your_data").child(uid);

        // 写入数据
        databaseReference.child("title").setValue(title);
        databaseReference.child("time").setValue(time);*/
