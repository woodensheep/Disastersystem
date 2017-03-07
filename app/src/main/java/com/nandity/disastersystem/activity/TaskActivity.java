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
    @BindView(R.id.tv_task_state)
    TextView tvTaskState;
    private String TAG = "TaskActivity";

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
    private TaskInfoBean taskInfoBean;
    private String[] mStates=new String[]{"未发送","已发送","已反馈","已完成","废弃"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        intent = getIntent();
        mId = intent.getStringExtra("TaskID");
        tvTaskId.setText(mId);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");

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
                            Log.d(TAG, "登录返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                if ("200".equals(status)) {
                                    //mListData.clear();
                                    JSONArray message = object.getJSONArray("message");
                                    JSONObject oj = message.getJSONObject(0);
                                    taskInfoBean = new TaskInfoBean();
                                    taskInfoBean.setmRowNumber(mId);
                                    taskInfoBean.setmHappenTime(oj.getString("happen_time"));
                                    taskInfoBean.setmTaskName(oj.getString("task_name"));
                                    taskInfoBean.setmSendName(oj.getString("send_name"));
                                    taskInfoBean.setmStartTime(oj.getString("start_time"));
                                    taskInfoBean.setmDisaster(oj.getString("dis_name"));
                                    taskInfoBean.setmAddress(oj.getString("survey_site"));
                                    taskInfoBean.setmSurveyTime(oj.getString("survey_time"));
                                    taskInfoBean.setmAreaName(oj.getString("area_name"));
                                    taskInfoBean.setmName(oj.getString("survey_name"));
                                    taskInfoBean.setmTaskState(oj.getString("task_state"));
                                    Log.d(TAG, "message：id-" + taskInfoBean.getmAddress());
                                    tvTaskId.setText("调查任务-"+taskInfoBean.getmDisaster());
                                    tvWorkersTime.setText("发起人："+taskInfoBean.getmSendName() + " 时间：" + taskInfoBean.getmStartTime());
                                    tvTaskDisaster.setText("灾害点："+taskInfoBean.getmDisaster());
                                    tvTaskAddress.setText("调查地点："+taskInfoBean.getmAddress());
                                    tvTaskSurveyTime.setText("调查时间："+taskInfoBean.getmSurveyTime());
                                    tvTaskTownship.setText("所属乡镇："+taskInfoBean.getmAreaName());
                                    tvTaskKind.setText("发生时间："+taskInfoBean.getmHappenTime());
                                    tvTaskReporter.setText("调查人："+taskInfoBean.getmName());
                                    tvTaskState.setText("任务状态："+mStates[Integer.parseInt(taskInfoBean.getmTaskState())]);
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
