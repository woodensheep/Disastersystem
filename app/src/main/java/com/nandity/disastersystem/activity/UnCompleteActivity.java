package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.TaskBean;
import com.nandity.disastersystem.database.TaskBeanDao;
import com.nandity.disastersystem.utils.DateTimePickUtil;
import com.nandity.disastersystem.utils.MyUtils;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;
import static com.nandity.disastersystem.app.MyApplication.getDaoSession;

public class UnCompleteActivity extends AppCompatActivity {
    private String TAG="UnCompleteActivity";
    private Toolbar comTaskToolbar;
    private List<String> mDisasterList;
    private ArrayAdapter<String> mDisasterAdapter;

    private List<String> mTownshipList;
    private ArrayAdapter<String> mTownshipAdapter;

    private List<String> mWorkersList;
    private ArrayAdapter<String> mWorkersAdapter;
    /*灾害发生时间*/
    private TextView tvHanppenTime;
    /* 任务发起人*/
    private TextView tvName;
    /*调查时间*/
    private TextView tvNewtaskTime;
    /*灾害点*/
    private TextView tvDisaster;
    /*调查地点*/
    private EditText etNewtaskDisaster;
    /*乡镇*/
    private Spinner spTownship;
    /*部门*/
    //private Spinner spDepartment;
    /*人员*/
    private Spinner spWorkers;

    private Button btnSave;
    private Button btnSubmit;
    private Button btnCancle;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private String sessionId;
    private Long mTaskBeanId;
    private TaskBean taskBean;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newtask);
        mTaskBeanId=getIntent().getLongExtra("taskbean_id",-1);
        Log.d("UnCompleteActivity",""+mTaskBeanId);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId", "");
        userName=sp.getString("userName","");
        progressDialog = new ProgressDialog(getApplicationContext(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
        initViews();
        setLinsteners();
        initData();
        setViewDataAll();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b=data.getExtras(); //data为B中回传的Intent
                String str1=b.getString("str1","");//disasterID
                String str2=b.getString("str2","");//disaster
                tvDisaster.setText(str2);
                taskBean.setMDisasterID(str1);
                break;
            default:
                break;
        }
    }

    private void setLinsteners() {
        tvDisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), QueryDisasterActivity.class);
                startActivityForResult(intent,0);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskBean taskBean=new TaskBean();
                taskBean.setId((long)mTaskBeanId);
                taskBean.setMTime(MyUtils.getSystemTime());
                taskBean.setMDisaster(tvDisaster.getText().toString().trim());
                taskBean.setMAddress(etNewtaskDisaster.getText().toString().trim());
                taskBean.setMTownship(spTownship.getSelectedItem().toString().trim());
                taskBean.setMWorkers(spWorkers.getSelectedItem().toString().trim());
                getDaoSession().getTaskBeanDao().update(taskBean);
                Toast.makeText(UnCompleteActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                cleanAll();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDaoSession().getTaskBeanDao().deleteByKey((long)mTaskBeanId);
                Toast.makeText(UnCompleteActivity.this,"刪除成功",Toast.LENGTH_SHORT).show();
                finish();
            }


        });

        tvNewtaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickUtil dateTimePicKDialog = new DateTimePickUtil(
                        UnCompleteActivity.this,new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                dateTimePicKDialog.dateTimePicKDialog(tvNewtaskTime);
                taskBean.setMTime(tvNewtaskTime.getText().toString().trim());
            }
        });

        tvHanppenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickUtil dateTimePicKDialog = new DateTimePickUtil(
                        UnCompleteActivity.this,new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                dateTimePicKDialog.dateTimePicKDialog(tvHanppenTime);
                taskBean.setMHappenTime(tvHanppenTime.getText().toString().trim());
            }
        });


    }


    /**
     * 设置数据库存的数据
     */
    private void setViewDataAll() {
        TaskBean taskBean=MyApplication.getDaoSession().getTaskBeanDao().queryBuilder()
                .where(TaskBeanDao.Properties.Id.eq(mTaskBeanId)).build().list().get(0);
        tvNewtaskTime.setText(taskBean.getMTime());
        tvHanppenTime.setText(taskBean.getMHappenTime());
        // 设置灾害点选中
        tvDisaster.setText(taskBean.getMDisaster());

        // 设置调查地点选中
        etNewtaskDisaster.setText(taskBean.getMAddress());


        // 设置乡镇选中
        for(int i=0;i<mTownshipAdapter.getCount();i++){
            String string=mTownshipAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMDisaster())){
                spTownship.setSelection(i,true);
                break;
            }
        }

        // 设置人员选中
        for(int i=0;i<mWorkersAdapter.getCount();i++){
            String string=mWorkersAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMDisaster())){
                spWorkers.setSelection(i,true);
                break;
            }
        }

    }

    private void cleanAll() {
        tvNewtaskTime.setText(MyUtils.getSystemTime());
        tvDisaster.setText("");
        etNewtaskDisaster.setText("");
        spTownship.setSelection(0,true);
        spWorkers.setSelection(0,true);
    }

    private void initViews() {
        comTaskToolbar= (Toolbar) findViewById(R.id.my_toolbar);
        comTaskToolbar.setVisibility(View.VISIBLE);
        tvNewtaskTime = (TextView) findViewById(R.id.et_newtask_time);
        tvDisaster = (TextView) findViewById(R.id.tv_disaster);
        spTownship = (Spinner) findViewById(R.id.sp_township);
        spWorkers = (Spinner) findViewById(R.id.sp_workers);
        etNewtaskDisaster = (EditText) findViewById(R.id.et_newtask_disaster);
        btnSave = (Button) findViewById(R.id.btn_newtask_save);
        btnSubmit = (Button) findViewById(R.id.btn_newtask_submit);
        btnCancle = (Button) findViewById(R.id.btn_newtask_cancle);
    }


    /**
     * 设置spinner等从网络获取的选项有哪些
     */
    private void initData() {

        tvNewtaskTime.setText(MyUtils.getSystemTime());
        tvName.setText(userName);
        //灾害点
        //数据

        //乡镇
        mTownshipList = new ArrayList<String>();
        mTownshipList.add("请选择乡镇");
        mTownshipAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTownshipList);
        mTownshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTownship.setAdapter(mTownshipAdapter);



        //人员
        mWorkersList = new ArrayList<String>();
        mWorkersList.add("请选择人员");
        mWorkersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mWorkersList);
        mWorkersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkers.setAdapter(mWorkersAdapter);

    }


    private void setOkHttp() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getAreaListUrl())
                    .addParams("sessionId",sessionId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            ToastUtils.showShortToast("网络故障，请检查网络！");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            String msg, status;
                            Log.d(TAG, "返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    mTownshipList.clear();
                                    mTownshipList.add("请选择乡镇");
                                    JSONArray message=object.getJSONArray("message");
                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        mTownshipList.add(oj.getString("area_name"));
                                    }
                                    mTownshipAdapter.notifyDataSetChanged();

                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            ToastUtils.showShortToast("加载失败！");
        }

        try {
            OkHttpUtils.get().url(new ConnectUrl().getPersonListUrl())
                    .addParams("sessionId",sessionId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            ToastUtils.showShortToast("网络故障，请检查网络！");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            String msg, status;
                            Log.d(TAG, "返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    mWorkersList.clear();
                                    mWorkersList.add("请选择人员");
                                    JSONArray message=object.getJSONArray("message");
                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        mWorkersList.add(oj.getString("name"));
                                    }
                                    mWorkersAdapter.notifyDataSetChanged();

                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            ToastUtils.showShortToast("加载失败！");
        }

    }
}
