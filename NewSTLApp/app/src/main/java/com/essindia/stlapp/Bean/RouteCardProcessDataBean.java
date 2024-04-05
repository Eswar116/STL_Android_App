package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteCardProcessDataBean implements Parcelable {

    private String routeNo;
    private String routeDate;
    private String productCode;
    private String vcProcessCode;
    private String vcProcessDesc;
    private String nuOkQuantity;
    private String vcActualBinNo;
    private String vcVirtualBinNo;
    private String nuPartWeight;
    private String status;

    public RouteCardProcessDataBean() {

    }

    public RouteCardProcessDataBean(Parcel in) {
        routeNo = in.readString();
        routeDate = in.readString();
        productCode = in.readString();
        vcProcessCode = in.readString();
        vcProcessDesc = in.readString();
        nuOkQuantity = in.readString();
        vcVirtualBinNo = in.readString();
        vcActualBinNo = in.readString();
        nuPartWeight = in.readString();
        status = in.readString();
    }

    public String getVcVirtualBinNo() {
        return vcVirtualBinNo;
    }

    public void setVcVirtualBinNo(String vcVirtualBinNo) {
        this.vcVirtualBinNo = vcVirtualBinNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(String routeDate) {
        this.routeDate = routeDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getVcProcessCode() {
        return vcProcessCode;
    }

    public void setVcProcessCode(String vcProcessCode) {
        this.vcProcessCode = vcProcessCode;
    }

 public String getVcProcessDesc() {
        return vcProcessDesc;
    }

    public void setVcProcessDesc(String vcProcessDesc) {
        this.vcProcessDesc = vcProcessDesc;
    }

    public String getNuOkQuantity() {
        return nuOkQuantity;
    }

    public void setNuOkQuantity(String nuOkQuantity) {
        this.nuOkQuantity = nuOkQuantity;
    }

    public String getVcActualBinNo() {
        return vcActualBinNo;
    }

    public void setVcActualBinNo(String vcActualBinNo) {
        this.vcActualBinNo = vcActualBinNo;
    }
    public String getNuPartWeight() {
        return nuPartWeight;
    }

    public void setNuPartWeight(String nuPartWeight) {
        this.nuPartWeight = nuPartWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(routeNo);
        parcel.writeString(routeDate);
        parcel.writeString(productCode);
        parcel.writeString(vcProcessCode);
        parcel.writeString(vcProcessDesc);
        parcel.writeString(nuOkQuantity);
        parcel.writeString(vcVirtualBinNo);
        parcel.writeString(vcActualBinNo);
        parcel.writeString(nuPartWeight);
        parcel.writeString(status);
    }

    public static final Creator<RouteCardProcessDataBean> CREATOR = new Creator<RouteCardProcessDataBean>() {
        @Override
        public RouteCardProcessDataBean createFromParcel(Parcel in) {
            return new RouteCardProcessDataBean(in);
        }

        @Override
        public RouteCardProcessDataBean[] newArray(int size) {
            return new RouteCardProcessDataBean[size];
        }
    };
}
