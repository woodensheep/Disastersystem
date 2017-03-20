package com.nandity.disastersystem.bean;

import java.io.Serializable;

/**
 * Created by qingsong on 2017/2/23.
 */

public class TaskInfoBean implements Serializable{
    private String taskExtendsId;

    /*是否为灾害点*/
    private String mIsDisaster;
    /*灾害点等级*/
    private String mDisasterLevel;
    /*灾害点名称*/
    private String mDisasterName;
    /*经度*/
    private String mDisasterLng;
    /*纬度*/
    private String mDisasterLat;
    /*地址*/
    private String mDisasterLocation;
    /*联系人*/
    private String mDisasterContact;
    /*号码*/
    private String mDisasterMobile;
    /*新旧灾害点*/
    private String mDisasterType;
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

    public String getTaskExtendsId() {
        return taskExtendsId;
    }

    public void setTaskExtendsId(String taskExtendsId) {
        this.taskExtendsId = taskExtendsId;
    }

    public String getmIsDisaster() {
        return mIsDisaster;
    }

    public void setmIsDisaster(String mIsDisaster) {
        this.mIsDisaster = mIsDisaster;
    }

    public String getmDisasterLevel() {
        return mDisasterLevel;
    }

    public void setmDisasterLevel(String mDisasterLevel) {
        this.mDisasterLevel = mDisasterLevel;
    }

    public String getmDisasterName() {
        return mDisasterName;
    }

    public void setmDisasterName(String mDisasterName) {
        this.mDisasterName = mDisasterName;
    }

    public String getmDisasterLng() {
        return mDisasterLng;
    }

    public void setmDisasterLng(String mDisasterLng) {
        this.mDisasterLng = mDisasterLng;
    }

    public String getmDisasterLat() {
        return mDisasterLat;
    }

    public void setmDisasterLat(String mDisasterLat) {
        this.mDisasterLat = mDisasterLat;
    }

    public String getmDisasterLocation() {
        return mDisasterLocation;
    }

    public void setmDisasterLocation(String mDisasterLocation) {
        this.mDisasterLocation = mDisasterLocation;
    }

    public String getmDisasterContact() {
        return mDisasterContact;
    }

    public void setmDisasterContact(String mDisasterContact) {
        this.mDisasterContact = mDisasterContact;
    }

    public String getmDisasterMobile() {
        return mDisasterMobile;
    }

    public void setmDisasterMobile(String mDisasterMobile) {
        this.mDisasterMobile = mDisasterMobile;
    }

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

    public String getmDisasterType() {
        return mDisasterType;
    }


    public void setmDisasterType(String mDisasterType) {

        this.mDisasterType = mDisasterType;
    }

    @Override
    public String toString() {
        return "TaskInfoBean{" +
                "mDisasterName='" + mDisasterName + '\'' +
                ", mDisasterLng='" + mDisasterLng + '\'' +
                ", mDisasterLat='" + mDisasterLat + '\'' +
                ", mDisasterLocation='" + mDisasterLocation + '\'' +
                ", mDisasterContact='" + mDisasterContact + '\'' +
                ", mDisasterMobile='" + mDisasterMobile + '\'' +
                ", mDisasterType='" + mDisasterType + '\'' +
                ", mHappenTime='" + mHappenTime + '\'' +
                ", mRowNumber='" + mRowNumber + '\'' +
                ", mDisaster='" + mDisaster + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mTaskId='" + mTaskId + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mAreaName='" + mAreaName + '\'' +
                ", mSendName='" + mSendName + '\'' +
                ", mSurveyTime='" + mSurveyTime + '\'' +
                ", mTaskState='" + mTaskState + '\'' +
                ", mName='" + mName + '\'' +
                ", mTaskName='" + mTaskName + '\'' +
                '}';
    }
}
