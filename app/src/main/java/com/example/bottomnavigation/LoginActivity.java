package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginbutton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUseremail() | !validateUserpassword()) {

                } else {
                    checkUser();
                }

            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    public Boolean validateUseremail() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            loginEmail.setError("不能為空");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    public Boolean validateUserpassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("不能為空");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUseremail = loginEmail.getText().toString().trim();
        String userpassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUseremail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginEmail.setError(null);
                    String passwordFromDB = snapshot.child(userUseremail).child("password").getValue(String.class);


                    if (Objects.equals(passwordFromDB, userpassword)) {

                        loginEmail.setError(null);
                        Intent intent = new Intent(LoginActivity.this,MainHome.class);
                        startActivity(intent);

                        /*Intent intent1 = new Intent(getApplicationContext(),Settings.class);
                        String user = loginEmail.getText().toString();
                        intent1.putExtra("USER",user);
                        startActivity(intent1);*/

                        finish();
                        /*Bundle bundle = new Bundle();
                        bundle.putString("username", userUseremail);

                        Intent intent = new Intent(LoginActivity.this, MainHome.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        finish();*/
                    } else {
                        loginPassword.setError("密碼錯誤");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginEmail.setError("使用者錯誤");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

