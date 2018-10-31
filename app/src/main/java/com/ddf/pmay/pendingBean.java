package com.ddf.pmay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class pendingBean {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("job_Id")
    @Expose
    private String jobId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pname")
    @Expose
    private String pname;
    @SerializedName("ward")
    @Expose
    private String ward;
    @SerializedName("stage")
    @Expose
    private String stage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
