package com.direct2web.citysip.Model.RestaurentModels.Timming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSpecialTime {

    @SerializedName("title_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("in_time")
    @Expose
    private String inTime;
    @SerializedName("out_time")
    @Expose
    private String outTime;


    public AddSpecialTime(String title) {
        this.title = title;
    }

    public AddSpecialTime(String id, String title, String time,String inTime,String outTime) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.inTime=inTime;
        this.outTime=outTime;
    }

    public AddSpecialTime(String id, String title, String time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}