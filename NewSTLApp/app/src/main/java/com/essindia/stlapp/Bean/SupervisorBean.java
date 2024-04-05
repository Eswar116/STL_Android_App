package com.essindia.stlapp.Bean;

public class SupervisorBean {
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

    public String getProductString() {
        return productString;
    }

    public void setProductString(String productString) {
        this.productString = productString;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    String productCode;
    String invoiceNumber;
    String productString;
    String invoiceDate;
}
