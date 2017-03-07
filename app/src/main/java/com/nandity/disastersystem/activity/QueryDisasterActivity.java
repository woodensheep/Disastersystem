package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.QueryDisasterAdapter;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.TaskBean;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

public class QueryDisasterActivity extends AppCompatActivity {
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    private String TAG = "QueryDisasterActivity";
    @BindView(R.id.search_clear)
    ImageView searchClear;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.rc_query_disaster)
    RecyclerView rcQueryDisaster;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private String sessionId;
    private List<TaskBean> mListData;
    /*保存查询的结果数据*/
    private List<TaskBean> mListQueryData;
    private LinearLayoutManager mLayoutManager;
    private QueryDisasterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_disaster);
        ButterKnife.bind(this);

        mListData = new ArrayList<TaskBean>();
        mListQueryData = new ArrayList<TaskBean>();
        mLayoutManager = new LinearLayoutManager(getContext());

        rcQueryDisaster.setLayoutManager(mLayoutManager);
        //固定高度
        rcQueryDisaster.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new QueryDisasterAdapter(getContext(), mListQueryData);
        rcQueryDisaster.setAdapter(mAdapter);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
        setOkHttp();
        setListeners();
    }

    private void setListeners() {

        mAdapter.setOnItemClickListener(new QueryDisasterAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TaskBean taskBean) {
                Intent intent = new Intent();
                intent.putExtra("str1", taskBean.getMDisasterID());
                intent.putExtra("str2", taskBean.getMDisaster());
                setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish();//此处一定要调用finish()方法
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListQueryData.clear();
                String str1=etSearchContent.getText().toString().trim();
                String result;
                //判断mListData名称是否包含输入的关键字
                for (int i=0;i<mListData.size();i++){
                    if(mListData.get(i).getMDisaster().toString().toString().indexOf(str1)>=0){
                        //说明，路径中包含关键字
                        mListQueryData.add(mListData.get(i));
                    }else{
                        //说明，路径中不包含关键字
                    }
                }
                if(mListQueryData.size()!=0){
                    mAdapter.notifyDataSetChanged();
                    result = "未找到到任何匹配的文件";
                }
            }
        });
    }


    private void setOkHttp() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getDisInfoListUrl())
                    .addParams("sessionId", sessionId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            ToastUtils.showShortToast("网络故障，请检查网络！");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            String msg, status;
                            Log.d(TAG, "返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    mListData.clear();
                                    JSONArray message = object.getJSONArray("message");
                                    for (int i = 0; i < message.length(); i++) {//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        Log.d(TAG, "message：id-" + oj.getString("id"));
                                        TaskBean taskBean = new TaskBean();
                                        taskBean.setMDisasterID(oj.getString("id"));
                                        taskBean.setMDisaster(oj.getString("dis_name"));
                                        mListData.add(taskBean);
                                        mListQueryData.add(taskBean);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            ToastUtils.showShortToast("加载失败！");
        }
    }
}
