package com.nandity.disastersystem.bean;

/**
 * Created by qingsong on 2017/2/23.
 */

public class TaskInfoBean {



    /*灾害发生时间*/
    private String mHappenTime;
    /* 任务排号*/
    private String mRowNumber;
    /*灾害点*/
    private String mDisaster;
    /*调查地点*/
    private String mAddress;
    /**
     * meiyong
     */
    private String mTaskId;
    /* 发起时间*/
    private String mStartTime;
    /* 所属乡镇 */
    private String mAreaName;
    /*发起人 */
    private String mSendName;

    public String getmSurveyTime() {
        return mSurveyTime;
    }

    public void setmSurveyTime(String mSurveyTime) {
        this.mSurveyTime = mSurveyTime;
    }

    /*调查时间 */
    private String mSurveyTime;
    /* 任务状态  1：未发送 2：已发送 3：已反馈 4：已完成 5：废弃*/
    private String mTaskState;

    public String getmSendName() {
        return mSendName;
    }

    public void setmSendName(String mSendName) {
        this.mSendName = mSendName;
    }

    public String getmAreaName() {
        return mAreaName;
    }

    public void setmAreaName(String mAreaName) {
        this.mAreaName = mAreaName;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmTaskState() {
        return mTaskState;
    }

    public void setmTaskState(String mTaskState) {
        this.mTaskState = mTaskState;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    /*    调查人    */
    private String mName;

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    /* 任务排号*/
    private String mTaskName;

    public String getmTaskId() {
        return mTaskId;
    }

    public void setmTaskId(String mTaskId) {
        this.mTaskId = mTaskId;
    }

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

    public String getmHappenTime() {
        return mHappenTime;
    }

    public void setmHappenTime(String mHappenTime) {
        this.mHappenTime = mHappenTime;
    }
}
