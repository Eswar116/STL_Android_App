package com.essindia.stlapp.Bean;

import java.util.List;

public class ResponseData {
    private List<DataItem> Data;
    private String Message;
    private String MessageCode;

    // Getter and setter methods for all fields

    public List<DataItem> getData() {
        return Data;
    }

    public void setData(List<DataItem> Data) {
        this.Data = Data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getMessageCode() {
        return MessageCode;
    }

    public void setMessageCode(String MessageCode) {
        this.MessageCode = MessageCode;
    }
}