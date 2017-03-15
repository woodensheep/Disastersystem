package com.nandity.disastersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.adapter.MyTaskAdapter;
import com.nandity.disastersystem.app.MyApplication;
import com.nandity.disastersystem.bean.Category;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.constant.ConnectUrl;
import com.nandity.disastersystem.database.AudioPathBean;
import com.nandity.disastersystem.database.AudioPathBeanDao;
import com.nandity.disastersystem.database.BaseInfoBean;
import com.nandity.disastersystem.database.BaseInfoBeanDao;
import com.nandity.disastersystem.database.CollectInfoBean;
import com.nandity.disastersystem.database.CollectInfoBeanDao;
import com.nandity.disastersystem.database.PicturePathBean;
import com.nandity.disastersystem.database.PicturePathBeanDao;
import com.nandity.disastersystem.database.TaskBean;
import com.nandity.disastersystem.database.TaskBeanDao;
import com.nandity.disastersystem.database.VideoPathBean;
import com.nandity.disastersystem.database.VideoPathBeanDao;
import com.nandity.disastersystem.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

import static com.nandity.disastersystem.app.MyApplication.getContext;

public class MyTaskActivity extends AppCompatActivity {
    private String TAG="MyTaskActivity";
    private List<TaskInfoBean> mListData;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView mMyTaskRecyclerview;
    private LinearLayoutManager mLayoutManager;
    private MyTaskAdapter mAdapter;
    private String page;
    private String rows;
    private String sessionId;
    private SharedPreferences sp;
    private TabLayout.Tab mTab1;
    private TabLayout.Tab mTab2;
    private TabLayout.Tab mTab3;
    private ProgressDialog progressDialog;
    /*保存过得任务*/
    private List<TaskInfoBean> mSaveDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        page=0+"";
        rows=6+"";
        sessionId=sp.getString("sessionId", "");
        toolbar = (Toolbar) findViewById(R.id.my_task_toolbar);
        tabLayout= (TabLayout) findViewById(R.id.my_task_tablayout);
        mMyTaskRecyclerview= (RecyclerView) findViewById(R.id.my_task_recyclerview);
        mListData = new ArrayList<TaskInfoBean>();
        mSaveDatas = new ArrayList<TaskInfoBean>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mMyTaskRecyclerview.setLayoutManager(mLayoutManager);
        //固定高度
        mMyTaskRecyclerview.setHasFixedSize(true);
        //绑定adapter
        mAdapter = new MyTaskAdapter(getContext(), mListData);
        mMyTaskRecyclerview.setAdapter(mAdapter);
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        initView();
        initDatas();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tabLayout.getTabAt(0).isSelected()){
            initDatas();
        }else{
            setTab(0);
        }
    }

    private void initDatas() {
        progressDialog.show();
        setOkHttp();
        //mAdapter.notifyDataSetChanged();
    }

    //加载未上传
    private void initDatas2() {
        progressDialog.show();
        mListData.clear();
        for (int i=0;i<mSaveDatas.size();i++){
            mListData.add(mSaveDatas.get(i));
        }
        mAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    private void setListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        initDatas();
                        Log.d(TAG,"--------------------------------1");
                        break;
                    case 1:
                        initDatas2();
                        Log.d(TAG,"--------------------------------2");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mAdapter.setOnItemClickListener(new MyTaskAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, TaskInfoBean taskInfoBean) {

                Intent intent=new Intent(getContext(),TaskActivity.class);
                //将任务id号传递
                intent.putExtra("TaskID",taskInfoBean.getmRowNumber());
                startActivity(intent);
            }
        });

    }

    private void initView() {
        mTab1=tabLayout.newTab().setText("未处理(0)");
        //mTab2=tabLayout.newTab().setText("已上传(0)");
        mTab3=tabLayout.newTab().setText("未完成(0)");
        tabLayout.addTab(mTab1,true);
        //tabLayout.addTab(mTab2);
        tabLayout.addTab(mTab3);
    }


    /*选择mTabID=0未处理和mTabID=1未上传 */
    public  void setTab(int mTabID){
        tabLayout.getTabAt(mTabID).select();
    }

    private void setOkHttp() {

        try {
            OkHttpUtils.get().url(new ConnectUrl().getStartTaskUrl())
                    .addParams("page", page)
                    .addParams("rows", rows)
                    .addParams("sessionId",sessionId)
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
                                if ("200".equals(status)) {
                                    mListData.clear();
                                    mSaveDatas.clear();
                                    JSONArray message=object.getJSONArray("message");
                                    for(int i = 0; i < message.length(); i++){//遍历JSONArray
                                        JSONObject oj = message.getJSONObject(i);
                                        Log.d(TAG, "message：id-" + oj.getString("id"));
                                        TaskInfoBean taskInfoBean=new TaskInfoBean();
                                        taskInfoBean.setmRowNumber(oj.getString("id"));
                                        taskInfoBean.setmTaskName(oj.getString("task_name"));
                                        taskInfoBean.setmAddress(oj.getString("survey_site"));
                                        taskInfoBean.setmDisaster(oj.getString("dis_name"));
                                        BaseInfoBean infoBean = MyApplication.getDaoSession().getBaseInfoBeanDao().queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(oj.getString("id"))).unique();
                                        CollectInfoBean collectInfoBean = MyApplication.getDaoSession().getCollectInfoBeanDao().queryBuilder().where(CollectInfoBeanDao.Properties.TaskId.eq(oj.getString("id"))).unique();
                                        AudioPathBean audioPathBean = MyApplication.getDaoSession().getAudioPathBeanDao().queryBuilder().where(AudioPathBeanDao.Properties.TaskId.eq(oj.getString("id"))).unique();
                                        List<PicturePathBean> picturePathBean = MyApplication.getDaoSession().getPicturePathBeanDao().queryBuilder().where(PicturePathBeanDao.Properties.TaskId.eq(oj.getString("id"))).list();
                                        VideoPathBean videoPathBean = MyApplication.getDaoSession().getVideoPathBeanDao().queryBuilder().where(VideoPathBeanDao.Properties.TaskId.eq(oj.getString("id"))).unique();
                                        String type=3+"";
                                        if (infoBean==null&&collectInfoBean==null&&audioPathBean==null&&picturePathBean.size()==0&&videoPathBean==null){
                                            if(oj.getString("task_state")==type){
                                                mSaveDatas.add(taskInfoBean);
                                            }else{
                                                mListData.add(taskInfoBean);
                                            }
                                        }else{
                                            mSaveDatas.add(taskInfoBean);
                                        }
                                    }
                                    mTab1.setText("未处理("+mListData.size()+")");
                                    mTab3.setText("未上传("+mSaveDatas.size()+")");
                                    mAdapter.notifyDataSetChanged();
                                } else if ("400".equals(status)) {
                                    ToastUtils.showShortToast(msg);
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
