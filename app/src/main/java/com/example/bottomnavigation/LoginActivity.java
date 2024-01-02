package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginbutton;
    TextView signupRedirectText;

    String account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化 UI 元件
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginbutton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // 設置按鈕點擊事件監聽器
        loginbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 檢查使用者輸入的郵件和密碼是否有效
                if (!validateUseremail() | !validateUserpassword()) {
                    // 如果無效，不執行後續操作
                } else {
                    // 如果有效，進行使用者檢查
                    checkUser();
                }
            }
        });

        // 設置註冊連結的點擊事件監聽器
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動註冊活動
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    // 驗證使用者輸入的郵件是否有效
    public Boolean validateUseremail() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            // 如果為空，顯示錯誤提示並返回 false
            loginEmail.setError("不能為空");
            return false;
        } else {
            // 如果有效，清除錯誤提示並返回 true
            loginEmail.setError(null);
            return true;
        }
    }

    // 驗證使用者輸入的密碼是否有效
    public Boolean validateUserpassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            // 如果為空，顯示錯誤提示並返回 false
            loginPassword.setError("不能為空");
            return false;
        } else {
            // 如果有效，清除錯誤提示並返回 true
            loginPassword.setError(null);
            return true;
        }
    }

    // 檢查使用者是否存在並檢查密碼是否正確
    public void checkUser() {
        // 獲取使用者輸入的郵件和密碼
        String userUseremail = loginEmail.getText().toString().trim();
        String userpassword = loginPassword.getText().toString().trim();

        // 獲取 user 物件實例
        user user = com.example.bottomnavigation.user.getInstance();

        // 設置使用者帳號
        user.setAccount(userUseremail);
        account = user.getAccount();

        // 記錄使用者帳號
        Log.d("SignUpActivity", "Account: " + account);

        // 獲取 Firebase 數據庫的參考
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // 使用 Query 查詢使用者資料
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUseremail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 檢查資料庫中是否存在該使用者
                if (snapshot.exists()) {
                    // 清除郵件輸入框的錯誤提示
                    loginEmail.setError(null);

                    // 從資料庫中獲取密碼
                    String passwordFromDB = snapshot.child(userUseremail).child("password").getValue(String.class);

                    // 檢查密碼是否正確
                    if (Objects.equals(passwordFromDB, userpassword)) {
                        // 清除郵件輸入框的錯誤提示
                        loginEmail.setError(null);

                        // 若正確，啟動主頁面
                        Intent intent = new Intent(LoginActivity.this, MainHome.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 若密碼錯誤，顯示密碼錯誤提示
                        loginPassword.setError("密碼錯誤");
                        loginPassword.requestFocus();
                    }
                } else {
                    // 若使用者不存在，顯示使用者錯誤提示
                    loginEmail.setError("使用者錯誤");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 處理取消事件
            }
        });
    }
}


