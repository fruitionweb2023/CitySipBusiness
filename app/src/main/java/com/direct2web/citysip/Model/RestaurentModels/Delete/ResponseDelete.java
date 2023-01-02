package com.direct2web.citysip.Model.RestaurentModels.Delete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDelete {

    @SerializedName("error")
    @Expose
    private Boolean mError;
    @SerializedName("message")
    @Expose
    private String mMessage;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
