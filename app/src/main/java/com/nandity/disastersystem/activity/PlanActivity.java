package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.PlanAdapter;
import com.nandity.disastersystem.bean.PlanBean;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


public class PlanActivity extends AppCompatActivity {
    private static final String TAG = "PlanActivity";
    @BindView(R.id.et_plan_name)
    EditText etPlanName;
    @BindView(R.id.et_plan_year)
    EditText etPlanYear;
    @BindView(R.id.sp_plan_type)
    Spinner spPlanType;
    @BindView(R.id.iv_plan_search)
    ImageView ivPlanSearch;
    @BindView(R.id.plan_recycler)
    PullLoadMoreRecyclerView planRecycler;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout searchProgress;
    @BindView(R.id.tv_plan_cancel)
    TextView tvPlanCancel;
    private SharedPreferences sp;
    private String sessionId;
    private int pageNum = 0;
    private static int rowsNum = 10;
    private List<PlanBean> planList = new ArrayList<>();
    private List<PlanBean> searchList = new ArrayList<>();
    private List<String> planTypeList = new ArrayList<>();
    private List<String> planTypeNum = new ArrayList<>();
    private PlanAdapter planAdapter;
    private PlanAdapter searchAdapter;
    private ArrayAdapter<String> planTypeAdapter;
    private RecyclerView mRecyclerView;
    private String planName, planYear, planType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setLoadMore();
        setListener();
    }

    private void setListener() {
        ivPlanSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planName = etPlanName.getText().toString().trim();
                planYear = etPlanYear.getText().toString().trim();
                planType = planTypeNum.get(spPlanType.getSelectedItemPosition());
                if (TextUtils.isEmpty(planYear) && TextUtils.isEmpty(planName) && spPlanType.getSelectedItemPosition() == 0) {
                    return;
                }
                searchProgress.setVisibility(View.VISIBLE);
                OkHttpUtils.get().url(new ConnectUrl().getPlanUrl())
                        .addParams("sessionId", sessionId)
                        .addParams("emergency_name", planName)
                        .addParams("emergency_type", planType + "")
                        .addParams("emergency_year", planYear)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showShortToast("连接服务器失败！");
                                searchProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String status, msg;
                                try {
                                    JSONObject object = new JSONObject(response);
                                    status = object.getString("status");
                                    msg = object.getString("message");
                                    if ("200".equals(status)) {
                                        searchList.clear();
                                        llSearch.setVisibility(View.VISIBLE);
                                        tvPlanCancel.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        JSONArray array = object.getJSONArray("message");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject oj = array.getJSONObject(i);
                                            PlanBean bean = new PlanBean();
                                            bean.setName(oj.getString("emergency_name"));
                                            bean.setPlace(oj.getString("emergency_xiaqu"));
                                            bean.setType(oj.getString("static_name"));
                                            bean.setYear(oj.getString("emergency_year"));
                                            searchList.add(bean);
                                        }
                                        searchAdapter.notifyDataSetChanged();
                                        searchProgress.setVisibility(View.GONE);
                                    } else if ("400".equals(status)) {
                                        searchProgress.setVisibility(View.GONE);
                                        sp.edit().putBoolean("isLogin", false).apply();
                                        ToastUtils.showShortToast(msg);
                                        Intent intent = new Intent(PlanActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if ("500".equals(status)) {
                                        ToastUtils.showShortToast("搜索条件不匹配！");
                                        searchProgress.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        tvPlanCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlanCancel.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setLoadMore() {
        mRecyclerView = planRecycler.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        planRecycler.setFooterViewText("正在加载...");
        planRecycler.setPullRefreshEnable(false);
        planRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                planRecycler.setRefreshing(true);
                loadMore();
            }
        });
    }

    private void loadMore() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getPlanUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        planRecycler.setPullLoadMoreCompleted();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "获取的数据：" + response);
                        String msg, status;
                        try {
                            JSONObject object = new JSONObject(response);
                            msg = object.getString("message");
                            status = object.getString("status");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    PlanBean bean = new PlanBean();
                                    bean.setName(oj.getString("emergency_name"));
                                    bean.setPlace(oj.getString("emergency_xiaqu"));
                                    bean.setType(oj.getString("static_name"));
                                    bean.setYear(oj.getString("emergency_year"));
                                    planList.add(bean);
                                }
                                planAdapter.notifyDataSetChanged();
                                planRecycler.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(PlanActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                planRecycler.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setAdapter() {
        planTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, planTypeList);
        planTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlanType.setAdapter(planTypeAdapter);
        planRecycler.setLinearLayout();
        planAdapter = new PlanAdapter(planList, this);
        planRecycler.setAdapter(planAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new PlanAdapter(searchList, this);
        searchRecycler.setAdapter(searchAdapter);

    }

    private void initData() {
        searchProgress.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(new ConnectUrl().getPlanUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        searchProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "获取的数据：" + response);
                        String msg, status;
                        try {
                            JSONObject object = new JSONObject(response);
                            msg = object.getString("message");
                            status = object.getString("status");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    PlanBean bean = new PlanBean();
                                    bean.setName(oj.getString("emergency_name"));
                                    bean.setPlace(oj.getString("emergency_xiaqu"));
                                    bean.setType(oj.getString("static_name"));
                                    bean.setYear(oj.getString("emergency_year"));
                                    planList.add(bean);
                                }
                                planAdapter.notifyDataSetChanged();
                                searchProgress.setVisibility(View.GONE);
                            } else if ("400".equals(status)) {
                                searchProgress.setVisibility(View.GONE);
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(PlanActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                searchProgress.setVisibility(View.GONE);
                                ToastUtils.showShortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        OkHttpUtils.get().url(new ConnectUrl().getSelectUrl())
                .addParams("sessionId", sessionId)
                .addParams("static_no", "7")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String msg, status;
                        try {
                            JSONObject object = new JSONObject(response);
                            msg = object.getString("message");
                            status = object.getString("status");
                            if ("200".equals(status)) {
                                planTypeNum.clear();
                                planTypeList.clear();
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    planTypeList.add(oj.getString("static_name"));
                                    planTypeNum.add(oj.getString("id"));
                                }
                                planTypeAdapter.notifyDataSetChanged();
                            } else {
                                ToastUtils.showShortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
