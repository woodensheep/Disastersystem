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
     * 调查时间
     **/
    private String mTime;
    /**
     * 灾害点
     **/
    private String mDisaster;
    /**
     * 调查地点
     **/
    private String mAddress;
    /**
     * 乡镇
     **/
    private String mTownship;
    /**
     * 部门
     **/
    private String mDepartment;
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
    @Generated(hash = 875566835)
    public TaskBean(Long id, String mTime, String mDisaster, String mAddress, String mTownship, String mDepartment, String mWorkers) {
        this.id = id;
        this.mTime = mTime;
        this.mDisaster = mDisaster;
        this.mAddress = mAddress;
        this.mTownship = mTownship;
        this.mDepartment = mDepartment;
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