package com.nandity.disastersystem.bean;

/**
 * Created by ChenPeng on 2017/4/5.
 */

public class PlanBean {
    private String name;
    private String type;
    private String year;
    private String place;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    @Override
    public String toString() {
        return "PlanBean{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", year='" + year + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
