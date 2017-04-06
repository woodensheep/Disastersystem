package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.PlanBean;

import java.util.List;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder>{
    private List<PlanBean> list;
    private Context context;

    public PlanAdapter(List<PlanBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_plan,null);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        PlanBean bean=list.get(position);
        holder.tvPlace.setText(bean.getPlace());
        holder.tvYear.setText(bean.getYear());
        holder.tvType.setText(bean.getType());
        holder.tvName.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvType,tvYear,tvPlace,tvPerson,tvTime;
        PlanViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.item_plan_name);
            tvType= (TextView) itemView.findViewById(R.id.item_plan_type);
            tvYear= (TextView) itemView.findViewById(R.id.item_plan_year);
            tvPlace= (TextView) itemView.findViewById(R.id.item_plan_place);
        }
    }
}
