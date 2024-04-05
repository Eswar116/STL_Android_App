package com.essindia.stlapp.Bean;


import com.google.gson.annotations.SerializedName;

public class ProductItem {
    @SerializedName("product_string")
    private String productString;
    @SerializedName("product_code")
    private String productCode;
    @SerializedName("invoice_number")
    private String invoiceNumber;

    @SerializedName("status")
    private boolean status;

    @SerializedName("invoice_date")
    private String invoice_date;

    public String getL_warehouse_code() {
        return l_warehouse_code;
    }

    public void setL_warehouse_code(String l_warehouse_code) {
        this.l_warehouse_code = l_warehouse_code;
    }

    @SerializedName("l_warehouse_code")
    private String l_warehouse_code;


    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    // Getter for status
    public boolean isStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter and setter methods for the fields

    public String getProductString() {
        return productString;
    }

    public void setProductString(String productString) {
        this.productString = productString;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
