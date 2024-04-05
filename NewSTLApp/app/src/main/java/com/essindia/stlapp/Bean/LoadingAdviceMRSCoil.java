package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadingAdviceMRSCoil implements Parcelable {

    public String getGRAD() {
        return GRAD;
    }

    public void setGRAD(String GRAD) {
        this.GRAD = GRAD;
    }

    public String getVc_TAG_NO() {
        return Vc_TAG_NO;
    }

    public void setVc_TAG_NO(String vc_TAG_NO) {
        Vc_TAG_NO = vc_TAG_NO;
    }

    public String getVC_DIVISION() {
        return VC_DIVISION;
    }

    public void setVC_DIVISION(String VC_DIVISION) {
        this.VC_DIVISION = VC_DIVISION;
    }

    public String getVC_ROUTE_NO() {
        return VC_ROUTE_NO;
    }

    public void setVC_ROUTE_NO(String VC_ROUTE_NO) {
        this.VC_ROUTE_NO = VC_ROUTE_NO;
    }

    public String getMRIR_DATE() {
        return MRIR_DATE;
    }

    public void setMRIR_DATE(String MRIR_DATE) {
        this.MRIR_DATE = MRIR_DATE;
    }

    public String getMRIR_NO() {
        return MRIR_NO;
    }

    public void setMRIR_NO(String MRIR_NO) {
        this.MRIR_NO = MRIR_NO;
    }

    public String getCOIL_WEIGHT() {
        return COIL_WEIGHT;
    }

    public void setCOIL_WEIGHT(String COIL_WEIGHT) {
        this.COIL_WEIGHT = COIL_WEIGHT;
    }

    public String getLOCATION_NAME() {
        return LOCATION_NAME;
    }

    public void setLOCATION_NAME(String LOCATION_NAME) {
        this.LOCATION_NAME = LOCATION_NAME;
    }

    public String getREQ_QUANTITY() {
        return REQ_QUANTITY;
    }

    public void setREQ_QUANTITY(String REQ_QUANTITY) {
        this.REQ_QUANTITY = REQ_QUANTITY;
    }

    public String getDT_ROUTE_DATE() {
        return DT_ROUTE_DATE;
    }

    public void setDT_ROUTE_DATE(String DT_ROUTE_DATE) {
        this.DT_ROUTE_DATE = DT_ROUTE_DATE;
    }

    public String getVC_S_HEAT_NO() {
        return VC_S_HEAT_NO;
    }

    public void setVC_S_HEAT_NO(String VC_S_HEAT_NO) {
        this.VC_S_HEAT_NO = VC_S_HEAT_NO;
    }

    public String getLOAD_STATUS() {
        return LOAD_STATUS;
    }

    public void setLOAD_STATUS(String LOAD_STATUS) {
        this.LOAD_STATUS = LOAD_STATUS;
    }

    @SerializedName("GRAD")
    @Expose
    private String GRAD;
    @SerializedName("Vc_TAG_NO")
    @Expose
    private String Vc_TAG_NO;
    @SerializedName("VC_DIVISION")
    @Expose
    private String VC_DIVISION;
    @SerializedName("VC_ROUTE_NO")
    @Expose
    private String VC_ROUTE_NO;
    @SerializedName("MRIR_DATE")
    @Expose
    private String MRIR_DATE;
    @SerializedName("MRIR_NO")
    @Expose
    private String MRIR_NO;
    @SerializedName("COIL_WEIGHT")
    @Expose
    private String COIL_WEIGHT;
    @SerializedName("LOCATION_NAME")
    @Expose
    private String LOCATION_NAME;
    @SerializedName("REQ_QUANTITY")
    @Expose
    private String REQ_QUANTITY;
    @SerializedName("DT_ROUTE_DATE")
    @Expose
    private String DT_ROUTE_DATE;
    @SerializedName("VC_S_HEAT_NO")
    @Expose
    private String VC_S_HEAT_NO;
    @SerializedName("LOAD_STATUS")
    @Expose
    private String LOAD_STATUS;

    public LoadingAdviceMRSCoil() {
        super();
    }

    protected LoadingAdviceMRSCoil(Parcel in) {
        GRAD = in.readString();
        Vc_TAG_NO = in.readString();
        VC_DIVISION = in.readString();
        VC_ROUTE_NO = in.readString();
        MRIR_DATE = in.readString();
        MRIR_NO = in.readString();
        COIL_WEIGHT = in.readString();
        LOCATION_NAME = in.readString();
        REQ_QUANTITY = in.readString();
        DT_ROUTE_DATE = in.readString();
        VC_S_HEAT_NO = in.readString();
        LOAD_STATUS = in.readString();
    }

    public static final Creator<LoadingAdviceMRSCoil> CREATOR = new Creator<LoadingAdviceMRSCoil>() {
        @Override
        public LoadingAdviceMRSCoil createFromParcel(Parcel in) {
            return new LoadingAdviceMRSCoil(in);
        }

        @Override
        public LoadingAdviceMRSCoil[] newArray(int size) {
            return new LoadingAdviceMRSCoil[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(GRAD);
        parcel.writeString(Vc_TAG_NO);
        parcel.writeString(VC_DIVISION);
        parcel.writeString(VC_ROUTE_NO);
        parcel.writeString(MRIR_DATE);
        parcel.writeString(MRIR_NO);
        parcel.writeString(COIL_WEIGHT);
        parcel.writeString(LOCATION_NAME);
        parcel.writeString(REQ_QUANTITY);
        parcel.writeString(DT_ROUTE_DATE);
        parcel.writeString(VC_S_HEAT_NO);
        parcel.writeString(LOAD_STATUS);
    }
}
