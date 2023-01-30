package com.direct2web.citysip.Model.RestaurentModels.MenuList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMenuList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("no_data_image")
    @Expose
    private String noDataImage;
    @SerializedName("menu_list")
    @Expose
    private List<Menu> menuList = null;

    public String getNoDataImage() {
        return noDataImage;
    }

    public void setNoDataImage(String noDataImage) {
        this.noDataImage = noDataImage;
    }

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

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

}