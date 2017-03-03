package com.nandity.disastersystem.bean;

/**
 * Created by qingsong on 2017/2/23.
 */

public class TaskInfoBean {
    /* 任务排号*/
    private String mRowNumber;
    /*灾害点*/
    private String mDisaster;
    /*调查地点*/
    private String mAddress;

    public String getmRowNumber() {
        return mRowNumber;
    }

    public void setmRowNumber(String mRowNumber) {
        this.mRowNumber = mRowNumber;
    }

    public String getmDisaster() {
        return mDisaster;
    }

    public void setmDisaster(String mDisaster) {
        this.mDisaster = mDisaster;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
