package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 26-12-2016.
 */

public class ItemBean implements Parcelable {

    @SerializedName("ITEM_DESC")
    @Expose
    private String iTEMDESC;
    @SerializedName("PEND_QTY")
    @Expose
    private String pENDQTY;
    @SerializedName("INVOICE_QTY")
    @Expose
    private String iNVOICEQTY;
    @SerializedName("ITEM_CODE")
    @Expose
    private String iTEMCODE;
    @SerializedName("NU_PRIORITY")
    @Expose
    private String nUPRIORITY;
    @SerializedName("RECEIVED_QTY")
    @Expose
    private String rECEIVEDQTY;
    @SerializedName("CLOSE_FLAG")
    @Expose
    private String cLOSEFLAG;
    @SerializedName("PACK_SIZE")
    @Expose
    private String pACKSIZE;

    public String getITEMDESC() {
        return iTEMDESC;
    }

    public void setITEMDESC(String iTEMDESC) {
        this.iTEMDESC = iTEMDESC;
    }

    public String getPENDQTY() {
        return pENDQTY;
    }

    public void setPENDQTY(String pENDQTY) {
        this.pENDQTY = pENDQTY;
    }

    public String getINVOICEQTY() {
        return iNVOICEQTY;
    }

    public void setINVOICEQTY(String iNVOICEQTY) {
        this.iNVOICEQTY = iNVOICEQTY;
    }

    public String getITEMCODE() {
        return iTEMCODE;
    }

    public void setITEMCODE(String iTEMCODE) {
        this.iTEMCODE = iTEMCODE;
    }

    public String getNUPRIORITY() {
        return nUPRIORITY;
    }

    public void setNUPRIORITY(String nUPRIORITY) {
        this.nUPRIORITY = nUPRIORITY;
    }

    public String getRECEIVEDQTY() {
        return rECEIVEDQTY;
    }

    public void setRECEIVEDQTY(String rECEIVEDQTY) {
        this.rECEIVEDQTY = rECEIVEDQTY;
    }

    public String getCLOSEFLAG() {
        return cLOSEFLAG;
    }

    public void setCLOSEFLAG(String cLOSEFLAG) {
        this.cLOSEFLAG = cLOSEFLAG;
    }

    public String getPACKSIZE() {
        return pACKSIZE;
    }

    public void setPACKSIZE(String pACKSIZE) {
        this.pACKSIZE = pACKSIZE;
    }


    protected ItemBean(Parcel in) {
        iTEMDESC = in.readString();
        pENDQTY = in.readString();
        iNVOICEQTY = in.readString();
        iTEMCODE = in.readString();
        nUPRIORITY = in.readString();
        rECEIVEDQTY = in.readString();
        cLOSEFLAG = in.readString();
        pACKSIZE = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iTEMDESC);
        dest.writeString(pENDQTY);
        dest.writeString(iNVOICEQTY);
        dest.writeString(iTEMCODE);
        dest.writeString(nUPRIORITY);
        dest.writeString(rECEIVEDQTY);
        dest.writeString(cLOSEFLAG);
        dest.writeString(pACKSIZE);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemBean> CREATOR = new Parcelable.Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };
}