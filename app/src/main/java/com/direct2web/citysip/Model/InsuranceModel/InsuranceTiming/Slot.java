package com.direct2web.citysip.Model.InsuranceModel.InsuranceTiming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {

@SerializedName("time")
@Expose
private String time;

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

}