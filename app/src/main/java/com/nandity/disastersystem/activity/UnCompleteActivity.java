package com.nandity.disastersystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.database.TaskBean;
import com.nandity.disastersystem.database.TaskBeanDao;
import com.nandity.disastersystem.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nandity.disastersystem.app.MyApplication.getDaoSession;

public class UnCompleteActivity extends AppCompatActivity {

    private Toolbar comTaskToolbar;
    private List<String> mDisasterList;
    private ArrayAdapter<String> mDisasterAdapter;

    private List<String> mTownshipList;
    private ArrayAdapter<String> mTownshipAdapter;

    private List<String> mDepartmentList;
    private ArrayAdapter<String> mDepartmentAdapter;

    private List<String> mWorkersList;
    private ArrayAdapter<String> mWorkersAdapter;

    /*调查时间*/
    private EditText etNewtaskTime;
    /*灾害点*/
    private Spinner spDisaster;
    /*调查地点*/
    private EditText etNewtaskDisaster;
    /*乡镇*/
    private Spinner spTownship;
    /*部门*/
    private Spinner spDepartment;
    /*人员*/
    private Spinner spWorkers;

    private Button btnSave;
    private Button btnSubmit;
    private Button btnCancle;

    private Long mTaskBeanId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newtask);
        mTaskBeanId=getIntent().getLongExtra("taskbean_id",-1);
        Log.d("UnCompleteActivity",""+mTaskBeanId);
        initViews();
        setLinsteners();
        initData();
        setViewDataAll();
    }


    private void setLinsteners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskBean taskBean=new TaskBean();
                taskBean.setId((long)mTaskBeanId);
                taskBean.setMTime(MyUtils.getSystemTime());
                taskBean.setMDisaster(spDisaster.getSelectedItem().toString().trim());
                taskBean.setMAddress(etNewtaskDisaster.getText().toString().trim());
                taskBean.setMTownship(spTownship.getSelectedItem().toString().trim());
                taskBean.setMDepartment(spDepartment.getSelectedItem().toString().trim());
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
    }


    /**
     * 设置数据库存的数据
     */
    private void setViewDataAll() {
         TaskBean taskBean=MyApplication.getDaoSession().getTaskBeanDao().queryBuilder()
                .where(TaskBeanDao.Properties.Id.eq(mTaskBeanId)).build().list().get(0);
        etNewtaskTime.setText(MyUtils.getSystemTime());

        // 设置灾害点选中
        for(int i=0;i<mDisasterAdapter.getCount();i++){
            String string=mDisasterAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMDisaster())){
                spDisaster.setSelection(i,true);
                break;
            }
        }

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
        // 设置部门选中
        for(int i=0;i<mDepartmentAdapter.getCount();i++){
            String string=mDepartmentAdapter.getItem(i).toString();
            if(string.equals(taskBean.getMDisaster())){
                spDepartment.setSelection(i,true);
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
        etNewtaskTime.setText(MyUtils.getSystemTime());
        spDisaster.setSelection(0,true);
        etNewtaskDisaster.setText("");
        spTownship.setSelection(0,true);
        spDepartment.setSelection(0,true);
        spWorkers.setSelection(0,true);
    }

    private void initViews() {
        comTaskToolbar= (Toolbar) findViewById(R.id.my_toolbar);
        comTaskToolbar.setVisibility(View.VISIBLE);
        etNewtaskTime = (EditText) findViewById(R.id.et_newtask_time);
        spDisaster = (Spinner) findViewById(R.id.sp_disaster);
        spTownship = (Spinner) findViewById(R.id.sp_township);
        spDepartment = (Spinner) findViewById(R.id.sp_department);
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

        etNewtaskTime.setText(MyUtils.getSystemTime());
        //灾害点
        //数据
        mDisasterList = new ArrayList<String>();
        mDisasterList.add("请选择灾害点");
        mDisasterList.add("四季坝");
        mDisasterList.add("四季坝滑坡");
        //适配器
        mDisasterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDisasterList);
        //设置样式
        mDisasterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spDisaster.setAdapter(mDisasterAdapter);

        //乡镇
        mTownshipList = new ArrayList<String>();
        mTownshipList.add("请选择乡镇");
        mTownshipList.add("XX镇");
        mTownshipList.add("YY镇");
        mTownshipAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTownshipList);
        mTownshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTownship.setAdapter(mTownshipAdapter);


        //部门
        mDepartmentList = new ArrayList<String>();
        mDepartmentList.add("请选择部门");
        mDepartmentList.add("XX部");
        mDepartmentList.add("YY部");
        mDepartmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDepartmentList);
        mDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(mDepartmentAdapter);

        //人员
        mWorkersList = new ArrayList<String>();
        mWorkersList.add("请选择人员");
        mWorkersList.add("XX");
        mWorkersList.add("YY");
        mWorkersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mWorkersList);
        mWorkersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkers.setAdapter(mWorkersAdapter);


    }
}
