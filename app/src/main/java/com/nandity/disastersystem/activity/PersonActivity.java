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
import com.nandity.disastersystem.adapter.PersonAdapter;
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

public class PersonActivity extends AppCompatActivity {
    private static final String TAG = "PersonActivity";
    @BindView(R.id.sp_person_type)
    Spinner spPersonType;
    @BindView(R.id.et_person_name)
    EditText etPersonName;
    @BindView(R.id.iv_person_search)
    ImageView ivPersonSearch;
    @BindView(R.id.tv_person_cancel)
    TextView tvPersonCancel;
    @BindView(R.id.person_recycler)
    PullLoadMoreRecyclerView personRecycler;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout searchProgress;
    private String sessionId;
    private SharedPreferences sp;
    private int pageNum = 0;
    private static int rowsNum = 10;
    private RecyclerView mRecyclerView;
    private List<PersonBean> personList = new ArrayList<>();
    private List<PersonBean> searchList = new ArrayList<>();
    private PersonAdapter personAdapter, searchAdapter;
    private ArrayAdapter<String> personTypeAdapter;
    private List<String> personTypeNum = new ArrayList<>();
    private List<String> personTypeList = new ArrayList<>();
    private String personName,personType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setLoadMore();
        setListener();
    }

    private void setListener() {
        ivPersonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personName=etPersonName.getText().toString().trim();
                personType=personTypeNum.get(spPersonType.getSelectedItemPosition());
                if (TextUtils.isEmpty(personName)&&spPersonType.getSelectedItemPosition()==0){
                    return;
                }
                searchProgress.setVisibility(View.VISIBLE);
                OkHttpUtils.get().url(new ConnectUrl().getPersonUrl())
                        .addParams("sessionId", sessionId)
                        .addParams("name", personName)
                        .addParams("type_id", personType)
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
                                        tvPersonCancel.setVisibility(View.VISIBLE);
                                        llNormal.setVisibility(View.INVISIBLE);
                                        JSONArray array = object.getJSONArray("message");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject oj = array.getJSONObject(i);
                                            PersonBean bean = new PersonBean();
                                            bean.setAddress(oj.getString("address"));
                                            bean.setMobile(oj.getString("mobile"));
                                            bean.setPersonName(oj.getString("name"));
                                            bean.setPersonType(oj.getString("static_name"));
                                            searchList.add(bean);
                                        }
                                        searchAdapter.notifyDataSetChanged();
                                        searchProgress.setVisibility(View.GONE);
                                    } else if ("400".equals(status)) {
                                        searchProgress.setVisibility(View.GONE);
                                        ToastUtils.showShortToast(msg);
                                        sp.edit().putBoolean("isLogin", false).apply();
                                        startActivity(new Intent(PersonActivity.this, LoginActivity.class));
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
        tvPersonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPersonCancel.setVisibility(View.GONE);
                llNormal.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setLoadMore() {
        mRecyclerView = personRecycler.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        personRecycler.setFooterViewText("正在加载...");
        personRecycler.setPullRefreshEnable(false);
        personRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                personRecycler.setRefreshing(true);
                loadMore();
            }
        });
    }

    private void loadMore() {
        pageNum += 10;
        OkHttpUtils.get().url(new ConnectUrl().getPersonUrl())
                .addParams("sessionId", sessionId)
                .addParams("page", pageNum + "")
                .addParams("rows", rowsNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        personRecycler.setPullLoadMoreCompleted();
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
                                    PersonBean bean = new PersonBean();
                                    bean.setPersonType(oj.getString("static_name"));
                                    bean.setPersonName(oj.getString("name"));
                                    bean.setMobile(oj.getString("mobile"));
                                    bean.setAddress(oj.getString("address"));
                                    personList.add(bean);
                                }
                                personAdapter.notifyDataSetChanged();
                                personRecycler.setPullLoadMoreCompleted();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(PersonActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShortToast(msg);
                                personRecycler.setPullLoadMoreCompleted();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setAdapter() {
        personTypeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,personTypeList);
        personTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPersonType.setAdapter(personTypeAdapter);
        personRecycler.setLinearLayout();
        personAdapter = new PersonAdapter(personList, this);
        personRecycler.setAdapter(personAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new PersonAdapter(searchList, this);
        searchRecycler.setAdapter(searchAdapter);
    }

    private void initData() {
        searchProgress.setVisibility(View.VISIBLE);
        OkHttpUtils.get().url(new ConnectUrl().getPersonUrl())
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
                                    PersonBean bean = new PersonBean();
                                    bean.setAddress(oj.getString("address"));
                                    bean.setMobile(oj.getString("mobile"));
                                    bean.setPersonName(oj.getString("name"));
                                    bean.setPersonType(oj.getString("static_name"));
                                    personList.add(bean);
                                }
                                personAdapter.notifyDataSetChanged();
                                searchProgress.setVisibility(View.GONE);
                            } else if ("400".equals(status)) {
                                searchProgress.setVisibility(View.GONE);
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(PersonActivity.this, LoginActivity.class));
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
                .addParams("static_no", "8")
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
                                personTypeNum.clear();
                                personTypeList.clear();
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    personTypeList.add(oj.getString("static_name"));
                                    personTypeNum.add(oj.getString("id"));
                                }
                                personTypeAdapter.notifyDataSetChanged();
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
