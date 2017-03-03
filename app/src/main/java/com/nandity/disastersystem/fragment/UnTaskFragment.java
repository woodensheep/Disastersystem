package com.nandity.disastersystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.UnCompleteActivity;
import com.nandity.disastersystem.adapter.UnTaskAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.database.TaskBean;
import com.nandity.disastersystem.database.TaskBeanDao;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemon on 2017/2/22.
 * 未提交任务
 */

public class UnTaskFragment extends Fragment  {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UnTaskAdapter mAdapter;
    private List<TaskBean> mListData;
    protected boolean isCreated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_untask, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rc_untask);
        mListData = new ArrayList<TaskBean>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //固定高度
        mRecyclerView.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new UnTaskAdapter(getActivity(), mListData);
        mRecyclerView.setAdapter(mAdapter);
        initListeners();
        return view;

    }

    private void initListeners() {

        //mRecyclerView的item点击
        mAdapter.setOnItemClickListener(new UnTaskAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TaskBean taskBean) {
                Intent i=new Intent(getContext(), UnCompleteActivity.class);
                Log.d("UnTaskFragment",""+taskBean.getId());
                i.putExtra("taskbean_id",taskBean.getId());
                startActivity(i);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isCreated = true;
        Log.d("onResume","111");
        mListData.clear();
        initData();
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {
        TaskBeanDao taskBeanDao = MyApplication.getDaoSession().getTaskBeanDao();
        QueryBuilder<TaskBean> qb = taskBeanDao.queryBuilder();
        List<TaskBean> taskBeans = qb.list();
        Log.d("initData", "taskBeans.size()"+taskBeans.size());
        for (int i = 0; i < taskBeans.size(); i++) {
            mListData.add(taskBeans.get(i));
        }

    }

    /**
     * 此方法目前仅适用于标示ViewPager中的Fragment是否真实可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
        if (isVisibleToUser) {
            Log.d("isVisibleToUser", "taskBeans.size()");
            mListData.clear();
            initData();
            mAdapter.notifyDataSetChanged();
        } else {
            Log.d("isUNVisibleToUser", "taskBeans.size()");
//            mListData.clear();
//            initData();
//            mAdapter.notifyDataSetChanged();
        }
    }

}