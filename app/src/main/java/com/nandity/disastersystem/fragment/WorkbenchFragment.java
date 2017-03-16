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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.CompleteTaskActivity;
import com.nandity.disastersystem.activity.CreateTaskActivity;
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.activity.MyTaskActivity;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

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
    private SharedPreferences sp;
    private String sessionId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view=inflater.inflate(R.layout.fragment_workbench, container, false);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId","");
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
        Log.d("WorkbenchFragment","sessionId:"+sessionId);
        OkHttpUtils.get().url(new ConnectUrl().getTaskNumUrl())
                .addParams("sessionId",sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status,msg;
                        try {
                            JSONObject object=new JSONObject(response);
                            status=object.getString("status");
                            msg=object.getString("message");
                            Log.d("WorkbenchFragment","获取任务数量的status:"+status);
                            Log.d("WorkbenchFragment","获取任务数量的msg:"+status);
                            if("200".equals(status)){
                                JSONArray array=object.getJSONArray("message");
                                String myTask = array.getJSONObject(0).getString("myTask");
                                String allTask = array.getJSONObject(1).getString("allTask");
                                Log.d("WorkbenchFragment","我的任务数量："+myTask);
                                Log.d("WorkbenchFragment","完成任务数量："+allTask);
                                tvMyTask.setText("任务数量：" + myTask + "个");
                                tvMyTaskNum.setText(myTask);
                                tvCompleteTask.setText("任务数量：" + allTask + "个");
                                tvCompleteTaskNum.setText(allTask);
                            }else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin",false).apply();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        myToolBar.setTitle("");
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
