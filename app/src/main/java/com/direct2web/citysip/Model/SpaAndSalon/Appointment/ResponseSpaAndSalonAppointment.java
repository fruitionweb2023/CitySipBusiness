package com.direct2web.citysip.Model.SpaAndSalon.Appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSpaAndSalonAppointment {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("order_list")
@Expose
private List<Order> orderList = null;

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

public List<Order> getOrderList() {
return orderList;
}

public void setOrderList(List<Order> orderList) {
this.orderList = orderList;
}

}