package com.example.ch12_ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText edt1,edt2;

    private Button btnAdd,btnClear,btnEnd;

    private static final String FILENAME="phone.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1=findViewById(R.id.edt1);
        edt2=findViewById(R.id.edt2);
        btnAdd=findViewById(R.id.btn1);
        btnClear=findViewById(R.id.btn2);
        btnEnd=findViewById(R.id.btn3);

        DisplayFile(FILENAME);
        btnAdd.setOnClickListener(listener);
        btnClear.setOnClickListener(listener);
        btnEnd.setOnClickListener(listener);
    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1:
                    if(edt1.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"電話號碼必須輸入!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    FileOutputStream fout=null;
                    BufferedOutputStream buffout=null;
                    try{
                        fout=openFileOutput(FILENAME,MODE_APPEND);
                        buffout=new BufferedOutputStream(fout);
                        buffout.write(edt1.getText().toString().getBytes());
                        buffout.write("\n".getBytes());
                        buffout.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    DisplayFile(FILENAME);
                    edt1.setText("");
                    break;
                case R.id.btn2:
                    try{
                        fout=openFileOutput(FILENAME,MODE_PRIVATE);
                        fout.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    DisplayFile(FILENAME);
                    break;
                case R.id.btn3:
                    finish();
            }
        }
    };

    private  void DisplayFile(String fname){
        FileInputStream fin=null;
        BufferedInputStream buffin=null;
        try{
            fin=openFileInput(fname);
            buffin=new BufferedInputStream(fin);
            byte[] buffbyte=new byte[20];
            edt2.setText("");
            do{
                int flag=buffin.read(buffbyte);
                if(flag==-1) break;
                else
                    edt2.append(new String(buffbyte),0,flag);
            }while(true);
            buffin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}