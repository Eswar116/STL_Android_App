package com.essindia.stlapp.Bean;


import java.io.Serializable;

public class InwardScanningData implements Serializable {

    public String getTotal_boxes() {
        return total_boxes;
    }

    public void setTotal_boxes(String total_boxes) {
        this.total_boxes = total_boxes;
    }

    public String getScanned_boxed() {
        return scanned_boxed;
    }

    public void setScanned_boxed(String scanned_boxed) {
        this.scanned_boxed = scanned_boxed;
    }

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

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
    public String getTruck_number() {
        return truck_number;
    }

    public void setTruck_number(String truck_number) {
        this.truck_number = truck_number;
    }

    String total_boxes;
    String scanned_boxed ;
    String L_WAREHOUSE_COMP_CODE;
    String invoice_date;
    String invoice_number;
    String driver_name;



    String truck_number;
}
