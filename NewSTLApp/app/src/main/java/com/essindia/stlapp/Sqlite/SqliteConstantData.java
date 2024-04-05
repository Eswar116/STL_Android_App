package com.essindia.stlapp.Sqlite;

/**
 * Created by Administrator on 08-11-2016.
 */

public class SqliteConstantData {

    //Tag
    public static final String KEY_GRN_VERIFICATION_LIST = "grn_verification_list";
    public static final String KEY_PICK_UP_LIST = "pick_up_list";

    //Table details
    public static final String TABLE_GRN_VERIFICATION_LIST = "grn_verification_list";
    public static final String TABLE_GRN_VERIFICATION_LOG = "grn_verification_log_list";
    public static final String TABLE_PICK_UP_LIST = "pick_away_list";
    public static final String TABLE_LOADING_ADVICE_LIST = "table_loading_advice_list";
    public static final String TABLE_LOADING_SCANNED_BOX = "table_loading_scanned_box";


    //GRN verification attributes
    public static final String KEY_HO_ORG_ID = "HO_ORG_ID";
    public static final String KEY_PROJ_ID = "PROJ_ID";
    public static final String KEY_CLD_ID = "CLD_ID";
    public static final String KEY_DEVICE_ID = "DEVICE_ID";
    public static final String KEY_TRAN_NO = "TRAN_NO";
    public static final String KEY_PACK_SIZE = "PACK_SIZE";
    public static final String KEY_PACK_TYPE = "PACK_TYPE";
    public static final String KEY_PROCESSED_BOXES = "PROCESSED_BOXES";
    public static final String KEY_IR_NO = "KEY_IR_NO";
    public static final String KEY_CLOSE_FLAG = "CLOSE_FLAG";
    public static final String KEY_EXCESS_PACK_QTY = "EXCESS_PACK_QTY";
    public static final String KEY_NO_OF_BOXES = "NO_OF_BOXES";
    public static final String KEY_ROUTE_DATE = "ROUTE_DATE";
    public static final String KEY_TRAN_date = "TRAN_date";
    public static final String KEY_SLOC_ID = "SLOC_ID";
    public static final String KEY_DT_MOD_DATE = "DT_MOD_DATE";
    public static final String KEY_ITEM_CODE = "ITEM_CODE";
    public static final String KEY_ROUTE_NO = "ROUTE_NO";
    public static final String KEY_SCAN_FLAG = "SCAN_FLAG";
    public static final String KEY_ORG_ID = "ORG_ID";
    public static final String KEY_EXCESS_FLAG = "EXCESS_FLAG";
    public static final String KEY_VC_MACHINE_NO = "VC_MACHINE_NO";

    public static final String KEY_VERIFIED_QTY = "VERIFIED_QTY";
    public static final String KEY_PEND_QTY = "PEND_QTY";
    public static final String KEY_ITEM_DESC = "ITEM_DESC";

    //Pickup list parameter
    public static final String KEY_CUSTOMER_CODE = "CUSTOMER_CODE";
    public static final String KEY_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_PRODUCT_NAME = "PRODUCT_NAME";
    public static final String KEY_PICK_QTY = "PICK_QTY";
    public static final String KEY_PRODUCT_CODE = "PRODUCT_CODE";
    public static final String KEY_ACTUAL_PICK_QTY = "ACTUAL_PICK_QTY";
    public static final String KEY_VC_USER_CODE = "VC_USER_CODE";
    public static final String KEY_PICK_NO = "PICK_NO";
    public static final String KEY_BOX_TYPE = "BOX_TYPE";
    public static final String KEY_PICK_DATE = "PICK_DATE";
    public static final String KEY_EXCESS_LESS_QTY_FLAG = "EXCESS_LESS_QTY_FLAG";
    public static final String KEY_P_AREA = "P_AREA";

    //Loading Advice list parameter
    public static final String KEY_PRE_ADVICE_DATE = "PRE_ADVICE_DATE";
    public static final String KEY_NU_PRIORITY = "ITEM_PRIORITY";
    public static final String KEY_RECEIVED_QTY = "RECEIVED_QTY";
    public static final String KEY_DRIVER_NAME = "DRIVER_NAME";
    public static final String KEY_VEHICLE_NUMBER = "VEHICLE_NUMBER";
    public static final String KEY_INVOICE_QTY = "INVOICE_QTY";
    public static final String KEY_PRE_ADVICE_NO = "PRE_ADVICE_NO";
    public static final String KEY_VC_BIN_NO = "VC_BIN_NO";
    public static final String KEY_VC_BIN_DESC = "VC_BIN_DESC";
    public static final String KEY_VC_LOT_NO = "VC_LOT_NO";
    public static final String KEY_BIN_QTY = "BIN_QTY";
    public static final String KEY_INVOICE = "INVOICE";

    //Loading Scanned Box parameter
    public static final String KEY_INVOICE_NO = "INVOICE_NO";
    public static final String KEY_INVOICE_DATE = "INVOICE_DATE";
    public static final String KEY_BOX_ID = "BOX_ID";
    public static final String KEY_BOX_CATEGORY = "BOX_CATEGORY";

    public static final String CREATE_GRN_VERIFICATION_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GRN_VERIFICATION_LIST + "("
            + KEY_HO_ORG_ID + " TEXT, "
            + KEY_PROJ_ID + " TEXT, "
            + KEY_CLD_ID + " TEXT, "
            + KEY_TRAN_NO + " TEXT, "
            + KEY_PACK_SIZE + " REAL, "
            + KEY_PACK_TYPE + " TEXT, "
            + KEY_PROCESSED_BOXES + " TEXT, "
            + KEY_IR_NO + " TEXT, "
            + KEY_CLOSE_FLAG + " TEXT, "
            + KEY_EXCESS_PACK_QTY + " TEXT, "
            + KEY_NO_OF_BOXES + " TEXT, "
            + KEY_ROUTE_DATE + " INTEGER, "
            + KEY_TRAN_date + " TEXT, "
            + KEY_SLOC_ID + " TEXT, "
            + KEY_DT_MOD_DATE + " TEXT, "
            + KEY_ITEM_CODE + " TEXT, "
            + KEY_ROUTE_NO + " TEXT, "
            + KEY_SCAN_FLAG + " TEXT, "
            + KEY_ORG_ID + " TEXT, "
            + KEY_VERIFIED_QTY + " TEXT, "
            + KEY_PEND_QTY + " TEXT, "
            + KEY_ITEM_DESC + " TEXT, "
            + KEY_VC_MACHINE_NO + " TEXT, "
            + KEY_EXCESS_FLAG + " TEXT" + ")";

    public static final String CREATE_GRN_VERIFICATION_LOG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GRN_VERIFICATION_LOG + "("
            + KEY_HO_ORG_ID + " TEXT, "
            + KEY_PROJ_ID + " TEXT, "
            + KEY_CLD_ID + " TEXT, "
            + KEY_TRAN_NO + " TEXT, "
            + KEY_PACK_SIZE + " REAL, "
            + KEY_PACK_TYPE + " TEXT, "
            + KEY_PROCESSED_BOXES + " TEXT, "
            + KEY_IR_NO + " TEXT, "
            + KEY_CLOSE_FLAG + " TEXT, "
            + KEY_EXCESS_PACK_QTY + " TEXT, "
            + KEY_NO_OF_BOXES + " TEXT, "
            + KEY_ROUTE_DATE + " INTEGER, "
            + KEY_TRAN_date + " TEXT, "
            + KEY_SLOC_ID + " TEXT, "
            + KEY_DT_MOD_DATE + " TEXT, "
            + KEY_ITEM_CODE + " TEXT, "
            + KEY_ROUTE_NO + " TEXT, "
            + KEY_SCAN_FLAG + " TEXT, "
            + KEY_ORG_ID + " TEXT, "
            + KEY_EXCESS_FLAG + " TEXT" + ")";

    public static final String CREATE_PICK_UP_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PICK_UP_LIST + "("
            + KEY_CUSTOMER_CODE + " TEXT, "
            + KEY_CUSTOMER_NAME + " TEXT, "
            + KEY_REASON + " TEXT, "
            + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_PICK_QTY + " TEXT, "
            + KEY_PRODUCT_CODE + " TEXT, "
            + KEY_CLOSE_FLAG + " TEXT, "
            + KEY_HO_ORG_ID + " TEXT, "
            + KEY_ACTUAL_PICK_QTY + " TEXT, "
            + KEY_ORG_ID + " TEXT, "
            + KEY_PACK_SIZE + " REAL, "
            + KEY_VC_USER_CODE + " TEXT, "
            + KEY_CLD_ID + " TEXT, "
            + KEY_PICK_NO + " INTEGER, "
            + KEY_PROJ_ID + " TEXT, "
            + KEY_PICK_DATE + " TEXT, "
            + KEY_EXCESS_LESS_QTY_FLAG + " TEXT, "
            + KEY_VC_BIN_NO + " TEXT, "
            + KEY_VC_BIN_DESC + " TEXT, "
            + KEY_VC_LOT_NO + " TEXT, "
            + KEY_BIN_QTY + " TEXT, "
            + KEY_PEND_QTY + " TEXT, "
            + KEY_BOX_TYPE + " TEXT, "
            + KEY_P_AREA + " TEXT, "
            + KEY_SLOC_ID + " TEXT" + ")";

    // Table for LOADING ADVICE LIST
    public static final String CREATE_LOADING_ADVICE_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOADING_ADVICE_LIST + "("
            + KEY_VC_USER_CODE + " TEXT, "
            + KEY_DRIVER_NAME + " TEXT, "
            + KEY_PRE_ADVICE_DATE + " TEXT, "
            + KEY_CLD_ID + " TEXT, "
            + KEY_PROJ_ID + " TEXT, "
            + KEY_VEHICLE_NUMBER + " TEXT, "
            + KEY_PRE_ADVICE_NO + " TEXT, "
            + KEY_SLOC_ID + " TEXT, "
            + KEY_HO_ORG_ID + " TEXT, "
            + KEY_ORG_ID + " TEXT, "

            + KEY_CUSTOMER_CODE + " TEXT, "
            + KEY_CUSTOMER_NAME + " TEXT, "
            + KEY_INVOICE_NO + " TEXT, "
            + KEY_INVOICE_DATE + " TEXT, "

            + KEY_ITEM_DESC + " TEXT, "
            + KEY_PEND_QTY + " TEXT, "
            + KEY_INVOICE_QTY + " TEXT, "
            + KEY_ITEM_CODE + " TEXT, "
            + KEY_NU_PRIORITY + " TEXT, "
            + KEY_RECEIVED_QTY + " TEXT, "
            + KEY_CLOSE_FLAG + " TEXT, "
            + KEY_PACK_SIZE + " TEXT"
            + ")";

    // Table for LOADING SCANNED BOX
    public static final String CREATE_LOADING_SCANNED_BOX_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOADING_SCANNED_BOX + "("
            + KEY_VC_USER_CODE + " TEXT, "
            + KEY_DRIVER_NAME + " TEXT, "
            + KEY_PRE_ADVICE_DATE + " TEXT, "
            + KEY_CLD_ID + " TEXT, "
            + KEY_PROJ_ID + " TEXT, "
            + KEY_VEHICLE_NUMBER + " TEXT, "
            + KEY_PRE_ADVICE_NO + " TEXT, "
            + KEY_CUSTOMER_CODE + " TEXT, "
            + KEY_CUSTOMER_NAME + " TEXT, "
            + KEY_INVOICE_NO + " TEXT, "
            + KEY_INVOICE_QTY + " TEXT, "
            + KEY_ITEM_CODE + " TEXT, "
            + KEY_PACK_SIZE + " TEXT, "
            + KEY_INVOICE_DATE + " TEXT, "
            + KEY_SLOC_ID + " TEXT, "
            + KEY_HO_ORG_ID + " TEXT, "
            + KEY_ORG_ID + " TEXT, "
            + KEY_BOX_ID + " TEXT" + ")";
            /*+ KEY_DEVICE_ID + " TEXT, "
            + KEY_NO_OF_BOXES + " TEXT, "*/
}
