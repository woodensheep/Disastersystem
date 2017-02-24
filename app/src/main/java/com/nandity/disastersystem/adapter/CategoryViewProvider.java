package com.nandity.disastersystem.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.bean.Category;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by lemon on 2017/2/23.
 */

public class CategoryViewProvider
        extends ItemViewProvider<Category, CategoryViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(
            @NonNull ViewHolder holder, @NonNull Category category) {
        holder.tv_setup.setText(category.text);
        holder.iv_setup.setBackgroundResource(category.id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull private final ImageView iv_setup;
        @NonNull private final TextView tv_setup;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_setup = (ImageView) itemView.findViewById(R.id.iv_setup);
            this.tv_setup = (TextView) itemView.findViewById(R.id.tv_setup);

        }
    }
}
