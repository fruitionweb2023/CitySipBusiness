package com.direct2web.citysip.Model.RestaurentModels.MenuList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewMenu {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dish_type_title")
    @Expose
    private String dishTypeTitle;
    @SerializedName("menu_item")
    @Expose
    private List<Menu> menuItem = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishTypeTitle() {
        return dishTypeTitle;
    }

    public void setDishTypeTitle(String dishTypeTitle) {
        this.dishTypeTitle = dishTypeTitle;
    }

    public List<Menu> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(List<Menu> menuItem) {
        this.menuItem = menuItem;
    }

}