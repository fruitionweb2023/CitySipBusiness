package com.direct2web.citysip.Model.RestaurentModels.BasicDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGetBasicDetails {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("detail_data")
    @Expose
    private String detailData;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getDetailData() {
        return detailData;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }

}