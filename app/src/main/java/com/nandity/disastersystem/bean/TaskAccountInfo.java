package com.nandity.disastersystem.bean;

/**
 * Created by lemon on 2017/4/7.
 */
public class TaskAccountInfo {
    //"新增", "原有", "险情", "户数(户)", "人数(人)",
    //"在家人数(人)", "房屋间数(间)", "面积(米*米)",
    // "降雨", "风化", "库水位", "切坡", "加载", "冲刷坡脚"

    private String text;
    private String xin;
    private String jiu;
    private String yg;
    private String qz_person_h;
    private String qz_person_no;
    private String qz_person_zj;
    private String qz_house_no;
    private String qz_house_area;
    private String jy;
    private String fh;
    private String ksw;
    private String qp;
    private String jz;
    private String cspj;

   // "死亡(人)", "重伤(人)", "直接损失(万元)", "房屋坍塌(间)", "面积(米*米)",
    private String dead_no;
    private String ss_no;
    private String zj_money;
    private String house_kt_no;
    private String house_kt_mj;

    @Override
    public String toString() {
        return "TaskAccountInfo{" +
                "text='" + text + '\'' +
                ", xin='" + xin + '\'' +
                ", jiu='" + jiu + '\'' +
                ", yg='" + yg + '\'' +
                ", qz_person_h='" + qz_person_h + '\'' +
                ", qz_person_no='" + qz_person_no + '\'' +
                ", qz_person_zj='" + qz_person_zj + '\'' +
                ", qz_house_no='" + qz_house_no + '\'' +
                ", qz_house_area='" + qz_house_area + '\'' +
                ", jy='" + jy + '\'' +
                ", fh='" + fh + '\'' +
                ", ksw='" + ksw + '\'' +
                ", qp='" + qp + '\'' +
                ", jz='" + jz + '\'' +
                ", cspj='" + cspj + '\'' +
                ", dead_no='" + dead_no + '\'' +
                ", ss_no='" + ss_no + '\'' +
                ", zj_money='" + zj_money + '\'' +
                ", house_kt_no='" + house_kt_no + '\'' +
                ", house_kt_mj='" + house_kt_mj + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getXin() {
        return xin;
    }

    public void setXin(String xin) {
        this.xin = xin;
    }

    public String getJiu() {
        return jiu;
    }

    public void setJiu(String jiu) {
        this.jiu = jiu;
    }

    public String getYg() {
        return yg;
    }

    public void setYg(String yg) {
        this.yg = yg;
    }

    public String getQz_person_h() {
        return qz_person_h;
    }

    public void setQz_person_h(String qz_person_h) {
        this.qz_person_h = qz_person_h;
    }

    public String getQz_person_no() {
        return qz_person_no;
    }

    public void setQz_person_no(String qz_person_no) {
        this.qz_person_no = qz_person_no;
    }

    public String getQz_person_zj() {
        return qz_person_zj;
    }

    public void setQz_person_zj(String qz_person_zj) {
        this.qz_person_zj = qz_person_zj;
    }

    public String getQz_house_no() {
        return qz_house_no;
    }

    public void setQz_house_no(String qz_house_no) {
        this.qz_house_no = qz_house_no;
    }

    public String getJy() {
        return jy;
    }

    public void setJy(String jy) {
        this.jy = jy;
    }

    public String getQz_house_area() {
        return qz_house_area;
    }

    public void setQz_house_area(String qz_house_area) {
        this.qz_house_area = qz_house_area;
    }

    public String getFh() {
        return fh;
    }

    public void setFh(String fh) {
        this.fh = fh;
    }

    public String getKsw() {
        return ksw;
    }

    public void setKsw(String ksw) {
        this.ksw = ksw;
    }

    public String getQp() {
        return qp;
    }

    public void setQp(String qp) {
        this.qp = qp;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getCspj() {
        return cspj;
    }

    public void setCspj(String cspj) {
        this.cspj = cspj;
    }

    public String getDead_no() {
        return dead_no;
    }

    public void setDead_no(String dead_no) {
        this.dead_no = dead_no;
    }

    public String getSs_no() {
        return ss_no;
    }

    public void setSs_no(String ss_no) {
        this.ss_no = ss_no;
    }

    public String getHouse_kt_no() {
        return house_kt_no;
    }

    public void setHouse_kt_no(String house_kt_no) {
        this.house_kt_no = house_kt_no;
    }

    public String getZj_money() {
        return zj_money;
    }

    public void setZj_money(String zj_money) {
        this.zj_money = zj_money;
    }

    public String getHouse_kt_mj() {
        return house_kt_mj;
    }

    public void setHouse_kt_mj(String house_kt_mj) {
        this.house_kt_mj = house_kt_mj;
    }
}
