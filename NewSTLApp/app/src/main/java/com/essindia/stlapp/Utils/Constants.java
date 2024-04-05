package com.essindia.stlapp.Utils;


public class Constants {


//    public static final String BASE_URL = "http://192.168.5.87:7101/mobapp_wms_t/";  // Jonu System ESS(Local Server)
//    public static final String BASE_URL = "http://192.168.5.210:7101/mobapp_wms_t/";  // Jonu System ESS(Local Server)
//    public static final String BASE_URL = "http://192.168.5.210:7101/mobapp_in_t/";  // Jonu System ESS(Local Server)
//    public static final String BASE_URL = "http://132.132.0.193:7001/";  // STL DLF SERVER System Ip (admin:7001 Otherwise 7003)
//    public static final String BASE_URL = "http://132.132.5.215:7003/mobapp/";  // STL DLF LIVE SERVER System Ip
//    public static final String BASE_URL = "http://132.132.5.215:7003/mobapp_in_t/";  // STL INVENTORY TEST SERVER
//    public static final String BASE_URL = "http://132.132.5.215:7003/mobapp_in_l/";  // STL INVENTORY LIVE SERVER
    // public static final String BASE_URL = "http://132.132.5.215:7003/mobapp_wms_t/";  //  STL WMS TEST SERVER

//    public static final String BASE_URL = "http://132.132.5.215:7101/mobapp_in_t/";  // STL INVENTORY TEST JDEV SERVER
//    public static final String BASE_URL = "http://132.132.5.215:7101/mobapp_in_l/";  // STL INVENTORY LIVE JDEV SERVER
//    public static final String BASE_URL = "http://132.132.5.215:7101/mobapp_wms_t/";  //  STL WMS TEST JDEV SERVER
//    public static final String BASE_URL = "http://132.132.5.215:7101/mobapp_wms_l/";  //  STL WMS TEST JDEV SERVER

    //public static final String BASE_URL = "http://10.10.2.140:7101/mobapp_in_t/";  //  STL WMS TEST JDEV SERVER
    //public static final String BASE_URL = "http://10.10.2.140:7101/mobapp_wms_l/";  //  STL WMS TEST JDEV SERVER

//    public static final String BASE_URL = BuildConfig.host;  //  Build file URL


    //    public static   String BASE_URL =  Constants.getInstance().SERVER_URL;  //  Build file URL
//    public static String BASE_URL=LoginActivity.BASE_URL_1;
    public static String BASE_URL = "";

    public static String LOGIN_POST = BASE_URL + "resources/mobileapp/login";
    public static String LOGOUT_POST = BASE_URL + "resources/mobileapp/logout";
    public static String GRN_VERIFICATION_LIST = BASE_URL + "resources/mobileapp/getGrnVerification";
    public static String GRN_VERIFICATION_LOG_POST = BASE_URL + "resources/mobileapp/postVerificationLog";
    public static String PUT_AWAY_SUMMARY_DATA = BASE_URL + "resources/mobileapp/postPutAwaySummary";
    public static String PUT_AWAY_DOCK_LOCATION = BASE_URL + "resources/mobileapp/postPutAwayBinQty";
    public static String POST_PUT_AWAY_LOG = BASE_URL + "resources/mobileapp/postPutAwayLog";
    public static String POST_DOCK_LOCATION = BASE_URL + "resources/mobileapp/dock_Location";
    public static String POST_SYNC_DATA = BASE_URL + "resources/mobileapp/postSyncData";
    public static String POST_PUT_AWAY_SUGGESTED_AREA = BASE_URL + "resources/mobileapp/postPutAwayProcess";
    public static String LOADING_SUMMARY_LIST = BASE_URL + "resources/mobileapp/postAllLoadingAdviceSummary";
    public static String LOADING_POST_BOX_DATA = BASE_URL + "resources/mobileapp/postloadingAdviceDetails";
    public static String POST_PICKUP_SUMMARY = BASE_URL + "resources/mobileapp/postPickUpSummary";
    public static String POST_PICKUP_RESERVE_SUMMARY = BASE_URL + "resources/mobileapp/postPickUpReservQty";
    public static String POST_PICKUP_DETAILS = BASE_URL + "resources/mobileapp/postPickUpDetails";
    public static String POST_PICKUP_REASON = BASE_URL + "resources/mobileapp/postPickUpGetReason";
    public static String STOP_SHAKE_ICON_URL = BASE_URL + "resources/mobileapp/postNotification";
    public static String RM_RECEIPT_TRAN_NO_LIST = BASE_URL + "resources/mobileapp/getRmTranNoList";
    public static String RM_RECEIPT_TRAN_DETAIL_LIST = BASE_URL + "resources/mobileapp/getRmTranDetailList";
    public static String RM_RECEIPT_TRAN_DETAIL_LIST_SUBMIT = BASE_URL + "resources/mobileapp/postRmReceiptItemList";
    public static String RM_ISSUE_MRS_NO_LIST = BASE_URL + "resources/mobileapp/getRmIssueMrsNoList";
    public static String RM_ISSUE_MRS_NO_ITEM_LIST = BASE_URL + "resources/mobileapp/getRmIssueMrsNoItemList";
    public static String RM_ISSUE_MRS_SUBMIT = BASE_URL + "resources/mobileapp/postRmIssueItemList";
    public static String RM_COIL_ISSUE_TRAN_NO_LIST = BASE_URL + "resources/mobileapp/getRmCoilIssueTranNoList";
    public static String RM_COIL_ISSUE_TRAN_DETAIL_LIST = BASE_URL + "resources/mobileapp/getRmCoilIssueTranDetailList";
    public static String RM_COIL_ISSUE_UPDATESCANNED_ITEM = BASE_URL + "resources/mobileapp/coilIssueUpdateScannedItem";
    public static String RM_COIL_ISSUE_SUBMIT_ITEM_LIST = BASE_URL + "resources/mobileapp/coilIssueSubmitItemList";
    public static String RM_COIL_RECEIVE_TRAN_NO_LIST = BASE_URL + "resources/mobileapp/getRmCoilReceiveTranNoList";
    public static String RM_COIL_RECEIVE_TRAN_DETAIL_LIST = BASE_URL + "resources/mobileapp/getRmCoilReceiveTranDetailList";
    public static String RM_COIL_RECEIVE_UPDATESCANNED_ITEM = BASE_URL + "resources/mobileapp/coilReceiveUpdateScannedItem";
    public static String RM_COIL_RECEIVE_SUBMIT_ITEM_LIST = BASE_URL + "resources/mobileapp/coilReceiveSubmitItemList";

    public static String LOADING_SHOP_FLOOR_TRAN_NO_LIST = BASE_URL + "resources/mobileapp/getLoadingShopFloorTranNoList";
    public static String LOADING_SHOP_FLOOR_TRAN_DETAIL_LIST = BASE_URL + "resources/mobileapp/getLoadingShopFloorTranDetailList";

    public static String LOADING_SHOP_FLOOR_UPDATESCANNED_ITEM = BASE_URL + "resources/mobileapp/loadingShopFloorUpdateScannedItem";
    public static String LOADING_SHOP_FLOOR_SUBMIT_ITEM_LIST = BASE_URL + "resources/mobileapp/loadingShopFloorSubmitItemList";

    public static String SUBMIT_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST = BASE_URL + "resources/mobileapp/submitUnLoadingShopFloorTranCoilList";
    public static String VERIFY_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST = BASE_URL + "resources/mobileapp/verifyUnLoadingShopFloorTranCoilList";
    public static String UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST = BASE_URL + "resources/mobileapp/getUnLoadingShopFloorTranCoilList";
    public static String UNLOADING_SHOP_FLOOR_TRAN_NO_LIST = BASE_URL + "resources/mobileapp/getUnLoadingShopFloorTranNoList";
    public static String UNLOADING_SHOP_FLOOR_TRAN_DETAIL_LIST = BASE_URL + "resources/mobileapp/getUnLoadingShopFloorTranDetailList";

    public static String UNLOADING_SHOP_FLOOR_UPDATESCANNED_ITEM = BASE_URL + "resources/mobileapp/unloadingShopFloorUpdateScannedItem";
    public static String UNLOADING_SHOP_FLOOR_SUBMIT_ITEM_LIST = BASE_URL + "resources/mobileapp/unloadingShopFloorSubmitItemList";

    public static String VERIFY_ROUTE_CARD_PROCESS_LIST = BASE_URL + "resources/mobileapp/verifyRouteCardProcessList";
    public static String ROUTE_CARD_PROCESS_LIST = BASE_URL + "resources/mobileapp/getRouteCardProcessList";
    public static String SUBMIT_ROUTE_CARD_PROCESS_LIST_ITEM = BASE_URL + "resources/mobileapp/submitRouteCardProcessListItem";


    /*PickUp After Changes*/
    public static String GET_PICKUP_SUMMARY = BASE_URL + "resources/mobileapp/getPickUpSummary";
    public static String GET_PICKUP_LOCATION_VALIDATE = BASE_URL + "resources/mobileapp/getPickUpLocationsValidate";
    public static String GET_PICKUP_LOCATIONS_ITEM = BASE_URL + "resources/mobileapp/getPickUpLocationsItem";
    public static String GET_PICKUP_ITEM_CODE_VALIDATE = BASE_URL + "resources/mobileapp/getPickUpItemQrCodeValidate";
    public static String GET_WAREHOUSE_LOADING_INVOICE_API = BASE_URL + "resources/mobileapp/getWarehouseLoadingInvoice";
    public static String GET_WAREHOUSE_LOADING_INVOICE_PRODUCT_API = BASE_URL + "resources/mobileapp/getWarehouseLoadingInvoiceProduct";
    public static String SUBMIT_WAREHOUSE_LOADING_INVOICE_PRODUCT_API = BASE_URL + "resources/mobileapp/submitWarehouseLoadingInvoiceProduct";

    public static String LOADING_ADVICE_MRS_LIST = BASE_URL + "resources/mobileapp/getLoadingAdviceMrsList";
    public static String LOADING_ADVICE_MRS_COIL_LIST = BASE_URL + "resources/mobileapp/getLoadingAdviceMrsCoilList";

    public static String VERIFY_LOADING_ADVICE_MRS_COIL = BASE_URL + "resources/mobileapp/postVerifyLoadingAdviceMRSCoil";
    public static String SUBMIT_LOADING_ADVICE_MRS_COIL = BASE_URL + "resources/mobileapp/postSubmitLoadingAdviceMRSCoil";
    public static String CHECK_WH_QR_CODE_INVOICE_STRING_KANBAN = BASE_URL + "resources/mobileapp/checkwhqrcode"; //Anil
    public static String CHECK_SCANNED_MATCH = BASE_URL + "resources/mobileapp/checkInsertdata"; //Anil
    public static String WAREHOUSE_SCANNING_INWARDS = BASE_URL + "resources/mobileapp/warehouseScanningInwards"; //Anil
    public static String WAREHOUSE_SCANNING_INWARDS_PRODUCT = BASE_URL + "resources/mobileapp/getWarehouseInwardProductScanning"; //Anil
    public static String SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT = BASE_URL + "resources/mobileapp/submitWarehouseInwardsInvoiceProduct"; //Anil
    public static String SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_FINAL_SUBMIT = BASE_URL + "resources/mobileapp/finalSubmit"; //Anil
    public static String SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_PENDING_SUPERVISOR = BASE_URL + "resources/mobileapp/pendingBox"; //Anil
    public static String SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_PENDING_SUPERVISOR_THIRD_PAGE = BASE_URL + "resources/mobileapp/supervisorPending"; //Anil


    public static String USER_PREF = "user_pref";
    public static String auth_Token = "auth_Token";
    public static String ACTUAL_QTY = "actual_Qty";
    public static String PICK_QR_CODE = "pick_qr_code";
    public static String USER_DEVICE_ID = "device_id";
    public static String AUTO_LOGIN = "auto_login";
    public static String KEY_ITEM = "item";
    public static String RESPONSE_DATA = "response_data";
    public static String USERNAME = "username";
    public static String BASE_URL_PREF = "base_url";
    public static String SUGGESTED_AREA = "suggested_area";
    public static String DELIMITER = "<>";
    public static String NEW_DELIMITER = ",";


    // API Request Id
    public static final int GET_PUT_AWAY_SUMMARY_DATA_API_REQ_ID = 4;
    public static final int PUT_AWAY_BIN_LOCATION_DATA_API_REQ_ID = 5;
    public static final int POST_PUT_AWAY_LOG_DATA_API_REQ_ID = 6;
    public static final int POST_DOCK_LOCATION_REQ_ID = 7;
    public static final int PUT_AWAY_POST_SUGGESTED_AREA_REQ_ID = 8;
    public static final int DASHBOARD_SYNC_DATA_REQ_ID = 9;
    public static final int LOADING_SUMMARY_LIST_REQ_ID = 10;
    public static final int PICK_UP_SUMMARY_REQ_ID = 11;
    public static final int LOADING_POST_BOX_REQ_ID = 12;
    public static final int PICK_UP_SUMMARY_RESERVE_ID = 13;
    public static final int PICK_UP_DETAIL_POST_RESERVE_ID = 14;
    public static final int PICK_UP_DETAIL_REASON_ID = 15;
    public static final int LOADING_SUMMARY_LIST_BY_SCAN_REQ_ID = 16;
    public static final int DASHBOARD_IS_SHAKE_REFRESH_BTN_REQ_ID = 17;
    public static final int DASHBOARD_STOP_REFRESH_BTN_REQ_ID = 18;
    public static final int RM_RECEIPT_LIST_REQ_ID = 19;
    public static final int RM_RECEIPT_LIST_DETAIL_REQ_ID = 20;
    public static final int RM_RECEIPT_LIST_SUBMIT_REQ_ID = 21;
    public static final int RM_ISSUE_MRS_NO_LIST_REQ_ID = 22;
    public static final int RM_ISSUE_MRS_NO_DETAIL_LIST_REQ_ID = 23;
    public static final int RM_ISSUE_LIST_SUBMIT_REQ_ID = 24;
    public static final int PICK_UP_VALIDATE_LOCATION_REQ_ID = 25;
    public static final int PICK_UP_LOCATION_ITEM_REQ_ID = 26;
    public static final int PICK_UP_ITEM_CODE_VALIDATE_REQ_ID = 27;
    public static final int RM_COIL_ISSUE_LIST_REQ_ID = 28;
    public static final int RM_COIL_ISSUE_LIST_DETAIL_REQ_ID = 29;
    public static final int RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID = 30;
    public static final int RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID = 31;
    public static final int RM_COIL_RECEIVE_LIST_REQ_ID = 28;
    public static final int RM_COIL_RECEIVE_LIST_DETAIL_REQ_ID = 29;
    public static final int RM_COIL_RECEIVE_SCAANED_ITEM_UPDATE_REQ_ID = 30;
    public static final int RM_COIL_RECEIVE_LIST_SUBMIT_REQ_ID = 31;
    public static final int GET_WH_INVOICE_REQ_ID = 32;
    public static final int GET_WH_INVOICE_PRODUCT_REQ_ID = 33;
    public static final int SUBMIT_WH_INVOICE_PRODUCT_REQ_ID = 34;

    public static final int LOADING_ADVICE_MRS_REQ_ID = 35;
    public static final int LOADING_ADVICE_MRS_COIL_REQ_ID = 36;
    public static final int LOADING_ADVICE_MRS_COIL_VERIFY = 37;
    public static final int LOADING_ADVICE_MRS_COIL_SUBMIT = 38;
    //    StartActivityForResult Req Id
    public static final int RM_RECEIPT_ITEM_MATCH_RESULT_REQ_ID = 1;
    public static final int RM_RECEIPT_ITEM_MISMATCH_RESULT_REQ_ID = 2;
    public static final int RM_ISSUE_ITEM_MATCH_RESULT_REQ_ID = 3;

    //    StartActivityForResult Req Id
    public static final int RM_COIL_ISSUE_LIST_SUBMIT_RESULT_REQ_ID = 4;
    public static final int SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING = 111;
    public static final int SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING_FINAL_SUBMIT= 112;
    public static final int GET_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING = 110;

    //API paramter key
    public static final String ORG_ID = "ORG_ID";
    public static final String BIN_DESC = "BIN_DESC";
    public static final String PACK_SIZE = "PACK_SIZE";
    public static final String ITEM_CODE = "ITEM_CODE";
    public static final String BOX_TYPE = "BOX_TYPE";
    //Common QR code paramter
    public static String QR_PROCESS_CODE = "processCode";  //prcoess code
    public static String QR_STL_PART_NO = "stlPartNo";     // item code
    public static String QR_ROUTE_CARD_NO = "routCardNo";   //GRN no
    public static String QR_STD_QTY = "stdQty";             //packsize||packqty
    public static String QR_ACTUAL_QTY = "actualQty";       //actual qty
    public static String QR_REFERENCE_NO = "referenceNo";   // IR no
    public static String QR_BOX_TYPE = "boxType";           // pack type
    //    public static String QR_ROUTE_DATE = "routeDate";       //route date
    public static String QR_TIME = "time";                  // time

    public static String STR1 = "str1";
    public static String STR2 = "str2";
    public static int STATUS_SCANNED = 0;
    public static String L_WAREHOUSE_COMP_CODE_VALUE = "com_code";
    public static String INWARDS_SCANNING_INVOICE_DATE = "date";
    public static String INWARDS_SCANNING_GATE_PASS = "getepass";
    public static String INWARDS_SCANNING_INVOICE_INVOICE_GATE_PASS = "gate_pass";
    public static String INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME = null;
    public static String INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER = null;

    public static String INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_SCANED = "total_box";
    public static String INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_ALREADY_SCANED = "already_scan";
//    public static String[] action_value_buf = new String[]{ScanManager.ACTION_DECODE, ScanManager.BARCODE_STRING_TAG};

}
