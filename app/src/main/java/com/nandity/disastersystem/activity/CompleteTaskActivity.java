package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nandity.disastersystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteTaskActivity extends AppCompatActivity {

    @BindView(R.id.com_task_toolbar)
    Toolbar comTaskToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);
        ButterKnife.bind(this);
        comTaskToolbar.setTitle("完成任务");
        setSupportActionBar(comTaskToolbar);
    }


}
