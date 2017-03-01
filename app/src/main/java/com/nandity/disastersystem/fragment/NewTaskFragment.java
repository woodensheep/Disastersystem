package com.nandity.disastersystem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.DirectoryAdapter;
import com.nandity.disastersystem.bean.DirectoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemon on 2017/2/22.
 *
 * 创建新任务
 */

public class NewTaskFragment extends Fragment {
    private View view;
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
    /*调查时间*/
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newtask, container, false);
        initViews();
        setLinsteners();
        initData();
        return view;
    }



    private void setLinsteners() {

    }

    private void initViews() {
        etNewtaskTime = (EditText) view.findViewById(R.id.et_newtask_time);
        spDisaster = (Spinner) view.findViewById(R.id.sp_disaster);
        spTownship = (Spinner) view.findViewById(R.id.sp_township);
        spDepartment = (Spinner) view.findViewById(R.id.sp_department);
        spWorkers = (Spinner) view.findViewById(R.id.sp_workers);
        etNewtaskDisaster = (EditText) view.findViewById(R.id.et_newtask_disaster);
        btnSave = (Button) view.findViewById(R.id.btn_newtask_save);
        btnSubmit = (Button) view.findViewById(R.id.btn_newtask_submit);
        btnCancle = (Button) view.findViewById(R.id.btn_newtask_cancle);
    }

    private void initData() {
        //灾害点
        //数据
        mDisasterList = new ArrayList<String>();
        mDisasterList.add("请选择灾害点");
        mDisasterList.add("四季坝");
        mDisasterList.add("四季坝滑坡");
        //适配器
        mDisasterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mDisasterList);
        //设置样式
        mDisasterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spDisaster.setAdapter(mDisasterAdapter);

        //乡镇
        mTownshipList = new ArrayList<String>();
        mTownshipList.add("请选择乡镇");
        mTownshipList.add("XX镇");
        mTownshipList.add("YY镇");
        mTownshipAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mTownshipList);
        mTownshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTownship.setAdapter(mTownshipAdapter);


        //部门
        mDepartmentList = new ArrayList<String>();
        mDepartmentList.add("请选择部门");
        mDepartmentList.add("XX部");
        mDepartmentList.add("YY部");
        mDepartmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mDepartmentList);
        mDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(mDepartmentAdapter);

        //人员
        mWorkersList = new ArrayList<String>();
        mWorkersList.add("请选择人员");
        mWorkersList.add("XX");
        mWorkersList.add("YY");
        mWorkersAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mWorkersList);
        mWorkersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkers.setAdapter(mWorkersAdapter);


    }
}