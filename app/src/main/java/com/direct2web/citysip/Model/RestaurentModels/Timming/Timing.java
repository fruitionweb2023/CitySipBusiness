package com.direct2web.citysip.Model.RestaurentModels.Timming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timing {

    @SerializedName("day_id")
    @Expose
    private String dayId;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("day_time")
    @Expose
    private String day_time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("addSpecialTime")
    @Expose
    private List<AddSpecialTime> addSpecialTime = null;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AddSpecialTime> getAddSpecialTime() {
        return addSpecialTime;
    }

    public void setAddSpecialTime(List<AddSpecialTime> addSpecialTime) {
        this.addSpecialTime = addSpecialTime;
    }

    public String getDay_time() {
        return day_time;
    }

    public void setDay_time(String day_time) {
        this.day_time = day_time;
    }
}