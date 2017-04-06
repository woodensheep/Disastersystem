package com.nandity.disastersystem.bean;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class NameBean {
    private int imageRes;
    private String name;

    public NameBean(int imageRes, String name) {
        this.imageRes = imageRes;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
