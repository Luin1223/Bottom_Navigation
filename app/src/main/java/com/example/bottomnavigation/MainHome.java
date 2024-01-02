package com.example.bottomnavigation;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
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
import android.text.TextUtils;
import android.util.Log;
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

import com.example.bottomnavigation.HomeFragment;
import com.example.bottomnavigation.LibraryFragment;
import com.example.bottomnavigation.R;
import com.example.bottomnavigation.Store;
import com.example.bottomnavigation.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 定義 MainHome 類別，擴展 AppCompatActivity
public class MainHome extends AppCompatActivity {

    // 宣告 FloatingActionButton、DrawerLayout、BottomNavigationView 等相關元件
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    DatabaseReference reference;

    // 在活動創建時調用的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設置畫面佈局
        setContentView(R.layout.activity_main);

        // 初始化元件
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        fab=findViewById(R.id.tab);

        // 將 HomeFragment 替換為初始顯示的 Fragment
        replaceFragment(new HomeFragment());

        // 設置 BottomNavigationView 的選擇監聽器
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // 根據選擇的項目替換對應的 Fragment
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.nav_settings) {
                replaceFragment(new LibraryFragment());
            }
            return true;
        });

        // 設置 FloatingActionButton 的點擊監聽器
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 顯示底部對話框
                showBottomDialog();
            }
        });
    }

    // 選項菜單項目選擇時的回調方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                // 打開抽屜
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 替換 Fragment 的方法
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // 顯示底部對話框的方法
    private void showBottomDialog() {
        // 創建對話框
        final  Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottonsheetlayout);

        // 初始化元件
        LinearLayout addnewtask = dialog.findViewById(R.id.Addnewtask);
        ImageView cancelButton = dialog.findViewById(R.id.cancelbtn);

        // 設置「新增任務」的點擊監聽器
        addnewtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更改對話框的內容為新增任務的佈局
                dialog.setContentView(R.layout.addtask);

                // 初始化元件
                ImageView cancelbtn = dialog.findViewById(R.id.cancelbtn);
                TextView create = dialog.findViewById(R.id.createText);
                EditText Title = dialog.findViewById(R.id.edt_title);

                // 設置「創建」按鈕的點擊監聽器
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 獲取任務標題
                        String title = Title.getText().toString();

                        // 检查输入是否为空
                        if (!TextUtils.isEmpty(title)) {
                            // 输入不为空，执行操作

                            // 將任務標題寫入 Firebase 數據庫
                            writeToFirebase(title);

                            // 關閉對話框
                            dialog.dismiss();
                        } else {
                            // 输入为空，顯示錯誤消息
                            Title.setError("請輸入任務");
                        }
                    }
                });

                // 設置「取消」按鈕的點擊監聽器
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 關閉對話框
                        dialog.dismiss();
                    }
                });

            }
        });

        // 設置「取消」按鈕的點擊監聽器
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 關閉對話框
                dialog.dismiss();
            }
        });

        // 顯示對話框
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    // 寫入數據到 Firebase 數據庫的方法
    private void writeToFirebase(String title) {
        // 獲取 Firebase 數據庫實例
        database = FirebaseDatabase.getInstance();

        // 獲取當前用戶的帳戶信息
        user user = com.example.bottomnavigation.user.getInstance();
        String account = user.getAccount();

        // 拼接用戶特定節點的路徑
        String userTasksPath = "users/" + account + "/tasks";

        // 獲取對應路徑的數據庫引用
        reference = database.getReference(userTasksPath);

        // 創建一個 Store 對象，Store 對象可能是你自定義的一個類，用於封裝任務的相關信息
        Store store = new Store(title);

        // 在用戶特定的 "tasks" 節點下創建子節點，節點的名稱為生成的唯一任務 ID，將 store 對象寫入該節點
        reference.child(title).setValue(store);
    }
}


