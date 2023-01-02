package com.direct2web.citysip.Model.RestaurentModels.Cuisines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCuisinesList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cuisines")
    @Expose
    private List<Cuisine> cuisines = null;
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

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }

    public List<DishType> getDishType() {
        return dishType;
    }

    public void setDishType(List<DishType> dishType) {
        this.dishType = dishType;
    }

}