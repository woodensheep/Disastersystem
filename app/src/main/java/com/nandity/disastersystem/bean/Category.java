package com.nandity.disastersystem.bean;

import android.support.annotation.NonNull;

/**
 * Created by lemon on 2017/2/22.
 */

public class Category {

    @NonNull
    public String text;
    public int id;

    public Category(@NonNull final String text,@NonNull final int id) {
        this.text = text;
        this.id = id;
    }
}
