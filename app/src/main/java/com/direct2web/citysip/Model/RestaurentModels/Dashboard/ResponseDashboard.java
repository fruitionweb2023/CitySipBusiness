package com.direct2web.citysip.Model.RestaurentModels.Dashboard;

import com.google.gson.annotations.SerializedName;

public class ResponseDashboard {

    @SerializedName("active_menu_item")
    private String mActiveMenuItem;
    @SerializedName("active_offer")
    private String mActiveOffer;
    @SerializedName("error")
    private Boolean mError;
    @SerializedName("inactive_menu_item")
    private String mInactiveMenuItem;
    @SerializedName("inactive_offer")
    private String mInactiveOffer;
    @SerializedName("monthly_turn_over")
    private String monthlyTurenOver;

    public String getMonthlyTurenOver() {
        return monthlyTurenOver;
    }

    public void setMonthlyTurenOver(String monthlyTurenOver) {
        this.monthlyTurenOver = monthlyTurenOver;
    }

    public String getmActiveMenuItem() {
        return mActiveMenuItem;
    }

    public void setmActiveMenuItem(String mActiveMenuItem) {
        this.mActiveMenuItem = mActiveMenuItem;
    }

    public String getmActiveOffer() {
        return mActiveOffer;
    }

    public void setmActiveOffer(String mActiveOffer) {
        this.mActiveOffer = mActiveOffer;
    }

    public Boolean getmError() {
        return mError;
    }

    public void setmError(Boolean mError) {
        this.mError = mError;
    }

    public String getmInactiveMenuItem() {
        return mInactiveMenuItem;
    }

    public void setmInactiveMenuItem(String mInactiveMenuItem) {
        this.mInactiveMenuItem = mInactiveMenuItem;
    }

    public String getmInactiveOffer() {
        return mInactiveOffer;
    }

    public void setmInactiveOffer(String mInactiveOffer) {
        this.mInactiveOffer = mInactiveOffer;
    }
}
