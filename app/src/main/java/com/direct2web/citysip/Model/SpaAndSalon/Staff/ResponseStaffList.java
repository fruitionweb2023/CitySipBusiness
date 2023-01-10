package com.direct2web.citysip.Model.SpaAndSalon.Staff;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseStaffList {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("staff_list")
@Expose
private List<Staff> staffList = null;

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

public List<Staff> getStaffList() {
return staffList;
}

public void setStaffList(List<Staff> staffList) {
this.staffList = staffList;
}

}