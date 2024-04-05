package com.essindia.stlapp.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductData {
    @SerializedName("Data")
    private List<ProductItem> data;
    @SerializedName("Message")
    private String message;
    @SerializedName("MessageCode")
    private String messageCode;

    // Getter and setter methods for the fields

    public List<ProductItem> getData() {
        return data;
    }

    public void setData(List<ProductItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}

