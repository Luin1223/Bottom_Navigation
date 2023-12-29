package com.example.bottomnavigation;

import static java.util.Locale.filter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import static java.util.Locale.filter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayAdapter<String> adapter,adapter1;
    private List<String> dataList = new ArrayList<>();
    private List<String> getDataList = new ArrayList<>();
    private DatabaseReference databaseReference,databaseReference1;

    SearchView searchView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.task_list_view);
        //ListView listView1 = view.findViewById(R.id.goal_list_view);

        searchView = view.findViewById(R.id.searchView);
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
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });



        // 初始化适配器
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        //adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,getDataList);

        // 设置适配器到 ListView
        listView.setAdapter(adapter);
        //listView1.setAdapter(adapter1);

        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 获取 Firebase 实时数据库引用
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tasks");
        //databaseReference1 = FirebaseDatabase.getInstance().getReference().child("goals");

        // 添加数据变化监听器
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 清空数据列表
                dataList.clear();

                // 遍历数据快照，将 title 添加到 dataList
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    if (title != null) {
                        dataList.add(title);
                    }
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 获取点击的项的数据
                        String selectedItem = dataList.get(position);

                        // 弹出确认删除对话框
                        showDeleteConfirmationDialog(selectedItem);
                    }
                });

                // 更新适配器
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });

        /*databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getDataList.clear();

                for (DataSnapshot goalSnapshot : snapshot.getChildren()) {
                    // 從子節點中獲取 goal 和 date
                    String goal = goalSnapshot.child("goal").getValue(String.class);
                    String date = goalSnapshot.child("date").getValue(String.class);

                    // 檢查 goal 和 date 是否為 null，然後將它們添加到列表中
                    if (goal != null && date != null) {
                        getDataList.add("目標：" + goal + "，日期：" + date);
                    }
                }

                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 處理取消事件
            }
        });*/




        return view;
    }

    private void resetTaskList() {
        // 清空过滤后的列表
        adapter.clear();

        // 重新加载所有的任务数据
        adapter.addAll(dataList);

        // 通知适配器数据已更改
        adapter.notifyDataSetChanged();


        // 刷新页面
        refreshPage();
    }

    private void refreshPage() {
        // 在这里执行页面刷新逻辑，例如重新加载数据或更新UI
        // 你可能需要调用适当的方法或重新获取数据
        // ...

        // 示例：重新加载数据
        loadData();
    }

    private void loadData() {
        // 重新从数据库获取数据
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 清空数据列表
                dataList.clear();

                // 遍历数据快照，将 title 添加到 dataList
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    if (title != null) {
                        dataList.add(title);
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


    private void filter(String query) {
        List<String> filteredList = new ArrayList<>();

        for (String task : dataList) {
            if (task.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(task);
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    // 在 HomeFragment 中添加一个方法用于显示确认删除对话框
    private void showDeleteConfirmationDialog(final String selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否要刪除 '" + selectedItem + "'？");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了确定按钮，执行删除操作
                deleteItem(selectedItem);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了取消按钮，关闭对话框
                dialog.dismiss();
            }
        });
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 在 HomeFragment 中添加一个方法用于执行删除操作
    private void deleteItem(String selectedItem) {

        dataList.remove(selectedItem);

        adapter.notifyDataSetChanged();

        Toast.makeText(requireContext(), "刪除成功", Toast.LENGTH_SHORT).show();

        // 同步执行 Firebase 数据库删除操作
        //deleteItemFromFirebase(selectedItem);
    }

    // 在 HomeFragment 中添加一个方法用于执行 Firebase 数据库删除操作
    /*private void deleteItemFromFirebase(final String selectedItem) {
        // 获取当前用户的 UID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 获取 Firebase 实时数据库引用
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("tasks").child(uid);

        // 查询要删除的数据
        databaseReference.orderByChild("title").equalTo(selectedItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 遍历查询结果
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 删除相应的数据
                    snapshot.getRef().removeValue();
                }

                // 删除完成后更新适配器
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });
    }*/



}
