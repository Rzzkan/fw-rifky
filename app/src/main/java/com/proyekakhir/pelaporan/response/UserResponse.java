package com.proyekakhir.pelaporan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.proyekakhir.pelaporan.model.UserModel;

import java.util.ArrayList;

public class UserResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<UserModel> data = null;

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

    public ArrayList<UserModel> getData() {
        return data;
    }

    public void setData(ArrayList<UserModel> data) {
        this.data = data;
    }
}
