package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.TaskInfoBean;
import com.nandity.disastersystem.database.TaskBean;

import java.util.List;

/**
 * Created by lemon on 2017/3/1.
 *  我的任务
 */

public class QueryDisasterAdapter extends RecyclerView.Adapter<QueryDisasterAdapter.MyViewHolder> implements View.OnClickListener {

    private List<TaskBean> mDatas;
    private Context context;
    private LayoutInflater inflater;

    public QueryDisasterAdapter(Context context, List<TaskBean> mDatas) {
        this.context = context;
        this.mDatas=mDatas;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_query, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvDisaster.setText(mDatas.get(position).getMDisaster());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDatas.get(position));
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(TaskBean) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDisaster;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDisaster = (TextView) itemView.findViewById(R.id.tv_query_disaster);
        }
    }


    /**
     * 设置item点击监听
     */
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, TaskBean taskBean);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
