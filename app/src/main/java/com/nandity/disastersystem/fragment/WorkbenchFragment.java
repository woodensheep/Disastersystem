package com.nandity.disastersystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.CompleteTaskActivity;
import com.nandity.disastersystem.activity.CreateTaskActivity;
import com.nandity.disastersystem.activity.EquipmentActivity;
import com.nandity.disastersystem.activity.LoginActivity;
import com.nandity.disastersystem.activity.MaterialActivity;
import com.nandity.disastersystem.activity.MyTaskActivity;
import com.nandity.disastersystem.activity.PersonActivity;
import com.nandity.disastersystem.activity.PlanActivity;
import com.nandity.disastersystem.bean.NameBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by lemon on 2017/2/22.
 */

public class WorkbenchFragment extends Fragment {
    @BindView(R.id.title_bar)
    TextView titleBar;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.el_workbench_list_view)
    ExpandableListView elWorkbenchListView;
    Unbinder unbinder;
    private Activity activity;
    private Intent intent;
    private String myTask="0";
    private String allTask="0";
    private SharedPreferences sp;
    private String sessionId;
    private List<NameBean> parent;
    private Map<String,List<String>> map;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_workbench, container, false);
        unbinder = ButterKnife.bind(this, view);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initData();
        setAdapter();
        setListener();
        ((AppCompatActivity) activity).setSupportActionBar(myToolbar);
        myToolbar.setTitle("");
        return view;
    }

    private void setAdapter() {
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        elWorkbenchListView.setIndicatorBounds(width-40, width-10);
        adapter=new MyAdapter();
        elWorkbenchListView.setAdapter(adapter);
    }

    private void initData() {
        parent=new ArrayList<>();
        map=new HashMap<>();
        parent.add(new NameBean(R.mipmap.investigation,"应急指挥"));
        parent.add(new NameBean(R.mipmap.manage,"日常管理"));
        parent.add(new NameBean(R.mipmap.statistics,"统计分析"));
        List<String> list1=new ArrayList<>();
        list1.add("我的任务   (数量:"+myTask+")");
        list1.add("完成任务   (数量:"+allTask+")");
        list1.add("发起任务");
        map.put(parent.get(0).getName(),list1);
        List<String> list2=new ArrayList<>();
        list2.add("应急预案管理");
        list2.add("救援人员管理");
        list2.add("应急设备管理");
        list2.add("应急物资管理");
        map.put(parent.get(1).getName(),list2);
        List<String> list3=new ArrayList<>();
        list3.add("任务台帐");
        list3.add("任务灾害点统计");
        list3.add("灾险情规模统计");
        list3.add("灾险情类型统计");
        list3.add("灾险情诱发因素统计");
        map.put(parent.get(2).getName(),list3);
    }

    private void setListener() {
        elWorkbenchListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition==0){
                    setData();
                }
                return false;
            }
        });
        elWorkbenchListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition==0){
                    if (childPosition==0){
                        intent=new Intent(activity, MyTaskActivity.class);//我的任务
                    }else if (childPosition==1){
                        intent=new Intent(activity, CompleteTaskActivity.class);//完成任务
                    }else {
                        intent=new Intent(activity, CreateTaskActivity.class);//发起任务
                    }
                }else if (groupPosition==1){
                    if (childPosition==0){
                        intent=new Intent(activity, PlanActivity.class);//应急预案管理
                    }else if (childPosition==1){
                        intent=new Intent(activity, PersonActivity.class);//救援人员管理
                    }else if (childPosition==2){
                        intent=new Intent(activity, EquipmentActivity.class);//应急设备管理
                    }else {
                        intent=new Intent(activity, MaterialActivity.class);//应急物资管理
                    }
                }else {
                    if (childPosition==0){
                        //任务台帐
                    }else if (childPosition==1){
                        //任务灾害点统计
                    }else if (childPosition==2){
                        //灾险情规模统计
                    }else if (childPosition==3){
                        //灾险情类型统计
                    }else {
                        //灾险情诱发因素统计
                    }
                }
                startActivity(intent);
                return true;
            }
        });
    }

    private void setData() {
        Log.d("WorkbenchFragment", "sessionId:" + sessionId);
        OkHttpUtils.get().url(new ConnectUrl().getTaskNumUrl())
                .addParams("sessionId", sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            Log.d("WorkbenchFragment", "获取任务数量的status:" + status);
                            Log.d("WorkbenchFragment", "获取任务数量的msg:" + status);
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                myTask = array.getJSONObject(0).getString("myTask");
                                allTask = array.getJSONObject(1).getString("allTask");
                                Log.d("WorkbenchFragment", "我的任务数量：" + myTask);
                                Log.d("WorkbenchFragment", "完成任务数量：" + allTask);
                                initData();
                                adapter.notifyDataSetChanged();
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            String key=parent.get(groupPosition).getName();
            int size=map.get(key).size();
            return size;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key=parent.get(groupPosition).getName();
            return map.get(key).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup group) {
            if (convertView==null){
                convertView=LayoutInflater.from(activity).inflate(R.layout.item_workbench_parent,null);
            }
            ImageView ivImage= (ImageView) convertView.findViewById(R.id.iv_workbench_parent);
            TextView tvName= (TextView) convertView.findViewById(R.id.tv_workbench_parent_name);
            NameBean bean=parent.get(groupPosition);
            ivImage.setImageResource(bean.getImageRes());
            tvName.setText(bean.getName());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup group) {
            if (convertView==null){
                convertView=LayoutInflater.from(activity).inflate(R.layout.item_workbench_child,null);
            }
            TextView tvName= (TextView) convertView.findViewById(R.id.tv_workbench_child_name);
            String key=parent.get(groupPosition).getName();
            String name=map.get(key).get(childPosition);
            tvName.setText(name);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
