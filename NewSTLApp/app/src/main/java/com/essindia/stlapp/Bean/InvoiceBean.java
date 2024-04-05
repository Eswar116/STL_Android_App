package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 26-12-2016.
 */

public class InvoiceBean implements Parcelable {

    @SerializedName("CUSTOMER_CODE")
    @Expose
    private String cUSTOMERCODE;
    @SerializedName("CUSTOMER_NAME")
    @Expose
    private String cUSTOMERNAME;
    @SerializedName("INVOICE_NO")
    @Expose
    private String iNVOICENO;
    @SerializedName("ITEM")
    @Expose
    private List<ItemBean> iTEM = null;
    @SerializedName("INVOICE_DATE")
    @Expose
    private String iNVOICEDATE;
    @SerializedName("VC_REF_INVOICE_NO")
    @Expose
    private String VC_REF_INVOICE_NO;
    @SerializedName("VC_CUSTOMER_NAME")
    @Expose
    private String VC_CUSTOMER_NAME;
    @SerializedName("VC_INVOICE_NO")
    @Expose
    private String VC_INVOICE_NO;
    @SerializedName("DT_REF_INVOICE_DATE")
    @Expose
    private String DT_REF_INVOICE_DATE;
    @SerializedName("NU_TOTAL_CASES")
    @Expose
    private String NU_TOTAL_CASES;
    @SerializedName("DT_INVOICE_DATE")
    @Expose
    private String DT_INVOICE_DATE;
    @SerializedName("VC_PRODUCT_CODE")
    @Expose
    private String VC_PRODUCT_CODE;
    @SerializedName("NU_PRODUCT_QUANTITY")
    @Expose
    private String NU_PRODUCT_QUANTITY;
    @SerializedName("NU_PACK_SIZE")
    @Expose
    private String NU_PACK_SIZE;
    @SerializedName("BOXES")
    @Expose
    private String BOXES;
    @SerializedName("TOT_PROD_SCAN_BOX")
    @Expose
    private String TOT_PROD_SCAN_BOX;
    @SerializedName("TOT_INV_SCAN_BOX")
    @Expose
    private String TOT_INV_SCAN_BOX;

    public String getTOT_PROD_SCAN_BOX() {
        return TOT_PROD_SCAN_BOX;
    }

    public void setTOT_PROD_SCAN_BOX(String TOT_PROD_SCAN_BOX) {
        this.TOT_PROD_SCAN_BOX = TOT_PROD_SCAN_BOX;
    }

    public String getTOT_INV_SCAN_BOX() {
        return TOT_INV_SCAN_BOX;
    }

    public void setTOT_INV_SCAN_BOX(String TOT_INV_SCAN_BOX) {
        this.TOT_INV_SCAN_BOX = TOT_INV_SCAN_BOX;
    }



    private int COUNT;

    public int getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(int COUNT) {
        this.COUNT = COUNT;
    }

    public String getVC_PRODUCT_CODE() {
        return VC_PRODUCT_CODE;
    }

    public void setVC_PRODUCT_CODE(String VC_PRODUCT_CODE) {
        this.VC_PRODUCT_CODE = VC_PRODUCT_CODE;
    }

    public String getNU_PRODUCT_QUANTITY() {
        return NU_PRODUCT_QUANTITY;
    }

    public void setNU_PRODUCT_QUANTITY(String NU_PRODUCT_QUANTITY) {
        this.NU_PRODUCT_QUANTITY = NU_PRODUCT_QUANTITY;
    }

    public String getNU_PACK_SIZE() {
        return NU_PACK_SIZE;
    }

    public void setNU_PACK_SIZE(String NU_PACK_SIZE) {
        this.NU_PACK_SIZE = NU_PACK_SIZE;
    }

    public String getBOXES() {
        return BOXES;
    }

    public void setBOXES(String BOXES) {
        this.BOXES = BOXES;
    }

    public String getVC_REF_INVOICE_NO() {
        return VC_REF_INVOICE_NO;
    }

    public void setVC_REF_INVOICE_NO(String VC_REF_INVOICE_NO) {
        this.VC_REF_INVOICE_NO = VC_REF_INVOICE_NO;
    }

    public String getVC_CUSTOMER_NAME() {
        return VC_CUSTOMER_NAME;
    }

    public void setVC_CUSTOMER_NAME(String VC_CUSTOMER_NAME) {
        this.VC_CUSTOMER_NAME = VC_CUSTOMER_NAME;
    }

    public String getVC_INVOICE_NO() {
        return VC_INVOICE_NO;
    }

    public void setVC_INVOICE_NO(String VC_INVOICE_NO) {
        this.VC_INVOICE_NO = VC_INVOICE_NO;
    }

    public String getDT_REF_INVOICE_DATE() {
        return DT_REF_INVOICE_DATE;
    }

    public void setDT_REF_INVOICE_DATE(String DT_REF_INVOICE_DATE) {
        this.DT_REF_INVOICE_DATE = DT_REF_INVOICE_DATE;
    }

    public String getNU_TOTAL_CASES() {
        return NU_TOTAL_CASES;
    }

    public void setNU_TOTAL_CASES(String NU_TOTAL_CASES) {
        this.NU_TOTAL_CASES = NU_TOTAL_CASES;
    }

    public String getDT_INVOICE_DATE() {
        return DT_INVOICE_DATE;
    }

    public void setDT_INVOICE_DATE(String DT_INVOICE_DATE) {
        this.DT_INVOICE_DATE = DT_INVOICE_DATE;
    }

    public String getCUSTOMERCODE() {
        return cUSTOMERCODE;
    }

    public void setCUSTOMERCODE(String cUSTOMERCODE) {
        this.cUSTOMERCODE = cUSTOMERCODE;
    }

    public String getCUSTOMERNAME() {
        return cUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String cUSTOMERNAME) {
        this.cUSTOMERNAME = cUSTOMERNAME;
    }

    public String getINVOICENO() {
        return iNVOICENO;
    }

    public void setINVOICENO(String iNVOICENO) {
        this.iNVOICENO = iNVOICENO;
    }

    public List<ItemBean> getITEM() {
        return iTEM;
    }

    public void setITEM(List<ItemBean> iTEM) {
        this.iTEM = iTEM;
    }

    public String getINVOICEDATE() {
        return iNVOICEDATE;
    }

    public void setINVOICEDATE(String iNVOICEDATE) {
        this.iNVOICEDATE = iNVOICEDATE;
    }

    public InvoiceBean() {
        super();
    }

    protected InvoiceBean(Parcel in) {
        cUSTOMERCODE = in.readString();
        cUSTOMERNAME = in.readString();
        iNVOICENO = in.readString();
        if (in.readByte() == 0x01) {
            iTEM = new ArrayList<ItemBean>();
            in.readList(iTEM, ItemBean.class.getClassLoader());
        } else {
            iTEM = null;
        }
        iNVOICEDATE = in.readString();
        VC_REF_INVOICE_NO = in.readString();
        VC_CUSTOMER_NAME = in.readString();
        VC_INVOICE_NO = in.readString();
        DT_REF_INVOICE_DATE = in.readString();
        NU_TOTAL_CASES = in.readString();
        DT_INVOICE_DATE = in.readString();
        VC_PRODUCT_CODE = in.readString();
        NU_PRODUCT_QUANTITY = in.readString();
        NU_PACK_SIZE = in.readString();
        BOXES = in.readString();
        TOT_PROD_SCAN_BOX = in.readString();
        TOT_INV_SCAN_BOX = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cUSTOMERCODE);
        dest.writeString(cUSTOMERNAME);
        dest.writeString(iNVOICENO);
        if (iTEM == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(iTEM);
        }
        dest.writeString(iNVOICEDATE);
        dest.writeString(VC_REF_INVOICE_NO);
        dest.writeString(VC_CUSTOMER_NAME);
        dest.writeString(VC_INVOICE_NO);
        dest.writeString(DT_REF_INVOICE_DATE);
        dest.writeString(NU_TOTAL_CASES);
        dest.writeString(DT_INVOICE_DATE);
        dest.writeString(VC_PRODUCT_CODE);
        dest.writeString(NU_PRODUCT_QUANTITY);
        dest.writeString(NU_PACK_SIZE);
        dest.writeString(BOXES);
        dest.writeString(TOT_PROD_SCAN_BOX);
        dest.writeString(TOT_INV_SCAN_BOX);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InvoiceBean> CREATOR = new Parcelable.Creator<InvoiceBean>() {
        @Override
        public InvoiceBean createFromParcel(Parcel in) {
            return new InvoiceBean(in);
        }

        @Override
        public InvoiceBean[] newArray(int size) {
            return new InvoiceBean[size];
        }
    };
}
