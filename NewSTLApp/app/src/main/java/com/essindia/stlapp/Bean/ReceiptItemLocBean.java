package com.essindia.stlapp.Bean;


import android.os.Parcel;
import android.os.Parcelable;
public class ReceiptItemLocBean implements Parcelable {

    String vcLocationDesc;
    String nuLocationCode;

    public String getVcLocationDesc() {
        return vcLocationDesc;
    }

    public void setVcLocationDesc(String vcLocationDesc) {
        this.vcLocationDesc = vcLocationDesc;
    }

    public String getNuLocationCode() {
        return nuLocationCode;
    }

    public void setNuLocationCode(String nuLocationCode) {
        this.nuLocationCode = nuLocationCode;
    }

    public ReceiptItemLocBean() {
    }

    protected ReceiptItemLocBean(Parcel in) {
        vcLocationDesc = in.readString();
        nuLocationCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vcLocationDesc);
        dest.writeString(nuLocationCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReceiptItemLocBean> CREATOR = new Parcelable.Creator<ReceiptItemLocBean>() {
        @Override
        public ReceiptItemLocBean createFromParcel(Parcel in) {
            return new ReceiptItemLocBean(in);
        }

        @Override
        public ReceiptItemLocBean[] newArray(int size) {
            return new ReceiptItemLocBean[size];
        }
    };
}