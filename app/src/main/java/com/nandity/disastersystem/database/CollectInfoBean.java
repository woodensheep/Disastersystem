package com.nandity.disastersystem.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenPeng on 2017/3/8.
 */
@Entity
public class CollectInfoBean {
    @Id(autoincrement = true)
    private Long id;
    private String emergencyHuNo;
    private String emergencyPersonNo;
    /*任务ID*/
    private String taskId;
    /*发生地点*/
    private String collectInfoPlace;
    /*灾险情类型*/
    private String collectInfoType;
    /*灾情还是险情*/
    private String collectInfoDisOrDan;
    /*灾险情规模*/
    private String collectInfoDisasterLevel;
    /*灾害成因*/
    private String collectInfoDisasterReason;
    /*是否调查过*/
    private String collectInfoIsResearch;
    /*死亡人数*/
    private String collectInfoDead;
    /*失踪人数*/
    private String collectInfoMiss;
    /*重伤人数*/
    private String collectInfoHeavyInjured;
    /*轻伤人数*/
    private String collectInfoSoftInjured;
    /*直接经济损失*/
    private String collectInfoEconomicLoss;
    /*房屋坍塌间数*/
    private String collectInfoHouseCollapseNum;
    /*房屋坍塌面积*/
    private String collectInfoHouseCollapseArea;
    /*房屋受损间数*/
    private String collectInfoHouseDamageNum;
    /*房屋受损面积*/
    private String collectInfoHouseDamageArea;
    /*其它损失*/
    private String collectInfoAnotherDamage;
    /*威胁户数*/
    private String collectInfoFamily;
    /*威胁人数*/
    private String collectInfoPeople;
    /*威胁在家户数*/
    private String collectInfoAtHomeFamily;
    /*威胁在家人数*/
    private String collectInfoAtHomePeople;
    /*威胁房屋间数*/
    private String collectInfoHouse;
    /*威胁房屋面积*/
    private String collectInfoHouseArea;
    /*其他威胁*/
    private String collectInfoAnotherDisaster;
    /*滑坡长度*/
    private String collectInfoLandslideLength;
    /*滑坡横宽*/
    private String collectInfoLandslideWidth;
    /*滑坡面积*/
    private String collectInfoLandslideArea;
    /*滑坡体积*/
    private String collectInfoLandslideVolume;
    /*变形区域长*/
    private String collectInfoDistortionLength;
    /*变形区域宽*/
    private String collectInfoDistortionWidth;
    /*变形面积*/
    private String collectInfoDistortionArea;
    /*变形体积*/
    private String collectInfoDistortionVolume;
    /*滑动距离*/
    private String collectInfoSlideDistance;
    /*地裂条数*/
    private String collectInfoCrackNumber;
    /*最小地裂长度*/
    private String collectInfoCrackMinLength;
    /*最大地裂长度*/
    private String collectInfoCrackMaxLength;
    /*最小地裂宽度*/
    private String collectInfoCrackMinWidth;
    /*最大地裂宽度*/
    private String collectInfoCrackMaxWidth;
    /*最大下错*/
    private String collectInfoMaxDislocation;
    /*危岩带长*/
    private String collectInfoRockLength;
    /*危岩带宽*/
    private String collectInfoRockWidth;
    /*危岩体积*/
    private String collectInfoRockVolume;
    /*崩塌体积*/
    private String collectInfoCollapseVolume;
    /*残留体积*/
    private String collectInfoResidualVolume;
    /*其他情况*/
    private String collectInfoAnotherThings;
    /*已采取措施*/
    private String collectInfoMeasure;
    /*措施备注*/
    private String collectInfoMeasureRemark;
    /*处置意见*/
    private String collectInfoDisposition;
    /*处置意见备注*/
    private String collectInfoDispositionRemark;
    /*出发时间*/
    private String collectInfoGoTime;
    /*上传时间*/
    private String uploadTime;
    @Generated(hash = 1871841491)
    public CollectInfoBean(Long id, String emergencyHuNo, String emergencyPersonNo,
            String taskId, String collectInfoPlace, String collectInfoType,
            String collectInfoDisOrDan, String collectInfoDisasterLevel,
            String collectInfoDisasterReason, String collectInfoIsResearch,
            String collectInfoDead, String collectInfoMiss, String collectInfoHeavyInjured,
            String collectInfoSoftInjured, String collectInfoEconomicLoss,
            String collectInfoHouseCollapseNum, String collectInfoHouseCollapseArea,
            String collectInfoHouseDamageNum, String collectInfoHouseDamageArea,
            String collectInfoAnotherDamage, String collectInfoFamily,
            String collectInfoPeople, String collectInfoAtHomeFamily,
            String collectInfoAtHomePeople, String collectInfoHouse,
            String collectInfoHouseArea, String collectInfoAnotherDisaster,
            String collectInfoLandslideLength, String collectInfoLandslideWidth,
            String collectInfoLandslideArea, String collectInfoLandslideVolume,
            String collectInfoDistortionLength, String collectInfoDistortionWidth,
            String collectInfoDistortionArea, String collectInfoDistortionVolume,
            String collectInfoSlideDistance, String collectInfoCrackNumber,
            String collectInfoCrackMinLength, String collectInfoCrackMaxLength,
            String collectInfoCrackMinWidth, String collectInfoCrackMaxWidth,
            String collectInfoMaxDislocation, String collectInfoRockLength,
            String collectInfoRockWidth, String collectInfoRockVolume,
            String collectInfoCollapseVolume, String collectInfoResidualVolume,
            String collectInfoAnotherThings, String collectInfoMeasure,
            String collectInfoMeasureRemark, String collectInfoDisposition,
            String collectInfoDispositionRemark, String collectInfoGoTime, String uploadTime) {
        this.id = id;
        this.emergencyHuNo = emergencyHuNo;
        this.emergencyPersonNo = emergencyPersonNo;
        this.taskId = taskId;
        this.collectInfoPlace = collectInfoPlace;
        this.collectInfoType = collectInfoType;
        this.collectInfoDisOrDan = collectInfoDisOrDan;
        this.collectInfoDisasterLevel = collectInfoDisasterLevel;
        this.collectInfoDisasterReason = collectInfoDisasterReason;
        this.collectInfoIsResearch = collectInfoIsResearch;
        this.collectInfoDead = collectInfoDead;
        this.collectInfoMiss = collectInfoMiss;
        this.collectInfoHeavyInjured = collectInfoHeavyInjured;
        this.collectInfoSoftInjured = collectInfoSoftInjured;
        this.collectInfoEconomicLoss = collectInfoEconomicLoss;
        this.collectInfoHouseCollapseNum = collectInfoHouseCollapseNum;
        this.collectInfoHouseCollapseArea = collectInfoHouseCollapseArea;
        this.collectInfoHouseDamageNum = collectInfoHouseDamageNum;
        this.collectInfoHouseDamageArea = collectInfoHouseDamageArea;
        this.collectInfoAnotherDamage = collectInfoAnotherDamage;
        this.collectInfoFamily = collectInfoFamily;
        this.collectInfoPeople = collectInfoPeople;
        this.collectInfoAtHomeFamily = collectInfoAtHomeFamily;
        this.collectInfoAtHomePeople = collectInfoAtHomePeople;
        this.collectInfoHouse = collectInfoHouse;
        this.collectInfoHouseArea = collectInfoHouseArea;
        this.collectInfoAnotherDisaster = collectInfoAnotherDisaster;
        this.collectInfoLandslideLength = collectInfoLandslideLength;
        this.collectInfoLandslideWidth = collectInfoLandslideWidth;
        this.collectInfoLandslideArea = collectInfoLandslideArea;
        this.collectInfoLandslideVolume = collectInfoLandslideVolume;
        this.collectInfoDistortionLength = collectInfoDistortionLength;
        this.collectInfoDistortionWidth = collectInfoDistortionWidth;
        this.collectInfoDistortionArea = collectInfoDistortionArea;
        this.collectInfoDistortionVolume = collectInfoDistortionVolume;
        this.collectInfoSlideDistance = collectInfoSlideDistance;
        this.collectInfoCrackNumber = collectInfoCrackNumber;
        this.collectInfoCrackMinLength = collectInfoCrackMinLength;
        this.collectInfoCrackMaxLength = collectInfoCrackMaxLength;
        this.collectInfoCrackMinWidth = collectInfoCrackMinWidth;
        this.collectInfoCrackMaxWidth = collectInfoCrackMaxWidth;
        this.collectInfoMaxDislocation = collectInfoMaxDislocation;
        this.collectInfoRockLength = collectInfoRockLength;
        this.collectInfoRockWidth = collectInfoRockWidth;
        this.collectInfoRockVolume = collectInfoRockVolume;
        this.collectInfoCollapseVolume = collectInfoCollapseVolume;
        this.collectInfoResidualVolume = collectInfoResidualVolume;
        this.collectInfoAnotherThings = collectInfoAnotherThings;
        this.collectInfoMeasure = collectInfoMeasure;
        this.collectInfoMeasureRemark = collectInfoMeasureRemark;
        this.collectInfoDisposition = collectInfoDisposition;
        this.collectInfoDispositionRemark = collectInfoDispositionRemark;
        this.collectInfoGoTime = collectInfoGoTime;
        this.uploadTime = uploadTime;
    }

    @Generated(hash = 1212400702)
    public CollectInfoBean() {
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

    public String getCollectInfoPlace() {
        return collectInfoPlace;
    }

    public void setCollectInfoPlace(String collectInfoPlace) {
        this.collectInfoPlace = collectInfoPlace;
    }

    public String getCollectInfoType() {
        return collectInfoType;
    }

    public void setCollectInfoType(String collectInfoType) {
        this.collectInfoType = collectInfoType;
    }

    public String getCollectInfoDisOrDan() {
        return collectInfoDisOrDan;
    }

    public void setCollectInfoDisOrDan(String collectInfoDisOrDan) {
        this.collectInfoDisOrDan = collectInfoDisOrDan;
    }

    public String getCollectInfoDisasterLevel() {
        return collectInfoDisasterLevel;
    }

    public void setCollectInfoDisasterLevel(String collectInfoDisasterLevel) {
        this.collectInfoDisasterLevel = collectInfoDisasterLevel;
    }

    public String getCollectInfoDisasterReason() {
        return collectInfoDisasterReason;
    }

    public void setCollectInfoDisasterReason(String collectInfoDisasterReason) {
        this.collectInfoDisasterReason = collectInfoDisasterReason;
    }

    public String getCollectInfoIsResearch() {
        return collectInfoIsResearch;
    }

    public void setCollectInfoIsResearch(String collectInfoIsResearch) {
        this.collectInfoIsResearch = collectInfoIsResearch;
    }

    public String getCollectInfoDead() {
        return collectInfoDead;
    }

    public void setCollectInfoDead(String collectInfoDead) {
        this.collectInfoDead = collectInfoDead;
    }

    public String getCollectInfoMiss() {
        return collectInfoMiss;
    }

    public void setCollectInfoMiss(String collectInfoMiss) {
        this.collectInfoMiss = collectInfoMiss;
    }

    public String getCollectInfoHeavyInjured() {
        return collectInfoHeavyInjured;
    }

    public void setCollectInfoHeavyInjured(String collectInfoHeavyInjured) {
        this.collectInfoHeavyInjured = collectInfoHeavyInjured;
    }

    public String getCollectInfoSoftInjured() {
        return collectInfoSoftInjured;
    }

    public void setCollectInfoSoftInjured(String collectInfoSoftInjured) {
        this.collectInfoSoftInjured = collectInfoSoftInjured;
    }

    public String getCollectInfoEconomicLoss() {
        return collectInfoEconomicLoss;
    }

    public void setCollectInfoEconomicLoss(String collectInfoEconomicLoss) {
        this.collectInfoEconomicLoss = collectInfoEconomicLoss;
    }

    public String getCollectInfoHouseCollapseNum() {
        return collectInfoHouseCollapseNum;
    }

    public void setCollectInfoHouseCollapseNum(String collectInfoHouseCollapseNum) {
        this.collectInfoHouseCollapseNum = collectInfoHouseCollapseNum;
    }

    public String getCollectInfoHouseCollapseArea() {
        return collectInfoHouseCollapseArea;
    }

    public void setCollectInfoHouseCollapseArea(String collectInfoHouseCollapseArea) {
        this.collectInfoHouseCollapseArea = collectInfoHouseCollapseArea;
    }

    public String getCollectInfoHouseDamageNum() {
        return collectInfoHouseDamageNum;
    }

    public void setCollectInfoHouseDamageNum(String collectInfoHouseDamageNum) {
        this.collectInfoHouseDamageNum = collectInfoHouseDamageNum;
    }

    public String getCollectInfoHouseDamageArea() {
        return collectInfoHouseDamageArea;
    }

    public void setCollectInfoHouseDamageArea(String collectInfoHouseDamageArea) {
        this.collectInfoHouseDamageArea = collectInfoHouseDamageArea;
    }

    public String getCollectInfoAnotherDamage() {
        return collectInfoAnotherDamage;
    }

    public void setCollectInfoAnotherDamage(String collectInfoAnotherDamage) {
        this.collectInfoAnotherDamage = collectInfoAnotherDamage;
    }

    public String getCollectInfoFamily() {
        return collectInfoFamily;
    }

    public void setCollectInfoFamily(String collectInfoFamily) {
        this.collectInfoFamily = collectInfoFamily;
    }

    public String getCollectInfoPeople() {
        return collectInfoPeople;
    }

    public void setCollectInfoPeople(String collectInfoPeople) {
        this.collectInfoPeople = collectInfoPeople;
    }

    public String getCollectInfoAtHomeFamily() {
        return collectInfoAtHomeFamily;
    }

    public void setCollectInfoAtHomeFamily(String collectInfoAtHomeFamily) {
        this.collectInfoAtHomeFamily = collectInfoAtHomeFamily;
    }

    public String getCollectInfoAtHomePeople() {
        return collectInfoAtHomePeople;
    }

    public void setCollectInfoAtHomePeople(String collectInfoAtHomePeople) {
        this.collectInfoAtHomePeople = collectInfoAtHomePeople;
    }

    public String getCollectInfoHouse() {
        return collectInfoHouse;
    }

    public void setCollectInfoHouse(String collectInfoHouse) {
        this.collectInfoHouse = collectInfoHouse;
    }

    public String getCollectInfoHouseArea() {
        return collectInfoHouseArea;
    }

    public void setCollectInfoHouseArea(String collectInfoHouseArea) {
        this.collectInfoHouseArea = collectInfoHouseArea;
    }

    public String getCollectInfoAnotherDisaster() {
        return collectInfoAnotherDisaster;
    }

    public void setCollectInfoAnotherDisaster(String collectInfoAnotherDisaster) {
        this.collectInfoAnotherDisaster = collectInfoAnotherDisaster;
    }

    public String getCollectInfoLandslideLength() {
        return collectInfoLandslideLength;
    }

    public void setCollectInfoLandslideLength(String collectInfoLandslideLength) {
        this.collectInfoLandslideLength = collectInfoLandslideLength;
    }

    public String getCollectInfoLandslideWidth() {
        return collectInfoLandslideWidth;
    }

    public void setCollectInfoLandslideWidth(String collectInfoLandslideWidth) {
        this.collectInfoLandslideWidth = collectInfoLandslideWidth;
    }

    public String getCollectInfoLandslideArea() {
        return collectInfoLandslideArea;
    }

    public void setCollectInfoLandslideArea(String collectInfoLandslideArea) {
        this.collectInfoLandslideArea = collectInfoLandslideArea;
    }

    public String getCollectInfoLandslideVolume() {
        return collectInfoLandslideVolume;
    }

    public void setCollectInfoLandslideVolume(String collectInfoLandslideVolume) {
        this.collectInfoLandslideVolume = collectInfoLandslideVolume;
    }

    public String getCollectInfoDistortionLength() {
        return collectInfoDistortionLength;
    }

    public void setCollectInfoDistortionLength(String collectInfoDistortionLength) {
        this.collectInfoDistortionLength = collectInfoDistortionLength;
    }

    public String getCollectInfoDistortionWidth() {
        return collectInfoDistortionWidth;
    }

    public void setCollectInfoDistortionWidth(String collectInfoDistortionWidth) {
        this.collectInfoDistortionWidth = collectInfoDistortionWidth;
    }

    public String getCollectInfoDistortionArea() {
        return collectInfoDistortionArea;
    }

    public void setCollectInfoDistortionArea(String collectInfoDistortionArea) {
        this.collectInfoDistortionArea = collectInfoDistortionArea;
    }

    public String getCollectInfoDistortionVolume() {
        return collectInfoDistortionVolume;
    }

    public void setCollectInfoDistortionVolume(String collectInfoDistortionVolume) {
        this.collectInfoDistortionVolume = collectInfoDistortionVolume;
    }

    public String getCollectInfoSlideDistance() {
        return collectInfoSlideDistance;
    }

    public void setCollectInfoSlideDistance(String collectInfoSlideDistance) {
        this.collectInfoSlideDistance = collectInfoSlideDistance;
    }

    public String getCollectInfoCrackNumber() {
        return collectInfoCrackNumber;
    }

    public void setCollectInfoCrackNumber(String collectInfoCrackNumber) {
        this.collectInfoCrackNumber = collectInfoCrackNumber;
    }

    public String getCollectInfoCrackMinLength() {
        return collectInfoCrackMinLength;
    }

    public void setCollectInfoCrackMinLength(String collectInfoCrackMinLength) {
        this.collectInfoCrackMinLength = collectInfoCrackMinLength;
    }

    public String getCollectInfoCrackMaxLength() {
        return collectInfoCrackMaxLength;
    }

    public void setCollectInfoCrackMaxLength(String collectInfoCrackMaxLength) {
        this.collectInfoCrackMaxLength = collectInfoCrackMaxLength;
    }

    public String getCollectInfoCrackMinWidth() {
        return collectInfoCrackMinWidth;
    }

    public void setCollectInfoCrackMinWidth(String collectInfoCrackMinWidth) {
        this.collectInfoCrackMinWidth = collectInfoCrackMinWidth;
    }

    public String getCollectInfoCrackMaxWidth() {
        return collectInfoCrackMaxWidth;
    }

    public void setCollectInfoCrackMaxWidth(String collectInfoCrackMaxWidth) {
        this.collectInfoCrackMaxWidth = collectInfoCrackMaxWidth;
    }

    public String getCollectInfoMaxDislocation() {
        return collectInfoMaxDislocation;
    }

    public void setCollectInfoMaxDislocation(String collectInfoMaxDislocation) {
        this.collectInfoMaxDislocation = collectInfoMaxDislocation;
    }

    public String getCollectInfoRockLength() {
        return collectInfoRockLength;
    }

    public void setCollectInfoRockLength(String collectInfoRockLength) {
        this.collectInfoRockLength = collectInfoRockLength;
    }

    public String getCollectInfoRockWidth() {
        return collectInfoRockWidth;
    }

    public void setCollectInfoRockWidth(String collectInfoRockWidth) {
        this.collectInfoRockWidth = collectInfoRockWidth;
    }

    public String getCollectInfoRockVolume() {
        return collectInfoRockVolume;
    }

    public void setCollectInfoRockVolume(String collectInfoRockVolume) {
        this.collectInfoRockVolume = collectInfoRockVolume;
    }

    public String getCollectInfoCollapseVolume() {
        return collectInfoCollapseVolume;
    }

    public void setCollectInfoCollapseVolume(String collectInfoCollapseVolume) {
        this.collectInfoCollapseVolume = collectInfoCollapseVolume;
    }

    public String getCollectInfoResidualVolume() {
        return collectInfoResidualVolume;
    }

    public void setCollectInfoResidualVolume(String collectInfoResidualVolume) {
        this.collectInfoResidualVolume = collectInfoResidualVolume;
    }

    public String getCollectInfoAnotherThings() {
        return collectInfoAnotherThings;
    }

    public void setCollectInfoAnotherThings(String collectInfoAnotherThings) {
        this.collectInfoAnotherThings = collectInfoAnotherThings;
    }

    public String getCollectInfoMeasure() {
        return collectInfoMeasure;
    }

    public void setCollectInfoMeasure(String collectInfoMeasure) {
        this.collectInfoMeasure = collectInfoMeasure;
    }

    public String getCollectInfoMeasureRemark() {
        return collectInfoMeasureRemark;
    }

    public void setCollectInfoMeasureRemark(String collectInfoMeasureRemark) {
        this.collectInfoMeasureRemark = collectInfoMeasureRemark;
    }

    public String getCollectInfoDisposition() {
        return collectInfoDisposition;
    }

    public void setCollectInfoDisposition(String collectInfoDisposition) {
        this.collectInfoDisposition = collectInfoDisposition;
    }

    public String getCollectInfoDispositionRemark() {
        return collectInfoDispositionRemark;
    }

    public void setCollectInfoDispositionRemark(String collectInfoDispositionRemark) {
        this.collectInfoDispositionRemark = collectInfoDispositionRemark;
    }

    public String getCollectInfoGoTime() {
        return collectInfoGoTime;
    }

    public void setCollectInfoGoTime(String collectInfoGoTime) {
        this.collectInfoGoTime = collectInfoGoTime;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return "CollectInfoBean{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", collectInfoPlace='" + collectInfoPlace + '\'' +
                ", collectInfoType='" + collectInfoType + '\'' +
                ", collectInfoDisOrDan='" + collectInfoDisOrDan + '\'' +
                ", collectInfoDisasterLevel='" + collectInfoDisasterLevel + '\'' +
                ", collectInfoDisasterReason='" + collectInfoDisasterReason + '\'' +
                ", collectInfoIsResearch='" + collectInfoIsResearch + '\'' +
                ", collectInfoDead='" + collectInfoDead + '\'' +
                ", collectInfoMiss='" + collectInfoMiss + '\'' +
                ", collectInfoHeavyInjured='" + collectInfoHeavyInjured + '\'' +
                ", collectInfoSoftInjured='" + collectInfoSoftInjured + '\'' +
                ", collectInfoEconomicLoss='" + collectInfoEconomicLoss + '\'' +
                ", collectInfoHouseCollapseNum='" + collectInfoHouseCollapseNum + '\'' +
                ", collectInfoHouseCollapseArea='" + collectInfoHouseCollapseArea + '\'' +
                ", collectInfoHouseDamageNum='" + collectInfoHouseDamageNum + '\'' +
                ", collectInfoHouseDamageArea='" + collectInfoHouseDamageArea + '\'' +
                ", collectInfoAnotherDamage='" + collectInfoAnotherDamage + '\'' +
                ", collectInfoFamily='" + collectInfoFamily + '\'' +
                ", collectInfoPeople='" + collectInfoPeople + '\'' +
                ", collectInfoAtHomeFamily='" + collectInfoAtHomeFamily + '\'' +
                ", collectInfoAtHomePeople='" + collectInfoAtHomePeople + '\'' +
                ", collectInfoHouse='" + collectInfoHouse + '\'' +
                ", collectInfoHouseArea='" + collectInfoHouseArea + '\'' +
                ", collectInfoAnotherDisaster='" + collectInfoAnotherDisaster + '\'' +
                ", collectInfoLandslideLength='" + collectInfoLandslideLength + '\'' +
                ", collectInfoLandslideWidth='" + collectInfoLandslideWidth + '\'' +
                ", collectInfoLandslideArea='" + collectInfoLandslideArea + '\'' +
                ", collectInfoLandslideVolume='" + collectInfoLandslideVolume + '\'' +
                ", collectInfoDistortionLength='" + collectInfoDistortionLength + '\'' +
                ", collectInfoDistortionWidth='" + collectInfoDistortionWidth + '\'' +
                ", collectInfoDistortionArea='" + collectInfoDistortionArea + '\'' +
                ", collectInfoDistortionVolume='" + collectInfoDistortionVolume + '\'' +
                ", collectInfoSlideDistance='" + collectInfoSlideDistance + '\'' +
                ", collectInfoCrackNumber='" + collectInfoCrackNumber + '\'' +
                ", collectInfoCrackMinLength='" + collectInfoCrackMinLength + '\'' +
                ", collectInfoCrackMaxLength='" + collectInfoCrackMaxLength + '\'' +
                ", collectInfoCrackMinWidth='" + collectInfoCrackMinWidth + '\'' +
                ", collectInfoCrackMaxWidth='" + collectInfoCrackMaxWidth + '\'' +
                ", collectInfoMaxDislocation='" + collectInfoMaxDislocation + '\'' +
                ", collectInfoRockLength='" + collectInfoRockLength + '\'' +
                ", collectInfoRockWidth='" + collectInfoRockWidth + '\'' +
                ", collectInfoRockVolume='" + collectInfoRockVolume + '\'' +
                ", collectInfoCollapseVolume='" + collectInfoCollapseVolume + '\'' +
                ", collectInfoResidualVolume='" + collectInfoResidualVolume + '\'' +
                ", collectInfoAnotherThings='" + collectInfoAnotherThings + '\'' +
                ", collectInfoMeasure='" + collectInfoMeasure + '\'' +
                ", collectInfoMeasureRemark='" + collectInfoMeasureRemark + '\'' +
                ", collectInfoDisposition='" + collectInfoDisposition + '\'' +
                ", collectInfoDispositionRemark='" + collectInfoDispositionRemark + '\'' +
                ", collectInfoGoTime='" + collectInfoGoTime + '\'' +
                '}';
    }

    public String getEmergencyPersonNo() {
        return this.emergencyPersonNo;
    }

    public void setEmergencyPersonNo(String emergencyPersonNo) {
        this.emergencyPersonNo = emergencyPersonNo;
    }

    public String getEmergencyHuNo() {
        return this.emergencyHuNo;
    }

    public void setEmergencyHuNo(String emergencyHuNo) {
        this.emergencyHuNo = emergencyHuNo;
    }
}
