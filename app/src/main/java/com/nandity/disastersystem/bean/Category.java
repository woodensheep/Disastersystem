package com.nandity.disastersystem.bean;

import android.support.annotation.NonNull;

/**
 * Created by lemon on 2017/2/22.
 */

public class Category {

    @NonNull
    public String text;
    //图片
    public int id;
    public int type;

    public Category(@NonNull final String text,@NonNull final int id,@NonNull final int type) {
        this.text = text;
        this.id = id;
        this.type = type;

    }
}
