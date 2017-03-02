package com.nandity.disastersystem.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.DirectoryAdapter;
import com.nandity.disastersystem.bean.DirectoryBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.JsonFormat;
import com.nandity.disastersystem.utils.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lemon on 2017/2/22.
 */

public class DirectoryFragment extends Fragment {
    private static String TAG = "DirectoryFragment";
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.directory_list)
    PullLoadMoreRecyclerView directoryList;
    private DirectoryAdapter adapter;
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private int pageNum = 0;
    private static int rowsNum = 10;
    private List<DirectoryBean> list = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = directoryList.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        directoryList.setFooterViewText("正在加载...");
        directoryList.setPullRefreshEnable(false);
        directoryList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                directoryList.setRefreshing(true);
                loadMore();
            }
        });
    }

    private void setAdapter() {
        directoryList.setLinearLayout();
        adapter = new DirectoryAdapter(context, list);
        directoryList.setAdapter(adapter);
    }

    private void initData() {
        OkHttpUtils.get().url(new ConnectUrl().getDirectoryUrl())
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .addParams("sessionId", sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络错误，请检查网络！");

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "第一次返回的数据：" + response);
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            Log.d(TAG, "用户数据：" + msg);
                            if ("200".equals(status)) {
                                list = JsonFormat.stringToList(msg, DirectoryBean.class);
                                Log.d(TAG, "第一次加载的数据：" + list.toString());
                                setAdapter();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loadMore() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getDirectoryUrl())
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .addParams("sessionId", sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络错误，请检查网络！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "上拉加载返回的数据：" + response);
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                List<DirectoryBean> morelist = JsonFormat.stringToList(msg, DirectoryBean.class);
                                Log.d(TAG, "上拉加载的数据：" + morelist.toString());
                                list.addAll(morelist);
                                adapter.notifyDataSetChanged();
                                directoryList.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                directoryList.setPullLoadMoreCompleted();
                            } else if ("500".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                directoryList.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}