package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nandity.disastersystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        toolbar.setTitle("创建任务");
    }

}
