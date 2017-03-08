package com.nandity.disastersystem.fragment;

import android.app.ProgressDialog;
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
import com.nandity.disastersystem.constant.ConnectUrl;
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
    private static final String TAG = "CollectInfoFragment";
    private Context context;
    private SharedPreferences sp;
    private String sessionId;
    private String currentTime;
    private ArrayAdapter spinnerAdapter;
    private List<String> measureList, dispositionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_info, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
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
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
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
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ((FillInfoActivity)getActivity()).progressDialog.dismiss();
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
                if (measureList!=null&&measureList.size()>0){
                initDialog(measureList,tvCollectinfoMeasure).show();
                }
            }
        });
        tvCollectinfoDisposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dispositionList!=null&&dispositionList.size()>0){
                initDialog(dispositionList,tvCollectinfoDisposition).show();
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

    }

    private void initView() {
        currentTime = new SimpleDateFormat("yyyy年MM月dd日 hh:mm").format(new Date());
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
    }
}
