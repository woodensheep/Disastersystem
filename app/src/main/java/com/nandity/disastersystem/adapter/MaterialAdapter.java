package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.MaterialBean;
import com.nandity.disastersystem.bean.PersonBean;

import java.util.List;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>{
    private List<MaterialBean> list;
    private Context context;

    public MaterialAdapter(List<MaterialBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_material,null);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder holder, int position) {
        MaterialBean bean=list.get(position);
        holder.tvNumber.setText(bean.getMaterialNumber());
        holder.tvName.setText(bean.getMaterialName());
        holder.tvType.setText(bean.getMaterialType());
        if ("1".equals(bean.getMaterialStatus())){
            holder.tvStatus.setText("正常");
        }else if ("2".equals(bean.getMaterialStatus())){
            holder.tvStatus.setText("耗尽");
        }else {
            holder.tvStatus.setText("废弃");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvType,tvNumber,tvStatus;
        MaterialViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.item_material_name);
            tvType= (TextView) itemView.findViewById(R.id.item_material_type);
            tvNumber= (TextView) itemView.findViewById(R.id.item_material_number);
            tvStatus= (TextView) itemView.findViewById(R.id.item_material_status);
        }
    }
}
