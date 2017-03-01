package com.nandity.disastersystem.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.SettingsActivity;
import com.nandity.disastersystem.bean.Category;

import me.drakeet.multitype.ItemViewProvider;

import static java.security.AccessController.getContext;

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
            @NonNull final ViewHolder holder, @NonNull final Category category) {
        holder.tv_setup.setText(category.text);
        holder.iv_setup.setBackgroundResource(category.id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(holder.itemView.getContext(), SettingsActivity.class);
                intent1.putExtra("settings", category.text);
                //Log.d("11111",category.type+"");
                intent1.putExtra("settings_type", category.type+"");
                holder.itemView.getContext().startActivity(intent1);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ImageView iv_setup;
        @NonNull
        private final TextView tv_setup;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_setup = (ImageView) itemView.findViewById(R.id.iv_setup);
            this.tv_setup = (TextView) itemView.findViewById(R.id.tv_setup);
        }
    }


}
