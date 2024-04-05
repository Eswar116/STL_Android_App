package com.essindia.stlapp.Bean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoadingAdviceListBean {

    @SerializedName("Data")
    private List<LoadingAdviceDataBean> loadingAdviceDataList = null;
    @SerializedName("Message")
    private String message;
    @SerializedName("MessageCode")
    private String messageCode;

    public List<LoadingAdviceDataBean> getData() {
        return loadingAdviceDataList;
    }

    public void setData(List<LoadingAdviceDataBean> data) {
        this.loadingAdviceDataList = data;
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