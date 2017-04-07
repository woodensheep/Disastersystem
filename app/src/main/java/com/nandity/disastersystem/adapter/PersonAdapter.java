package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.PersonBean;
import com.nandity.disastersystem.bean.PlanBean;

import java.util.List;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    private List<PersonBean> list;
    private Context context;

    public PersonAdapter(List<PersonBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_person,null);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        PersonBean bean=list.get(position);
        holder.tvAddress.setText(bean.getAddress());
        holder.tvMobile.setText(bean.getMobile());
        holder.tvName.setText(bean.getPersonName());
        holder.tvType.setText(bean.getPersonType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvType,tvAddress,tvMobile;
        PersonViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.item_person_name);
            tvType= (TextView) itemView.findViewById(R.id.item_person_type);
            tvAddress= (TextView) itemView.findViewById(R.id.item_person_address);
            tvMobile= (TextView) itemView.findViewById(R.id.item_person_mobile);
        }
    }
}
