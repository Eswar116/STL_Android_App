package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class LoadingAdviceItemBean implements Parcelable {
    @SerializedName("ITEM_DESC")
    private String itemDesc;
    @SerializedName("PEND_QTY")
    private String pendQty;
    @SerializedName("INVOICE_QTY")
    private String invoiceQty;
    @SerializedName("ITEM_CODE")
    private String itemCode;
    @SerializedName("NU_PRIORITY")
    private String nuPriority;
    @SerializedName("RECEIVED_QTY")
    private String receivedQty;
    @SerializedName("CLOSE_FLAG")
    private String closeFlag;
    @SerializedName("PACK_SIZE")
    private String packSize;
    @SerializedName("VC_BOX_CODE")
    private String vcBoxCode;

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getPendQty() {
        return pendQty;
    }

    public void setPendQty(String pendQty) {
        this.pendQty = pendQty;
    }

    public String getInvoiceQty() {
        return invoiceQty;
    }

    public void setInvoiceQty(String invoiceQty) {
        this.invoiceQty = invoiceQty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getNuPriority() {
        return nuPriority;
    }

    public void setNuPriority(String nuPriority) {
        this.nuPriority = nuPriority;
    }

    public String getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(String receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(String closeFlag) {
        this.closeFlag = closeFlag;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getVcBoxCode() {
        return vcBoxCode;
    }

    public void setVcBoxCode(String vcBoxCode) {
        this.vcBoxCode = vcBoxCode;
    }

    protected LoadingAdviceItemBean(Parcel in) {
        itemDesc = in.readString();
        pendQty = in.readString();
        invoiceQty = in.readString();
        itemCode = in.readString();
        nuPriority= in.readString();
        receivedQty = in.readString();
        closeFlag = in.readString();
        packSize = in.readString();
        vcBoxCode = in.readString();
//        pendQty = in.readByte() == 0x00 ? null : in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemDesc);
        dest.writeString(pendQty);
        dest.writeString(invoiceQty);
        dest.writeString(itemCode);
        dest.writeString(nuPriority);
        dest.writeString(receivedQty);
        dest.writeString(closeFlag);
        dest.writeString(packSize);
        dest.writeString(vcBoxCode);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoadingAdviceItemBean> CREATOR = new Parcelable.Creator<LoadingAdviceItemBean>() {
        @Override
        public LoadingAdviceItemBean createFromParcel(Parcel in) {
            return new LoadingAdviceItemBean(in);
        }

        @Override
        public LoadingAdviceItemBean[] newArray(int size) {
            return new LoadingAdviceItemBean[size];
        }
    };
}