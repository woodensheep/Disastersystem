package com.nandity.disastersystem.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.nandity.disastersystem.app.MyApplication;

/**
 * Created by ChenPeng on 2017/3/1.
 * 何志武
 * wzadmin
 * 192.168.1.137
 * 8080
 * 183.230.169.136:8085
 * http://183.230.169.136:8085/cmdapp/index.jsp;jsessionid=5D34CB704FAA469C4AF959752B636C8F
 */

public class ConnectUrl {
    private SharedPreferences sp;
    private String port;
    private String ip;

    public ConnectUrl() {
        sp = MyApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        ip = sp.getString("IP", "");
        port = sp.getString("PORT", "");
    }

    public String getLoginUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/androidLogin.do";
    }
    public String getTaskNumUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getTaskNo.do";
    }

    public String getDirectoryUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getAddressList.do";
    }

    //更新app
    public String getUpdateServiceUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/downloadApk.do";
    }

    //    获取版本号
    public String getUpdateVerCodeUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/haveNewVersion.do";
    }

    public String getStartTaskUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getStartTask.do";
    }

    public String getOneTaskUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getOneTask.do";
    }

    /*获取灾害点 */
    public String getDisInfoListUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getDisInfoList.do";
    }

    /*获取乡镇 */
    public String getAreaListUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getAreaList.do";
    }

    /*获取调查人的 */
    public String getPersonListUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getPersonList.do";
    }

    /*发起任务提交 */
    public String saveSuveyTaskUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/saveSuveyTask.do";
    }


    public String getMeaDisListUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getNextList.do";
    }

    public String getDisasterPictureUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getDisPic.do";
    }

    public String getBaseInfoUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/saveDisInfo.do";
    }

    public String getConnectInfoUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/saveTaskExtends.do";
    }

    public String getMediaUploadUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/uploadFile.do";
    }

    public String getCompleteTaskUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getAllEndTask.do";
    }

    public String getCompleteInfoUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getTaskExtendsToAndroid.do";
    }
    public String getMediaInfoUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getFileToAndroid.do";
    }
    public String getMediaInfoListUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getAllFile.do";
    }

    public String getTaskStatusUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/firstSubmit.do";
    }

    public String getPlanUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/contingencyPlan.do";
    }

    /* 任务台账*/
    public String getParameterUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getParameter.do";
    }

    /*  灾险情诱发因素统计*/
    public String getDisReasonUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getDisReason.do";
    }

    /*  灾险情类型统计*/
    public String getDisTypeUrl() {
        return "http://" + ip + ":" + port + "/cmdapp/getDisType.do";
    }

}
