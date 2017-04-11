package com.nandity.disastersystem.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.DisType;
import com.nandity.disastersystem.bean.TaskAccountInfo;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

/**
 * timeType：1表示月份，2表示年，3,表示同比，4表示季度
 * timeType为null，空也是表示月份
 *
 * type    1表示灾情，2表示险情
 *
 * 灾险情诱发因素统计
 */
public class DisCurveByScaleActivity extends AppCompatActivity implements View.OnClickListener{

    //声明所需变量
    public final static String[] types = new String[]{
            "特大型", "大型", "中型", "小型"};
    public final static String[] typeNames = new String[]{
            "灾情规模分析","险情规模分析"};
    public static final int[] COLORS = new int[]{
            Color.parseColor("#FFB90F"), Color.parseColor("#FF4500"), Color.parseColor("#8B8682"),
            Color.parseColor("#7FFFD4"),Color.parseColor("#CAFF70"),Color.parseColor("#AB82FF"),
            Color.parseColor("#E0FFFF"),Color.parseColor("#7171C6"),Color.parseColor("#00FF00"),
            Color.parseColor("#CD0000"),Color.parseColor("#F7F7F7"),Color.parseColor("#0000EE")
            };
    private List<DisType> mDisTypes;
    ColumnChartView columnChart;
    ColumnChartData columnData;
    List<Column> lsColumn = new ArrayList<Column>();
    List<SubcolumnValue> lsValue;

    private Spinner spTimeType;
    private RadioGroup rgType;
    private TextView tvStartTime,tvEndTime,tvStatistics,tvDisTypeName,tvDistypeTitle;
    private DatePicker datePicker;// 日期控件
    private AlertDialog dialog;// 选择时间日期对话框
    private List<TaskAccountInfo> taskAccountInfo1,taskAccountInfo2;
    private SharedPreferences sp;
    private String sessionId;
    private String TAG = "DisCurveByScaleActivity";
    private ProgressDialog progressDialog;
    private Context context;
    private String timeType,Type;
    private RadioButton rbType1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_type);
        context = DisCurveByScaleActivity.this;
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        initViews();


    }

    private void setBiaoZhu(){

        final LinearLayout lin1 = (LinearLayout) findViewById(R.id.ll_biaozhu1);
        final LinearLayout lin2 = (LinearLayout) findViewById(R.id.ll_biaozhu2);
        final LinearLayout lin3 = (LinearLayout) findViewById(R.id.ll_biaozhu3);
        lin1.removeAllViews();
        lin2.removeAllViews();
        lin3.removeAllViews();
        LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout newSingleRL=new LinearLayout(this);

        if (timeType.equals(4+"")){
            for(int i=0;i<mDisTypes.size();i++)
            {
                newSingleRL=generateSingleLayout(i,COLORS[i],mDisTypes.get(i).getHappen_time());
                if(i<2){
                    lin1.addView(newSingleRL,LP_FW);//全部用父结点的布局参数
                }else if(i>=2||i<4){
                    lin2.setVisibility(View.VISIBLE);
                    lin2.addView(newSingleRL,LP_FW);
                }else{
                    lin2.setVisibility(View.VISIBLE);
                    lin3.addView(newSingleRL,LP_FW);
                }

            }
        }else{
            for(int i=0;i<mDisTypes.size();i++)
            {
                newSingleRL=generateSingleLayout(i,COLORS[i],mDisTypes.get(i).getHappen_time());
                if(i<4){
                    lin1.addView(newSingleRL,LP_FW);//全部用父结点的布局参数
                }else if(i>=4||i<8){
                    lin2.setVisibility(View.VISIBLE);
                    lin2.addView(newSingleRL,LP_FW);
                }else{
                    lin2.setVisibility(View.VISIBLE);
                    lin3.addView(newSingleRL,LP_FW);
                }

            }
        }

    }

    private LinearLayout generateSingleLayout(int id,int color,String str)
    {

        LinearLayout layout_sub_Lin=new LinearLayout(this);
        layout_sub_Lin.setGravity(Gravity.CENTER_VERTICAL);
        layout_sub_Lin.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
                20, 20);
        LP_WW.setMargins(10,0,5,0);
        tv.setBackgroundColor(color);
        tv.setLayoutParams(LP_WW);

        layout_sub_Lin.addView(tv);

        TextView tv2 = new TextView(this);
        LinearLayout.LayoutParams LL_MW = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);//尤其注意这个位置，用的是父容器的布局参数
        tv2.setLayoutParams(LL_MW);
        tv2.setTextSize(12);
        tv2.setGravity(Gravity.CENTER);
        tv2.setText(str);

        layout_sub_Lin.addView(tv2);

        return layout_sub_Lin;

    }

    private void initViews() {

        columnChart= (ColumnChartView) findViewById(R.id.chart);

        tvStartTime= (TextView) findViewById(R.id.tv_distype_time_start);
        tvEndTime= (TextView) findViewById(R.id.tv_distype_time_end);
        tvStatistics= (TextView) findViewById(R.id.tv_distype_statistics);
        tvDisTypeName= (TextView) findViewById(R.id.tv_disType_name);
        spTimeType= (Spinner) findViewById(R.id.sp_distype_timetype);
        spTimeType= (Spinner) findViewById(R.id.sp_distype_timetype);
        rgType= (RadioGroup) findViewById(R.id.rg_distype_type);
        rbType1= (RadioButton) findViewById(R.id.rb_type_1);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvStatistics.setOnClickListener(this);

        tvDistypeTitle= (TextView) findViewById(R.id.tv_distype_title);
        tvDistypeTitle.setText("灾险情规模统计");
        spTimeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
            timeType=pos+1+"";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG,"checkedId::"+checkedId);
                int id=checkedId;
                if(checkedId==rbType1.getId()){
                    Type=2+"";
                }else {
                    Type=1+"";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_distype_time_start:
                showDialog(tvStartTime);
                break;
            case R.id.tv_distype_time_end:
                showDialog(tvEndTime);
                break;
            case R.id.tv_distype_statistics:
                if("".equals(tvStartTime.getText().toString())
                        ||"".equals(tvEndTime.getText().toString())
                        ||"".equals(Type)||null==Type
                        ||"".equals(timeType)||null==timeType){
                    ToastUtils.showShortToast("请输入完整");
                }else{
                    setOkHttp1();
                }
                break;
        }
    }


    /**
     * 显示获取时间的对话框
     */
    private void showDialog(final TextView tvTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_date_time1, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker1);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        builder.setTitle("设置时间");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String month1 = month + "";
                String day1 = day + "";
                if (month <= 9) {
                    month1 = 0 + month1;
                }
                if (day <= 9) {
                    day1 = 0 + day1;
                }
                tvTime.setText(year + "-" + month1 + "-" + day1);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    //多少个月或年
    int numSubcolumns =1;
    //多少个类型，横轴条数
    int numColumns;
    //点击时显示的值
    List<AxisValue> axisValues = new ArrayList<AxisValue>();
    List<Column> columns = new ArrayList<Column>();
    //数值
    List<SubcolumnValue> values;


    //初始化数据并显示在图表上
    private void dataInit() {
        numColumns = types.length;
        columns.clear();
        Log.d(TAG,mDisTypes.size()+"");
        numSubcolumns=mDisTypes.size();
        axisValues.clear();
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                switch (i){
                    case 0: values.add(new SubcolumnValue((float)mDisTypes.get(j).getTeda(), COLORS[j]));break;
                    case 1: values.add(new SubcolumnValue((float)mDisTypes.get(j).getDa(), COLORS[j]));break;
                    case 2: values.add(new SubcolumnValue((float)mDisTypes.get(j).getZhong(), COLORS[j]));break;
                    case 3: values.add(new SubcolumnValue((float)mDisTypes.get(j).getXiao(), COLORS[j]));break;
                }

            }
            // 点击柱状图就展示数据量
            axisValues.add(new AxisValue(i).setLabel(types[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(false).setHasLabels(true));
        }
        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true)
                .setTextColor(Color.BLACK).setTextSize(10).setHasSeparationLine(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true)
                .setTextColor(Color.BLACK).setTextSize(10).setMaxLabelChars(4));

        columnChart.setColumnChartData(columnData);
        // Set value touch listener that will trigger changes for chartTop.
        columnChart.setOnValueTouchListener(new ValueTouchListener());
        // Set selection mode to keep selected month column highlighted.
        columnChart.setValueSelectionEnabled(true);
        columnChart.setZoomType(ZoomType.HORIZONTAL);

    }



    /**
     * 柱状图监听器
     *
     * @author 1017
     */
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex,
                                    SubcolumnValue value) {
        // generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {

        // generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }



    private void setOkHttp1() {
        progressDialog.show();
        try {
            OkHttpUtils.get().url(new ConnectUrl().getTaskDisCurveByScaleUrl())
                    .addParams("sessionId", sessionId)
                    .addParams("startTime", tvStartTime.getText().toString().trim())
                    .addParams("endTime", tvEndTime.getText().toString().trim())
                    .addParams("dis_id", Type)
                    .addParams("state", timeType)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressDialog.dismiss();
                            ToastUtils.showShortToast("网络故障，请检查网络！");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressDialog.dismiss();
                            String msg, status;
                            Log.d(TAG, "返回的数据：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                status = object.getString("status");
                                msg = object.getString("message");
                                //Log.d(TAG,"----"+object.toString());
                                if ("200".equals(status)) {
                                    mDisTypes=new ArrayList<DisType>();
                                    JSONObject message=object.getJSONObject("message");
                                    JSONArray dmtx=message.getJSONArray("happen_time");
                                    for(int i=0;i<dmtx.length();i++){
                                        DisType disType=new DisType();
                                        disType.setHappen_time(message.getJSONArray("happen_time").getString(i));
                                        disType.setDa(message.getJSONArray("da").getInt(i));
                                        disType.setZhong(message.getJSONArray("zhong").getInt(i));
                                        disType.setTeda(message.getJSONArray("teda").getInt(i));
                                        disType.setXiao(message.getJSONArray("xiao").getInt(i));

                                        Log.d(TAG, "disType.toString()：" + disType.toString());
                                        mDisTypes.add(disType);
                                    }
                                    LinearLayout ll = (LinearLayout) findViewById(R.id.ll_disType_charts);
                                    ll.setVisibility(View.VISIBLE);
                                    dataInit();
                                    setBiaoZhu();
                                 tvDisTypeName.setText(typeNames[Integer.parseInt(Type)-1]);
                                } else if ("400".equals(status)) {

                                    ToastUtils.showShortToast(msg);
                                    sp.edit().putBoolean("isLogin", false).apply();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            ToastUtils.showShortToast("加载失败！");
        }
    }
}
