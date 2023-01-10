package com.direct2web.citysip.Model.SpaAndSalon.DashBoard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSpaAndSalonDeshboard {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("active_menu_item")
@Expose
private Integer activeMenuItem;
@SerializedName("inactive_menu_item")
@Expose
private Integer inactiveMenuItem;
@SerializedName("active_offer")
@Expose
private Integer activeOffer;
@SerializedName("inactive_offer")
@Expose
private Integer inactiveOffer;
@SerializedName("monthly_turn_over")
@Expose
private String monthlyTurnOver;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public Integer getActiveMenuItem() {
return activeMenuItem;
}

public void setActiveMenuItem(Integer activeMenuItem) {
this.activeMenuItem = activeMenuItem;
}

public Integer getInactiveMenuItem() {
return inactiveMenuItem;
}

public void setInactiveMenuItem(Integer inactiveMenuItem) {
this.inactiveMenuItem = inactiveMenuItem;
}

public Integer getActiveOffer() {
return activeOffer;
}

public void setActiveOffer(Integer activeOffer) {
this.activeOffer = activeOffer;
}

public Integer getInactiveOffer() {
return inactiveOffer;
}

public void setInactiveOffer(Integer inactiveOffer) {
this.inactiveOffer = inactiveOffer;
}

public String getMonthlyTurnOver() {
return monthlyTurnOver;
}

public void setMonthlyTurnOver(String monthlyTurnOver) {
this.monthlyTurnOver = monthlyTurnOver;
}

}