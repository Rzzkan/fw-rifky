package com.proyekakhir.pelaporan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.proyekakhir.pelaporan.model.ScheduleModel;

import java.util.ArrayList;

public class ScheduleResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<ScheduleModel> data = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ScheduleModel> getData() {
        return data;
    }

    public void setData(ArrayList<ScheduleModel> data) {
        this.data = data;
    }
}
