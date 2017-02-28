package com.nandity.disastersystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nandity.disastersystem.R;

public class SettingsActivity extends AppCompatActivity {

    private Intent intent;
    private TextView tv_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initviews();
        intent = getIntent();
        tv_settings.setText(intent.getStringExtra("settings"));
    }

    private void initviews() {
        tv_settings = (TextView) findViewById(R.id.tv_settings);
    }
}
