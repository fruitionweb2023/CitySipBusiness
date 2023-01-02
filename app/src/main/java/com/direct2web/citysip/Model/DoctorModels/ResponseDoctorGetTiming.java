package com.direct2web.citysip.Model.DoctorModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDoctorGetTiming {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("timingList")
@Expose
private List<Timing> timingList = null;

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

public List<Timing> getTimingList() {
return timingList;
}

public void setTimingList(List<Timing> timingList) {
this.timingList = timingList;
}

}