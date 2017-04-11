package com.nandity.disastersystem.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.CompleteInfoActivity;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 查看反馈信息---基本信息页面
 * Created by ChenPeng on 2017/3/14.
 */
public class CompleteBaseInfoFragment extends Fragment {
    @BindView(R.id.tv_base_info_name)
    TextView tvBaseInfoName;
    @BindView(R.id.tv_base_info_lng)
    TextView tvBaseInfoLng;
    @BindView(R.id.tv_base_info_lat)
    TextView tvBaseInfoLat;
    @BindView(R.id.tv_base_info_address)
    TextView tvBaseInfoAddress;
    @BindView(R.id.tv_base_info_contact)
    TextView tvBaseInfoContact;
    @BindView(R.id.tv_base_info_mobile)
    TextView tvBaseInfoMobile;
    @BindView(R.id.tv_base_info_level)
    TextView tvBaseInfoLevel;
    @BindView(R.id.tv_base_info_type)
    TextView tvBaseInfoType;
    @BindView(R.id.tv_base_info_is_disaster)
    TextView tvBaseInfoIsDisaster;
    @BindView(R.id.iv_base_info_image)
    ImageView ivBaseInfoImage;
    private TaskInfoBean taskInfoBean;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_baseinfo, null);
        ButterKnife.bind(this, view);
        taskInfoBean = ((CompleteInfoActivity) getActivity()).taskInfoBean;
        initView();
        setListener();
        return view;
    }

    private void setListener() {
        ivBaseInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_show_photo,null);
                ImageView imageView= (ImageView) view.findViewById(R.id.iv_dialog_picture);
                imageView.setImageBitmap(bitmap);
                new MaterialDialog.Builder(getActivity())
                        .cancelable(true)
                        .customView(view,false)
                        .show();
            }
        });
    }

    private void initView() {
        OkHttpUtils.get().url(new ConnectUrl().getDisasterPictureUrl())
                .addParams("dis_id",taskInfoBean.getmRowNumber())
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        Log.d("picture", "测试图片：" + response);
                        bitmap=response;
                        Matrix matrix=new Matrix();
                        matrix.setScale(0.5f,0.5f);
                        Bitmap bmp = Bitmap.createBitmap(response, 0, 0, response.getWidth(), response.getHeight(), matrix, true);
                        ivBaseInfoImage.setImageBitmap(bmp);
                    }
                });
        tvBaseInfoName.setText("null".equals(taskInfoBean.getmDisasterName())?"":taskInfoBean.getmDisasterName());
        tvBaseInfoAddress.setText("null".equals(taskInfoBean.getmAddress()) ? "" : taskInfoBean.getmAddress());
        tvBaseInfoContact.setText("null".equals(taskInfoBean.getmDisasterContact()) ? "" : taskInfoBean.getmDisasterContact());
        tvBaseInfoIsDisaster.setText(getIsDisaster(taskInfoBean.getmIsDisaster()));
        tvBaseInfoLat.setText("null".equals(taskInfoBean.getmDisasterLat()) ? "" : taskInfoBean.getmDisasterLat());
        tvBaseInfoLng.setText("null".equals(taskInfoBean.getmDisasterLng()) ? "" : taskInfoBean.getmDisasterLng());
        tvBaseInfoMobile.setText("null".equals(taskInfoBean.getmDisasterMobile()) ? "" : taskInfoBean.getmDisasterMobile());
        tvBaseInfoLevel.setText(getLevel(taskInfoBean.getmDisasterLevel()));
        tvBaseInfoType.setText(getType(taskInfoBean.getmDisasterType()));
    }

    private String getType(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }else if ("1".equals(s)){
            result="新灾害点";
        }else {
            result="旧灾害点";
        }
        return result;
    }

    private String getIsDisaster(String s) {
        String result = "";
        if ("null".equals(s)) {
            return result;
        }else if ("1".equals(s)){
            result="是";
        }else {
            result="否";
        }
        return result;
    }

    private String getLevel(String s) {
        String result = "";
        if ("null".equals(s)) {
            return result;
        }
        switch (Integer.valueOf(s)) {
            case 1:
                result = "一级";
                break;
            case 2:
                result = "二级";
                break;
            case 3:
                result = "三级";
                break;
            case 4:
                result = "四级";
                break;
            case 5:
                result = "五级";
                break;
            case 6:
                result = "六级";
                break;
        }
        return result;
    }

}
