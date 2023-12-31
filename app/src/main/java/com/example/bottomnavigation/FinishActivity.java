package com.example.bottomnavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FinishActivity extends AppCompatActivity{

    private TextView textView;

    private ListView listView;

    List<String> removedDataList;

    private SearchView searchView;
    ArrayAdapter<String> adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        textView = findViewById(R.id.finish);
        listView = findViewById(R.id.finish_tasks);
        searchView = findViewById(R.id.searchview);

        removedDataList = CustomAdapter.removedDataList;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("finish");

        // 创建适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, removedDataList);

        // 设置适配器到 ListView
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的项的数据
                String selectedItem = removedDataList.get(position);

                // 弹出确认删除对话框，或者直接删除
                showDeleteConfirmationDialog(selectedItem);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 清空数据列表
                removedDataList.clear();

                // 遍历数据快照的子节点
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 获取子节点的值，即 finish 节点的内容
                    String finishValue = snapshot.getValue(String.class);

                    if (finishValue != null) {
                        // 添加到 dataList 中
                        removedDataList.add(finishValue);
                    }
                }

                // 更新适配器
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 在這裡處理提交搜索的事件
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 在這裡處理搜索框文字變化的事件
                if (TextUtils.isEmpty(newText)) {
                    // 如果搜索框中的文字為空，顯示所有的任務
                    resetTaskList();
                } else {
                    // 否則，執行過濾邏輯
                    filter(newText);
                }
                return true;
            }
        });

        // 將 setOnCloseListener 設置為在點擊叉叉圖標時不顯示鍵盤
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // 隱藏鍵盤
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return false;
            }
        });

        // 將 setOnQueryTextFocusChangeListener 設置為顯示搜索框時就彈出鍵盤
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // 彈出鍵盤
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

    }

    private void showDeleteConfirmationDialog(String selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("確認刪除");
        builder.setMessage("確定要刪除這個任務嗎?");
        builder.setIcon(R.drawable.baseline_delete_24);

        // 添加确认按钮
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击确定，执行删除逻辑
                handleCheckBoxClick(selectedItem);
            }
        });

        // 添加取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消，关闭对话框
                dialog.dismiss();
            }
        });

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleCheckBoxClick(String selectedItem) {
        // 获取 Firebase 实时数据库引用
        DatabaseReference tasksReference = FirebaseDatabase.getInstance().getReference().child("finish");

        // 从 removedDataList 中移除被点击的项
        removedDataList.remove(selectedItem);

        // 更新适配器
        adapter.notifyDataSetChanged();

        // 删除 Firebase 中相应的节点
        tasksReference.orderByValue().equalTo(selectedItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 遍历查询结果
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 删除相应的数据
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });
    }


    private void filter(String query) {
        List<String> filteredList = new ArrayList<>();

        for (String finish : removedDataList) {
            if (finish.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(finish);
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
    }

    private void resetTaskList() {
        // 清空过滤后的列表
        adapter.clear();

        // 重新加载所有的任务数据
        adapter.addAll(removedDataList);

        // 通知适配器数据已更改
        adapter.notifyDataSetChanged();

        // 刷新页面
        loadData();
    }

    private void loadData() {
        // 重新从数据库获取数据
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 清空数据列表
                removedDataList.clear();

                // 遍历数据快照的子节点
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 获取子节点的值，即 finish 节点的内容
                    String finishValue = snapshot.getValue(String.class);

                    if (finishValue != null) {
                        // 添加到 dataList 中
                        removedDataList.add(finishValue);
                    }
                }

                // 更新适配器
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });
    }


}
