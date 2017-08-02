package com.nandity.disastersystem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.TaskAccountInfo;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

public class TaskAccountActivity extends Activity implements View.OnClickListener{

    private String[] rows1 = new String[]{"类型", "滑坡", "泥石流", "危岩", "不稳定斜坡"
            , "地面塌陷", "地裂缝", "塌岸", "合计"};

    private String[] columns1 = new String[]{"新增", "原有", "险情", "户数(户)", "人数(人)"
            , "在家人数(人)", "房屋间数(间)", "面积(米*米)", "降雨", "风化", "库水位", "切坡", "加载", "冲刷坡脚","户数(户)","人数(人)"};
    private String[] columns2 = new String[]{"新增", "原有", "灾情", "死亡(人)", "重伤(人)"
            , "直接损失(万元)", "房屋坍塌(间)", "面积(米*米)", "降雨", "风化", "库水位", "切坡", "加载", "冲刷坡脚","户数(户)","人数(人)"};
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private int row;
    private int column;
    private TableLayout tableLayout;
    private TableLayout tableLayout2;
    private SharedPreferences sp;
    private String sessionId;
    private String TAG = "TaskAccountActivity";
    private ProgressDialog progressDialog;
    private Context context;
    private TextView tvStartTime,tvEndTime,tvAccountStatistics;
    private DatePicker datePicker;// 日期控件
    private AlertDialog dialog;// 选择时间日期对话框
    private List<TaskAccountInfo> taskAccountInfo1,taskAccountInfo2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_account);
        context = TaskAccountActivity.this;
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        row = 9;
        column = 17;
        taskAccountInfo1=new ArrayList<TaskAccountInfo>();
        taskAccountInfo2=new ArrayList<TaskAccountInfo>();
        initViews();
        setOkHttp1();
    }

    private void initViews() {
        tvStartTime= (TextView) findViewById(R.id.tv_time_start);
        tvEndTime= (TextView) findViewById(R.id.tv_time_end);
        tvAccountStatistics=(TextView) findViewById(R.id.tv_account_statistics);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvAccountStatistics.setOnClickListener(this);
    }


    private void initDatas1() {
        if (row > 0 && column > 0) {
            //把输入的行和列转为整形
            int row_int = row;
            int col_int = column;

            //获取控件tableLayout
            tableLayout = (TableLayout) findViewById(R.id.table1);
            //清除表格所有行
            //tableLayout.removeAllViews();
            //全部列自动填充空白处
            tableLayout.setStretchAllColumns(true);
            //生成X行，Y列的表格
            for (int i = 1; i <= row_int; i++) {
                TableRow tableRow = new TableRow(TaskAccountActivity.this);
                if (i == 1) {
                    TextView tv1 = getNewTextView(context);
                    tv1.setText(rows1[i - 1]);
                    tableRow.addView(tv1);
                    for (int j = 2; j <= col_int; j++) {
                        //tv用于显示
                        TextView tv = getNewTextView(context);
                        tv.setText(columns1[j - 2]);
                        tableRow.addView(tv);
                    }
                }else {
                    for (int j = 1; j <= col_int; j++) {
                        if (j == 1 && i != 1) {
                            TextView tv = getNewTextView(context);
                            tv.setText(rows1[i - 1]);
                            tableRow.addView(tv);
                        } else {
                            TextView tv = getNewTextView(context);
                            for (int m=0;m<taskAccountInfo1.size();m++){
                                String num = null;
                                if(taskAccountInfo1.get(m).getText().equals(rows1[i - 1])){
                                    switch (j){
                                        case 2:num=taskAccountInfo1.get(m).getXin();break;
                                        case 3:num=taskAccountInfo1.get(m).getJiu();break;
                                        case 4:num=taskAccountInfo1.get(m).getYg();break;
                                        case 5:num=taskAccountInfo1.get(m).getQz_person_h();break;
                                        case 6:num=taskAccountInfo1.get(m).getQz_person_no();break;
                                        case 7:num=taskAccountInfo1.get(m).getQz_person_zj();break;
                                        case 8:num=taskAccountInfo1.get(m).getQz_house_no();break;
                                        case 9:num=taskAccountInfo1.get(m).getQz_house_area();break;
                                        case 10:num=taskAccountInfo1.get(m).getJy();break;
                                        case 11:num=taskAccountInfo1.get(m).getFh();break;
                                        case 12:num=taskAccountInfo1.get(m).getKsw();break;
                                        case 13:num=taskAccountInfo1.get(m).getQp();break;
                                        case 14:num=taskAccountInfo1.get(m).getJz();break;
                                        case 15:num=taskAccountInfo1.get(m).getCspj();break;
                                        case 16:num=taskAccountInfo1.get(m).getEmergency_hu_no();break;
                                        case 17:num=taskAccountInfo1.get(m).getEmergency_person_no();break;
                                    }
                                    tv.setText(num);
                                }
                            }
                            tableRow.addView(tv);
                        }


                    }
                }

                //新建的TableRow添加到TableLayout

                tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));

            }
        } else {
        }
    }

    private void initDatas2() {
        if (row > 0 && column > 0) {
            //把输入的行和列转为整形
            int row_int = row;
            int col_int = column;

            //获取控件tableLayout
            tableLayout2 = (TableLayout) findViewById(R.id.table2);
            //清除表格所有行
            //tableLayout.removeAllViews();
            //全部列自动填充空白处
            tableLayout2.setStretchAllColumns(true);
            //生成X行，Y列的表格
            for (int i = 1; i <= row_int; i++) {
                TableRow tableRow = new TableRow(TaskAccountActivity.this);
                if (i == 1) {
                    TextView tv1 = getNewTextView(context);
                    tv1.setText(rows1[i - 1]);
                    tableRow.addView(tv1);
                    for (int j = 2; j <= col_int; j++) {
                        //tv用于显示
                        TextView tv = getNewTextView(context);
                        tv.setText(columns2[j - 2]);
                        tableRow.addView(tv);
                    }
                }else {
                    for (int j = 1; j <= col_int; j++) {
                        if (j == 1 && i != 1) {
                            TextView tv = getNewTextView(context);
                            tv.setText(rows1[i - 1]);
                            tableRow.addView(tv);
                        } else {
                            TextView tv = getNewTextView(context);
                            for (int m=0;m<taskAccountInfo2.size();m++){
                                String num = null;
                                if(taskAccountInfo2.get(m).getText().equals(rows1[i - 1])){
                                    switch (j){
                                        case 2:num=taskAccountInfo2.get(m).getXin();break;
                                        case 3:num=taskAccountInfo2.get(m).getJiu();break;
                                        case 4:num=taskAccountInfo2.get(m).getYg();break;
                                        case 5:num=taskAccountInfo2.get(m).getDead_no();break;
                                        case 6:num=taskAccountInfo2.get(m).getSs_no();break;
                                        case 7:num=taskAccountInfo2.get(m).getZj_money();break;
                                        case 8:num=taskAccountInfo2.get(m).getHouse_kt_no();break;
                                        case 9:num=taskAccountInfo2.get(m).getHouse_kt_mj();break;
                                        case 10:num=taskAccountInfo2.get(m).getJy();break;
                                        case 11:num=taskAccountInfo2.get(m).getFh();break;
                                        case 12:num=taskAccountInfo2.get(m).getKsw();break;
                                        case 13:num=taskAccountInfo2.get(m).getQp();break;
                                        case 14:num=taskAccountInfo2.get(m).getJz();break;
                                        case 15:num=taskAccountInfo2.get(m).getCspj();break;
                                        case 16:num=taskAccountInfo2.get(m).getEmergency_hu_no();break;
                                        case 17:num=taskAccountInfo2.get(m).getEmergency_person_no();break;
                                    }
                                    tv.setText(num);
                                }
                            }
                            tableRow.addView(tv);
                        }


                    }
                }

                //新建的TableRow添加到TableLayout

                tableLayout2.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));

            }
        } else {
        }
    }

    private TextView getNewTextView(Context context) {
        TextView tv = new TextView(context);
        tv.setBackgroundResource(R.drawable.table_tv);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 10, 10, 10);
        return tv;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_time_start:
                showDialog(tvStartTime);
                break;
            case R.id.tv_time_end:
                showDialog(tvEndTime);
                break;
            case R.id.tv_account_statistics:
                if("".equals(tvStartTime.getText().toString())||"".equals(tvEndTime.getText().toString())){

                }else{
                    taskAccountInfo1.clear();
                    taskAccountInfo2.clear();
                    tableLayout.removeViews(1,9);
                    tableLayout2.removeViews(1,9);
                    setOkHttp3();
                }
                break;
        }
    }

    /**
     * 显示获取时间的对话框
     */
    private void showDialog(final TextView tvTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_date_time1, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        builder.setTitle("设置时间");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String month1 = month + "";
                String day1 = day + "";
                if (month <= 9) {
                    month1 = 0 + month1;
                }
                if (day <= 9) {
                    day1 = 0 + day1;
                }
                tvTime.setText(year + "-" + month1 + "-" + day1);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    /**
     * 1代表灾情
     */
    private void setOkHttp1() {
        progressDialog.show();
        try {
            OkHttpUtils.get().url(new ConnectUrl().getParameterUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("type", 1+"")
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
                                //Log.d(TAG,"----"+object.toString());
                                if ("200".equals(status)) {
                                    JSONArray array=object.getJSONArray("message");
                                    for(int i=0;i<array.length();i++){
                                        JSONObject ob=array.getJSONObject(i);
                                        TaskAccountInfo info=new TaskAccountInfo();
                                        info.setText(ob.getString("text"));
                                        info.setXin(ob.getString("xin"));
                                        info.setJiu(ob.getString("jiu"));
                                        info.setYg(ob.getString("yg"));
                                        info.setQz_person_h(ob.getString("qz_person_h"));
                                        info.setQz_person_no(ob.getString("qz_person_no"));
                                        info.setQz_person_zj(ob.getString("qz_person_zj"));
                                        info.setQz_house_no(ob.getString("qz_house_no"));
                                        info.setQz_house_area(ob.getString("qz_house_area"));
                                        info.setJy(ob.getString("jy"));
                                        info.setFh(ob.getString("fh"));
                                        info.setKsw(ob.getString("ksw"));
                                        info.setQp(ob.getString("qp"));
                                        info.setJz(ob.getString("jz"));
                                        info.setCspj(ob.getString("cspj"));
                                        info.setDead_no(ob.getString("dead_no"));
                                        info.setSs_no(ob.getString("ss_no"));
                                        info.setZj_money(ob.getString("zj_money"));
                                        info.setHouse_kt_no(ob.getString("house_kt_no"));
                                        info.setHouse_kt_mj(ob.getString("house_kt_mj"));
                                        info.setEmergency_hu_no(ob.getString("emergency_hu_no"));
                                        info.setEmergency_person_no(ob.getString("emergency_person_no"));
                                        Log.d(TAG, "---"+info.toString());
                                        taskAccountInfo2.add(info);
                                    }
                                    setOkHttp2();
                                } else if ("400".equals(status)) {
                                    progressDialog.dismiss();
                                    ToastUtils.showShortToast(msg);
                                    sp.edit().putBoolean("isLogin", false).apply();
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


    /**
     *  .addParams("type", 2+"")
     * 2代表险情
     */
    private void setOkHttp2() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getParameterUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("type", 2+"")
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
                                //Log.d(TAG,"----"+object.toString());
                                if ("200".equals(status)) {
                                    JSONArray array=object.getJSONArray("message");
                                    for(int i=0;i<array.length();i++) {
                                        JSONObject ob = array.getJSONObject(i);
                                        TaskAccountInfo info = new TaskAccountInfo();
                                        info.setText(ob.getString("text"));
                                        info.setXin(ob.getString("xin"));
                                        info.setJiu(ob.getString("jiu"));
                                        info.setYg(ob.getString("yg"));
                                        info.setQz_person_h(ob.getString("qz_person_h"));
                                        info.setQz_person_no(ob.getString("qz_person_no"));
                                        info.setQz_person_zj(ob.getString("qz_person_zj"));
                                        info.setQz_house_no(ob.getString("qz_house_no"));
                                        info.setQz_house_area(ob.getString("qz_house_area"));
                                        info.setJy(ob.getString("jy"));
                                        info.setFh(ob.getString("fh"));
                                        info.setKsw(ob.getString("ksw"));
                                        info.setQp(ob.getString("qp"));
                                        info.setJz(ob.getString("jz"));
                                        info.setCspj(ob.getString("cspj"));
                                        info.setDead_no(ob.getString("dead_no"));
                                        info.setSs_no(ob.getString("ss_no"));
                                        info.setZj_money(ob.getString("zj_money"));
                                        info.setHouse_kt_no(ob.getString("house_kt_no"));
                                        info.setHouse_kt_mj(ob.getString("house_kt_mj"));
                                        info.setEmergency_hu_no(ob.getString("emergency_hu_no"));
                                        info.setEmergency_person_no(ob.getString("emergency_person_no"));
                                        Log.d(TAG, "---"+info.toString());
                                        taskAccountInfo1.add(info);
                                    }
                                    initDatas1();
                                    initDatas2();
                                } else if ("400".equals(status)) {
                                    progressDialog.dismiss();
                                    ToastUtils.showShortToast(msg);
                                    sp.edit().putBoolean("isLogin", false).apply();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
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

    private void setOkHttp3() {
        progressDialog.show();
        try {
            OkHttpUtils.get().url(new ConnectUrl().getParameterUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("type", 1+"")
                    .addParams("startTime", tvStartTime.getText().toString().trim())
                    .addParams("endTime",  tvEndTime.getText().toString().trim())
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
                                //Log.d(TAG,"----"+object.toString());
                                if ("200".equals(status)) {
                                    JSONArray array=object.getJSONArray("message");
                                    for(int i=0;i<array.length();i++) {
                                        JSONObject ob = array.getJSONObject(i);
                                        TaskAccountInfo info = new TaskAccountInfo();
                                        info.setText(ob.getString("text"));
                                        info.setXin(ob.getString("xin"));
                                        info.setJiu(ob.getString("jiu"));
                                        info.setYg(ob.getString("yg"));
                                        info.setQz_person_h(ob.getString("qz_person_h"));
                                        info.setQz_person_no(ob.getString("qz_person_no"));
                                        info.setQz_person_zj(ob.getString("qz_person_zj"));
                                        info.setQz_house_no(ob.getString("qz_house_no"));
                                        info.setQz_house_area(ob.getString("qz_house_area"));
                                        info.setJy(ob.getString("jy"));
                                        info.setFh(ob.getString("fh"));
                                        info.setKsw(ob.getString("ksw"));
                                        info.setQp(ob.getString("qp"));
                                        info.setJz(ob.getString("jz"));
                                        info.setCspj(ob.getString("cspj"));
                                        info.setDead_no(ob.getString("dead_no"));
                                        info.setSs_no(ob.getString("ss_no"));
                                        info.setZj_money(ob.getString("zj_money"));
                                        info.setHouse_kt_no(ob.getString("house_kt_no"));
                                        info.setHouse_kt_mj(ob.getString("house_kt_mj"));
                                        info.setEmergency_hu_no(ob.getString("emergency_hu_no"));
                                        info.setEmergency_person_no(ob.getString("emergency_person_no"));
                                        Log.d(TAG, "---"+info.toString());
                                        taskAccountInfo2.add(info);
                                    }
                                    setOkHttp4();
                                } else if ("400".equals(status)) {
                                    progressDialog.dismiss();
                                    ToastUtils.showShortToast(msg);
                                    sp.edit().putBoolean("isLogin", false).apply();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
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
    private void setOkHttp4() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getParameterUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("type", 2+"")
                    .addParams("startTime", tvStartTime.getText().toString().trim())
                    .addParams("endTime",  tvEndTime.getText().toString().trim())
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
                                //Log.d(TAG,"----"+object.toString());
                                if ("200".equals(status)) {
                                    JSONArray array=object.getJSONArray("message");
                                    for(int i=0;i<array.length();i++) {
                                        JSONObject ob = array.getJSONObject(i);
                                        TaskAccountInfo info = new TaskAccountInfo();
                                        info.setText(ob.getString("text"));
                                        info.setXin(ob.getString("xin"));
                                        info.setJiu(ob.getString("jiu"));
                                        info.setYg(ob.getString("yg"));
                                        info.setQz_person_h(ob.getString("qz_person_h"));
                                        info.setQz_person_no(ob.getString("qz_person_no"));
                                        info.setQz_person_zj(ob.getString("qz_person_zj"));
                                        info.setQz_house_no(ob.getString("qz_house_no"));
                                        info.setQz_house_area(ob.getString("qz_house_area"));
                                        info.setJy(ob.getString("jy"));
                                        info.setFh(ob.getString("fh"));
                                        info.setKsw(ob.getString("ksw"));
                                        info.setQp(ob.getString("qp"));
                                        info.setJz(ob.getString("jz"));
                                        info.setCspj(ob.getString("cspj"));
                                        info.setDead_no(ob.getString("dead_no"));
                                        info.setSs_no(ob.getString("ss_no"));
                                        info.setZj_money(ob.getString("zj_money"));
                                        info.setHouse_kt_no(ob.getString("house_kt_no"));
                                        info.setHouse_kt_mj(ob.getString("house_kt_mj"));
                                        info.setEmergency_hu_no(ob.getString("emergency_hu_no"));
                                        info.setEmergency_person_no(ob.getString("emergency_person_no"));
                                        Log.d(TAG, "---"+info.toString());
                                        taskAccountInfo1.add(info);
                                    }
                                    initDatas1();
                                    initDatas2();
                                } else if ("400".equals(status)) {
                                    progressDialog.dismiss();
                                    ToastUtils.showShortToast(msg);
                                    sp.edit().putBoolean("isLogin", false).apply();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
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
