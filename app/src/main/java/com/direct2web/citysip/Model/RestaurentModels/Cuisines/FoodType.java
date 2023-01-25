package com.direct2web.citysip.Model.RestaurentModels.Cuisines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodType {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public FoodType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public FoodType(String name) {
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}