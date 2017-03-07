package com.nandity.disastersystem.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenPeng on 2017/3/7.
 */
@Entity
public class BaseInfoBean {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 灾害点名称
     */
    private String baseInfoName;
    /**
     * 经度
     */
    private String baseInfoLng;
    /**
     * 纬度
     */
    private String baseInfoLat;
    /**
     * 地址
     */
    private String baseInfoAddress;
    /**
     * 乡镇
     */
    private String baseInfoVillage;
    /**
     * 联系人
     */
    private String baseInfoContact;
    /**
     * 联系人电话
     */
    private String baseInfoMobile;
    /**
     * 灾害等级
     */
    private String baseInfoLevel;
    /**
     * 是否新灾害点
     */
    private String baseInfoType;
    /**
     * 是否为灾害点
     */
    private String baseInfoIsDisaster;

    @Generated(hash = 215187071)
    public BaseInfoBean(Long id, String taskId, String baseInfoName,
            String baseInfoLng, String baseInfoLat, String baseInfoAddress,
            String baseInfoVillage, String baseInfoContact, String baseInfoMobile,
            String baseInfoLevel, String baseInfoType, String baseInfoIsDisaster) {
        this.id = id;
        this.taskId = taskId;
        this.baseInfoName = baseInfoName;
        this.baseInfoLng = baseInfoLng;
        this.baseInfoLat = baseInfoLat;
        this.baseInfoAddress = baseInfoAddress;
        this.baseInfoVillage = baseInfoVillage;
        this.baseInfoContact = baseInfoContact;
        this.baseInfoMobile = baseInfoMobile;
        this.baseInfoLevel = baseInfoLevel;
        this.baseInfoType = baseInfoType;
        this.baseInfoIsDisaster = baseInfoIsDisaster;
    }

    @Generated(hash = 139291923)
    public BaseInfoBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBaseInfoName() {
        return baseInfoName;
    }

    public void setBaseInfoName(String baseInfoName) {
        this.baseInfoName = baseInfoName;
    }

    public String getBaseInfoLng() {
        return baseInfoLng;
    }

    public void setBaseInfoLng(String baseInfoLng) {
        this.baseInfoLng = baseInfoLng;
    }

    public String getBaseInfoLat() {
        return baseInfoLat;
    }

    public void setBaseInfoLat(String baseInfoLat) {
        this.baseInfoLat = baseInfoLat;
    }

    public String getBaseInfoAddress() {
        return baseInfoAddress;
    }

    public void setBaseInfoAddress(String baseInfoAddress) {
        this.baseInfoAddress = baseInfoAddress;
    }

    public String getBaseInfoVillage() {
        return baseInfoVillage;
    }

    public void setBaseInfoVillage(String baseInfoVillage) {
        this.baseInfoVillage = baseInfoVillage;
    }

    public String getBaseInfoContact() {
        return baseInfoContact;
    }

    public void setBaseInfoContact(String baseInfoContact) {
        this.baseInfoContact = baseInfoContact;
    }

    public String getBaseInfoMobile() {
        return baseInfoMobile;
    }

    public void setBaseInfoMobile(String baseInfoMobile) {
        this.baseInfoMobile = baseInfoMobile;
    }

    public String getBaseInfoLevel() {
        return baseInfoLevel;
    }

    public void setBaseInfoLevel(String baseInfoLevel) {
        this.baseInfoLevel = baseInfoLevel;
    }

    public String getBaseInfoType() {
        return baseInfoType;
    }

    public void setBaseInfoType(String baseInfoType) {
        this.baseInfoType = baseInfoType;
    }

    public String getBaseInfoIsDisaster() {
        return baseInfoIsDisaster;
    }

    public void setBaseInfoIsDisaster(String baseInfoIsDisaster) {
        this.baseInfoIsDisaster = baseInfoIsDisaster;
    }

    @Override
    public String toString() {
        return "BaseInfoBean{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", baseInfoName='" + baseInfoName + '\'' +
                ", baseInfoLng='" + baseInfoLng + '\'' +
                ", baseInfoLat='" + baseInfoLat + '\'' +
                ", baseInfoAddress='" + baseInfoAddress + '\'' +
                ", baseInfoVillage='" + baseInfoVillage + '\'' +
                ", baseInfoContact='" + baseInfoContact + '\'' +
                ", baseInfoMobile='" + baseInfoMobile + '\'' +
                ", baseInfoLevel='" + baseInfoLevel + '\'' +
                ", baseInfoType='" + baseInfoType + '\'' +
                ", baseInfoIsDisaster='" + baseInfoIsDisaster + '\'' +
                '}';
    }
}
