package com.direct2web.citysip.Model.InsuranceModel.InsuranceServices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseInsuranceServices {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("service_list")
@Expose
private List<Service> serviceList = null;

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

public List<Service> getServiceList() {
return serviceList;
}

public void setServiceList(List<Service> serviceList) {
this.serviceList = serviceList;
}

}