package com.example.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// 在你的适配器中
public class CustomAdapter extends ArrayAdapter<String> {

    public static List<String> removedDataList;
    private Context context;
    private List<String> dataList;

    public CustomAdapter(Context context, int resource, List<String> dataList) {
        super(context, resource, dataList);
        this.context = context;
        this.dataList = dataList;
        this.removedDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 获取当前项的数据
        String currentItem = dataList.get(position);

        // 如果convertView为null，表示这是第一次加载该项，需要创建布局
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_with_checkbox, parent, false);
        }

        // 获取CheckBox，并设置文本
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        // 设置CheckBox的文本
        checkBox.setText(currentItem);
        checkBox.setTextColor(Color.DKGRAY);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理CheckBox点击事件
                checkBox.setChecked(!checkBox.isChecked());
                notifyDataSetChanged();
                handleCheckBoxClick(currentItem);
            }
        });

        return convertView;
    }

    private void handleCheckBoxClick(String selectedItem) {
        // 获取 Firebase 实时数据库引用
        DatabaseReference tasksReference = FirebaseDatabase.getInstance().getReference().child("tasks");
        // 将被移除的数据添加到removedDataList中
        removedDataList.add(selectedItem);

        // 更新适配器
        notifyDataSetChanged();

        // 查询包含指定 title 的子节点
        tasksReference.orderByChild("title").equalTo(selectedItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 遍历查询结果
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 获取子节点的 key，即 {dynamicValue}
                    String dynamicValue = snapshot.getKey();

                    // 构造删除节点的路径
                    String pathToDelete = "tasks/" + dynamicValue + "/title";

                    // 获取对应路径的数据库引用
                    DatabaseReference taskTitleReference = FirebaseDatabase.getInstance().getReference().child(pathToDelete);

                    // 删除相应的数据
                    taskTitleReference.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });

        // 在这里可以将被移除的数据传递到另一个地方，例如调用方法传递给另一个组件
        // 或者通过接口回调等方式传递数据
        sendDataToAnotherPlace(selectedItem);
    }




    private void sendDataToAnotherPlace(String data) {
        // 在这里实现将数据传递到另一个地方的逻辑
        // 例如，调用接口回调或通过广播等方式

        // 将数据添加到 "finish" 节点
        DatabaseReference finishReference = FirebaseDatabase.getInstance().getReference().child("finish");
        finishReference.push().setValue(data);
    }

    // 添加一个方法，用于获取被移除的数据列表
    public List<String> getRemovedDataList() {
        return removedDataList;
    }
}



