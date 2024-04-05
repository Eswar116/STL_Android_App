package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 27-12-2016.
 */

public class LoadingAdviceInvoiceBean implements Parcelable {

    @SerializedName("CUSTOMER_CODE")
    private String customerCode;
    @SerializedName("CUSTOMER_NAME")
    private String customerName;
    @SerializedName("INVOICE_NO")
    private String invoiceNo;
    @SerializedName("ITEM")
    private List<LoadingAdviceItemBean> loadingAdviceItemlist = null;
    @SerializedName("INVOICE_DATE")
    private String invoiceDate;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public List<LoadingAdviceItemBean> getLoadingAdviceItemlist() {
        return loadingAdviceItemlist;
    }

    public void setLoadingAdviceItemlist(List<LoadingAdviceItemBean> loadingAdviceItemlist) {
        this.loadingAdviceItemlist = loadingAdviceItemlist;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    protected LoadingAdviceInvoiceBean(Parcel in) {
        customerCode = in.readString();
        customerName = in.readString();
        invoiceNo = in.readString();
        if (in.readByte() == 0x01) {
            loadingAdviceItemlist = new ArrayList<LoadingAdviceItemBean>();
            in.readList(loadingAdviceItemlist, LoadingAdviceItemBean.class.getClassLoader());
        } else {
            loadingAdviceItemlist = null;
        }
        invoiceDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerCode);
        dest.writeString(customerName);
        dest.writeString(invoiceNo);
        if (loadingAdviceItemlist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(loadingAdviceItemlist);
        }
        dest.writeString(invoiceDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoadingAdviceInvoiceBean> CREATOR = new Parcelable.Creator<LoadingAdviceInvoiceBean>() {
        @Override
        public LoadingAdviceInvoiceBean createFromParcel(Parcel in) {
            return new LoadingAdviceInvoiceBean(in);
        }

        @Override
        public LoadingAdviceInvoiceBean[] newArray(int size) {
            return new LoadingAdviceInvoiceBean[size];
        }
    };
}
