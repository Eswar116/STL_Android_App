package com.essindia.stlapp.Bean;

import java.io.Serializable;

public class SupervisorModel implements Serializable {
    public String getL_WAREHOUSE_COMP_CODE() {
        return L_WAREHOUSE_COMP_CODE;
    }

    public void setL_WAREHOUSE_COMP_CODE(String l_WAREHOUSE_COMP_CODE) {
        L_WAREHOUSE_COMP_CODE = l_WAREHOUSE_COMP_CODE;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    String L_WAREHOUSE_COMP_CODE;
    String invoice_date;
    String invoice_number;
}
