package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadingAdviceMRS implements Parcelable {

    public LoadingAdviceMRS() {
        super();
    }

    public LoadingAdviceMRS(Parcel in) {
        TRAN_NO = in.readString();
        TRAN_DATE = in.readString();
        GRAD = in.readString();
        LOCATION_NAME = in.readString();
    }

    public static final Creator<LoadingAdviceMRS> CREATOR = new Creator<LoadingAdviceMRS>() {
        @Override
        public LoadingAdviceMRS createFromParcel(Parcel in) {
            return new LoadingAdviceMRS(in);
        }

        @Override
        public LoadingAdviceMRS[] newArray(int size) {
            return new LoadingAdviceMRS[size];
        }
    };

    public String getTRAN_NO() {
        return TRAN_NO;
    }

    public void setTRAN_NO(String TRAN_NO) {
        this.TRAN_NO = TRAN_NO;
    }

    public String getTRAN_DATE() {
        return TRAN_DATE;
    }

    public void setTRAN_DATE(String TRAN_DATE) {
        this.TRAN_DATE = TRAN_DATE;
    }

    public String getGRAD() {
        return GRAD;
    }

    public void setGRAD(String GRAD) {
        this.GRAD = GRAD;
    }

    public String getLOCATION_NAME() {
        return LOCATION_NAME;
    }

    public void setLOCATION_NAME(String LOCATION_NAME) {
        this.LOCATION_NAME = LOCATION_NAME;
    }

    @SerializedName("TRAN_NO")
    @Expose
    private String TRAN_NO;
    @SerializedName("TRAN_DATE")
    @Expose
    private String TRAN_DATE;
    @SerializedName("GRAD")
    @Expose
    private String GRAD;
    @SerializedName("LOCATION_NAME")
    @Expose
    private String LOCATION_NAME;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(TRAN_NO);
        parcel.writeString(TRAN_DATE);
        parcel.writeString(GRAD);
        parcel.writeString(LOCATION_NAME);
    }
}
