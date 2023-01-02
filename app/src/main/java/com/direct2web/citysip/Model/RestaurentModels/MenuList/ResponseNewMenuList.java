package com.direct2web.citysip.Model.RestaurentModels.MenuList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseNewMenuList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("new_menu_list")
    @Expose
    private List<NewMenu> newMenuList = null;

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

    public List<NewMenu> getNewMenuList() {
        return newMenuList;
    }

    public void setNewMenuList(List<NewMenu> newMenuList) {
        this.newMenuList = newMenuList;
    }

}