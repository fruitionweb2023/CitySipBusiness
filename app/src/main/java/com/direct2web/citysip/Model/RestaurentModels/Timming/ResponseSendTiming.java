package com.direct2web.citysip.Model.RestaurentModels.Timming;

import com.google.gson.annotations.SerializedName;

public class ResponseSendTiming {

    @SerializedName("error")
    private Boolean mError;
    @SerializedName("message")
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
