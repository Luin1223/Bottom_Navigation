package com.example.bottomnavigation;

import static java.util.Locale.filter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import static java.util.Locale.filter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<String> getDataList = new ArrayList<>();
    private DatabaseReference databaseReference;
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.task_list_view);

        Button button = view.findViewById(R.id.finish_button);

        searchView = view.findViewById(R.id.searchview);

        FloatingActionButton fab = view.findViewById(R.id.fab_revise);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {showRegularDialog();}

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),FinishActivity.class);
                startActivity(intent);
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
        adapter = new CustomAdapter(getContext(), android.R.layout.simple_list_item_multiple_choice, dataList);

        // 设置适配器到 ListView
        listView.setAdapter(adapter);

        // 获取 Firebase 实时数据库引用
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tasks");

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

                // 更新适配器
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });

        return view;
    }

    public void showRegularDialog() {
        // 创建对话框
        Dialog dialog = new Dialog(requireContext());

        // 设置对话框的布局
        dialog.setContentView(R.layout.task_revise);

        // 获取对话框中的控件
        EditText edtoldTask = dialog.findViewById(R.id.edt);
        EditText edtnewTask = dialog.findViewById(R.id.edt1);
        Button button = dialog.findViewById(R.id.revise_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取编辑的任务
                String old = edtoldTask.getText().toString().trim();
                String newedt = edtnewTask.getText().toString().trim();

                // 输入验证
                if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newedt)) {
                    // 提示用户输入有效数据
                    edtnewTask.setError("不能為空");
                    edtoldTask.setError("不能為空");
                } else {
                    // 在这里执行保存逻辑，比如写入数据库等
                    updateTaskInFirebase(old, newedt);
                    // 关闭对话框
                    dialog.dismiss();
                }
            }
        });

        // 设置对话框中的取消按钮点击事件
        ImageView cancelbtn = dialog.findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭对话框
                dialog.dismiss();
            }
        });

        // 显示对话框
        dialog.show();
    }
    private void updateTaskInFirebase(String oldTask, String newTask) {
        DatabaseReference tasksReference = FirebaseDatabase.getInstance().getReference().child("tasks");

        // 查询与旧任务相符的数据
        tasksReference.orderByChild("title").equalTo(oldTask).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    // 获取匹配的任务节点的 key
                    String taskKey = taskSnapshot.getKey();

                    // 构建新的任务数据
                    Map<String, Object> updatedTask = new HashMap<>();
                    updatedTask.put("title", newTask);

                    // 更新到 Firebase 数据库
                    tasksReference.child(taskKey).setValue(updatedTask);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 处理取消事件
            }
        });
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



}
