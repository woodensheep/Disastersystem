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
import com.nandity.disastersystem.bean.DirectoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemon on 2017/2/22.
 */

public class UnTaskFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private LinearLayoutManager mLayoutManager;
    private DirectoryAdapter mAdapter;
    private List<DirectoryBean> mListData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_untask, container, false);
        initData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.directory);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //固定高度
        mRecyclerView.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new DirectoryAdapter(getActivity(), mListData);
        mRecyclerView.setAdapter(mAdapter);
        return view;


    }

    private void initData() {
        mListData = new ArrayList();
        mListData.add(new DirectoryBean("张三", "18883673743"));
        mListData.add(new DirectoryBean("李四", "18883673744"));
        mListData.add(new DirectoryBean("王五", "18883673745"));
        mListData.add(new DirectoryBean("朱六", "18883673746"));
    }
}