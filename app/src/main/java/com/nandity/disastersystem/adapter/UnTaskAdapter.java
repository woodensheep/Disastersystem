package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.dataBase.TaskBean;

import java.util.List;

/**
 * Created by lemon on 2017/3/1.
 */

public class UnTaskAdapter extends RecyclerView.Adapter<UnTaskAdapter.MyViewHolder>{

    private List<TaskBean> mDatas;
    private Context context;
    private LayoutInflater inflater;

    public UnTaskAdapter(Context context,List<TaskBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_task,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvTask.setText("本地未上传任务");
        holder.tvAddress.setText("地址： "+mDatas.get(position).getMaddress());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvAddress;
        TextView tvTask;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTask= (TextView) itemView.findViewById(R.id.tv_task);
            tvAddress= (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}
