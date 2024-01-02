// 引入相應的類別和套件
package com.example.bottomnavigation;

// import 必要的 Android 程式庫
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 定義 SignUpActivity 類別，擴展 AppCompatActivity
public class SignUpActivity extends AppCompatActivity {

    // 宣告相關元件
    EditText signupEmail, signupPassword;
    Button signupbutton;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;

    String account;

    // 在創建活動時調用的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 設置畫面佈局
        setContentView(R.layout.activity_sign_up);

        // 初始化元件
        signupbutton = findViewById(R.id.button);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // 設定按鈕點擊監聽器
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 檢查用戶輸入的電子郵件和密碼是否有效
                if (!validateUseremail() | !validateUserpassword()) {
                    // 如果無效，不執行後續操作
                } else {
                    // 獲取 Firebase 數據庫的實例
                    database = FirebaseDatabase.getInstance();
                    // 獲取到數據庫中的一個引用，這個引用指向名為 "users" 的節點
                    reference = database.getReference("users");

                    // 獲取用戶輸入的電子郵件和密碼
                    String email = signupEmail.getText().toString();
                    String password = signupPassword.getText().toString();

                    // 創建 HelperClass 對象
                    HelperClass helperClass = new HelperClass(email, password);

                    // 獲取 user 物件實例
                    user user = com.example.bottomnavigation.user.getInstance();

                    // 設置使用者帳號
                    user.setAccount(email);
                    account = user.getAccount();

                    // 記錄使用者帳號
                    Log.d("SignUpActivity", "Account: " + account);

                    // 使用 orderByChild("email").equalTo(email) 方法檢查是否已經存在相同電子郵件的用戶
                    reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // 如果存在相同電子郵件的用戶，顯示帳號重複的訊息
                                Log.d("SignUpActivity", "帳號重複，請嘗試其他組合");
                                Toast.makeText(SignUpActivity.this, "帳號重複，請嘗試其他組合", Toast.LENGTH_LONG).show();
                            } else {
                                // 如果不存在相同電子郵件的用戶，將新用戶的資訊添加到 Firebase 數據庫中
                                reference.child(account).setValue(helperClass);
                                Log.d("SignUpActivity", "註冊成功!");
                                Toast.makeText(SignUpActivity.this, "註冊成功!", Toast.LENGTH_LONG).show();
                                // 跳轉到登錄活動
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);

                                // 結束當前活動
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 處理取消事件
                            Log.e("SignUpActivity", "Firebase Database Error: " + error.getMessage());
                            Toast.makeText(SignUpActivity.this, "Firebase Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // 設定登錄文本點擊監聽器
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 點擊時跳轉到登錄活動
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    // 用於檢查用戶輸入的電子郵件是否有效的方法
    public Boolean validateUseremail() {
        String val = signupEmail.getText().toString();
        if (val.isEmpty()) {
            signupEmail.setError("不能為空");
            return false;
        } else {
            signupEmail.setError(null);
            return true;
        }
    }

    // 用於檢查用戶輸入的密碼是否有效的方法
    public Boolean validateUserpassword() {
        String val = signupPassword.getText().toString();
        if (val.isEmpty()) {
            signupPassword.setError("不能為空");
            return false;
        } else {
            signupPassword.setError(null);
            return true;
        }
    }
}
