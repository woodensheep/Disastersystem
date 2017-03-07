package com.nandity.disastersystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.FillInfoActivity;
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by ChenPeng on 2017/3/6.
 */

public class BaseInfoFragment extends Fragment {
    @BindView(R.id.iv_baseinfo_image)
    ImageView ivBaseinfoImage;
    @BindView(R.id.et_baseinfo_name)
    EditText etBaseinfoName;
    @BindView(R.id.et_baseinfo_lng)
    EditText etBaseinfoLng;
    @BindView(R.id.et_baseinfo_lat)
    EditText etBaseinfoLat;
    @BindView(R.id.et_baseinfo_address)
    EditText etBaseinfoAddress;
    @BindView(R.id.sp_baseinfo_village)
    Spinner spBaseinfoVillage;
    @BindView(R.id.et_baseinfo_contact)
    EditText etBaseinfoContact;
    @BindView(R.id.et_baseinfo_mobile)
    EditText etBaseinfoMobile;
    @BindView(R.id.sp_baseinfo_level)
    Spinner spBaseinfoLevel;
    @BindView(R.id.sp_baseinfo_type)
    Spinner spBaseinfoType;
    @BindView(R.id.sp_baseinfo_isdisaster)
    Spinner spBaseinfoIsdisaster;
    @BindView(R.id.btn_baseinfo_save)
    Button btnBaseinfoSave;
    private static final String TAG="BaseInfoFragment";
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private String[] levels;
    private String[] neworold;
    private String[] isdisasters;
    private List<String> villageList;
    private TaskInfoBean taskInfoBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId","");
        taskInfoBean=((FillInfoActivity)getActivity()).taskInfoBean;
        initView();
        initData();
        setListener();
        return view;
    }

    private void initData() {
        OkHttpUtils.get().url(new ConnectUrl().getAreaListUrl())
                .addParams("sessionId",sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网络！");
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status,msg;
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
                        try {
                            JSONObject object=new JSONObject(response);
                            status=object.getString("status");
                            msg=object.getString("message");
                            if("200".equals(status)){
                                villageList=new ArrayList<String>();
                                JSONArray array=object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject json=array.getJSONObject(i);
                                    villageList.add(json.getString("area_name"));
                                }
                                Log.d(TAG,"乡镇数据："+villageList.toString());
                                ArrayAdapter villageAdapter=new ArrayAdapter<String>(context,R.layout.spinner_item,villageList);
                                villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spBaseinfoVillage.setAdapter(villageAdapter);
                            }else if("400".equals(status)){
                                ToastUtils.showShortToast(msg);
                                context.startActivity(new Intent(context, LoginActivity.class));
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setListener() {
        btnBaseinfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        levels = new String[]{"小", "中", "大", "特大"};
        neworold = new String[]{"新灾害点", "旧灾害点"};
        isdisasters = new String[]{"是", "否"};
        ArrayAdapter levelsAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, levels);
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaseinfoLevel.setAdapter(levelsAdapter);
        ArrayAdapter neworoldAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, neworold);
        neworoldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaseinfoType.setAdapter(neworoldAdapter);//TODO 通过后台信息设置新旧灾害点
        if("1".equals(taskInfoBean.getmDisasterType())){
            spBaseinfoType.setSelection(0,true);
        }else {
            spBaseinfoType.setSelection(1,false);
        }
        ArrayAdapter isdisasterAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, isdisasters);
        isdisasterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaseinfoIsdisaster.setAdapter(isdisasterAdapter);//TODO 如果是新的，可以选择，旧的设置为“是”
        if("2".equals(taskInfoBean.getmDisasterType())){
            spBaseinfoIsdisaster.setSelection(0,true);
            spBaseinfoIsdisaster.setEnabled(false);
        }
    }
}
