package com.nandity.disastersystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.DirectoryBean;

import java.util.List;

/**
 * Created by qingsong on 2017/2/23.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private Context context;
    private List<DirectoryBean> beanList;

    public DirectoryAdapter(Context context, List<DirectoryBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final DirectoryBean item = beanList.get(position);

        viewHolder.mTextView.setText(item.getName());
        viewHolder.mTextView1.setText(item.getNumber());
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return null == beanList ? 0 : beanList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView, mTextView1;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.directoryName);
            mTextView1 = (TextView) view.findViewById(R.id.directoryNumber);
        }
    }
}
