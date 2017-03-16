package com.nandity.disastersystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.CompleteInfoActivity;
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by ChenPeng on 2017/3/14.
 */

public class CompleteCollectInfoFragment extends Fragment {
    private static final String TAG = "CompleteCollectInfoFragment";
    @BindView(R.id.tv_collect_info_place)
    TextView tvCollectInfoPlace;
    @BindView(R.id.tv_collect_info_type)
    TextView tvCollectInfoType;
    @BindView(R.id.tv_collect_info_dis_or_dan)
    TextView tvCollectInfoDisOrDan;
    @BindView(R.id.tv_collect_info_level)
    TextView tvCollectInfoLevel;
    @BindView(R.id.tv_collect_info_reason)
    TextView tvCollectInfoReason;
    @BindView(R.id.tv_collect_info_isResearch)
    TextView tvCollectInfoIsResearch;
    @BindView(R.id.tv_collect_info_dead)
    TextView tvCollectInfoDead;
    @BindView(R.id.tv_collect_info_miss)
    TextView tvCollectInfoMiss;
    @BindView(R.id.tv_collect_info_heavy_injured)
    TextView tvCollectInfoHeavyInjured;
    @BindView(R.id.tv_collect_info_soft_injured)
    TextView tvCollectInfoSoftInjured;
    @BindView(R.id.tv_collect_info_economic_loss)
    TextView tvCollectInfoEconomicLoss;
    @BindView(R.id.tv_collect_info_house_collapse_number)
    TextView tvCollectInfoHouseCollapseNumber;
    @BindView(R.id.tv_collect_info_house_collapse_area)
    TextView tvCollectInfoHouseCollapseArea;
    @BindView(R.id.tv_collect_info_house_damage_number)
    TextView tvCollectInfoHouseDamageNumber;
    @BindView(R.id.tv_collect_info_house_damage_area)
    TextView tvCollectInfoHouseDamageArea;
    @BindView(R.id.tv_collect_info_another_damage)
    TextView tvCollectInfoAnotherDamage;
    @BindView(R.id.tv_collect_info_family)
    TextView tvCollectInfoFamily;
    @BindView(R.id.tv_collect_info_people)
    TextView tvCollectInfoPeople;
    @BindView(R.id.tv_collect_info_home_family)
    TextView tvCollectInfoHomeFamily;
    @BindView(R.id.tv_collect_info_home_people)
    TextView tvCollectInfoHomePeople;
    @BindView(R.id.tv_collect_info_threaten_house_number)
    TextView tvCollectInfoThreatenHouseNumber;
    @BindView(R.id.tv_collect_info_threaten_house_area)
    TextView tvCollectInfoThreatenHouseArea;
    @BindView(R.id.tv_collect_info_another_threaten)
    TextView tvCollectInfoAnotherThreaten;
    @BindView(R.id.tv_collect_info_landslide_length)
    TextView tvCollectInfoLandslideLength;
    @BindView(R.id.tv_collect_info_landslide_width)
    TextView tvCollectInfoLandslideWidth;
    @BindView(R.id.tv_collect_info_landslide_area)
    TextView tvCollectInfoLandslideArea;
    @BindView(R.id.tv_collect_info_landslide_volume)
    TextView tvCollectInfoLandslideVolume;
    @BindView(R.id.tv_collect_info_distortion_length)
    TextView tvCollectInfoDistortionLength;
    @BindView(R.id.tv_collect_info_distortion_width)
    TextView tvCollectInfoDistortionWidth;
    @BindView(R.id.tv_collect_info_distortion_area)
    TextView tvCollectInfoDistortionArea;
    @BindView(R.id.tv_collect_info_distortion_volume)
    TextView tvCollectInfoDistortionVolume;
    @BindView(R.id.tv_collect_info_slide_distance)
    TextView tvCollectInfoSlideDistance;
    @BindView(R.id.tv_collect_info_crack_number)
    TextView tvCollectInfoCrackNumber;
    @BindView(R.id.tv_collect_info_crack_min_length)
    TextView tvCollectInfoCrackMinLength;
    @BindView(R.id.tv_collect_info_crack_max_length)
    TextView tvCollectInfoCrackMaxLength;
    @BindView(R.id.tv_collect_info_crack_min_width)
    TextView tvCollectInfoCrackMinWidth;
    @BindView(R.id.tv_collect_info_crack_max_width)
    TextView tvCollectInfoCrackMaxWidth;
    @BindView(R.id.tv_collect_info_max_dislocation)
    TextView tvCollectInfoMaxDislocation;
    @BindView(R.id.tv_collect_info_rock_length)
    TextView tvCollectInfoRockLength;
    @BindView(R.id.tv_collect_info_rock_width)
    TextView tvCollectInfoRockWidth;
    @BindView(R.id.tv_collect_info_rock_volume)
    TextView tvCollectInfoRockVolume;
    @BindView(R.id.tv_collect_info_collapse_volume)
    TextView tvCollectInfoCollapseVolume;
    @BindView(R.id.tv_collect_info_residual_volume)
    TextView tvCollectInfoResidualVolume;
    @BindView(R.id.tv_collect_info_another_things)
    TextView tvCollectInfoAnotherThings;
    @BindView(R.id.tv_collect_info_measure)
    TextView tvCollectInfoMeasure;
    @BindView(R.id.tv_collect_info_measure_remark)
    TextView tvCollectInfoMeasureRemark;
    @BindView(R.id.tv_collect_info_disposition)
    TextView tvCollectInfoDisposition;
    @BindView(R.id.tv_collect_info_disposition_remark)
    TextView tvCollectInfoDispositionRemark;
    @BindView(R.id.tv_collect_info_go_time)
    TextView tvCollectInfoGoTime;
    private SharedPreferences sp;
    private String sessionId;
    private String taskId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_collect_info, null);
        ButterKnife.bind(this, view);
        sp=getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId=sp.getString("sessionId","");
        taskId=((CompleteInfoActivity)getActivity()).taskInfoBean.getmTaskId();
        initView();
        return view;
    }

    private void initView() {
        OkHttpUtils.get().url(new ConnectUrl().getCompleteInfoUrl())
                .addParams("sessionId",sessionId)
                .addParams("taskId",taskId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG,"服务器返回的数据："+response);
                        String status,msg;
                        try {
                            JSONObject object=new JSONObject(response);
                            status=object.getString("status");
                            msg=object.getString("message");
                            if("200".equals(status)){
                                JSONArray array=object.getJSONArray("message");
                                JSONObject oj=array.getJSONObject(0);
                                tvCollectInfoPlace.setText("null".equals(oj.getString("survey_site"))?"":oj.getString("survey_site"));
                                tvCollectInfoType.setText(getType(oj.getString("dis_type")));
                                tvCollectInfoDisOrDan.setText(getDisOrDan(oj.getString("disaster_or_danger")));
                                tvCollectInfoLevel.setText(getLevel(oj.getString("scale")));
                                tvCollectInfoReason.setText(getReason(oj.getString("inducing_factor")));
                                tvCollectInfoIsResearch.setText(getIsResearch(oj.getString("sf_dcg")));
                                tvCollectInfoDead.setText("null".equals(oj.getString("dead_no"))?"":oj.getString("dead_no"));
                                tvCollectInfoMiss.setText("null".equals(oj.getString("sz_no"))?"":oj.getString("sz_no"));
                                tvCollectInfoHeavyInjured.setText("null".equals(oj.getString("zs_no"))?"":oj.getString("zs_no"));
                                tvCollectInfoSoftInjured.setText("null".equals(oj.getString("qs_no"))?"":oj.getString("qs_no"));
                                tvCollectInfoEconomicLoss.setText("null".equals(oj.getString("zj_money"))?"":oj.getString("zj_money"));
                                tvCollectInfoHouseCollapseNumber.setText("null".equals(oj.getString("house_kt_no"))?"":oj.getString("house_kt_no"));
                                tvCollectInfoHouseCollapseArea.setText("null".equals(oj.getString("house_kt_mj"))?"":oj.getString("house_kt_mj"));
                                tvCollectInfoHouseDamageNumber.setText("null".equals(oj.getString("house_kt_mj"))?"":oj.getString("house_ss_no"));
                                tvCollectInfoHouseDamageArea.setText("null".equals(oj.getString("house_kt_mj"))?"":oj.getString("house_ss_mj"));
                                tvCollectInfoAnotherDamage.setText("null".equals(oj.getString("other_ss"))?"":oj.getString("other_ss"));
                                tvCollectInfoFamily.setText("null".equals(oj.getString("qz_person_h"))?"":oj.getString("qz_person_h"));
                                tvCollectInfoPeople.setText("null".equals(oj.getString("qz_person_no"))?"":oj.getString("qz_person_no"));
                                tvCollectInfoHomeFamily.setText("null".equals(oj.getString("qz_family_zj"))?"":oj.getString("qz_family_zj"));
                                tvCollectInfoHomePeople.setText("null".equals(oj.getString("qz_person_zj"))?"":oj.getString("qz_person_zj"));
                                tvCollectInfoThreatenHouseNumber.setText("null".equals(oj.getString("qz_house_no"))?"":oj.getString("qz_house_no"));
                                tvCollectInfoThreatenHouseArea.setText("null".equals(oj.getString("qz_house_area"))?"":oj.getString("qz_house_area"));
                                tvCollectInfoAnotherThreaten.setText("null".equals(oj.getString("qz_other"))?"":oj.getString("qz_other"));
                                tvCollectInfoLandslideLength.setText("null".equals(oj.getString("hp_c"))?"":oj.getString("hp_c"));
                                tvCollectInfoLandslideWidth.setText("null".equals(oj.getString("hp_k"))?"":oj.getString("hp_k"));
                                tvCollectInfoLandslideArea.setText("null".equals(oj.getString("hp_a"))?"":oj.getString("hp_a"));
                                tvCollectInfoLandslideVolume.setText("null".equals(oj.getString("hp_v"))?"":oj.getString("hp_v"));
                                tvCollectInfoDistortionLength.setText("null".equals(oj.getString("bx_c"))?"":oj.getString("bx_c"));
                                tvCollectInfoDistortionWidth.setText("null".equals(oj.getString("bx_k"))?"":oj.getString("bx_k"));
                                tvCollectInfoDistortionArea.setText("null".equals(oj.getString("bx_a"))?"":oj.getString("bx_a"));
                                tvCollectInfoDistortionVolume.setText("null".equals(oj.getString("bx_v"))?"":oj.getString("bx_v"));
                                tvCollectInfoSlideDistance.setText("null".equals(oj.getString("bx_hd"))?"":oj.getString("bx_hd"));
                                tvCollectInfoCrackNumber.setText("null".equals(oj.getString("dl_no"))?"":oj.getString("dl_no"));
                                tvCollectInfoCrackMinLength.setText("null".equals(oj.getString("dl_min_c"))?"":oj.getString("dl_min_c"));
                                tvCollectInfoCrackMaxLength.setText("null".equals(oj.getString("dl_max_c"))?"":oj.getString("dl_max_c"));
                                tvCollectInfoCrackMinWidth.setText("null".equals(oj.getString("dl_min_k"))?"":oj.getString("dl_min_k"));
                                tvCollectInfoCrackMaxWidth.setText("null".equals(oj.getString("dl_max_k"))?"":oj.getString("dl_max_k"));
                                tvCollectInfoMaxDislocation.setText("null".equals(oj.getString("dl_xc"))?"":oj.getString("dl_xc"));
                                tvCollectInfoRockLength.setText("null".equals(oj.getString("wy_c"))?"":oj.getString("wy_c"));
                                tvCollectInfoRockWidth.setText("null".equals(oj.getString("wy_g"))?"":oj.getString("wy_g"));
                                tvCollectInfoRockVolume.setText("null".equals(oj.getString("wy_v"))?"":oj.getString("wy_v"));
                                tvCollectInfoCollapseVolume.setText("null".equals(oj.getString("wy_bt"))?"":oj.getString("wy_bt"));
                                tvCollectInfoResidualVolume.setText("null".equals(oj.getString("wy_cl"))?"":oj.getString("wy_cl"));
                                tvCollectInfoAnotherThings.setText("null".equals(oj.getString("wy_other"))?"":oj.getString("wy_other"));
                                tvCollectInfoMeasure.setText("null".equals(oj.getString("take_steps"))?"":oj.getString("take_steps"));
                                tvCollectInfoMeasureRemark.setText("null".equals(oj.getString("take_steps_remark"))?"":oj.getString("take_steps_remark"));
                                tvCollectInfoDisposition.setText("null".equals(oj.getString("next_plan"))?"":oj.getString("next_plan"));
                                tvCollectInfoDispositionRemark.setText("null".equals(oj.getString("next_plan_remark"))?"":oj.getString("next_plan_remark"));
                                tvCollectInfoGoTime.setText("null".equals(oj.getString("departure_time"))?"":oj.getString("departure_time"));
                            }else if ("400".equals(status)){
                                ToastUtils.showShortToast(msg);
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }else {
                                ToastUtils.showShortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String getIsResearch(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }
        switch (Integer.valueOf(s)){
            case 1:
                result="是";
                break;
            case 2:
                result="否";
                break;
        }
        return result;
    }

    private String getReason(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }
        switch (Integer.valueOf(s)){
            case 46:
                result="降雨";
                break;
            case 45:
                result="风化";
                break;
            case 44:
                result="库水位";
                break;
            case 43:
                result="切破";
                break;
            case 39:
                result="加载";
                break;
            case 26:
                result="冲刷坡脚";
                break;
        }
        return result;
    }

    private String getLevel(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }
        switch (Integer.valueOf(s)){
            case 5:
                result="小型";
                break;
            case 27:
                result="中型";
                break;
            case 33:
                result="大型";
                break;
            case 35:
                result="特大型";
                break;
        }
        return result;
    }

    private String getDisOrDan(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }
        switch (Integer.valueOf(s)){
            case 1:
                result="灾情";
                break;
            case 2:
                result="险情";
                break;

        }
        return result;
    }

    private String getType(String s) {
        String result="";
        if ("null".equals(s)){
            return result;
        }
        switch (Integer.valueOf(s)){
            case 1:
                result="滑坡";
                break;
            case 2:
                result="泥石流";
                break;
            case 3:
                result="危岩";
                break;
            case 4:
                result="不稳定斜坡";
                break;
            case 5:
                result="地面塌陷";
                break;
            case 6:
                result="地裂缝";
                break;
        }
        return result;
    }
}
