package com.direct2web.citysip.Model.RestaurentModels.Cuisines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseImageDishNameTypeList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("dish_title")
    @Expose
    private List<DishTitle> dishTitle = null;
    @SerializedName("dish_type")
    @Expose
    private List<DishType> dishType = null;

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

    public List<DishTitle> getDishTitle() {
        return dishTitle;
    }

    public void setDishTitle(List<DishTitle> dishTitle) {
        this.dishTitle = dishTitle;
    }

    public List<DishType> getDishType() {
        return dishType;
    }

    public void setDishType(List<DishType> dishType) {
        this.dishType = dishType;
    }

}