package com.proyekakhir.pelaporan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportModel {
    @SerializedName("id_report")
    @Expose
    private String idReport;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("report")
    @Expose
    private String report;

    public ReportModel(String idReport, String name, String phone, String address, String date, String img, String report) {
        this.idReport = idReport;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.img = img;
        this.report = report;
    }

    public String getIdReport() {
        return idReport;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getImg() {
        return img;
    }

    public String getReport() {
        return report;
    }
}
