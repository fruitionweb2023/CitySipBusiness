package com.direct2web.citysip.Model.InsuranceModel.InsuranceServices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseInsuranceServiceAndCompanyList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("service_list")
    @Expose
    private List<ServiceList> serviceList = null;
    @SerializedName("company_list")
    @Expose
    private List<CompanyList> companyList = null;

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

    public List<ServiceList> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceList> serviceList) {
        this.serviceList = serviceList;
    }

    public List<CompanyList> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyList> companyList) {
        this.companyList = companyList;
    }

}