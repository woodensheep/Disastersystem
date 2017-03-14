package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.MyTaskAdapter;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

public class CompleteTaskActivity extends AppCompatActivity {
    private static final String TAG = "CompleteTaskActivity";
    @BindView(R.id.et_search_content)
    AutoCompleteTextView etSearchContent;
    @BindView(R.id.search_clear)
    ImageView searchClear;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recyclerview)
    RecyclerView searchRecyclerview;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout searchProgress;
    @BindView(R.id.rv_complete_task)
    PullLoadMoreRecyclerView rvCompleteTask;
    private SharedPreferences sp;
    private String sessionId;
    private int pageNum = 0;
    private int rowsNum = 10;
    private MyTaskAdapter adapter;
    private MyTaskAdapter searchAdapter;
    private List<TaskInfoBean> taskInfoList = new ArrayList<>();
    private List<TaskInfoBean> searchList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setLoadMore();
        initAutoComplete(etSearchContent);
        setListener();
    }

    private void setLoadMore() {
        mRecyclerView=rvCompleteTask.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        rvCompleteTask.setFooterViewText("正在加载...");
        rvCompleteTask.setPullRefreshEnable(false);
        rvCompleteTask.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                rvCompleteTask.setRefreshing(true);
                loadMore();
            }
        });
    }

    private void loadMore() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getCompleteTaskUrl())
                .addParams("sessionId",sessionId)
                .addParams("page",pageNum+"")
                .addParams("rows",rowsNum+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        rvCompleteTask.setPullLoadMoreCompleted();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    TaskInfoBean bean = new TaskInfoBean();
                                    bean.setmTaskId(oj.getString("id"));
                                    bean.setmDisaster(oj.getString("dis_name"));
                                    bean.setmAddress(oj.getString("survey_site"));
                                    taskInfoList.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                                rvCompleteTask.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                rvCompleteTask.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setAdapter() {
        rvCompleteTask.setLinearLayout();
        adapter = new MyTaskAdapter(this, taskInfoList);
        rvCompleteTask.setAdapter(adapter);
        searchRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter=new MyTaskAdapter(this,searchList);
        searchRecyclerview.setAdapter(searchAdapter);
    }

    private void setListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param=etSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(param)){
                    return;
                }
                String paramName=getParamName(param);
                OkHttpUtils.get().url(new ConnectUrl().getCompleteTaskUrl())
                        .addParams(paramName,param)
                        .addParams("sessionId",sessionId)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showShortToast("连接服务器失败！");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String status,msg;
                                try {
                                    JSONObject object=new JSONObject(response);
                                    status=object.getString("status");
                                    msg=object.getString("message");
                                    if ("200".equals(status)){
                                        searchList.clear();
                                        saveHistory("taskHistory",etSearchContent);
                                        llSearch.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        JSONArray array = object.getJSONArray("message");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject oj = array.getJSONObject(i);
                                            TaskInfoBean bean = new TaskInfoBean();
                                            bean.setmTaskId(oj.getString("id"));
                                            bean.setmDisaster(oj.getString("dis_name"));
                                            bean.setmAddress(oj.getString("survey_site"));
                                            searchList.add(bean);
                                        }
                                        searchAdapter.notifyDataSetChanged();
                                    }else if("400".equals(status)){
                                        ToastUtils.showShortToast(msg);
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if ("500".equals(status)){
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
        adapter.setOnItemClickListener(new MyTaskAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TaskInfoBean taskInfoBean) {
                Intent intent=new Intent(getContext(),TaskInfoActivity.class);
                intent.putExtra("TaskId",taskInfoBean.getmTaskId());
                startActivity(intent);
            }
        });
        searchAdapter.setOnItemClickListener(new MyTaskAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TaskInfoBean taskInfoBean) {
                Intent intent=new Intent(getContext(),TaskInfoActivity.class);
                intent.putExtra("TaskId",taskInfoBean.getmTaskId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        OkHttpUtils.get().url(new ConnectUrl().getCompleteTaskUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    TaskInfoBean bean = new TaskInfoBean();
                                    bean.setmTaskId(oj.getString("id"));
                                    bean.setmDisaster(oj.getString("dis_name"));
                                    bean.setmAddress(oj.getString("survey_site"));
                                    taskInfoList.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 初始化历史搜索记录
     */
    private void initAutoComplete(AutoCompleteTextView auto) {
        String longhistory = sp.getString("taskHistory", "nothing");
        String[] hisArrays = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hisArrays);
        //只保留最近的50条的记录
        if (hisArrays.length > 50) {
            String[] newArrays = new String[50];
            System.arraycopy(hisArrays, 0, newArrays, 0, 50);
            adapter = new ArrayAdapter<String>(this,
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
            sp.edit().putString("taskHistory", sb.toString()).apply();
        }

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
}
