package com.proyekakhir.pelaporan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdateImageResponse {
    @Expose
    @SerializedName("success") int success;
    @Expose
    @SerializedName("message")
    String message;
    @Expose
    @SerializedName("url")
    String url;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
