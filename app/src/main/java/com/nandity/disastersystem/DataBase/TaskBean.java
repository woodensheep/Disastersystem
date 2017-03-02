package com.nandity.disastersystem.dataBase;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by qingsong on 2017/2/23.
 */

public class TaskBean extends SugarRecord {
    /** 调查时间**/
    private String mtime;
    /** 灾害点**/
    private String mdisaster;
    /** 调查地点**/
    private String maddress;
    /** 乡镇**/
    private String mtownship;
    /** 部门**/
    private String mdepartment;
    /** 人员**/
    private String mworkers;

    public TaskBean() {
    }

//    public TaskBean(String mTime, String mDisaster, String mAddress, String mTownship, String mDepartment, String mWorkers) {
//        this.mTime = mTime;
//        this.mDisaster = mDisaster;
//        this.mAddress = mAddress;
//        this.mTownship = mTownship;
//        this.mDepartment = mDepartment;
//        this.mWorkers = mWorkers;
//    }


    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getMdisaster() {
        return mdisaster;
    }

    public void setMdisaster(String mdisaster) {
        this.mdisaster = mdisaster;
    }

    public String getMaddress() {
        return maddress;
    }

    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }

    public String getMtownship() {
        return mtownship;
    }

    public void setMtownship(String mtownship) {
        this.mtownship = mtownship;
    }

    public String getMdepartment() {
        return mdepartment;
    }

    public void setMdepartment(String mdepartment) {
        this.mdepartment = mdepartment;
    }

    public String getMworkers() {
        return mworkers;
    }

    public void setMworkers(String mworkers) {
        this.mworkers = mworkers;
    }
}

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