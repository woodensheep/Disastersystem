package com.nandity.disastersystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.CompleteTaskActivity;
import com.nandity.disastersystem.activity.CreateTaskActivity;
import com.nandity.disastersystem.activity.MyTaskActivity;

/**
 * Created by lemon on 2017/2/22.
 */

public class WorkbenchFragment extends Fragment implements View.OnClickListener{
    private Toolbar myToolBar;
    private CardView myTask,completeTask,createTask;
    private Activity activity;
    private Intent intent;
    private TextView tvMyTask;
    private TextView tvCompleteTask;
    private TextView tvMyTaskNum;
    private TextView tvCompleteTaskNum;
    /*我的任务数量*/
    private String mNUM1=0+"";
    /*调查信息任务数量*/
    private String mNUM2=0+"";
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view=inflater.inflate(R.layout.fragment_workbench, container, false);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        initView(view);
        setData();
        setListener();
        ((AppCompatActivity)activity).setSupportActionBar(myToolBar);
        return view;
    }

    private void setListener() {
        myTask.setOnClickListener(this);
        completeTask.setOnClickListener(this);
        createTask.setOnClickListener(this);
    }

    private void setData() {

        mNUM1=sp.getString("myTaskNum", "*");
        mNUM2=sp.getString("allTaskNum", "*");
        myToolBar.setTitle("");
        tvMyTask.setText("任务数量："+mNUM1+"个");
        tvMyTaskNum.setText(mNUM1);
        tvCompleteTask.setText("任务数量："+mNUM2+"个");
        tvCompleteTaskNum.setText(mNUM2);
    }

    private void initView(View view) {
        myToolBar= (Toolbar) view.findViewById(R.id.my_toolbar);
        myTask= (CardView) view.findViewById(R.id.my_task);
        completeTask= (CardView) view.findViewById(R.id.com_task);
        createTask= (CardView) view.findViewById(R.id.create_task);
        tvMyTask= (TextView) view.findViewById(R.id.tv_my_task);
        tvMyTaskNum= (TextView) view.findViewById(R.id.tv_my_task_num);
        tvCompleteTask= (TextView) view.findViewById(R.id.tv_com_task);
        tvCompleteTaskNum= (TextView) view.findViewById(R.id.tv_com_task_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_task://打开我的任务界面
                intent=new Intent(activity, MyTaskActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.com_task://打开已完成任务界面
                intent=new Intent(activity, CompleteTaskActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.create_task://打开创建任务界面
                intent=new Intent(activity, CreateTaskActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}
