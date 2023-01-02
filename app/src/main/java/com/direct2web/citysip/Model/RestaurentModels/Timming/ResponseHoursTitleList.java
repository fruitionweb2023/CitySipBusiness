package com.direct2web.citysip.Model.RestaurentModels.Timming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseHoursTitleList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("hour_title")
    @Expose
    private List<HourTitle> hourTitle = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HourTitle> getHourTitle() {
        return hourTitle;
    }

    public void setHourTitle(List<HourTitle> hourTitle) {
        this.hourTitle = hourTitle;
    }

}