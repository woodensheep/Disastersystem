package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.MyTaskAdapter;
import com.nandity.disastersystem.bean.Category;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

import static android.R.attr.name;
import static com.nandity.disastersystem.app.MyApplication.getContext;

public class MyTaskActivity extends AppCompatActivity {
    private String TAG="MyTaskActivity";
    private List<TaskInfoBean> mListData;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView mMyTaskRecyclerview;
    private LinearLayoutManager mLayoutManager;
    private MyTaskAdapter mAdapter;
    private String page;
    private String rows;
    private String sessionId;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        page=1+"";
        rows=6+"";
        sessionId=sp.getString("sessionId", "");
        toolbar = (Toolbar) findViewById(R.id.my_task_toolbar);
        tabLayout= (TabLayout) findViewById(R.id.my_task_tablayout);
        mMyTaskRecyclerview= (RecyclerView) findViewById(R.id.my_task_recyclerview);
        mListData = new ArrayList<TaskInfoBean>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mMyTaskRecyclerview.setLayoutManager(mLayoutManager);
        //固定高度
        mMyTaskRecyclerview.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new MyTaskAdapter(getContext(), mListData);
        mMyTaskRecyclerview.setAdapter(mAdapter);
        initView();
        initDatas();
        setListener();
    }

    private void initDatas() {
        mListData.clear();

        for (int i=0;i<7;i++) {
            TaskInfoBean taskInfoBean = new TaskInfoBean();
            taskInfoBean.setmRowNumber("2016第XX号"+i);
            taskInfoBean.setmDisaster("XXX滑坡");
            taskInfoBean.setmAddress("XX村XX组");
            mListData.add(taskInfoBean);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        setOkHttp();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void initView() {
        tabLayout.addTab(tabLayout.newTab().setText("未处理(0)"));
        tabLayout.addTab(tabLayout.newTab().setText("已上传(0)"));
        tabLayout.addTab(tabLayout.newTab().setText("未上传(0)"));
    }


    private void setOkHttp() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getStartTaskUrl())
                    .addParams("page", page)
                    .addParams("rows", rows)
                    .addParams("sessionId",sessionId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast("网络故障，请检查网络！");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String msg, status;
                            Log.d(TAG, "登录返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                } else {
                                    ToastUtils.showShortToast(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            //ToastUtils.showShortToast("登录失败，请检查IP是否设置！");
        }
    }
}
