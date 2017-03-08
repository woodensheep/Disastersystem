package com.nandity.disastersystem.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * Created by qingsong on 2017/2/23.
 */

@Entity
public class TaskBean {
    @Id(autoincrement =true)
    private Long id;
    /**
     * 灾害发生时间
     **/
    private String mHappenTime;
    /**
     * 调查时间
     **/
    private String mTime;
    /**
     * 灾害点id
     **/
    private String mDisasterID;
    /**
     * 灾害点
     **/
    private String mDisaster;
    /**
     * 调查地点
     **/
    private String mAddress;
    /**
     * 乡镇ID
     **/
    private String mTownshipID;
    /**
     * 乡镇
     **/
    private String mTownship;
    /**
     * 部门
     **/
    private String mDepartment;
    /**
     * 人员ID
     **/
    private String mWorkersID;

    @Override
    public String toString() {
        return "TaskBean{" +
                "id=" + id +
                ", mHappenTime='" + mHappenTime + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mDisasterID='" + mDisasterID + '\'' +
                ", mDisaster='" + mDisaster + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mTownshipID='" + mTownshipID + '\'' +
                ", mTownship='" + mTownship + '\'' +
                ", mDepartment='" + mDepartment + '\'' +
                ", mWorkersID='" + mWorkersID + '\'' +
                ", mWorkers='" + mWorkers + '\'' +
                '}';
    }

    /**
     * 人员
     **/
    private String mWorkers;
    public String getMWorkers() {
        return this.mWorkers;
    }
    public void setMWorkers(String mWorkers) {
        this.mWorkers = mWorkers;
    }
    public String getMDepartment() {
        return this.mDepartment;
    }
    public void setMDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }
    public String getMTownship() {
        return this.mTownship;
    }
    public void setMTownship(String mTownship) {
        this.mTownship = mTownship;
    }
    public String getMAddress() {
        return this.mAddress;
    }
    public void setMAddress(String mAddress) {
        this.mAddress = mAddress;
    }
    public String getMDisaster() {
        return this.mDisaster;
    }
    public void setMDisaster(String mDisaster) {
        this.mDisaster = mDisaster;
    }
    public String getMTime() {
        return this.mTime;
    }
    public void setMTime(String mTime) {
        this.mTime = mTime;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMWorkersID() {
        return this.mWorkersID;
    }
    public void setMWorkersID(String mWorkersID) {
        this.mWorkersID = mWorkersID;
    }
    public String getMTownshipID() {
        return this.mTownshipID;
    }
    public void setMTownshipID(String mTownshipID) {
        this.mTownshipID = mTownshipID;
    }
    public String getMDisasterID() {
        return this.mDisasterID;
    }
    public void setMDisasterID(String mDisasterID) {
        this.mDisasterID = mDisasterID;
    }
    public String getMHappenTime() {
        return this.mHappenTime;
    }
    public void setMHappenTime(String mHappenTime) {
        this.mHappenTime = mHappenTime;
    }
    @Generated(hash = 1356342499)
    public TaskBean(Long id, String mHappenTime, String mTime, String mDisasterID, String mDisaster, String mAddress,
            String mTownshipID, String mTownship, String mDepartment, String mWorkersID, String mWorkers) {
        this.id = id;
        this.mHappenTime = mHappenTime;
        this.mTime = mTime;
        this.mDisasterID = mDisasterID;
        this.mDisaster = mDisaster;
        this.mAddress = mAddress;
        this.mTownshipID = mTownshipID;
        this.mTownship = mTownship;
        this.mDepartment = mDepartment;
        this.mWorkersID = mWorkersID;
        this.mWorkers = mWorkers;
    }
    @Generated(hash = 1443476586)
    public TaskBean() {
    }


}
//    public TaskBean(String mTime, String mDisaster, String mAddress, String mTownship, String mDepartment, String mWorkers) {
//        this.mTime = mTime;
//        this.mDisaster = mDisaster;
//        this.mAddress = mAddress;
//        this.mTownship = mTownship;
//        this.mDepartment = mDepartment;
//        this.mWorkers = mWorkers;
//    }



//        //增加一条数据
//        Book book=new Book();
//        book.setName("java");
//        book.setAuthor("xiaochen");
//        book.setNumber("510110");
//        long rec= book.save();
//        Log.i("sugertest", "rec" + String.valueOf(rec));    //rec=2
//
//        //查询一条数据
//        Book queryBook=Book.findById(Book.class, 1);
//        Log.i("sugertest","queryBook"+queryBook.toString());
//
//        //find
//        List<Book> booklist=Book.find(Book.class, "number=?", "510110");
//        Log.i("sugertest","booklist"+booklist.size());
//        //findWithQuery
//        List<Book> books=Book.findWithQuery(Book.class,"Select * from Book where number=?","java");
//        Log.i("sugertest","books"+books.size());
//
//        //更新一条数据
//        Book book1=Book.findById(Book.class,1);
//        book1.setName("android");
//        book1.save();
//
//        //查询一条数据
//        Book queryBook1=Book.findById(Book.class, 1);
//        Log.i("sugertest", "queryBook" + queryBook1.toString());
//
//        //删除一条数据
//        Book book2=Book.findById(Book.class,1);
//        book2.delete();