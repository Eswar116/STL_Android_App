package com.essindia.stlapp.Constant;

import static java.sql.Types.TIMESTAMP;

/**
 * Created by jonu.kumar on 21-11-2016.
 */

public class ReqResParamKey {

    //Dashboard ReqResParam
    public static String TotalCount = "TotalCount";

    //newly added parameter on 01/feb/2017 for GRN and put Away from ERP
    public static String EXCESS_PACK_QTY = "EXCESS_PACK_QTY";
    public static String TOKEN = "auth_Token";

    // PUT AWAY RESPONSE PARAM
    public static String QR_CODE_DATA = "QR_CODE_DATA";
    public static String HO_ORG_ID = "HO_ORG_ID";
    public static String ORG_ID = "ORG_ID";
    public static String P_COMP_CODE = "P_COMP_CODE";
    public static String P_LOADING_NO = "P_LOADING_NO";
    public static String P_LOADING_DATE = "P_LOADING_DATE";
    public static String P_INWARD_FLAG = "P_INWARD_FLAG";
    public static String ACTUAL_BOX_QTY = "ACTUAL_BOX_QTY";
    public static String SELECTED_AREA = "SELECTED_AREA";
    public static String SLOC_ID = "SLOC_ID";
    public static String PROJ_ID = "PROJ_ID";
    public static String CLD_ID = "CLD_ID";
    public static String VERIFICATION_TIMESTAMP = "VERIFICATION_TIMESTAMP";
    public static String USER_ID = "USER_ID";
    public static String DEVICE_ID = "DEVICE_ID";
    public static String PUT_AWAY_ID = "PUT_AWAY_ID";
    public static String ROUTE_NO = "ROUTE_NO";
    public static String ROUTE_DATE = "ROUTE_DATE";
    public static String TRAN_NO = "TRAN_NO";
    public static String TRAN_DATE = "TRAN_DATE";
    public static String MRS_NO = "MRS_NO";
    public static String MRS_DATE = "MRS_DATE";
    public static String MRIR_NO = "MRIR_NO";
    public static String MRIR_DATE = "MRIR_DATE";
    public static String GRAD = "GRAD";
    public static String COIL_WEIGHT = "COIL_WEIGHT";
    public static String IR_NO = "IR_NO";
    public static String ITEM_CODE = "ITEM_CODE";
    public static String PACK_TYPE = "PACK_TYPE";
    public static String PACK_SIZE = "PACK_SIZE";
    public static String EXCESS_PACK_SIZE = "EXCESS_PACK_SIZE";
    public static String BOX_ID = "BOX_ID";
    public static String BOX_QTY = "BOX_QTY";
    public static String DT_MOD_DATE = "DT_MOD_DATE";
    public static String PEND_QTY = "PEND_QTY";
    public static String CLOSE_FLAG = "CLOSE_FLAG";
    public static String NO_OF_BOXES = "NO_OF_BOXES";
    public static String Message = "Message";
    public static String MessageCode = "MessageCode";
    public static String VC_REASON_DESC = "VC_REASON_DESC";
    public static String PICKUP_REASON = "REASON";
    public static String LOCATION_NAME = "LOCATION_NAME";
    public static String REQ_QUANTITY = "REQ_QUANTITY";

    public static String BIN_NO = "BIN_NO";
    public static String VC_BIN_NO = "VC_BIN_NO";
    public static String BIN_DESC = "BIN_DESC";
    public static String SUGGESTED_AREA = "SUGGESTED_AREA";
    public static String LOT_NO = "LOT_NO";
    public static String CURR_CAPACITY = "CURR_CAPACITY";
    public static String DOCK_AREA = "DOCK_AREA";
    public static String PUT_QTY = "PUT_QTY";
    public static String REASON = "Fully Picked";
    public static String USER_NAME = "user_name";


    public static String EMPTY_BIN_CAPACITY = "EMPTY_BIN_CAPACITY";
    //Pick up

    public static String PICK_NO = "PICK_NO";
    public static String PICK_DATE = "PICK_DATE";
    public static String BIN_LOCATION = "BIN_LOCATION";
    public static String PRODUCT_CODE = "PRODUCT_CODE";
    public static String BIN_QTY = "BIN_QTY";
    public static String PICK_QTY = "PICK_QTY";
    public static String PICKED_BOXES = "PICKED_BOXES";
    public static String SYNC_FLAG = "SYNC_FLAG";
    public static String SYNC_DATE = "SYNC_DATE";
    public static String TROLLY_ID = "TROLLY_ID";
    public static String TIMESTAMP = "TIMESTAMP";
    public static String TIME_STAMP = "TIME_STAMP";
    public static String CUSTOMER_CODE = "CUSTOMER_CODE";
    public static String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static String PRODUCT_NAME = "PRODUCT_NAME";
    public static String VC_USER_CODE = "VC_USER_CODE";
    public static String PICK_TIMESTAMP = "PICK_TIMESTAMP";
    public static final String PROCESS_CODE = "PROCESS_CODE";
    public static final String MRS_STR = "MRS_STR";
    //LOADING param
    public static String PRE_ADVICE_NO = "PRE_ADVICE_NO";
    public static String PRE_ADVICE_DATE = "PRE_ADVICE_DATE";
    public static final String VC_PROCESS_DESC = "VC_PROCESS_DESC";
    public static final String VC_PROCESS_CODE = "VC_PROCESS_CODE";
    public static final String VC_ACTUAL_BIN_NO = "VC_ACTUAL_BIN_NO";
    public static final String VC_VIRTUAL_BIN_NO = "VC_VIRTUAL_BIN_NO";
    public static final String NU_OK_QUANTITY = "NU_OK_QUANTITY";
    public static final String NU_PART_WEIGHT = "NU_PART_WEIGHT";
    //    RM RECEIPT PARAM
    public static String VC_COMP_CODE = "VC_COMP_CODE";
    public static String VC_TRAN_NO = "VC_TRAN_NO";
    public static String DT_TRAN_DATE = "DT_TRAN_DATE";
    public static String VC_ORDER_NO = "VC_ORDER_NO";
    public static String VC_GATE_ENT_NO = "VC_GATE_ENT_NO";
    public static String VC_INVOICE_NO = "VC_INVOICE_NO";
    public static String VC_ITEM_CODE = "VC_ITEM_CODE";
    public static String DT_CHALLAN_DATE = "DT_CHALLAN_DATE";
    public static String DT_S_MRIR_DATE = "DT_S_MRIR_DATE";
    public static String NU_QUANTITY = "NU_QUANTITY";
    public static String VC_S_ROUTE_NO = "VC_S_ROUTE_NO";
    public static String VC_MRIR_NO = "VC_MRIR_NO";
    public static String VC_S_WIRE_DIA = "VC_S_WIRE_DIA";
    public static String VC_CHALLAN_NO = "VC_CHALLAN_NO";
    public static String DT_INVOICE_DATE = "DT_INVOICE_DATE";
    public static String L_WAREHOUSE_COMP_CODE = "L_WAREHOUSE_COMP_CODE";
    public static String VC_S_HEAT_NO = "VC_S_HEAT_NO";
    public static String NU_PARTY_CODE = "NU_PARTY_CODE";
    public static String NU_S_DIV_QTY = "NU_S_DIV_QTY";
    public static String VC_S_DIVISION = "VC_S_DIVISION";
    public static String DT_MRIR_DATE = "DT_MRIR_DATE";
    public static String VC_S_MRIR_NO = "VC_S_MRIR_NO";
    public static String VC_S_COMP_CODE = "VC_S_COMP_CODE";
    public static String VC_S_COIL_NO = "VC_S_COIL_NO";
    public static String LOAD_STATUS = "LOAD_STATUS";
    public static String UNLOAD_STATUS = "UNLOAD_STATUS";
    public static String VC_TAG_NO = "Vc_TAG_NO";
    public static String DT_S_ROUTE_DATE = "DT_S_ROUTE_DATE";
    public static String DT_ORDER_DATE = "DT_ORDER_DATE";
    public static String VC_S_GRADE_NO = "VC_S_GRADE_NO";
    public static String VC_S_ITEM_CODE = "VC_S_ITEM_CODE";
    public static String DT_GATE_ENT_DATE = "DT_GATE_ENT_DATE";
    public static String ITEM_LOC_LIST = "ITEM_LOC_LIST";
    public static String VC_LOCATION_NAME = "VC_LOCATION_NAME";
    public static String VC_LOCATION_CODE = "VC_LOCATION_CODE";
    public static String VC_MATCH_UNM = "VC_MATCH_UNM";
    public static String VC_LOCATION_DESC = "VC_LOCATION_DESC";
    public static String NU_LOCATION_CODE = "NU_LOCATION_CODE";

    /* RM ISSUE REQ RES KEY*/
    public static String VC_MACHINE_NO = "VC_MACHINE_NO";
    public static String VC_MACHINE_CODE = "VC_MACHINE_CODE";
    public static String VC_ROUTE_NO = "VC_ROUTE_NO";
    public static String VC_MRS_NO = "VC_MRS_NO";
    public static String DT_MRS_DATE = "DT_MRS_DATE";
    public static String DT_MRN_DATE = "DT_MRN_DATE";
    public static String DT_DESP_DATE = "DT_DESP_DATE";
    public static String VC_DESP_NO = "VC_DESP_NO";
    public static String VC_LOT_NO = "VC_LOT_NO";
    public static String NU_QTY_ISSUED = "NU_QTY_ISSUED";
    public static String VC_AUTH_CODE = "VC_AUTH_CODE";
    public static String VC_MRN_NO = "VC_MRN_NO";
    public static String VC_HEAT_NO = "VC_HEAT_NO";
    public static String VC_COIL_NO = "VC_COIL_NO";
    public static String DT_ROUTE_DATE = "DT_ROUTE_DATE";
    public static String VC_GRADE_NO = "VC_GRADE_NO";
    public static String VC_WIRE_DIA = "VC_WIRE_DIA";
    public static String DT_PROCESS_DATE = "DT_PROCESS_DATE";
    public static String VC_LOCATION = "VC_LOCATION";
    public static String VC_MACHINE_NAME = "VC_MACHINE_NAME";
    public static String VC_DIVISION = "VC_DIVISION";
    public static String ROUTE_CARD_NO = "ROUTE_CARD_NO";
    public static String ROUTE_CARD_DATE = "ROUTE_CARD_DATE";
    public static String MACHINE_CODE = "MACHINE_CODE";
    public static String P_STR = "p_STR";
    public static String R_STR = "r_STR";
    public static String M_STR = "m_STR";
    public static String INVOICE_STR = "INVOICE_STR";
    public static String PLANT_INVOICE_QR_CODE = "Plant_Invoice_Code";
    public static String WAREHOUSE_SCAN_INWARD = "invoice_string";
    public static String WAREHOUSE_SCAN_INWARD_TRUCK_DRIVER= "truck_driver";
    public static String WAREHOUSE_SCAN_INWARD_TRUCK_NUMBER = "truck_number";

    public static String PRODUCT_STR = "PRODUCT_STR";
    public static String PRODUCT_COUNT = "PRODUCT_COUNT";
    public static String FLAG = "FLAG";
    public static String CUSTOMER_NAME_INWARDS = "customerName";

}
