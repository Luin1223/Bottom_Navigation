package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText signupEmail, signupPassword;
    Button signupbutton;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupbutton = findViewById(R.id.button);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (!validateUseremail() | !validateUserpassword()) {

                    } else {
                        //获取到 Firebase 数据库的实例。
                        database = FirebaseDatabase.getInstance();
                        //获取到数据库中的一个引用，这个引用指向名为 "users" 的节点。
                        reference = database.getReference("users");

                        String email = signupEmail.getText().toString();
                        String password = signupPassword.getText().toString();

                        HelperClass helperClass = new HelperClass(email, password);
                        /*
                        在 "users" 节点下的一个子节点，该子节点的名称是用户的电子邮件地址（email）。
                        将 helperClass 对象的数据写入到这个子节点中。
                         */
                        reference.child(email).setValue(helperClass);

                        /*reference.child(email).setValue(helperClass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "成功註冊!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "註冊失敗: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });*/




                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(SignUpActivity.this, "帳號重複，請嘗試其他組合", Toast.LENGTH_SHORT).show();
                                } else {
                                    HelperClass helperClass = new HelperClass(email, password);
                                    reference.child(email).setValue(helperClass);

                                    Toast.makeText(SignUpActivity.this, "註冊成功!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }


        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

}
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
/*HelperClass helperClass = new HelperClass(email,password);
                reference.child(email).setValue(helperClass);

                Toast.makeText(SignUpActivity.this,"成功註冊!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);*/