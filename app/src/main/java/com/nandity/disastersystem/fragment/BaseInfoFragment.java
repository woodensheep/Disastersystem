package com.nandity.disastersystem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.FillInfoActivity;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.BaseInfoBean;
import com.nandity.disastersystem.database.BaseInfoBeanDao;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

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
    private static final String TAG = "BaseInfoFragment";
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private String[] levels;
    private String[] neworold;
    private String[] isdisasters;
    private TaskInfoBean taskInfoBean;
    private boolean isSave = false;
    private Bitmap baseInfoBitmap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_info, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        taskInfoBean = ((FillInfoActivity) getActivity()).taskInfoBean;
        initView();
        initData();
        setListener();
        return view;
    }

    private void initData() {
        OkHttpUtils.get().url(new ConnectUrl().getDisasterPictureUrl())
                .addParams("dis_id",taskInfoBean.getmTaskId())
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        Log.e(TAG,"加载的图片："+response);
                        baseInfoBitmap=response;
                        Matrix matrix=new Matrix();
                        matrix.setScale(0.5f,0.5f);
                        Bitmap bmp = Bitmap.createBitmap(response, 0, 0, response.getWidth(), response.getHeight(), matrix, true);
                        ivBaseinfoImage.setImageBitmap(bmp);
                    }
                });
    }

    private void setListener() {
        btnBaseinfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBaseInfo();
            }
        });
        ivBaseinfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(context).inflate(R.layout.dialog_show_photo,null);
                ImageView imageView= (ImageView) view.findViewById(R.id.iv_dialog_picture);
                imageView.setImageBitmap(baseInfoBitmap);
                new MaterialDialog.Builder(getActivity())
                        .cancelable(true)
                        .customView(view,false)
                        .show();
            }
        });
    }

    private void saveBaseInfo() {
        String name = etBaseinfoName.getText().toString().trim();
        String lng = etBaseinfoLng.getText().toString().trim();
        String lat = etBaseinfoLat.getText().toString().trim();
        String address = etBaseinfoAddress.getText().toString().trim();
        String contact = etBaseinfoContact.getText().toString().trim();
        String mobile = etBaseinfoMobile.getText().toString().trim();
        String level = spBaseinfoLevel.getSelectedItemPosition() + "";
        String type = spBaseinfoType.getSelectedItemPosition() + "";
        String is = spBaseinfoIsdisaster.getSelectedItemPosition() + "";
        BaseInfoBean baseInfoBean = new BaseInfoBean();
        baseInfoBean.setTaskId(taskInfoBean.getmTaskId());
        if (TextUtils.isEmpty(name)) {
            baseInfoBean.setBaseInfoName("");
        } else {
            baseInfoBean.setBaseInfoName(name);
        }
        if (TextUtils.isEmpty(lng)) {
            baseInfoBean.setBaseInfoLng("");
        } else {
            baseInfoBean.setBaseInfoLng(lng);
        }
        if (TextUtils.isEmpty(lat)) {
            baseInfoBean.setBaseInfoLat("");
        } else {
            baseInfoBean.setBaseInfoLat(lat);
        }
        if (TextUtils.isEmpty(address)) {
            baseInfoBean.setBaseInfoAddress("");
        } else {
            baseInfoBean.setBaseInfoAddress(address);
        }
        if (TextUtils.isEmpty(contact)) {
            baseInfoBean.setBaseInfoContact("");
        } else {
            baseInfoBean.setBaseInfoContact(contact);
        }
        if (TextUtils.isEmpty(mobile)) {
            baseInfoBean.setBaseInfoMobile("");
        } else {
            baseInfoBean.setBaseInfoMobile(mobile);

        }
        if (TextUtils.isEmpty(level)) {
            baseInfoBean.setBaseInfoLevel("");
        } else {
            baseInfoBean.setBaseInfoLevel(level);
        }
        if (TextUtils.isEmpty(type)) {
            baseInfoBean.setBaseInfoType("");
        } else {
            baseInfoBean.setBaseInfoType(type);

        }
        if (TextUtils.isEmpty(is)) {
            baseInfoBean.setBaseInfoIsDisaster("");
        } else {
            baseInfoBean.setBaseInfoIsDisaster(is);
        }
        Log.e(TAG, "获取输入框内容：" + baseInfoBean.toString());
        BaseInfoBean infoBean = MyApplication.getDaoSession().getBaseInfoBeanDao().queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (infoBean == null) {
            MyApplication.getDaoSession().getBaseInfoBeanDao().insertOrReplace(baseInfoBean);
            ToastUtils.showShortToast("保存成功");
        } else {
            infoBean.setBaseInfoName(name);
            infoBean.setBaseInfoLng(lng);
            infoBean.setBaseInfoLat(lat);
            infoBean.setBaseInfoAddress(address);
            infoBean.setBaseInfoContact(contact);
            infoBean.setBaseInfoMobile(mobile);
            infoBean.setBaseInfoLevel(level);
            infoBean.setBaseInfoType(type);
            infoBean.setBaseInfoIsDisaster(is);
            MyApplication.getDaoSession().getBaseInfoBeanDao().update(infoBean);
            ToastUtils.showShortToast("更新成功");

        }
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
        spBaseinfoType.setAdapter(neworoldAdapter);
        if ("1".equals(taskInfoBean.getmDisasterType())) {
            spBaseinfoType.setSelection(0, true);
            spBaseinfoType.setEnabled(false);
        } else {
            spBaseinfoType.setSelection(1, false);
            spBaseinfoType.setEnabled(false);
        }
        ArrayAdapter isdisasterAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, isdisasters);
        isdisasterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBaseinfoIsdisaster.setAdapter(isdisasterAdapter);
        if ("2".equals(taskInfoBean.getmDisasterType())) {
            spBaseinfoIsdisaster.setSelection(0, true);
            spBaseinfoIsdisaster.setEnabled(false);
        }
        BaseInfoBean bean= MyApplication.getDaoSession().getBaseInfoBeanDao().queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        Log.e(TAG, "保存的数据：" + bean);
        if (bean != null) {
            isSave = true;
        }
        if (TextUtils.isEmpty(taskInfoBean.getmDisasterName())) {//如果服务器返回空,从数据库获取，不为空设置为服务器返回的数据，名称不允许改动
            if (isSave) {
                etBaseinfoName.setText(bean.getBaseInfoName());
            }
        } else {
            etBaseinfoName.setText(taskInfoBean.getmDisasterName());
            etBaseinfoName.setEnabled(false);
        }
        if (isSave) {
            etBaseinfoLng.setText(bean.getBaseInfoLng());
            etBaseinfoLat.setText(bean.getBaseInfoLat());
            etBaseinfoAddress.setText(bean.getBaseInfoAddress());
            etBaseinfoContact.setText(bean.getBaseInfoContact());
            etBaseinfoMobile.setText(bean.getBaseInfoMobile());
            spBaseinfoLevel.setSelection(Integer.valueOf(bean.getBaseInfoLevel()));
            spBaseinfoType.setSelection(Integer.valueOf(bean.getBaseInfoType()));
            spBaseinfoIsdisaster.setSelection(Integer.valueOf(bean.getBaseInfoIsDisaster()));
        } else {
            etBaseinfoLng.setText("null".equals(taskInfoBean.getmDisasterLng())?"":taskInfoBean.getmDisasterLng());
            etBaseinfoLat.setText("null".equals(taskInfoBean.getmDisasterLat())?"":taskInfoBean.getmDisasterLat());
            etBaseinfoAddress.setText("null".equals(taskInfoBean.getmDisasterLocation())?"":taskInfoBean.getmDisasterLocation());
            etBaseinfoContact.setText("null".equals(taskInfoBean.getmDisasterContact())?"":taskInfoBean.getmDisasterContact());
            etBaseinfoMobile.setText("null".equals(taskInfoBean.getmDisasterMobile())?"":taskInfoBean.getmDisasterMobile());
        }

    }
}
