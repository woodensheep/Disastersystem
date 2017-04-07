package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.EquipmentBean;
import com.nandity.disastersystem.bean.PersonBean;

import java.util.List;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder>{
    private List<EquipmentBean> list;
    private Context context;

    public EquipmentAdapter(List<EquipmentBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_equipment,null);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder holder, int position) {
        EquipmentBean bean=list.get(position);
        holder.tvName.setText(bean.getEquipmentName());
        holder.tvNumber.setText(bean.getEquipmentNum());
        holder.tvPlace.setText(bean.getEquipmentPlace());
        holder.tvType.setText(bean.getEquipmentType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvType,tvNumber,tvPlace;
        EquipmentViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.item_equipment_name);
            tvType= (TextView) itemView.findViewById(R.id.item_equipment_type);
            tvNumber= (TextView) itemView.findViewById(R.id.item_equipment_number);
            tvPlace= (TextView) itemView.findViewById(R.id.item_equipment_place);
        }
    }
}
