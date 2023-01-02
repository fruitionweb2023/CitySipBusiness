package com.direct2web.citysip.Model.LawyerModels.LawyerBusinessDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLawyerBusinessDetails {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line_2")
    @Expose
    private String addressLine2;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("email_id")
    @Expose
    private Object emailId;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("about_you_name")
    @Expose
    private String aboutYouName;
    @SerializedName("about_you_mobile")
    @Expose
    private String aboutYouMobile;
    @SerializedName("about_you_d_o_b")
    @Expose
    private String aboutYouDOB;
    @SerializedName("about_you_nationality")
    @Expose
    private String aboutYouNationality;
    @SerializedName("status")
    @Expose
    private String status;

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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Object getEmailId() {
        return emailId;
    }

    public void setEmailId(Object emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAboutYouName() {
        return aboutYouName;
    }

    public void setAboutYouName(String aboutYouName) {
        this.aboutYouName = aboutYouName;
    }

    public String getAboutYouMobile() {
        return aboutYouMobile;
    }

    public void setAboutYouMobile(String aboutYouMobile) {
        this.aboutYouMobile = aboutYouMobile;
    }

    public String getAboutYouDOB() {
        return aboutYouDOB;
    }

    public void setAboutYouDOB(String aboutYouDOB) {
        this.aboutYouDOB = aboutYouDOB;
    }

    public String getAboutYouNationality() {
        return aboutYouNationality;
    }

    public void setAboutYouNationality(String aboutYouNationality) {
        this.aboutYouNationality = aboutYouNationality;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}