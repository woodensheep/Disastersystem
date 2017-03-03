package com.nandity.disastersystem.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.LoginActivity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lemon on 2017/2/22.
 */

public class DirectoryFragment extends Fragment {
    private static String TAG = "DirectoryFragment";
    @BindView(R.id.directory_list)
    PullLoadMoreRecyclerView directoryList;
    @BindView(R.id.et_search_content)
    AutoCompleteTextView etSearchContent;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.search_clear)
    ImageView searchClear;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recyclerview)
    RecyclerView searchRecyclerview;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout directoryProgress;
    private DirectoryAdapter normalAdapter;
    private DirectoryAdapter searchAdapter;
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private int pageNum = 0;
    private static int rowsNum = 10;
    private List<DirectoryBean> list = new ArrayList<>();
    private List<DirectoryBean> searchList = new ArrayList<>();
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
        directoryProgress.setVisibility(View.VISIBLE);
        initData();
        initAutoComplete("history", etSearchContent, searchClear);
        setListener();
        return view;
    }


    private void setListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = etSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(param)) {
                    return;
                }
                String paramName = getParamName(param);
                directoryProgress.setVisibility(View.VISIBLE);
                OkHttpUtils.get().url(new ConnectUrl().getDirectoryUrl())
                        .addParams(paramName, param)
                        .addParams("sessionId", sessionId)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showShortToast("网络故障，请检查网路！");
                                directoryProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "搜索返回的数据：" + response);
                                String status, msg;
                                try {
                                    JSONObject object = new JSONObject(response);
                                    status = object.getString("status");
                                    msg = object.getString("message");
                                    directoryProgress.setVisibility(View.GONE);
                                    Log.d(TAG, "用户数据：" + msg);
                                    if ("200".equals(status)) {
                                        saveHistory("history", etSearchContent);
                                        llSearch.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        searchList = JsonFormat.stringToList(msg, DirectoryBean.class);
                                        searchRecyclerview.setLayoutManager(new LinearLayoutManager(context));
                                        searchAdapter = new DirectoryAdapter(context, searchList);
                                        searchRecyclerview.setAdapter(searchAdapter);
                                        itemClickListener(searchAdapter, searchList, searchRecyclerview);
                                    } else if ("400".equals(status)) {
                                        ToastUtils.showShortToast(msg);
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        getActivity().finish();
                                    } else if ("500".equals(status)) {
                                        ToastUtils.showShortToast("搜索条件不匹配！");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchContent.setText("");
                etSearchContent.dismissDropDown();
            }
        });
        etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    searchClear.setVisibility(View.INVISIBLE);
                    llNormal.setVisibility(View.VISIBLE);
                    llSearch.setVisibility(View.INVISIBLE);
                } else {
                    searchClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    /**
     * 判断输入的是字符还是数字
     *
     * @param param
     * @return
     */
    private String getParamName(String param) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(param);
        if (m.matches()) {
            return "phoneNo";
        }
        return "userName";
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
        normalAdapter = new DirectoryAdapter(context, list);
        directoryList.setAdapter(normalAdapter);
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
                        directoryProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "第一次返回的数据：" + response);
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            directoryProgress.setVisibility(View.GONE);
                            Log.d(TAG, "用户数据：" + msg);
                            if ("200".equals(status)) {
                                list = JsonFormat.stringToList(msg, DirectoryBean.class);
                                Log.d(TAG, "第一次加载的数据：" + list.toString());
                                setAdapter();
                                itemClickListener(normalAdapter, list, mRecyclerView);
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void itemClickListener(DirectoryAdapter adapter, final List<DirectoryBean> directoryBeanList, final RecyclerView recyclerView) {
        adapter.setOnItemClickListener(new DirectoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                DirectoryBean bean = directoryBeanList.get(position);
                String number = bean.getMobile();
                call(number);
            }
        });
    }


    private void call(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel://" + number));
        context.startActivity(intent);

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
                        directoryList.setPullLoadMoreCompleted();
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
                                normalAdapter.notifyDataSetChanged();
                                directoryList.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                directoryList.setPullLoadMoreCompleted();
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                                getActivity().finish();
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

    /**
     * 初始化历史搜索记录
     *
     * @param field
     * @param auto
     * @param searchClear
     */
    private void initAutoComplete(String field, AutoCompleteTextView auto, final ImageView searchClear) {
        String longhistory = sp.getString("history", "nothing");
        String[] hisArrays = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, hisArrays);
        //只保留最近的50条的记录
        if (hisArrays.length > 50) {
            String[] newArrays = new String[50];
            System.arraycopy(hisArrays, 0, newArrays, 0, 50);
            adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, newArrays);
        }
        auto.setAdapter(adapter);
        auto.setDropDownHeight(350);
        auto.setThreshold(1);
        auto.setCompletionHint("最近的5条记录");
        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    /**
     * 保存搜索记录
     *
     * @param field
     * @param auto
     */
    private void saveHistory(String field, AutoCompleteTextView auto) {
        String text = auto.getText().toString();
        String longhistory = sp.getString(field, "nothing");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).apply();
        }

    }


}