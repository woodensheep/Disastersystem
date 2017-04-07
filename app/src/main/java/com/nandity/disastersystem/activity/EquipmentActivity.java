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
import com.nandity.disastersystem.adapter.EquipmentAdapter;
import com.nandity.disastersystem.bean.EquipmentBean;
import com.nandity.disastersystem.bean.PersonBean;
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

public class EquipmentActivity extends AppCompatActivity {
    private static final String TAG = "EquipmentActivity";
    @BindView(R.id.sp_person_type)
    Spinner spPersonType;
    @BindView(R.id.et_equipment_name)
    EditText etEquipmentName;
    @BindView(R.id.iv_equipment_search)
    ImageView ivEquipmentSearch;
    @BindView(R.id.tv_equipment_cancel)
    TextView tvEquipmentCancel;
    @BindView(R.id.equipment_recycler)
    PullLoadMoreRecyclerView equipmentRecycler;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout searchProgress;
    private int pageNum = 0;
    private static int rowsNum = 10;
    private String sessionId;
    private SharedPreferences sp;
    private List<EquipmentBean> equipmentList = new ArrayList<>();
    private List<EquipmentBean> searchList=new ArrayList<>();
    private List<String> equipmentTypeNum = new ArrayList<>();
    private List<String> equipmentTypeList = new ArrayList<>();
    private EquipmentAdapter equipmentAdapter;
    private EquipmentAdapter searchAdapter;
    private ArrayAdapter<String> equipmentTypeAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setLoadMare();
        setListener();
    }

    private void setListener() {
        ivEquipmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etEquipmentName.getText().toString().trim();
                String type=equipmentTypeNum.get(spPersonType.getSelectedItemPosition());
                if (TextUtils.isEmpty(name)&&spPersonType.getSelectedItemPosition()==0){
                    return;
                }
                searchProgress.setVisibility(View.VISIBLE);
                OkHttpUtils.get().url(new ConnectUrl().getEquipmentUrl())
                        .addParams("sessionId", sessionId)
                        .addParams("device_name", name)
                        .addParams("device_type", type)
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
                                        searchList.clear();
                                        llSearch.setVisibility(View.VISIBLE);
                                        tvEquipmentCancel.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        JSONArray array = object.getJSONArray("message");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject oj = array.getJSONObject(i);
                                            EquipmentBean bean=new EquipmentBean();
                                            bean.setEquipmentName(oj.getString("device_name"));
                                            bean.setEquipmentNum(oj.getString("device_no"));
                                            bean.setEquipmentPlace(oj.getString("device_address"));
                                            bean.setEquipmentType(oj.getString("static_name"));
                                            searchList.add(bean);
                                        }
                                        searchAdapter.notifyDataSetChanged();
                                        searchProgress.setVisibility(View.GONE);
                                    } else if ("400".equals(status)) {
                                        searchProgress.setVisibility(View.GONE);
                                        ToastUtils.showShortToast(msg);
                                        sp.edit().putBoolean("isLogin", false).apply();
                                        startActivity(new Intent(EquipmentActivity.this, LoginActivity.class));
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
        tvEquipmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEquipmentCancel.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setLoadMare() {
        mRecyclerView=equipmentRecycler.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        equipmentRecycler.setFooterViewText("正在加载...");
        equipmentRecycler.setPullRefreshEnable(false);
        equipmentRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                equipmentRecycler.setRefreshing(true);
                loadMore();
            }
        });
    }

    private void loadMore() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getEquipmentUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        equipmentRecycler.setPullLoadMoreCompleted();
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
                                    EquipmentBean bean=new EquipmentBean();
                                    bean.setEquipmentName(oj.getString("device_name"));
                                    bean.setEquipmentNum(oj.getString("device_no"));
                                    bean.setEquipmentPlace(oj.getString("device_address"));
                                    bean.setEquipmentType(oj.getString("static_name"));
                                    equipmentList.add(bean);
                                }
                                equipmentAdapter.notifyDataSetChanged();
                                equipmentRecycler.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(EquipmentActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                equipmentRecycler.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setAdapter() {
        equipmentTypeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,equipmentTypeList);
        equipmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPersonType.setAdapter(equipmentTypeAdapter);
        equipmentRecycler.setLinearLayout();
        equipmentAdapter=new EquipmentAdapter(equipmentList,this);
        equipmentRecycler.setAdapter(equipmentAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter=new EquipmentAdapter(searchList,this);
        searchRecycler.setAdapter(searchAdapter);
    }

    private void initData() {
        searchProgress.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(new ConnectUrl().getEquipmentUrl())
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
                                    EquipmentBean bean=new EquipmentBean();
                                    bean.setEquipmentName(oj.getString("device_name"));
                                    bean.setEquipmentNum(oj.getString("device_no"));
                                    bean.setEquipmentPlace(oj.getString("device_address"));
                                    bean.setEquipmentType(oj.getString("static_name"));
                                    equipmentList.add(bean);
                                }
                                equipmentAdapter.notifyDataSetChanged();
                                searchProgress.setVisibility(View.GONE);
                            } else if ("400".equals(status)) {
                                searchProgress.setVisibility(View.GONE);
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(EquipmentActivity.this, LoginActivity.class));
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
                .addParams("static_no", "6")
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
                                equipmentTypeNum.clear();
                                equipmentTypeList.clear();
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    equipmentTypeList.add(oj.getString("static_name"));
                                    equipmentTypeNum.add(oj.getString("id"));
                                }
                                equipmentTypeAdapter.notifyDataSetChanged();
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
