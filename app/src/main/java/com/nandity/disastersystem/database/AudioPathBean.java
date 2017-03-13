package com.nandity.disastersystem.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenPeng on 2017/3/10.
 */
@Entity
public class AudioPathBean {
    @Id(autoincrement = true)
    private Long id;
    /*任务ID*/
    private String taskId;
    /*录音路径*/
    private String path;
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2085822792)
    public AudioPathBean(Long id, String taskId, String path) {
        this.id = id;
        this.taskId = taskId;
        this.path = path;
    }
    @Generated(hash = 745923026)
    public AudioPathBean() {
    }
}
