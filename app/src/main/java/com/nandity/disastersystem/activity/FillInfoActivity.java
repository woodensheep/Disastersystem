package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nandity.disastersystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FillInfoActivity extends AppCompatActivity {

    @BindView(R.id.fill_info_toolbar)
    Toolbar fillInfoToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);
        fillInfoToolbar.setTitle("信息填报");
        setSupportActionBar(fillInfoToolbar);

    }

}
