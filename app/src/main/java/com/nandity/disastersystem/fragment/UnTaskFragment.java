package com.nandity.disastersystem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.DirectoryAdapter;
import com.nandity.disastersystem.adapter.UnTaskAdapter;
import com.nandity.disastersystem.bean.DirectoryBean;
import com.nandity.disastersystem.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemon on 2017/2/22.
 * 未提交任务
 *
 */

public class UnTaskFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UnTaskAdapter mAdapter;
    private List<TaskBean> mListData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_untask, container, false);
        initData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rc_untask);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //固定高度
        mRecyclerView.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new UnTaskAdapter(getActivity(), mListData);
        mRecyclerView.setAdapter(mAdapter);
        return view;


    }

    private void initData() {
        mListData =new ArrayList<TaskBean>();
        mListData.add(new TaskBean("1","家里"));
        mListData.add(new TaskBean("1","家里"));
        mListData.add(new TaskBean("1","家里"));

    }
}