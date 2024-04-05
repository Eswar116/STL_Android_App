package com.essindia.stlapp.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 05-12-2016.
 */

public class PickUpSummaryData implements Parcelable {

    @SerializedName("VC_BIN_NO")
    private String VC_BIN_NO;
    @SerializedName("VC_BIN_DESC")
    private String VC_BIN_DESC;
    @SerializedName("VC_LOT_NO")
    private String VC_LOT_NO;
    @SerializedName("BIN_QTY")
    private String BIN_QTY;
    @SerializedName("PEND_QTY")
    private String PEND_QTY;
    @SerializedName("PEND_BOXES")
    private String PEND_BOXES;
    @SerializedName("BOX_TYPE")
    private String BOX_TYPE;
    @SerializedName("CUSTOMER_CODE")
    private String cUSTOMERCODE;
    @SerializedName("CUSTOMER_NAME")
    private String cUSTOMERNAME;
    @SerializedName("REASON")
    private String rEASON;
    @SerializedName("PRODUCT_NAME")
    private String pRODUCTNAME;
    @SerializedName("PICK_QTY")
    private String pICKQTY;
    @SerializedName("PRODUCT_CODE")
    private String pRODUCTCODE;
    @SerializedName("CLOSE_FLAG")
    private String cLOSEFLAG;
    @SerializedName("HO_ORG_ID")
    private String hOORGID;
    @SerializedName("PICK_BOXES")
    private String pickBoxes;
    @SerializedName("ORG_ID")
    private String oRGID;
    @SerializedName("PACK_SIZE")
    private String pACKSIZE;
    @SerializedName("VC_USER_CODE")
    private String vCUSERCODE;
    @SerializedName("CLD_ID")
    private String cLDID;
    @SerializedName("PICK_NO")
    private String pICKNO;
    @SerializedName("PROJ_ID")
    private String pROJID;
    @SerializedName("PICK_DATE")
    private String pICKDATE;
    @SerializedName("EXCESS_LESS_QTY_FLAG")
    private String eXCESSLESSQTYFLAG;
    @SerializedName("SLOC_ID")
    private String sLOCID;


    public String getBOX_TYPE() {
        return BOX_TYPE;
    }

    public void setBOX_TYPE(String BOX_TYPE) {
        this.BOX_TYPE = BOX_TYPE;
    }

    public String getPEND_QTY() {
        return PEND_QTY;
    }

    public void setPEND_QTY(String PEND_QTY) {
        this.PEND_QTY = PEND_QTY;
    }

    public String getPEND_BOXES() {
        return PEND_BOXES;
    }

    public void setPEND_BOXES(String PEND_BOXES) {
        this.PEND_BOXES = PEND_BOXES;
    }

    public String getBIN_QTY() {
        return BIN_QTY;
    }

    public void setBIN_QTY(String BIN_QTY) {
        this.BIN_QTY = BIN_QTY;
    }

    public String getVC_BIN_NO() {
        return VC_BIN_NO;
    }

    public void setVC_BIN_NO(String VC_BIN_NO) {
        this.VC_BIN_NO = VC_BIN_NO;
    }

    public String getVC_BIN_DESC() {
        return VC_BIN_DESC;
    }

    public void setVC_BIN_DESC(String VC_BIN_DESC) {
        this.VC_BIN_DESC = VC_BIN_DESC;
    }

    public String getVC_LOT_NO() {
        return VC_LOT_NO;
    }

    public void setVC_LOT_NO(String VC_LOT_NO) {
        this.VC_LOT_NO = VC_LOT_NO;
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


    public String getREASON() {
        return rEASON;
    }


    public void setREASON(String rEASON) {
        this.rEASON = rEASON;
    }


    public String getPRODUCTNAME() {
        return pRODUCTNAME;
    }


    public void setPRODUCTNAME(String pRODUCTNAME) {
        this.pRODUCTNAME = pRODUCTNAME;
    }


    public String getPICKQTY() {
        return pICKQTY;
    }


    public void setPICKQTY(String pICKQTY) {
        this.pICKQTY = pICKQTY;
    }


    public String getPRODUCTCODE() {
        return pRODUCTCODE;
    }


    public void setPRODUCTCODE(String pRODUCTCODE) {
        this.pRODUCTCODE = pRODUCTCODE;
    }


    public String getCLOSEFLAG() {
        return cLOSEFLAG;
    }


    public void setCLOSEFLAG(String cLOSEFLAG) {
        this.cLOSEFLAG = cLOSEFLAG;
    }


    public String getHOORGID() {
        return hOORGID;
    }


    public void setHOORGID(String hOORGID) {
        this.hOORGID = hOORGID;
    }


    public String getPickBoxes() {
        return pickBoxes;
    }


    public void setPickBoxes(String pickBoxes) {
        this.pickBoxes = pickBoxes;
    }


    public String getORGID() {
        return oRGID;
    }


    public void setORGID(String oRGID) {
        this.oRGID = oRGID;
    }


    public String getPACKSIZE() {
        return pACKSIZE;
    }


    public void setPACKSIZE(String pACKSIZE) {
        this.pACKSIZE = pACKSIZE;
    }


    public String getVCUSERCODE() {
        return vCUSERCODE;
    }


    public void setVCUSERCODE(String vCUSERCODE) {
        this.vCUSERCODE = vCUSERCODE;
    }


    public String getCLDID() {
        return cLDID;
    }


    public void setCLDID(String cLDID) {
        this.cLDID = cLDID;
    }


    public String getPICKNO() {
        return pICKNO;
    }


    public void setPICKNO(String pICKNO) {
        this.pICKNO = pICKNO;
    }


    public String getPROJID() {
        return pROJID;
    }


    public void setPROJID(String pROJID) {
        this.pROJID = pROJID;
    }


    public String getPICKDATE() {
        return pICKDATE;
    }


    public void setPICKDATE(String pICKDATE) {
        this.pICKDATE = pICKDATE;
    }


    public String getEXCESSLESSQTYFLAG() {
        return eXCESSLESSQTYFLAG;
    }


    public void setEXCESSLESSQTYFLAG(String eXCESSLESSQTYFLAG) {
        this.eXCESSLESSQTYFLAG = eXCESSLESSQTYFLAG;
    }

    public String getSLOCID() {
        return sLOCID;
    }

    public void setSLOCID(String sLOCID) {
        this.sLOCID = sLOCID;
    }

    public PickUpSummaryData() {
    }

    protected PickUpSummaryData(Parcel in) {
        cUSTOMERCODE = in.readString();
        cUSTOMERNAME = in.readString();
        rEASON = in.readString();
        pRODUCTNAME = in.readString();
        pICKQTY = in.readString();
        pRODUCTCODE = in.readString();
        cLOSEFLAG = in.readString();
        hOORGID = in.readString();
        pickBoxes = in.readString();
        oRGID = in.readString();
        pACKSIZE = in.readString();
        vCUSERCODE = in.readString();
        cLDID = in.readString();
        pICKNO = in.readString();
        pROJID = in.readString();
        pICKDATE = in.readString();
        eXCESSLESSQTYFLAG = in.readString();
        sLOCID = in.readString();
        VC_BIN_NO = in.readString();
        VC_BIN_DESC = in.readString();
        VC_LOT_NO = in.readString();
        BIN_QTY = in.readString();
        PEND_QTY = in.readString();
        PEND_BOXES = in.readString();
        BOX_TYPE = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cUSTOMERCODE);
        dest.writeString(cUSTOMERNAME);
        dest.writeString(rEASON);
        dest.writeString(pRODUCTNAME);
        dest.writeString(pICKQTY);
        dest.writeString(pRODUCTCODE);
        dest.writeString(cLOSEFLAG);
        dest.writeString(hOORGID);
        dest.writeString(pickBoxes);
        dest.writeString(oRGID);
        dest.writeString(pACKSIZE);
        dest.writeString(vCUSERCODE);
        dest.writeString(cLDID);
        dest.writeString(pICKNO);
        dest.writeString(pROJID);
        dest.writeString(pICKDATE);
        dest.writeString(eXCESSLESSQTYFLAG);
        dest.writeString(sLOCID);
        dest.writeString(VC_BIN_NO);
        dest.writeString(VC_BIN_DESC);
        dest.writeString(VC_LOT_NO);
        dest.writeString(BIN_QTY);
        dest.writeString(PEND_QTY);
        dest.writeString(PEND_BOXES);
        dest.writeString(BOX_TYPE);
    }

    public static final Creator<PickUpSummaryData> CREATOR = new Creator<PickUpSummaryData>() {
        @Override
        public PickUpSummaryData createFromParcel(Parcel in) {
            return new PickUpSummaryData(in);
        }

        @Override
        public PickUpSummaryData[] newArray(int size) {
            return new PickUpSummaryData[size];
        }
    };
}
