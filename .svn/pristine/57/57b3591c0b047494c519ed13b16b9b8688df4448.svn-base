package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class LoadingAdviceDataBean implements Parcelable {
    @SerializedName("VC_USER_CODE")
    private String vcUserCode;
    @SerializedName("DRIVER_NAME")
    private String driverName;
    @SerializedName("PRE_ADVICE_DATE")
    private String preAdviceDate;
    @SerializedName("CLD_ID")
    private String cldId;
    @SerializedName("PROJ_ID")
    private String projId;
    @SerializedName("VEHICLE_NUMBER")
    private String vehicleNumber;
    @SerializedName("PRE_ADVICE_NO")
    private String preAdviceNo;
    @SerializedName("INVOICE")
    private List<LoadingAdviceInvoiceBean> invoiceList = null;
    @SerializedName("SLOC_ID")
    private String slocId;
    @SerializedName("HO_ORG_ID")
    private String hoOrgId;
    @SerializedName("ORG_ID")
    private String orgId;

    public String getVcUserCode() {
        return vcUserCode;
    }

    public void setVcUserCode(String vcUserCode) {
        this.vcUserCode = vcUserCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPreAdviceDate() {
        return preAdviceDate;
    }

    public void setPreAdviceDate(String preAdviceDate) {
        this.preAdviceDate = preAdviceDate;
    }

    public String getCldId() {
        return cldId;
    }

    public void setCldId(String cldId) {
        this.cldId = cldId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPreAdviceNo() {
        return preAdviceNo;
    }

    public void setPreAdviceNo(String preAdviceNo) {
        this.preAdviceNo = preAdviceNo;
    }

    public List<LoadingAdviceInvoiceBean> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<LoadingAdviceInvoiceBean> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public String getSlocId() {
        return slocId;
    }

    public void setSlocId(String slocId) {
        this.slocId = slocId;
    }

    public String getHoOrgId() {
        return hoOrgId;
    }

    public void setHoOrgId(String hoOrgId) {
        this.hoOrgId = hoOrgId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public LoadingAdviceDataBean(){}
    public LoadingAdviceDataBean(Parcel in) {
        vcUserCode = in.readString();
        driverName = in.readString();
        preAdviceDate = in.readString();
        cldId = in.readString();
        projId = in.readString();
        vehicleNumber = in.readString();
        preAdviceNo = in.readString();
        if (in.readByte() == 0x01) {
            invoiceList = new ArrayList<LoadingAdviceInvoiceBean>();
            in.readList(invoiceList, LoadingAdviceInvoiceBean.class.getClassLoader());
        } else {
            invoiceList = null;
        }
        slocId = in.readString();
        hoOrgId = in.readString();
        orgId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vcUserCode);
        dest.writeString(driverName);
        dest.writeString(preAdviceDate);
        dest.writeString(cldId);
        dest.writeString(projId);
        dest.writeString(vehicleNumber);
        dest.writeString(preAdviceNo);
        if (invoiceList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(invoiceList);
        }
        dest.writeString(slocId);
        dest.writeString(hoOrgId);
        dest.writeString(orgId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoadingAdviceDataBean> CREATOR = new Parcelable.Creator<LoadingAdviceDataBean>() {
        @Override
        public LoadingAdviceDataBean createFromParcel(Parcel in) {
            return new LoadingAdviceDataBean(in);
        }

        @Override
        public LoadingAdviceDataBean[] newArray(int size) {
            return new LoadingAdviceDataBean[size];
        }
    };
}
