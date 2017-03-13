package com.nandity.disastersystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.FillInfoActivity;
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.CollectInfoBean;
import com.nandity.disastersystem.database.CollectInfoBeanDao;
import com.nandity.disastersystem.utils.DateTimePickUtil;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by ChenPeng on 2017/3/6.
 */

public class CollectInfoFragment extends Fragment {
    @BindView(R.id.et_collectinfo_place)
    EditText etCollectinfoPlace;
    @BindView(R.id.sp_collectinfo_type)
    Spinner spCollectinfoType;
    @BindView(R.id.sp_collectinfo_isdisaster)
    Spinner spCollectinfoIsdisaster;
    @BindView(R.id.sp_collectinfo_disaster_level)
    Spinner spCollectinfoDisasterLevel;
    @BindView(R.id.sp_collectinfo_disaster_reason)
    Spinner spCollectinfoDisasterReason;
    @BindView(R.id.sp_collectinfo_isresearch)
    Spinner spCollectinfoIsresearch;
    @BindView(R.id.et_collectinfo_family)
    EditText etCollectinfoFamily;
    @BindView(R.id.et_collectinfo_people)
    EditText etCollectinfoPeople;
    @BindView(R.id.et_collectinfo_athome_family)
    EditText etCollectinfoAthomeFamily;
    @BindView(R.id.et_collectinfo_athome_people)
    EditText etCollectinfoAthomePeople;
    @BindView(R.id.et_collectinfo_house)
    EditText etCollectinfoHouse;
    @BindView(R.id.et_collectinfo_area)
    EditText etCollectinfoArea;
    @BindView(R.id.et_collectinfo_another_disaster)
    EditText etCollectinfoAnotherDisaster;
    @BindView(R.id.et_collectinfo_landslide_length)
    EditText etCollectinfoLandslideLength;
    @BindView(R.id.et_collectinfo_landslide_width)
    EditText etCollectinfoLandslideWidth;
    @BindView(R.id.et_collectinfo_landslide_area)
    EditText etCollectinfoLandslideArea;
    @BindView(R.id.et_collectinfo_landslide_volume)
    EditText etCollectinfoLandslideVolume;
    @BindView(R.id.et_collectinfo_distortion_length)
    EditText etCollectinfoDistortionLength;
    @BindView(R.id.et_collectinfo_distortion_width)
    EditText etCollectinfoDistortionWidth;
    @BindView(R.id.et_collectinfo_distortion_area)
    EditText etCollectinfoDistortionArea;
    @BindView(R.id.et_collectinfo_distortion_volume)
    EditText etCollectinfoDistortionVolume;
    @BindView(R.id.et_collectinfo_slide_distance)
    EditText etCollectinfoSlideDistance;
    @BindView(R.id.et_collectinfo_crack_number)
    EditText etCollectinfoCrackNumber;
    @BindView(R.id.et_collectinfo_crack_minlength)
    EditText etCollectinfoCrackMinlength;
    @BindView(R.id.et_collectinfo_crack_maxlength)
    EditText etCollectinfoCrackMaxlength;
    @BindView(R.id.et_collectinfo_crack_minwidth)
    EditText etCollectinfoCrackMinwidth;
    @BindView(R.id.et_collectinfo_crack_maxwidth)
    EditText etCollectinfoCrackMaxwidth;
    @BindView(R.id.et_collectinfo_maxdislocation)
    EditText etCollectinfoMaxdislocation;
    @BindView(R.id.et_collectinfo_rock_length)
    EditText etCollectinfoRockLength;
    @BindView(R.id.et_collectinfo_rock_width)
    EditText etCollectinfoRockWidth;
    @BindView(R.id.et_collectinfo_rock_volume)
    EditText etCollectinfoRockVolume;
    @BindView(R.id.et_collectinfo_collapse_volume)
    EditText etCollectinfoCollapseVolume;
    @BindView(R.id.et_collectinfo_residual_volume)
    EditText etCollectinfoResidualVolume;
    @BindView(R.id.et_collectinfo_another_things)
    EditText etCollectinfoAnotherThings;
    @BindView(R.id.tv_collectinfo_measure)
    TextView tvCollectinfoMeasure;
    @BindView(R.id.et_collectinfo_measure_remark)
    EditText etCollectinfoMeasureRemark;
    @BindView(R.id.et_collectinfo_disposition_remark)
    EditText etCollectinfoDispositionRemark;
    @BindView(R.id.tv_collectinfo_go_time)
    TextView tvCollectinfoGoTime;
    @BindView(R.id.btn_collect_save)
    Button btnCollectSave;
    @BindView(R.id.tv_collectinfo_disposition)
    TextView tvCollectinfoDisposition;
    @BindView(R.id.et_collectinfo_dead)
    EditText etCollectinfoDead;
    @BindView(R.id.et_collectinfo_miss)
    EditText etCollectinfoMiss;
    @BindView(R.id.et_collectinfo_heavy_injured)
    EditText etCollectinfoHeavyInjured;
    @BindView(R.id.et_collectinfo_soft_injured)
    EditText etCollectinfoSoftInjured;
    @BindView(R.id.et_collectinfo_economic_loss)
    EditText etCollectinfoEconomicLoss;
    @BindView(R.id.et_collectinfo_house_collapse_number)
    EditText etCollectinfoHouseCollapseNumber;
    @BindView(R.id.et_collectinfo_house_collapse_area)
    EditText etCollectinfoHouseCollapseArea;
    @BindView(R.id.et_collectinfo_house_damage_number)
    EditText etCollectinfoHouseDamageNumber;
    @BindView(R.id.et_collectinfo_house_damage_area)
    EditText etCollectinfoHouseDamageArea;
    @BindView(R.id.et_collectinfo_another_damage)
    EditText etCollectinfoAnotherDamage;
    private static final String TAG = "CollectInfoFragment";
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private String currentTime;
    private ArrayAdapter spinnerAdapter;
    private List<String> measureList, dispositionList;
    private TaskInfoBean taskInfoBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_info, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        taskInfoBean = ((FillInfoActivity) getActivity()).taskInfoBean;
        initView();
        initData();
        setListener();
        return view;
    }

    private void initData() {
        measureList = new ArrayList<>();
        dispositionList = new ArrayList<>();
        sessionId = sp.getString("sessionId", "");
        OkHttpUtils.get().url(new ConnectUrl().getMeaDisListUrl())
                .addParams("sessionId", sessionId)
                .addParams("id", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网络！");
                        ((FillInfoActivity) getActivity()).progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ((FillInfoActivity) getActivity()).progressDialog.dismiss();
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject json = array.getJSONObject(i);
                                    measureList.add(json.getString("static_name"));
                                }
                                Log.d(TAG, "已采取措施数据：" + measureList.toString());
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        OkHttpUtils.get().url(new ConnectUrl().getMeaDisListUrl())
                .addParams("sessionId", sessionId)
                .addParams("id", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网络！");
                        ((FillInfoActivity) getActivity()).progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ((FillInfoActivity) getActivity()).progressDialog.dismiss();
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject json = array.getJSONObject(i);
                                    dispositionList.add(json.getString("static_name"));
                                }
                                Log.d(TAG, "处置意见数据数据：" + dispositionList.toString());
                            } else if ("400".equals(status)) {
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

    private MaterialDialog initDialog(final List<String> list, final TextView textView) {
        return new MaterialDialog.Builder(getActivity())
                .title("请选择措施")
                .titleColor(Color.BLUE)
                .items(list)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        String Str = "";
                        for (int i = 0; i < which.length; i++) {
                            Str += list.get(which[i]) + ",";
                        }
                        textView.setText(Str.substring(0, Str.length() - 1));
                        return true;
                    }
                })
                .positiveText("确定")
                .negativeText("取消")
                .build();
    }


    private void setListener() {
        tvCollectinfoGoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickUtil(getActivity(), currentTime).dateTimePicKDialog(tvCollectinfoGoTime);
            }
        });
        tvCollectinfoMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (measureList != null && measureList.size() > 0) {
                    initDialog(measureList, tvCollectinfoMeasure).show();
                }
            }
        });
        tvCollectinfoDisposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dispositionList != null && dispositionList.size() > 0) {
                    initDialog(dispositionList, tvCollectinfoDisposition).show();
                }
            }
        });
        btnCollectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {

        String place = etCollectinfoPlace.getText().toString().trim();
        String type = spCollectinfoType.getSelectedItemPosition() + "";
        String disOrDan = spCollectinfoIsdisaster.getSelectedItemPosition() + "";
        String level = spCollectinfoDisasterLevel.getSelectedItemPosition() + "";
        String reason = spCollectinfoDisasterReason.getSelectedItemPosition() + "";
        String isResearch = spCollectinfoIsresearch.getSelectedItemPosition() + "";
        String dead=etCollectinfoDead.getText().toString().trim();
        String miss=etCollectinfoMiss.getText().toString().trim();
        String heavyInjured = etCollectinfoHeavyInjured.getText().toString().trim();
        String softInjured=etCollectinfoSoftInjured.getText().toString().trim();
        String economicLoss = etCollectinfoEconomicLoss.getText().toString().trim();
        String houseCollapseNum = etCollectinfoHouseCollapseNumber.getText().toString().trim();
        String houseCollapseArea=etCollectinfoHouseCollapseArea.getText().toString().trim();
        String houseDamageNum=etCollectinfoHouseDamageNumber.getText().toString().trim();
        String houseDamageArea=etCollectinfoHouseDamageArea.getText().toString().trim();
        String anotherDamage=etCollectinfoAnotherDamage.getText().toString().trim();
        String family = etCollectinfoFamily.getText().toString().trim();
        String people = etCollectinfoPeople.getText().toString().trim();
        String atHomeFamily = etCollectinfoAthomeFamily.getText().toString().trim();
        String atHomePeople = etCollectinfoAthomePeople.getText().toString().trim();
        String house = etCollectinfoHouse.getText().toString().trim();
        String houseArea = etCollectinfoArea.getText().toString().trim();
        String anotherDis = etCollectinfoAnotherDisaster.getText().toString().trim();
        String landSlideLen = etCollectinfoLandslideLength.getText().toString().trim();
        String landSlideWid = etCollectinfoLandslideWidth.getText().toString().trim();
        String landSlideArea = etCollectinfoLandslideArea.getText().toString().trim();
        String landSlideVolume = etCollectinfoLandslideVolume.getText().toString().trim();
        String distortionLen = etCollectinfoDistortionLength.getText().toString().trim();
        String distortionWid = etCollectinfoDistortionWidth.getText().toString().trim();
        String distortionArea = etCollectinfoDistortionArea.getText().toString().trim();
        String distortionVolume = etCollectinfoDistortionVolume.getText().toString().trim();
        String slideDistance = etCollectinfoSlideDistance.getText().toString().trim();
        String crackNum = etCollectinfoCrackNumber.getText().toString().trim();
        String minCrackLen = etCollectinfoCrackMinlength.getText().toString().trim();
        String maxCrackLen = etCollectinfoCrackMaxlength.getText().toString().trim();
        String minCrackWid = etCollectinfoCrackMinwidth.getText().toString().trim();
        String maxCrackWid = etCollectinfoCrackMaxwidth.getText().toString().trim();
        String maxDislocation = etCollectinfoMaxdislocation.getText().toString().trim();
        String rockLen = etCollectinfoRockLength.getText().toString().trim();
        String rockWid = etCollectinfoRockWidth.getText().toString().trim();
        String rockVolume = etCollectinfoRockVolume.getText().toString().trim();
        String collapseVolume = etCollectinfoCollapseVolume.getText().toString().trim();
        String residualVolume = etCollectinfoResidualVolume.getText().toString().trim();
        String anotherThings = etCollectinfoAnotherThings.getText().toString().trim();
        String measure = tvCollectinfoMeasure.getText().toString().trim();
        String measureMark = etCollectinfoMeasureRemark.getText().toString().trim();
        String disposition = tvCollectinfoDisposition.getText().toString().trim();
        String dispositionMark = etCollectinfoDispositionRemark.getText().toString().trim();
        String goTime = tvCollectinfoGoTime.getText().toString().trim();
        CollectInfoBean collectInfoBean = MyApplication.getDaoSession().getCollectInfoBeanDao().queryBuilder().where(CollectInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (collectInfoBean == null) {
            CollectInfoBean bean = new CollectInfoBean();
            bean.setTaskId(taskInfoBean.getmTaskId());
            bean.setCollectInfoPlace(place);
            bean.setCollectInfoType(type);
            bean.setCollectInfoDisOrDan(disOrDan);
            bean.setCollectInfoDisasterLevel(level);
            bean.setCollectInfoDisasterReason(reason);
            bean.setCollectInfoIsResearch(isResearch);
            bean.setCollectInfoDead(dead);
            bean.setCollectInfoMiss(miss);
            bean.setCollectInfoHeavyInjured(heavyInjured);
            bean.setCollectInfoSoftInjured(softInjured);
            bean.setCollectInfoEconomicLoss(economicLoss);
            bean.setCollectInfoHouseCollapseNum(houseCollapseNum);
            bean.setCollectInfoHouseCollapseArea(houseCollapseArea);
            bean.setCollectInfoHouseDamageNum(houseDamageNum);
            bean.setCollectInfoHouseDamageArea(houseDamageArea);
            bean.setCollectInfoAnotherDamage(anotherDamage);
            bean.setCollectInfoPeople(people);
            bean.setCollectInfoFamily(family);
            bean.setCollectInfoAtHomeFamily(atHomeFamily);
            bean.setCollectInfoAtHomePeople(atHomePeople);
            bean.setCollectInfoHouse(house);
            bean.setCollectInfoHouseArea(houseArea);
            bean.setCollectInfoAnotherDisaster(anotherDis);
            bean.setCollectInfoLandslideLength(landSlideLen);
            bean.setCollectInfoLandslideWidth(landSlideWid);
            bean.setCollectInfoLandslideArea(landSlideArea);
            bean.setCollectInfoLandslideVolume(landSlideVolume);
            bean.setCollectInfoDistortionLength(distortionLen);
            bean.setCollectInfoDistortionWidth(distortionWid);
            bean.setCollectInfoDistortionArea(distortionArea);
            bean.setCollectInfoDistortionVolume(distortionVolume);
            bean.setCollectInfoSlideDistance(slideDistance);
            bean.setCollectInfoCrackNumber(crackNum);
            bean.setCollectInfoCrackMinLength(minCrackLen);
            bean.setCollectInfoCrackMinWidth(minCrackWid);
            bean.setCollectInfoCrackMaxLength(maxCrackLen);
            bean.setCollectInfoCrackMaxWidth(maxCrackWid);
            bean.setCollectInfoMaxDislocation(maxDislocation);
            bean.setCollectInfoRockLength(rockLen);
            bean.setCollectInfoRockWidth(rockWid);
            bean.setCollectInfoRockVolume(rockVolume);
            bean.setCollectInfoCollapseVolume(collapseVolume);
            bean.setCollectInfoResidualVolume(residualVolume);
            bean.setCollectInfoAnotherThings(anotherThings);
            bean.setCollectInfoMeasure(measure);
            bean.setCollectInfoMeasureRemark(measureMark);
            bean.setCollectInfoDisposition(disposition);
            bean.setCollectInfoDispositionRemark(dispositionMark);
            bean.setCollectInfoGoTime(goTime);
            MyApplication.getDaoSession().getCollectInfoBeanDao().insertOrReplace(bean);
            ToastUtils.showShortToast("保存成功");
        } else {
            collectInfoBean.setCollectInfoPlace(place);
            collectInfoBean.setCollectInfoType(type);
            collectInfoBean.setCollectInfoDisOrDan(disOrDan);
            collectInfoBean.setCollectInfoDisasterLevel(level);
            collectInfoBean.setCollectInfoDisasterReason(reason);
            collectInfoBean.setCollectInfoIsResearch(isResearch);
            collectInfoBean.setCollectInfoDead(dead);
            collectInfoBean.setCollectInfoMiss(miss);
            collectInfoBean.setCollectInfoHeavyInjured(heavyInjured);
            collectInfoBean.setCollectInfoSoftInjured(softInjured);
            collectInfoBean.setCollectInfoEconomicLoss(economicLoss);
            collectInfoBean.setCollectInfoHouseCollapseNum(houseCollapseNum);
            collectInfoBean.setCollectInfoHouseCollapseArea(houseCollapseArea);
            collectInfoBean.setCollectInfoHouseDamageNum(houseDamageNum);
            collectInfoBean.setCollectInfoHouseDamageArea(houseDamageArea);
            collectInfoBean.setCollectInfoAnotherDamage(anotherDamage);
            collectInfoBean.setCollectInfoPeople(people);
            collectInfoBean.setCollectInfoFamily(family);
            collectInfoBean.setCollectInfoAtHomeFamily(atHomeFamily);
            collectInfoBean.setCollectInfoAtHomePeople(atHomePeople);
            collectInfoBean.setCollectInfoHouse(house);
            collectInfoBean.setCollectInfoHouseArea(houseArea);
            collectInfoBean.setCollectInfoAnotherDisaster(anotherDis);
            collectInfoBean.setCollectInfoLandslideLength(landSlideLen);
            collectInfoBean.setCollectInfoLandslideWidth(landSlideWid);
            collectInfoBean.setCollectInfoLandslideArea(landSlideArea);
            collectInfoBean.setCollectInfoLandslideVolume(landSlideVolume);
            collectInfoBean.setCollectInfoDistortionLength(distortionLen);
            collectInfoBean.setCollectInfoDistortionWidth(distortionWid);
            collectInfoBean.setCollectInfoDistortionArea(distortionArea);
            collectInfoBean.setCollectInfoDistortionVolume(distortionVolume);
            collectInfoBean.setCollectInfoSlideDistance(slideDistance);
            collectInfoBean.setCollectInfoCrackNumber(crackNum);
            collectInfoBean.setCollectInfoCrackMinLength(minCrackLen);
            collectInfoBean.setCollectInfoCrackMinWidth(minCrackWid);
            collectInfoBean.setCollectInfoCrackMaxLength(maxCrackLen);
            collectInfoBean.setCollectInfoCrackMaxWidth(maxCrackWid);
            collectInfoBean.setCollectInfoMaxDislocation(maxDislocation);
            collectInfoBean.setCollectInfoRockLength(rockLen);
            collectInfoBean.setCollectInfoRockWidth(rockWid);
            collectInfoBean.setCollectInfoRockVolume(rockVolume);
            collectInfoBean.setCollectInfoCollapseVolume(collapseVolume);
            collectInfoBean.setCollectInfoResidualVolume(residualVolume);
            collectInfoBean.setCollectInfoAnotherThings(anotherThings);
            collectInfoBean.setCollectInfoMeasure(measure);
            collectInfoBean.setCollectInfoMeasureRemark(measureMark);
            collectInfoBean.setCollectInfoDisposition(disposition);
            collectInfoBean.setCollectInfoDispositionRemark(dispositionMark);
            collectInfoBean.setCollectInfoGoTime(goTime);
            MyApplication.getDaoSession().getCollectInfoBeanDao().update(collectInfoBean);
            ToastUtils.showShortToast("更新成功");
        }
    }

    private void initView() {
        currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        tvCollectinfoGoTime.setText(currentTime);
        String[] disasterType = {"滑坡", "泥石流", "危岩", "不稳定斜坡", "地面塌陷", "地裂缝", "塌岸"};
        String[] disasterReason = {"降雨", "风化", "库水位", "切坡", "加载", "冲刷坡脚"};
        String[] disasterOrDanger = {"灾情", "险情"};
        String[] disasterScale = {"小型", "中型", "大型", "特大型"};
        String[] isFind = {"是", "否"};
        spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, disasterType);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollectinfoType.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, disasterReason);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollectinfoDisasterReason.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, disasterOrDanger);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollectinfoIsdisaster.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, disasterScale);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollectinfoDisasterLevel.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, isFind);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollectinfoIsresearch.setAdapter(spinnerAdapter);
        CollectInfoBean collectInfoBean = MyApplication.getDaoSession().getCollectInfoBeanDao().queryBuilder().where(CollectInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (collectInfoBean != null) {
            etCollectinfoPlace.setText(collectInfoBean.getCollectInfoPlace());
            spCollectinfoType.setSelection(Integer.valueOf(collectInfoBean.getCollectInfoType()));
            spCollectinfoIsdisaster.setSelection(Integer.valueOf(collectInfoBean.getCollectInfoDisOrDan()));
            spCollectinfoDisasterLevel.setSelection(Integer.valueOf(collectInfoBean.getCollectInfoDisasterLevel()));
            spCollectinfoDisasterReason.setSelection(Integer.valueOf(collectInfoBean.getCollectInfoDisasterReason()));
            spCollectinfoIsresearch.setSelection(Integer.valueOf(collectInfoBean.getCollectInfoIsResearch()));
            etCollectinfoDead.setText(collectInfoBean.getCollectInfoDead());
            etCollectinfoMiss.setText(collectInfoBean.getCollectInfoMiss());
            etCollectinfoHeavyInjured.setText(collectInfoBean.getCollectInfoHeavyInjured());
            etCollectinfoSoftInjured.setText(collectInfoBean.getCollectInfoSoftInjured());
            etCollectinfoEconomicLoss.setText(collectInfoBean.getCollectInfoEconomicLoss());
            etCollectinfoHouseCollapseNumber.setText(collectInfoBean.getCollectInfoHouseCollapseNum());
            etCollectinfoHouseCollapseArea.setText(collectInfoBean.getCollectInfoHouseCollapseArea());
            etCollectinfoHouseDamageNumber.setText(collectInfoBean.getCollectInfoHouseDamageNum());
            etCollectinfoHouseDamageArea.setText(collectInfoBean.getCollectInfoHouseDamageArea());
            etCollectinfoAnotherDamage.setText(collectInfoBean.getCollectInfoAnotherDamage());
            etCollectinfoFamily.setText(collectInfoBean.getCollectInfoFamily());
            etCollectinfoPeople.setText(collectInfoBean.getCollectInfoPeople());
            etCollectinfoAthomeFamily.setText(collectInfoBean.getCollectInfoAtHomeFamily());
            etCollectinfoAthomePeople.setText(collectInfoBean.getCollectInfoAtHomePeople());
            etCollectinfoHouse.setText(collectInfoBean.getCollectInfoHouse());
            etCollectinfoArea.setText(collectInfoBean.getCollectInfoHouseArea());
            etCollectinfoAnotherDisaster.setText(collectInfoBean.getCollectInfoAnotherDisaster());
            etCollectinfoLandslideLength.setText(collectInfoBean.getCollectInfoLandslideLength());
            etCollectinfoLandslideWidth.setText(collectInfoBean.getCollectInfoLandslideWidth());
            etCollectinfoLandslideArea.setText(collectInfoBean.getCollectInfoLandslideArea());
            etCollectinfoLandslideVolume.setText(collectInfoBean.getCollectInfoLandslideVolume());
            etCollectinfoDistortionLength.setText(collectInfoBean.getCollectInfoDistortionLength());
            etCollectinfoDistortionWidth.setText(collectInfoBean.getCollectInfoDistortionWidth());
            etCollectinfoDistortionArea.setText(collectInfoBean.getCollectInfoDistortionArea());
            etCollectinfoDistortionVolume.setText(collectInfoBean.getCollectInfoDistortionVolume());
            etCollectinfoSlideDistance.setText(collectInfoBean.getCollectInfoSlideDistance());
            etCollectinfoCrackNumber.setText(collectInfoBean.getCollectInfoCrackNumber());
            etCollectinfoCrackMinlength.setText(collectInfoBean.getCollectInfoCrackMinLength());
            etCollectinfoCrackMaxlength.setText(collectInfoBean.getCollectInfoCrackMaxLength());
            etCollectinfoCrackMinwidth.setText(collectInfoBean.getCollectInfoCrackMinWidth());
            etCollectinfoCrackMaxwidth.setText(collectInfoBean.getCollectInfoCrackMaxWidth());
            etCollectinfoMaxdislocation.setText(collectInfoBean.getCollectInfoMaxDislocation());
            etCollectinfoRockLength.setText(collectInfoBean.getCollectInfoRockLength());
            etCollectinfoRockWidth.setText(collectInfoBean.getCollectInfoRockWidth());
            etCollectinfoRockVolume.setText(collectInfoBean.getCollectInfoRockVolume());
            etCollectinfoCollapseVolume.setText(collectInfoBean.getCollectInfoCollapseVolume());
            etCollectinfoResidualVolume.setText(collectInfoBean.getCollectInfoResidualVolume());
            etCollectinfoAnotherThings.setText(collectInfoBean.getCollectInfoAnotherThings());
            tvCollectinfoMeasure.setText(collectInfoBean.getCollectInfoMeasure());
            etCollectinfoMeasureRemark.setText(collectInfoBean.getCollectInfoMeasureRemark());
            tvCollectinfoDisposition.setText(collectInfoBean.getCollectInfoDisposition());
            etCollectinfoDispositionRemark.setText(collectInfoBean.getCollectInfoDispositionRemark());
            tvCollectinfoGoTime.setText(collectInfoBean.getCollectInfoGoTime());
        }
    }
}
