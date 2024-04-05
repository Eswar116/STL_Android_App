package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CoilIssueItemDetailBean implements Parcelable {
    private String tranNo;
    private String tranDate;
    private String mrsNo;
    private String mrsDate;
    private String mrirNo;
    private String mrirDate;
    private String grad;
    private String coilWeight;
    private String vcSHeatNo;
    private String vcSCoilNo;
    private String tagNo;
    private String loadStatus;
    private String routeCardNo;
    private String routeCardDate;
    private String locationName;
    private String locationCode;
    private String vcDivision;

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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getVcDivision() {
        return vcDivision;
    }

    public void setVcDivision(String vcDivision) {
        this.vcDivision = vcDivision;
    }

    public String getTranNo() {
        return tranNo;
    }

    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getMrsNo() {
        return mrsNo;
    }

    public void setMrsNo(String mrsNo) {
        this.mrsNo = mrsNo;
    }

    public String getMrsDate() {
        return mrsDate;
    }

    public void setMrsDate(String mrsDate) {
        this.mrsDate = mrsDate;
    }

    public String getMrirNo() {
        return mrirNo;
    }

    public void setMrirNo(String mrirNo) {
        this.mrirNo = mrirNo;
    }

    public String getMrirDate() {
        return mrirDate;
    }

    public void setMrirDate(String mrirDate) {
        this.mrirDate = mrirDate;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getCoilWeight() {
        return coilWeight;
    }

    public void setCoilWeight(String coilWeight) {
        this.coilWeight = coilWeight;
    }

    public String getVcSHeatNo() {
        return vcSHeatNo;
    }

    public void setVcSHeatNo(String vcSHeatNo) {
        this.vcSHeatNo = vcSHeatNo;
    }

    public String getVcSCoilNo() {
        return vcSCoilNo;
    }

    public void setVcSCoilNo(String vcSCoilNo) {
        this.vcSCoilNo = vcSCoilNo;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public CoilIssueItemDetailBean() {
    }

    public CoilIssueItemDetailBean(Parcel in) {
        tranNo = in.readString();
        tranDate = in.readString();
        mrsNo = in.readString();
        mrsDate = in.readString();
        mrirNo = in.readString();
        mrirDate = in.readString();
        grad = in.readString();
        coilWeight = in.readString();
        vcSHeatNo = in.readString();
        vcSCoilNo = in.readString();
        tagNo = in.readString();
        loadStatus = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tranNo);
        dest.writeString(tranDate);
        dest.writeString(mrsNo);
        dest.writeString(mrsDate);
        dest.writeString(mrirNo);
        dest.writeString(mrirDate);
        dest.writeString(grad);
        dest.writeString(coilWeight);
        dest.writeString(vcSHeatNo);
        dest.writeString(vcSCoilNo);
        dest.writeString(tagNo);
        dest.writeString(loadStatus);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CoilIssueItemDetailBean> CREATOR = new Parcelable.Creator<CoilIssueItemDetailBean>() {
        @Override
        public CoilIssueItemDetailBean createFromParcel(Parcel in) {
            return new CoilIssueItemDetailBean(in);
        }

        @Override
        public CoilIssueItemDetailBean[] newArray(int size) {
            return new CoilIssueItemDetailBean[size];
        }
    };
}
