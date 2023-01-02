
package com.direct2web.citysip.Model.RestaurentModels.aboutrestaurant;

import com.google.gson.annotations.SerializedName;

public class AboutResturant {

    @SerializedName("address_line_1")
    private String mAddressLine1;
    @SerializedName("address_line_2")
    private String mAddressLine2;
    @SerializedName("business_name")
    private String mBusinessName;
    @SerializedName("city_id")
    private String mCityId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("email_id")
    private String mEmailId;
    @SerializedName("error")
    private Boolean mError;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("phone_no")
    private String mPhoneNo;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("website")
    private String mWebsite;

    public String getAddressLine1() {
        return mAddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        mAddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return mAddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        mAddressLine2 = addressLine2;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setEmailId(String emailId) {
        mEmailId = emailId;
    }

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getPhoneNo() {
        return mPhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        mPhoneNo = phoneNo;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

}
