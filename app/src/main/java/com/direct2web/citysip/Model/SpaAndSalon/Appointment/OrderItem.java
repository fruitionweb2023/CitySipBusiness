package com.direct2web.citysip.Model.SpaAndSalon.Appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem {

@SerializedName("id")
@Expose
private String id;
@SerializedName("item_id")
@Expose
private String itemId;
@SerializedName("qty")
@Expose
private String qty;
@SerializedName("amount")
@Expose
private String amount;
@SerializedName("service_name")
@Expose
private String serviceName;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getItemId() {
return itemId;
}

public void setItemId(String itemId) {
this.itemId = itemId;
}

public String getQty() {
return qty;
}

public void setQty(String qty) {
this.qty = qty;
}

public String getAmount() {
return amount;
}

public void setAmount(String amount) {
this.amount = amount;
}

public String getServiceName() {
return serviceName;
}

public void setServiceName(String serviceName) {
this.serviceName = serviceName;
}

}