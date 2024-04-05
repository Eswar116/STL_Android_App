package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 08-11-2016.
 */

public class GRN_Verification_Bean implements Parcelable {


    private String HO_ORG_ID;
    private String PROJ_ID;
    private String CLD_ID;
    private String TRAN_NO;
    private String PACK_SIZE;
    private String PACK_TYPE;
    private String PROCESSED_BOXES;
    private String IR_NO;
    private String CLOSE_FLAG;
    private String EXCESS_PACK_QTY;
    private String NO_OF_BOXES;
    private String ROUTE_DATE;
    private String TRAN_date;
    private String SLOC_ID;
    private String DT_MOD_DATE;
    private String ITEM_CODE;
    private String ROUTE_NO;
    private String SCAN_FLAG;
    private String ORG_ID;
    private String EXCESS_FLAG;
    private String VERIFIED_QTY;
    private String PEND_QTY;
    private String ITEM_DESC;
    private String VC_MACHINE_NO;
    private String PUT_AWAY_ID;

    public String getVERIFIED_QTY() {
        return VERIFIED_QTY;
    }

    public void setVERIFIED_QTY(String VERIFIED_QTY) {
        this.VERIFIED_QTY = VERIFIED_QTY;
    }

    public String getPEND_QTY() {
        return PEND_QTY;
    }

    public void setPEND_QTY(String PEND_QTY) {
        this.PEND_QTY = PEND_QTY;
    }

    public String getITEM_DESC() {
        return ITEM_DESC;
    }

    public void setITEM_DESC(String ITEM_DESC) {
        this.ITEM_DESC = ITEM_DESC;
    }

    public String getHO_ORG_ID() {
        return HO_ORG_ID;
    }

    public void setHO_ORG_ID(String HO_ORG_ID) {
        this.HO_ORG_ID = HO_ORG_ID;
    }

    public String getPROJ_ID() {
        return PROJ_ID;
    }

    public void setPROJ_ID(String PROJ_ID) {
        this.PROJ_ID = PROJ_ID;
    }

    public String getCLD_ID() {
        return CLD_ID;
    }

    public void setCLD_ID(String CLD_ID) {
        this.CLD_ID = CLD_ID;
    }

    public String getPROCESSED_BOXES() {
        return PROCESSED_BOXES;
    }

    public void setPROCESSED_BOXES(String PROCESSED_BOXES) {
        this.PROCESSED_BOXES = PROCESSED_BOXES;
    }

    public String getCLOSE_FLAG() {
        return CLOSE_FLAG;
    }

    public void setCLOSE_FLAG(String CLOSE_FLAG) {
        this.CLOSE_FLAG = CLOSE_FLAG;
    }

    public String getNO_OF_BOXES() {
        return NO_OF_BOXES;
    }

    public void setNO_OF_BOXES(String NO_OF_BOXES) {
        this.NO_OF_BOXES = NO_OF_BOXES;
    }

    public String getSLOC_ID() {
        return SLOC_ID;
    }

    public void setSLOC_ID(String SLOC_ID) {
        this.SLOC_ID = SLOC_ID;
    }

    public String getDT_MOD_DATE() {
        return DT_MOD_DATE;
    }

    public void setDT_MOD_DATE(String DT_MOD_DATE) {
        this.DT_MOD_DATE = DT_MOD_DATE;
    }

    public String getORG_ID() {
        return ORG_ID;
    }

    public void setORG_ID(String ORG_ID) {
        this.ORG_ID = ORG_ID;
    }

    public String getTRAN_NO() {
        return TRAN_NO;
    }

    public void setTRAN_NO(String TRAN_NO) {
        this.TRAN_NO = TRAN_NO;
    }

    public String getPACK_SIZE() {
        return PACK_SIZE;
    }

    public void setPACK_SIZE(String PACK_SIZE) {
        this.PACK_SIZE = PACK_SIZE;
    }

    public String getPACK_TYPE() {
        return PACK_TYPE;
    }

    public void setPACK_TYPE(String PACK_TYPE) {
        this.PACK_TYPE = PACK_TYPE;
    }

    public String getIR_NO() {
        return IR_NO;
    }

    public void setIR_NO(String IR_NO) {
        this.IR_NO = IR_NO;
    }

    public String getEXCESS_PACK_QTY() {
        return EXCESS_PACK_QTY;
    }

    public void setEXCESS_PACK_QTY(String EXCESS_PACK_QTY) {
        this.EXCESS_PACK_QTY = EXCESS_PACK_QTY;
    }

    public String getROUTE_DATE() {
        return ROUTE_DATE;
    }

    public void setROUTE_DATE(String ROUTE_DATE) {
        this.ROUTE_DATE = ROUTE_DATE;
    }

    public String getTRAN_date() {
        return TRAN_date;
    }

    public void setTRAN_date(String TRAN_date) {
        this.TRAN_date = TRAN_date;
    }

    public String getITEM_CODE() {
        return ITEM_CODE;
    }

    public void setITEM_CODE(String ITEM_CODE) {
        this.ITEM_CODE = ITEM_CODE;
    }

    public String getROUTE_NO() {
        return ROUTE_NO;
    }

    public void setROUTE_NO(String ROUTE_NO) {
        this.ROUTE_NO = ROUTE_NO;
    }

    public String getEXCESS_FLAG() {
        return EXCESS_FLAG;
    }

    public void setEXCESS_FLAG(String EXCESS_FLAG) {
        this.EXCESS_FLAG = EXCESS_FLAG;
    }

    public String getSCAN_FLAG() {
        return SCAN_FLAG;
    }

    public void setSCAN_FLAG(String SCAN_FLAG) {
        this.SCAN_FLAG = SCAN_FLAG;
    }

    public String getVC_MACHINE_NO() {
        return VC_MACHINE_NO;
    }

    public void setVC_MACHINE_NO(String VC_MACHINE_NO) {
        this.VC_MACHINE_NO = VC_MACHINE_NO;
    }

    public String getPUT_AWAY_ID() {
        return PUT_AWAY_ID;
    }

    public void setPUT_AWAY_ID(String PUT_AWAY_ID) {
        this.PUT_AWAY_ID = PUT_AWAY_ID;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(HO_ORG_ID);
        parcel.writeString(PROJ_ID);
        parcel.writeString(CLD_ID);
        parcel.writeString(TRAN_NO);
        parcel.writeString(PACK_SIZE);
        parcel.writeString(PACK_TYPE);
        parcel.writeString(PROCESSED_BOXES);
        parcel.writeString(IR_NO);
        parcel.writeString(CLOSE_FLAG);
        parcel.writeString(EXCESS_PACK_QTY);

        parcel.writeString(NO_OF_BOXES);
        parcel.writeString(ROUTE_DATE);
        parcel.writeString(TRAN_date);
        parcel.writeString(SLOC_ID);
        parcel.writeString(DT_MOD_DATE);
        parcel.writeString(ITEM_CODE);
        parcel.writeString(ROUTE_NO);
        parcel.writeString(SCAN_FLAG);
        parcel.writeString(ORG_ID);
        parcel.writeString(EXCESS_FLAG);
        parcel.writeString(VC_MACHINE_NO);

        parcel.writeString(VERIFIED_QTY);
        parcel.writeString(PEND_QTY);
        parcel.writeString(ITEM_DESC);
        parcel.writeString(PUT_AWAY_ID);
    }

    public GRN_Verification_Bean() {
    }

    private GRN_Verification_Bean(Parcel in) {

        this.HO_ORG_ID = in.readString();
        this.PROJ_ID = in.readString();
        this.CLD_ID = in.readString();
        this.TRAN_NO = in.readString();
        this.PACK_SIZE = in.readString();
        this.PACK_TYPE = in.readString();
        this.PROCESSED_BOXES = in.readString();
        this.IR_NO = in.readString();
        this.CLOSE_FLAG = in.readString();
        this.EXCESS_PACK_QTY = in.readString();
        this.NO_OF_BOXES = in.readString();
        this.ROUTE_DATE = in.readString();
        this.TRAN_date = in.readString();
        this.SLOC_ID = in.readString();
        this.DT_MOD_DATE = in.readString();
        this.ITEM_CODE = in.readString();
        this.ROUTE_NO = in.readString();
        this.SCAN_FLAG = in.readString();
        this.ORG_ID = in.readString();
        this.EXCESS_FLAG = in.readString();
        this.VC_MACHINE_NO = in.readString();

        this.VERIFIED_QTY = in.readString();
        this.PEND_QTY = in.readString();
        this.ITEM_DESC = in.readString();
        this.PUT_AWAY_ID = in.readString();
    }

    public static final Creator<GRN_Verification_Bean> CREATOR = new Creator<GRN_Verification_Bean>() {

        @Override
        public GRN_Verification_Bean createFromParcel(Parcel source) {
            return new GRN_Verification_Bean(source);
        }

        @Override
        public GRN_Verification_Bean[] newArray(int size) {
            return new GRN_Verification_Bean[size];
        }
    };
}
