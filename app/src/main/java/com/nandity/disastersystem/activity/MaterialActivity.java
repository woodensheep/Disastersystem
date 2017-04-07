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
import com.nandity.disastersystem.adapter.MaterialAdapter;
import com.nandity.disastersystem.bean.EquipmentBean;
import com.nandity.disastersystem.bean.MaterialBean;
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

public class MaterialActivity extends AppCompatActivity {
    private static final String TAG = "MaterialActivity";
    @BindView(R.id.sp_material_type)
    Spinner spMaterialType;
    @BindView(R.id.et_material_name)
    EditText etMaterialName;
    @BindView(R.id.iv_material_search)
    ImageView ivMaterialSearch;
    @BindView(R.id.tv_material_cancel)
    TextView tvMaterialCancel;
    @BindView(R.id.material_recycler)
    PullLoadMoreRecyclerView materialRecycler;
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
    private List<MaterialBean> materialList = new ArrayList<>();
    private List<MaterialBean> searchList = new ArrayList<>();
    private List<String> materialTypeNum = new ArrayList<>();
    private List<String> materialTypeList = new ArrayList<>();
    private MaterialAdapter materialAdapter;
    private MaterialAdapter searchAdapter;
    private ArrayAdapter<String> materialTypeAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setLoadMare();
        setListener();
    }

    private void setListener() {
        ivMaterialSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=etMaterialName.getText().toString().trim();
                String type=materialTypeNum.get(spMaterialType.getSelectedItemPosition());
                if (TextUtils.isEmpty(name)&&spMaterialType.getSelectedItemPosition()==0){
                    return;
                }
                searchProgress.setVisibility(View.VISIBLE);
                OkHttpUtils.get().url(new ConnectUrl().getMaterialUrl())
                        .addParams("sessionId", sessionId)
                        .addParams("mat_name", name)
                        .addParams("mat_type", type)
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
                                        tvMaterialCancel.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        JSONArray array = object.getJSONArray("message");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject oj = array.getJSONObject(i);
                                            MaterialBean bean=new MaterialBean();
                                            bean.setMaterialName(oj.getString("mat_name"));
                                            bean.setMaterialType(oj.getString("static_name"));
                                            bean.setMaterialStatus(oj.getString("mat_status"));
                                            bean.setMaterialNumber(oj.getString("mat_no"));
                                            searchList.add(bean);
                                        }
                                        searchAdapter.notifyDataSetChanged();
                                        searchProgress.setVisibility(View.GONE);
                                    } else if ("400".equals(status)) {
                                        searchProgress.setVisibility(View.GONE);
                                        ToastUtils.showShortToast(msg);
                                        sp.edit().putBoolean("isLogin", false).apply();
                                        startActivity(new Intent(MaterialActivity.this, LoginActivity.class));
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
        tvMaterialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMaterialCancel.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setLoadMare() {
        mRecyclerView=materialRecycler.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        materialRecycler.setFooterViewText("正在加载...");
        materialRecycler.setPullRefreshEnable(false);
        materialRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                materialRecycler.setRefreshing(true);
                loadMare();
            }
        });
    }

    private void loadMare() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getMaterialUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        materialRecycler.setPullLoadMoreCompleted();
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
                                    MaterialBean bean=new MaterialBean();
                                    bean.setMaterialName(oj.getString("mat_name"));
                                    bean.setMaterialType(oj.getString("static_name"));
                                    bean.setMaterialStatus(oj.getString("mat_status"));
                                    bean.setMaterialNumber(oj.getString("mat_no"));
                                    materialList.add(bean);
                                }
                                materialAdapter.notifyDataSetChanged();
                                materialRecycler.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(MaterialActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                materialRecycler.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setAdapter() {
        materialTypeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,materialTypeList);
        materialTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaterialType.setAdapter(materialTypeAdapter);
        materialRecycler.setLinearLayout();
        materialAdapter=new MaterialAdapter(materialList,this);
        materialRecycler.setAdapter(materialAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter=new MaterialAdapter(searchList,this);
        searchRecycler.setAdapter(searchAdapter);
    }

    private void initData() {
        searchProgress.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(new ConnectUrl().getMaterialUrl())
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
                                    MaterialBean bean=new MaterialBean();
                                    bean.setMaterialName(oj.getString("mat_name"));
                                    bean.setMaterialNumber(oj.getString("mat_no"));
                                    bean.setMaterialStatus(oj.getString("mat_status"));
                                    bean.setMaterialType(oj.getString("static_name"));
                                    materialList.add(bean);
                                }
                                materialAdapter.notifyDataSetChanged();
                                searchProgress.setVisibility(View.GONE);
                            } else if ("400".equals(status)) {
                                searchProgress.setVisibility(View.GONE);
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(MaterialActivity.this, LoginActivity.class));
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
                .addParams("static_no", "3")
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
                                materialTypeNum.clear();
                                materialTypeList.clear();
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    materialTypeList.add(oj.getString("static_name"));
                                    materialTypeNum.add(oj.getString("id"));
                                }
                                materialTypeAdapter.notifyDataSetChanged();
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
