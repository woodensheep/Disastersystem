package com.nandity.disastersystem.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.receiver.UpdataService;
import com.nandity.disastersystem.utils.MyUtils;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

import static com.nandity.disastersystem.R.id.et_address;
import static com.nandity.disastersystem.R.id.et_port;
import static com.nandity.disastersystem.R.id.tv_address;
import static com.nandity.disastersystem.R.id.tv_port;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "MainActivity", status, msg;
    private Intent intent;
    private int mType;
    private TextView tvTitle_bar;

    private LinearLayout llType1;
    private TextView tvAddress;
    private TextView tvPort;
    private EditText etAddress;
    private EditText etPort;

    private RadioGroup rgType2;
    private RadioButton rbType2_1;
    private RadioButton rbType2_2;
    private RadioButton rbType2_3;

    private LinearLayout llType3;
    private TextView tvType3_1;
    private TextView tvType3_4;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intent = getIntent();
        mType = Integer.parseInt(intent.getStringExtra("settings_type"));
        //Toast.makeText(this, intent.getStringExtra("settings_type"), Toast.LENGTH_SHORT).show();
        initviews();
        setviews();

    }

    private void setviews() {
        tvTitle_bar.setText(intent.getStringExtra("settings"));
        switch (mType) {
            case 3:
                rgType2.setVisibility(View.VISIBLE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("手机内存");
                rbType2_2.setText("SD卡");
                break;
            case 4:
                rgType2.setVisibility(View.VISIBLE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("内置后摄像头");
                rbType2_2.setText("内置前摄像头");
                break;
            case 5:
                rgType2.setVisibility(View.VISIBLE);
                rbType2_2.setVisibility(View.GONE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("15");
                break;
            case 6:
                rgType2.setVisibility(View.VISIBLE);
                rbType2_2.setVisibility(View.GONE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("512k");
                break;
            case 7:
                rgType2.setVisibility(View.VISIBLE);
                rbType2_2.setVisibility(View.GONE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("1280*720");
                break;
            case 8:
                llType3.setVisibility(View.VISIBLE);
                tvType3_1.setText("app版本号： " + MyUtils.getVerName(this));
                tvType3_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                button.setVisibility(View.GONE);
                break;

            default:
                llType1.setVisibility(View.VISIBLE);
                tvAddress.setText(intent.getStringExtra("settings") + "服务器地址：");
                break;
        }

    }

    private void updateManager() {
        Log.d(TAG, "updateManager:" + MyUtils.getVerCode(this));
        OkHttpUtils.get().url(new ConnectUrl().getUpdateVerCodeUrl())
                .addParams("versionNumber", MyUtils.getVerCode(this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网路！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "返回的数据：" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            Log.d(TAG, "用户数据：" + status);
                            if ("200".equals(status)) {
                                showNoticeDialog();
                            } else if ("300".equals(status)) {
                                ToastUtils.showShortToast("暂时没有更新");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showNoticeDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("软件版本更新");
        builder.setMessage("版本号： "+MyUtils.getVerName(this));
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent service = new Intent(SettingsActivity.this, UpdataService.class);
                startService(service);
//                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
//                intent.putExtra("settings_type","8");
//                intent.putExtra("settings", "系统信息");
//                startActivity(intent);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void initviews() {

        tvTitle_bar = (TextView) findViewById(R.id.title_bar);
        button = (Button) findViewById(R.id.btn_settings_save);

        llType1 = (LinearLayout) findViewById(R.id.ll_type1);
        tvAddress = (TextView) findViewById(tv_address);
        tvPort = (TextView) findViewById(tv_port);
        etAddress = (EditText) findViewById(et_address);
        etPort = (EditText) findViewById(et_port);


        rgType2 = (RadioGroup) findViewById(R.id.rg_type2);
        rbType2_1 = (RadioButton) findViewById(R.id.rb_1);
        rbType2_2 = (RadioButton) findViewById(R.id.rb_2);
        rbType2_3 = (RadioButton) findViewById(R.id.rb_3);

        llType3 = (LinearLayout) findViewById(R.id.ll_type3);
        tvType3_1 = (TextView) findViewById(R.id.tv_type3_1);
        tvType3_4 = (TextView) findViewById(R.id.tv_type3_4);
        tvType3_4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_type3_4:
//                Intent service = new Intent(SettingsActivity.this,UpdataService.class);
//                  startService(service);
                updateManager();

                break;
            default:
        }

    }
}
