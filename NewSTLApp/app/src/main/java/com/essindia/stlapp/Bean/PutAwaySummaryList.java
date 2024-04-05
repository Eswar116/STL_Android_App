package com.essindia.stlapp.Bean;

/**
 * Created by jonu.kumar on 21-11-2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class PutAwaySummaryList {
    @SerializedName("MessageCode")
    private String messageCode;

    public String getMessageCode() {
        return messageCode;
    }

//    public void setMessageCode(String messageCode) {
//        this.messageCode = messageCode;
//    }

    @SerializedName("Data")
    private ArrayList<PutAwaySummaryData> data = new ArrayList<PutAwaySummaryData>();

    /**
     * @return The data
     */
    public ArrayList<PutAwaySummaryData> getData() {
        return data;
    }

    /**
     * @param data The Data
     */
//    public void setData(ArrayList<PutAwaySummaryData> data) {
//        this.data = data;
//    }




}