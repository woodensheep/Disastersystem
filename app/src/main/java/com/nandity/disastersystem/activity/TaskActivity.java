package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.greendao.annotation.Id;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

/**
 * 填报反馈信息
 */
public class TaskActivity extends AppCompatActivity {
    private String TAG="TaskActivity";

    @BindView(R.id.tv_task_id)
    TextView tvTaskId;
    @BindView(R.id.tv_workers_time)
    TextView tvWorkersTime;
    @BindView(R.id.tv_task_disaster)
    TextView tvTaskDisaster;
    @BindView(R.id.tv_task_address)
    TextView tvTaskAddress;
    @BindView(R.id.tv_task_survey_time)
    TextView tvTaskSurveyTime;
    @BindView(R.id.tv_task_kind)
    TextView tvTaskKind;
    @BindView(R.id.tv_task_township)
    TextView tvTaskTownship;
    @BindView(R.id.tv_task_reporter)
    TextView tvTaskReporter;
    @BindView(R.id.btn_task_com)
    Button btnTaskCom;


    private Intent intent;
    private String mId;
    private SharedPreferences sp;
    private String sessionId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        intent=getIntent();
        mId=intent.getStringExtra("TaskID");
        tvTaskId.setText(mId);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId", "");

        initData();

    }

    private void initData() {
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
        setOkHttp();
    }


    private void setOkHttp() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getOneTaskUrl())
                    .addParams("id", mId)
                    .addParams("sessionId",sessionId)
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
                            Log.d(TAG, "登录返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    //mListData.clear();
                                    JSONArray message=object.getJSONArray("message");
//                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
//                                        JSONObject oj = message.getJSONObject(i);
//                                        Log.d(TAG, "message：id-" + oj.getString("id"));
//                                        TaskInfoBean taskInfoBean=new TaskInfoBean();
//                                        taskInfoBean.setmRowNumber(oj.getString("id"));
//                                        taskInfoBean.setmTaskName(oj.getString("task_name"));
//                                        taskInfoBean.setmAddress(oj.getString("survey_site"));
//                                        taskInfoBean.setmDisaster(oj.getString("dis_name"));
//                                       // mListData.add(taskInfoBean);
//                                    }
                                    //ToastUtils.showShortToast(msg);
                                   // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
            progressDialog.dismiss();
            e.printStackTrace();
            ToastUtils.showShortToast("加载失败！");
        }
    }

    

}
