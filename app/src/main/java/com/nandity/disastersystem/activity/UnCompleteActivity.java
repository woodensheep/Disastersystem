package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.CItem;
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

public class UnCompleteActivity extends AppCompatActivity {
    private String TAG="UnCompleteActivity";
    private List<String> mTownshipList;
    List<CItem> CITownship = new ArrayList<CItem>();
    private ArrayAdapter<String> mTownshipAdapter;

    private List<String> mDepartmentList;
    private ArrayAdapter<String> mDepartmentAdapter;

    private List<String> mWorkersList;
    List<CItem> CIWorkers = new ArrayList<CItem>();
    private ArrayAdapter<String> mWorkersAdapter;

    /*灾害发生时间*/
    private TextView tvHanppenTime;
    /*调查时间*/
    private TextView etNewtaskTime;
    /*任务过期时间*/
    private TextView tvNewtaskOverTime;
    /* 任务发起人*/
    private TextView tvName;
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

    private Toolbar tbTitle;

    private Button btnSave;
    private Button btnSubmit;
    private Button btnCancle;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private String sessionId;
    private String userName;
    private TaskBean taskBean;
    private long mTaskBeanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.fragment_newtask);
        mTaskBeanId=getIntent().getLongExtra("taskbean_id",-1);
        Log.d(TAG,"----------"+mTaskBeanId);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId", "");
        userName=sp.getString("userName","");
        progressDialog = new ProgressDialog(UnCompleteActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        //progressDialog.show();
        initViews();
        setLinsteners();
        initData();
        setOkHttp();
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
                taskBean.setMDisaster(str2);
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
                getTaskBean();
                MyApplication.getDaoSession().getTaskBeanDao().update(taskBean);
                Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                //cleanAll();
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,taskBean.toString());
                if (isNotDataEmpty()) {
                    getTaskBean();
                    setOkHttpCommit();
                }else{
                    ToastUtils.showShortToast("请填写完整信息！");
                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getDaoSession().getTaskBeanDao().delete(taskBean);
                finish();
            }

        });
        etNewtaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickUtil dateTimePicKDialog = new DateTimePickUtil(
                        UnCompleteActivity.this,new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
                dateTimePicKDialog.dateTimePicKDialog(etNewtaskTime);

            }
        });

        tvHanppenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickUtil dateTimePicKDialog = new DateTimePickUtil(
                        UnCompleteActivity.this,new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
                dateTimePicKDialog.dateTimePicKDialog(tvHanppenTime);

            }
        });


        tvNewtaskOverTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickUtil dateTimePicKDialog = new DateTimePickUtil(
                        UnCompleteActivity.this,new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
                dateTimePicKDialog.dateTimePicKDialog(tvNewtaskOverTime);
            }
        });

        tvNewtaskOverTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String date1 = MyUtils.getSystemTime();
                String date2 = s.toString().trim();
                int bl=MyUtils.TimeCompare(1, date1, date2);
                Log.d(TAG, "----bl" + bl);
                if (!("".equals(date2))){
                    Log.d(TAG, "----date1" + date1);
                    Log.d(TAG, "----date2" + date2);
                    if (bl==1) {
                        //taskBean.setMOverTime(s.toString());
                    } else {
                        tvNewtaskOverTime.setText("");
                        ToastUtils.showShortToast("请选择今天以后的时间！");
                    }
                }
            }
        });

    }


    private boolean isNotDataEmpty() {
        if ("".equals(tvHanppenTime.getText().toString().trim())){return false;}
        if ("".equals(etNewtaskTime.getText().toString().trim())){return false;}
        if ("".equals(tvNewtaskOverTime.getText().toString().trim())){return false;}
        if ("请选择灾害点".equals(tvDisaster.getText().toString().trim())){return false;}
        if ("".equals(etNewtaskDisaster.getText().toString().trim())){return false;}
        if ("-1".equals(CITownship.get(spTownship.getSelectedItemPosition()).GetID())){return false;}
        if ("-1".equals(CIWorkers.get(spWorkers.getSelectedItemPosition()).GetID())){return false;}
        return true;
    }

    /**
     * 设置数据库存的数据
     */
    private void setViewDataAll() {
        taskBean=MyApplication.getDaoSession().getTaskBeanDao().queryBuilder()
                .where(TaskBeanDao.Properties.Id.eq(mTaskBeanId)).build().list().get(0);
        tvHanppenTime.setText(taskBean.getMHappenTime());
        etNewtaskTime.setText(taskBean.getMTime());
        tvNewtaskOverTime.setText(taskBean.getMOverTime());
        // 设置灾害点选中
        if(taskBean.getMDisaster()==null) {
            tvDisaster.setText("请选择灾害点");
        }else{
            tvDisaster.setText(taskBean.getMDisaster());
        }
        // 设置调查地点选中
        etNewtaskDisaster.setText(taskBean.getMAddress());

        // 设置乡镇选中
        for(int i=0;i<mTownshipAdapter.getCount();i++){
            String string=mTownshipAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMTownship())){
                spTownship.setSelection(i,true);
                break;
            }
        }

        // 设置人员选中
        for(int i=0;i<mWorkersAdapter.getCount();i++){
            String string=mWorkersAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMWorkers())){
                spWorkers.setSelection(i,true);
                break;
            }
        }

    }
    private void getTaskBean(){
        if("".equals(etNewtaskTime.getText().toString().trim())){
        }else{
            taskBean.setMTime(etNewtaskTime.getText().toString().trim()+":00");
        }
        if("".equals(tvHanppenTime.getText().toString().trim())){
        }else{
            taskBean.setMHappenTime(tvHanppenTime.getText().toString().trim()+":00");
        }
        if("".equals(tvNewtaskOverTime.getText().toString().trim())){
        }else{
            taskBean.setMOverTime(tvNewtaskOverTime.getText().toString().trim());
        }
        if("".equals(etNewtaskDisaster.getText().toString().trim())){
        }else{
            taskBean.setMAddress(etNewtaskDisaster.getText().toString().trim());
        }

        if("-1".equals(CITownship.get(spTownship.getSelectedItemPosition()).GetID())){
        }else{
            taskBean.setMTownshipID(CITownship.get(spTownship.getSelectedItemPosition()).GetID());
            taskBean.setMTownship(CITownship.get(spTownship.getSelectedItemPosition()).GetValue());

        }

        if("-1".equals(CIWorkers.get(spWorkers.getSelectedItemPosition()).GetID())){
        }else{
            taskBean.setMWorkersID(CIWorkers.get(spWorkers.getSelectedItemPosition()).GetID());
            taskBean.setMWorkers(CIWorkers.get(spWorkers.getSelectedItemPosition()).GetValue());

        }

    }

    private void cleanAll() {
        taskBean=new TaskBean();
        tvDisaster.setText("请选择灾害点");
        tvHanppenTime.setText("");
        etNewtaskTime.setText("");
        etNewtaskDisaster.setText("");
        spTownship.setSelection(0,true);
        //spDepartment.setSelection(0,true);
        spWorkers.setSelection(0,true);
    }

    private void initViews() {
        tbTitle= (Toolbar) findViewById(R.id.my_toolbar);
        tbTitle.setVisibility(View.VISIBLE);
        tvHanppenTime= (TextView)findViewById(R.id.tv_newtask_hanppen_time);
        tvName= (TextView) findViewById(R.id.tv_newtask_name);
        etNewtaskTime = (TextView)findViewById(R.id.et_newtask_time);
        tvNewtaskOverTime= (TextView)findViewById(R.id.et_newtask_overtime);
        tvDisaster = (TextView)findViewById(R.id.tv_disaster);
        spTownship = (Spinner)findViewById(R.id.sp_township);
        //spDepartment = (Spinner) view.findViewById(R.id.sp_department);
        spWorkers = (Spinner) findViewById(R.id.sp_workers);
        etNewtaskDisaster = (EditText)findViewById(R.id.et_newtask_disaster);
        btnSave = (Button)findViewById(R.id.btn_newtask_save);
        btnSubmit = (Button)findViewById(R.id.btn_newtask_submit);
        btnCancle = (Button)findViewById(R.id.btn_newtask_cancle);
    }

    /**
     * 设置spinner等从网络获取的选项有哪些
     */
    private void initData() {
        tvHanppenTime.setText("");
        etNewtaskTime.setText("");
        tvName.setText(userName);
        //灾害点
        tvDisaster.setText("请选择灾害点");

        //乡镇
        mTownshipList = new ArrayList<String>();
        CITownship=new ArrayList<CItem>();
        mTownshipList.add("请选择乡镇");
        mTownshipAdapter = new ArrayAdapter<String>(UnCompleteActivity.this, android.R.layout.simple_spinner_item, mTownshipList);
        mTownshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTownship.setAdapter(mTownshipAdapter);


        //人员
        mWorkersList = new ArrayList<String>();
        CITownship=new ArrayList<CItem>();
        mWorkersList.add("请选择人员");
        mWorkersAdapter = new ArrayAdapter<String>(UnCompleteActivity.this, android.R.layout.simple_spinner_item, mWorkersList);
        mWorkersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkers.setAdapter(mWorkersAdapter);

    }


    private void setOkHttpCommit() {
        progressDialog.show();
        try {
            OkHttpUtils.get().url(new ConnectUrl().saveSuveyTaskUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("happen_time", taskBean.getMHappenTime())
                    .addParams("survey_time", taskBean.getMTime())
                    .addParams("overTime",taskBean.getMOverTime())
                    .addParams("dis_id", taskBean.getMDisasterID())
                    .addParams("dis_name", taskBean.getMDisaster())
                    .addParams("survey_site", taskBean.getMAddress())
                    .addParams("area_id", taskBean.getMTownshipID())
                    .addParams("survey_id", taskBean.getMWorkersID())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            //getActivity().finish();
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
                                    MyApplication.getDaoSession().getTaskBeanDao().update(taskBean);
                                    finish();
                                    ToastUtils.showShortToast(msg);
                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                    Intent intent = new Intent(UnCompleteActivity.this, LoginActivity.class);
                                    UnCompleteActivity.this.startActivity(intent);
                                    finish();
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

    private void setOkHttp() {
        progressDialog.show();
        try {
            OkHttpUtils.get().url(new ConnectUrl().getAreaListUrl())
                    .addParams("sessionId",sessionId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            finish();
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
                                    CITownship.clear();
                                    mTownshipList.add("请选择乡镇");
                                    CITownship.add(new CItem("-1","请选择乡镇"));
                                    JSONArray message=object.getJSONArray("message");
                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        mTownshipList.add(oj.getString("area_name"));
                                        CITownship.add(new CItem(oj.getString("id"),oj.getString("area_name")));
                                    }
                                    mTownshipAdapter.notifyDataSetChanged();
                                    setViewDataAll();
                                    Log.d(TAG,taskBean.toString());
                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                    Intent intent = new Intent(UnCompleteActivity.this, LoginActivity.class);
                                    UnCompleteActivity.this.startActivity(intent);
                                    finish();
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
                            finish();
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
                                    CIWorkers.clear();
                                    mWorkersList.add("请选择人员");
                                    CIWorkers.add(new CItem("-1","请选择人员"));
                                    JSONArray message=object.getJSONArray("message");
                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        mWorkersList.add(oj.getString("name"));
                                        CIWorkers.add(new CItem(oj.getString("id"),oj.getString("name")));
                                    }
                                    mWorkersAdapter.notifyDataSetChanged();
                                    setViewDataAll();
                                    Log.d(TAG,2+taskBean.toString());
                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                    Intent intent = new Intent(UnCompleteActivity.this, LoginActivity.class);
                                    UnCompleteActivity.this.startActivity(intent);
                                    finish();
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
