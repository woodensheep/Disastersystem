package com.nandity.disastersystem.bean;

/**
 * Created by qingsong on 2017/2/23.
 */

public class DirectoryBean {
    private String name;
    private String number;

    public DirectoryBean(String name,String number ) {
        this.name = name;
        this.number = number;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
