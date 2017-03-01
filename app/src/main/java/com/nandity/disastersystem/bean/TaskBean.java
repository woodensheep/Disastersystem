package com.nandity.disastersystem.bean;

/**
 * Created by qingsong on 2017/2/23.
 */

public class TaskBean {
    private String name;
    private String address;

    public TaskBean(String name, String address ) {
        this.name = name;
        this.address = address;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
