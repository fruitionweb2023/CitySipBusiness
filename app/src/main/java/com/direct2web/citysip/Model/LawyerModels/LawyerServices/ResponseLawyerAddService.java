package com.direct2web.citysip.Model.LawyerModels.LawyerServices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLawyerAddService {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;

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

}