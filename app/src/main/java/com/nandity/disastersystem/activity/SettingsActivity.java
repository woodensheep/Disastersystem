package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nandity.disastersystem.R;

import static com.nandity.disastersystem.R.id.et_address;
import static com.nandity.disastersystem.R.id.et_port;
import static com.nandity.disastersystem.R.id.tv_address;
import static com.nandity.disastersystem.R.id.tv_port;

public class SettingsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intent = getIntent();
        mType=Integer.parseInt(intent.getStringExtra("settings_type"));
        //Toast.makeText(this, intent.getStringExtra("settings_type"), Toast.LENGTH_SHORT).show();
        initviews();
        setviews();

    }

    private void setviews() {
        tvTitle_bar.setText(intent.getStringExtra("settings"));
        switch (mType){
            case 3 :
                rgType2.setVisibility(View.VISIBLE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("手机内存");
                rbType2_2.setText("SD卡");
                break;
            case 4 :
                rgType2.setVisibility(View.VISIBLE);
                rbType2_3.setVisibility(View.GONE);
                rbType2_1.setText("内置后摄像头");
                rbType2_2.setText("内置前摄像头");
                break;
            case 5 :
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
                tvType3_4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                break;

            default :
                llType1.setVisibility(View.VISIBLE);
                tvAddress.setText(intent.getStringExtra("settings")+"服务器地址：");
                break;
        }

    }


    private void initviews() {

        tvTitle_bar= (TextView) findViewById(R.id.title_bar);

        llType1= (LinearLayout) findViewById(R.id.ll_type1);
        tvAddress= (TextView) findViewById(tv_address);
        tvPort= (TextView) findViewById(tv_port);
        etAddress= (EditText) findViewById(et_address);
        etPort= (EditText) findViewById(et_port);


        rgType2= (RadioGroup) findViewById(R.id.rg_type2);
        rbType2_1= (RadioButton) findViewById(R.id.rb_1);
        rbType2_2= (RadioButton) findViewById(R.id.rb_2);
        rbType2_3= (RadioButton) findViewById(R.id.rb_3);

        llType3= (LinearLayout) findViewById(R.id.ll_type3);
        tvType3_1= (TextView) findViewById(R.id.tv_type3_1);
        tvType3_4= (TextView) findViewById(R.id.tv_type3_4);

    }
}
