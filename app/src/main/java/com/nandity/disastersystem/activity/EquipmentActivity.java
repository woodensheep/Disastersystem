package com.nandity.disastersystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipmentActivity extends AppCompatActivity {

    @BindView(R.id.sp_person_type)
    Spinner spPersonType;
    @BindView(R.id.et_equipment_name)
    EditText etEquipmentName;
    @BindView(R.id.iv_equipment_search)
    ImageView ivEquipmentSearch;
    @BindView(R.id.tv_equipment_cancel)
    TextView tvEquipmentCancel;
    @BindView(R.id.equipment_recycler)
    PullLoadMoreRecyclerView equipmentRecycler;
    @BindView(R.id.ll_normal)
    LinearLayout llNormal;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.search_progress)
    RelativeLayout searchProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);
    }
}
