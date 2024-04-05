package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 26-12-2016.
 */

public class ReceiptItemBean implements Parcelable {

    @SerializedName("VC_TRAN_NO")
    @Expose
    private String vcTranNo;
    @SerializedName("VC_COMP_CODE")
    @Expose
    private String vcCompCode;
    @SerializedName("DT_TRAN_DATE")
    @Expose
    private String tranDate;

    private String machineCode;

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    private String routeCardNo;

    private String routeCardDate;

    public String getRouteCardNo() {
        return routeCardNo;
    }

    public void setRouteCardNo(String routeCardNo) {
        this.routeCardNo = routeCardNo;
    }

    public String getRouteCardDate() {
        return routeCardDate;
    }

    public void setRouteCardDate(String routeCardDate) {
        this.routeCardDate = routeCardDate;
    }

    public String getVcTranNo() {
        return vcTranNo;
    }

    public void setVcTranNo(String vcTranNo) {
        this.vcTranNo = vcTranNo;
    }

    public String getVcCompCode() {
        return vcCompCode;
    }

    public void setVcCompCode(String vcCompCode) {
        this.vcCompCode = vcCompCode;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    protected ReceiptItemBean(Parcel in) {
        vcTranNo = in.readString();
        vcCompCode = in.readString();
        tranDate = in.readString();
    }

    public ReceiptItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vcTranNo);
        dest.writeString(vcCompCode);
        dest.writeString(tranDate);
    }

    @SuppressWarnings("unused")
    public static final Creator<ReceiptItemBean> CREATOR = new Creator<ReceiptItemBean>() {
        @Override
        public ReceiptItemBean createFromParcel(Parcel in) {
            return new ReceiptItemBean(in);
        }

        @Override
        public ReceiptItemBean[] newArray(int size) {
            return new ReceiptItemBean[size];
        }
    };
}