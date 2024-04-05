package com.essindia.stlapp.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.ReceiptTranDetailListAdapter;
import com.essindia.stlapp.Bean.ReceiptTranItemDetailBean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivingItemListActivity extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, View.OnClickListener {
    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private EditText mEtItemQrcode;
    private ImageView mIvVerify;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranDetailList;
    private LinearLayoutManager layoutManager;
    private ReceiptTranDetailListAdapter adapter;
    private ArrayList<ReceiptTranItemDetailBean> mTranDetailList;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private int mScanningItemIndex = 0;
    private int mTotalScannedItem = 0;
    private int mTotalScanableItem = 0;
    private String mTranNo = "";
    private String mTranDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_detail);
        intializeXml();
        getRmReceiptItemListData();
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mEtItemQrcode = (EditText) findViewById(R.id.et_item_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        mIvVerify.setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mRvTranDetailList = (RecyclerView) findViewById(R.id.rv_tran_detail);
        toolbar.setTitle(R.string.receiving_item_list);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);
        mUserPref = new UserPref(this);
        mConnectionDetector = new ConnectionDetector(this);
    }

    private void getRmReceiptItemListData() {
        Bundle b = getIntent().getExtras();
        mTranNo = b.getString(BundleKey.RM_RECEIPT_TRAN_NO);
        mTranDate = b.getString(BundleKey.RM_RECEIPT_TRAN_DATE);
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.TOKEN, mUserPref.getToken());
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
            param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.VC_TRAN_NO, mTranNo);
            param.put(ReqResParamKey.DT_TRAN_DATE, mTranDate);
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(ReceivingItemListActivity.this, Constants.RM_RECEIPT_TRAN_DETAIL_LIST, param.toString(), ReceivingItemListActivity.this, Constants.RM_RECEIPT_LIST_DETAIL_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(ReceivingItemListActivity.this, mUserPref.getBASEUrl()+Constants.RM_RECEIPT_TRAN_DETAIL_LIST, param.toString(), ReceivingItemListActivity.this, Constants.RM_RECEIPT_LIST_DETAIL_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AidcManager.create(this, new AidcManager.CreatedCallback() {
                @Override
                public void onCreated(AidcManager aidcManager) {
                    manager = aidcManager;
                    try {
                        bcr = manager.createBarcodeReader();
                        bcr.claim();
                        barcodeScanner();
                    } catch (InvalidScannerNameException e) {
                        showToast("Invalid Scanner Name Exception: " + e.getMessage());
                    } catch (ScannerUnavailableException e) {
                        showToast("Scanner unavailable" + e.getMessage());
                    } catch (Exception e) {
                        showToast("Exception: " + e.getMessage());
                    }
                }
            });
        } catch (IllegalArgumentException exp) {
            showToast("Your device is not compatible with QR Code scanner");
            Log.e("Loading scanner exp", exp.toString());
        } catch (Exception exp) {
            showToast("Your device is not compatible with QR Code scanner");
            Log.e("Loading scanner exp1", exp.toString());
        }
    }

    private void barcodeScanner() {
        try {
            if (bcr != null) {
                bcr.addBarcodeListener(this);
                bcr.addTriggerListener(this);
            } else {
                System.out.println("Device is not QR enabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (bcr != null) {
                bcr.release();
            }
        } catch (NullPointerException npe) {
            npe.getStackTrace();
        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTotalScanableItem > 0) {
                    if (mTotalScannedItem < mTotalScanableItem) {
                        spliteData(barcodeReadEvent.getBarcodeData().toString().trim());
                    } else {
                        showToast("All item scanned, Total scanned item is equal to total assigned item.");
                    }
                } else {
                    showToast("No item for scanning");
                }
            }
        });

    }

    private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String mrirNo, mrirDate, routeCardNo, routeCardDate, coilNo, div, heatNo, coilDivWeight, grade, wireDia, itemCode, processDate;
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", "Qr code is empty cannot load data item");
        }
        /*else if (dataSp.length < 13 || dataSp.length > 13) {
            AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", "Please enter correct format !");
        }
        else if (dataSp.length == 13)*/
        else {
            mrirNo = dataSp[0].trim().toUpperCase();
            mrirDate = dataSp[1].trim().toUpperCase();
            routeCardNo = dataSp[2].trim().toUpperCase();
            routeCardDate = dataSp[3].trim().toUpperCase();
            coilNo = dataSp[4].trim().toUpperCase();
            div = dataSp[5].trim().toUpperCase();
            heatNo = dataSp[6].trim().toUpperCase();
            coilDivWeight = dataSp[7].trim().toUpperCase();
            grade = dataSp[8].trim().toUpperCase();
            wireDia = dataSp[9].trim().toUpperCase();
            itemCode = dataSp[10].trim().toUpperCase();
            processDate = dataSp[11].trim().toUpperCase();
            String itemUniqueId = mrirNo + mrirDate + routeCardNo + routeCardDate + coilNo + div + heatNo + coilDivWeight + grade + wireDia + itemCode /*processDate*/;
            if (mTranDetailList != null && mTranDetailList.size() > 0) {
                boolean isItemMatch = false;
                for (int i = 0; i < mTranDetailList.size(); i++) {
                    ReceiptTranItemDetailBean rmReceiptTranItemDetailBean = mTranDetailList.get(i);
                    String beanMrirNo, beanMrirDate, beanRouteCardNo, beanRouteCardDate, beanCoilNoNDiv, beanHeatNo, beanCoilDivWeight, beanGrade, beanWireDia, beanItemCode, beanProcessDate;
                    beanMrirNo = rmReceiptTranItemDetailBean.getVcSMrirNo();
                    beanMrirDate = rmReceiptTranItemDetailBean.getDtSMrirDate();
                    beanRouteCardNo = rmReceiptTranItemDetailBean.getVcSRouteNo();
                    beanRouteCardDate = rmReceiptTranItemDetailBean.getDtSRouteDate();
                    String vcSDivision = rmReceiptTranItemDetailBean.getVcSDivision();
                    if (vcSDivision != null && !vcSDivision.isEmpty() && !vcSDivision.equalsIgnoreCase("null")) {
                        beanCoilNoNDiv = rmReceiptTranItemDetailBean.getVcSCoilNo() + vcSDivision;
                    } else {
                        beanCoilNoNDiv = rmReceiptTranItemDetailBean.getVcSCoilNo();
                    }
//                    beanCoilNoNDiv = rmReceiptTranItemDetailBean.getVcSCoilNo() + " " + rmReceiptTranItemDetailBean.getVcSDivision();
                    beanHeatNo = rmReceiptTranItemDetailBean.getVcSHeatNo();
                    beanCoilDivWeight = rmReceiptTranItemDetailBean.getNuSDivQty();
                    beanGrade = rmReceiptTranItemDetailBean.getVcSGradeNo();
                    beanWireDia = rmReceiptTranItemDetailBean.getVcSWireDia();
                    beanItemCode = rmReceiptTranItemDetailBean.getVcSItemCode();
//                    beanProcessDate = rmReceiptTranItemDetailBean.getDtSRouteDate();//Subroj

                    String beanItemUniqueId = beanMrirNo + beanMrirDate + beanRouteCardNo + beanRouteCardDate + beanCoilNoNDiv + beanHeatNo + beanCoilDivWeight + beanGrade + beanWireDia + beanItemCode /*beanProcessDate*/;
//                    if (mrirNo.equalsIgnoreCase(rmReceiptTranItemDetailBean.getVcSMrirNo())) {
                    if (itemUniqueId.equalsIgnoreCase(beanItemUniqueId)) {
                        isItemMatch = true;
                        if (!rmReceiptTranItemDetailBean.isItemScanned()) {
                            mScanningItemIndex = i;
                            Intent in = new Intent(ReceivingItemListActivity.this, ReceivingItemDetailActivity.class);
                            in.putExtra(BundleKey.RM_RECEIPT_ITEM_DETAIL, rmReceiptTranItemDetailBean);
                            startActivityForResult(in, Constants.RM_RECEIPT_ITEM_MATCH_RESULT_REQ_ID);
                            break;
                        } else {
                            mEtItemQrcode.setText("");
                            showToast("This item already scanned");
                            break;
                        }
                    }
                }
                if (!isItemMatch) {
                    showToast("Item Not Matched");
                    ReceiptTranItemDetailBean scanItemBean = new ReceiptTranItemDetailBean();
                    scanItemBean.setVcSMrirNo(mrirNo);
                    scanItemBean.setDtSMrirDate(mrirDate);
                    scanItemBean.setVcSRouteNo(routeCardNo);
                    scanItemBean.setDtSRouteDate(routeCardDate);
                    scanItemBean.setVcSCoilNo(coilNo);
                    scanItemBean.setVcSDivision(div);
                    scanItemBean.setVcSHeatNo(heatNo);
                    scanItemBean.setNuSDivQty(coilDivWeight);
                    scanItemBean.setVcSGradeNo(grade);
                    scanItemBean.setVcSWireDia(wireDia);
                    scanItemBean.setVcSItemCode(itemCode);
                    scanItemBean.setVcSCompCode(mUserPref.getOrgId());
//                    scanItemBean.setDtSRouteDate(processDate);
                    mismatchItemProceedConfirmAlert("Alert!\nItem Not Matched", "Do You Want To Proceed?", scanItemBean);
                }
            } else {
                showToast("No Item For Scanning");
            }
        }
    }

    @Override
    public void onBackPressed() {
        confirmAlert("Record is not submit!", "Do you want to go back without submit the record");
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (response != null) {
            try {
                JSONObject result = new JSONObject(response);
                System.out.println("response:" + response);
                String statusCode = result.getString("MessageCode");
                String message = result.getString("Message");
                if (request_id == Constants.RM_RECEIPT_LIST_DETAIL_REQ_ID) {
                    if (statusCode.equalsIgnoreCase("0")) {
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            mTranDetailList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                ReceiptTranItemDetailBean bean = new ReceiptTranItemDetailBean();
                                JSONObject obj = array.optJSONObject(i);
                                bean.setVcOrderNo(obj.optString(ReqResParamKey.VC_ORDER_NO));
                                bean.setVcGateEntNo(obj.optString(ReqResParamKey.VC_GATE_ENT_NO));
                                bean.setVcInvoiceNo(obj.optString(ReqResParamKey.VC_INVOICE_NO));
                                bean.setVcItemCode(obj.optString(ReqResParamKey.VC_ITEM_CODE));
                                bean.setDtChallanDate(obj.optString(ReqResParamKey.DT_CHALLAN_DATE));
                                bean.setDtSMrirDate(obj.optString(ReqResParamKey.DT_S_MRIR_DATE));
                                bean.setNuQuantity(obj.optString(ReqResParamKey.NU_QUANTITY));
                                bean.setVcSRouteNo(obj.optString(ReqResParamKey.VC_S_ROUTE_NO));
                                bean.setVcMrirNo(obj.optString(ReqResParamKey.VC_MRIR_NO));
                                bean.setVcSWireDia(obj.optString(ReqResParamKey.VC_S_WIRE_DIA));
                                bean.setVcChallanNo(obj.optString(ReqResParamKey.VC_CHALLAN_NO));
                                bean.setDtInvoiceDate(obj.optString(ReqResParamKey.DT_INVOICE_DATE));
                                bean.setVcSHeatNo(obj.optString(ReqResParamKey.VC_S_HEAT_NO));
                                bean.setNuPartyCode(obj.optString(ReqResParamKey.NU_PARTY_CODE));
                                bean.setNuSDivQty(obj.optString(ReqResParamKey.NU_S_DIV_QTY));
                                bean.setVcSDivision(obj.optString(ReqResParamKey.VC_S_DIVISION));
                                bean.setDtMrirDate(obj.optString(ReqResParamKey.DT_MRIR_DATE));
                                bean.setVcSMrirNo(obj.optString(ReqResParamKey.VC_S_MRIR_NO));
                                bean.setVcSCompCode(obj.optString(ReqResParamKey.VC_COMP_CODE));
                                bean.setVcUserCode(obj.optString(ReqResParamKey.VC_USER_CODE));
                                bean.setVcAuthCode(obj.optString(ReqResParamKey.VC_AUTH_CODE));
                                bean.setVcSCoilNo(obj.optString(ReqResParamKey.VC_S_COIL_NO));
                                bean.setDtSRouteDate(obj.optString(ReqResParamKey.DT_S_ROUTE_DATE));
                                bean.setDtOrderDate(obj.optString(ReqResParamKey.DT_ORDER_DATE));
                                bean.setVcSGradeNo(obj.optString(ReqResParamKey.VC_S_GRADE_NO));
                                bean.setVcSItemCode(obj.optString(ReqResParamKey.VC_S_ITEM_CODE));
                                bean.setDtGateEntDate(obj.optString(ReqResParamKey.DT_GATE_ENT_DATE));
                               /* JSONArray itemLocArray = obj.optJSONArray(ReqResParamKey.ITEM_LOC_LIST);
                                ArrayList<ReceiptItemLocBean> locList = new ArrayList<>();
                                if (itemLocArray != null && itemLocArray.length() > 0) {
                                    for (int j = 0; j < itemLocArray.length(); j++) {
                                        JSONObject jObjItemLoc = itemLocArray.optJSONObject(j);
                                        ReceiptItemLocBean itemLocBean = new ReceiptItemLocBean();
                                        itemLocBean.setVcLocationDesc(jObjItemLoc.optString(ReqResParamKey.VC_LOCATION_DESC));
                                        itemLocBean.setNuLocationCode(jObjItemLoc.optString(ReqResParamKey.NU_LOCATION_CODE));
                                        locList.add(itemLocBean);
                                    }
                                }
                                bean.setItemLocBeen(locList);*/
                                mTranDetailList.add(bean);
                            }
                            mTotalScanableItem = mTranDetailList.size();
                            adapter = new ReceiptTranDetailListAdapter(getApplicationContext(), mTranDetailList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranDetailList.setLayoutManager(layoutManager);
                            mRvTranDetailList.setAdapter(adapter);
                        } else {
                            mTranDetailList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");
                        }
                    } else {
                        showToast(message);
//                    AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", message);
                        goBackTranListScreen();
                    }
                } else if (request_id == Constants.RM_RECEIPT_LIST_SUBMIT_REQ_ID) {
                    if (statusCode.equalsIgnoreCase("0")) {
                        showPostSuccessAlert("Success", message);
                        showLongToast(message);
                    } else {
                        showLongToast(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", "Server not responding.");
        }
    }

    private void goBackTranListScreen() {
        Intent i = new Intent(ReceivingItemListActivity.this, ReceivingListActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void showPostSuccessAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goBackTranListScreen();
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();
    }

    private void mismatchItemProceedConfirmAlert(String title, String msg, final ReceiptTranItemDetailBean pBean) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScanningItemIndex = -1;
                Intent in = new Intent(ReceivingItemListActivity.this, ReceivingItemDetailActivity.class);
                in.putExtra(BundleKey.RM_RECEIPT_ITEM_DETAIL, pBean);
                startActivityForResult(in, Constants.RM_RECEIPT_ITEM_MISMATCH_RESULT_REQ_ID);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();
    }

    private void confirmAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                callItemSubmitApi();
                Intent rmReceivingListIntent = new Intent(ReceivingItemListActivity.this, ReceivingListActivity.class);
                startActivity(rmReceivingListIntent);
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_verify:
                String itemQrCode = mEtItemQrcode.getText().toString();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    spliteData(mEtItemQrcode.getText().toString());
                } else showToast("Please Enter Item QR Code");
                break;
            case R.id.btn_submit:
                if (mTranDetailList != null && mTranDetailList.size() > 0) {
                    if (mTotalScannedItem == mTotalScanableItem) {
                        callItemSubmitApi();
                    } else {
                        showToast("Please scan all item.");
                    }
                } else {
                    showToast("No item for submit");
                }
//                confirmAlert("All Record is not submit!", "Do you want to proceed without submit the record");
                break;
        }
    }

    private void callItemSubmitApi() {
        JSONArray itemArray = new JSONArray();
        try {
            for (ReceiptTranItemDetailBean bean : mTranDetailList) {
                JSONObject param = new JSONObject();
                param.put(ReqResParamKey.TOKEN, mUserPref.getToken());
                param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
                param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
                param.put(ReqResParamKey.VC_ORDER_NO, bean.getVcOrderNo());
                param.put(ReqResParamKey.VC_GATE_ENT_NO, bean.getVcGateEntNo());
                param.put(ReqResParamKey.VC_INVOICE_NO, bean.getVcInvoiceNo());
                param.put(ReqResParamKey.VC_ITEM_CODE, bean.getVcItemCode());
                param.put(ReqResParamKey.DT_CHALLAN_DATE, bean.getDtChallanDate());
                param.put(ReqResParamKey.DT_S_MRIR_DATE, bean.getDtSMrirDate());
                param.put(ReqResParamKey.NU_QUANTITY, bean.getNuQuantity());
                param.put(ReqResParamKey.VC_S_ROUTE_NO, bean.getVcSRouteNo());
                param.put(ReqResParamKey.VC_MRIR_NO, bean.getVcMrirNo());
                param.put(ReqResParamKey.VC_S_WIRE_DIA, bean.getVcSWireDia());
                param.put(ReqResParamKey.VC_S_ITEM_CODE, bean.getVcSItemCode());
                param.put(ReqResParamKey.VC_CHALLAN_NO, bean.getVcChallanNo());
                param.put(ReqResParamKey.DT_INVOICE_DATE, bean.getDtInvoiceDate());
                param.put(ReqResParamKey.VC_S_HEAT_NO, bean.getVcSHeatNo());
                param.put(ReqResParamKey.NU_PARTY_CODE, bean.getNuPartyCode());
                param.put(ReqResParamKey.NU_S_DIV_QTY, bean.getNuSDivQty());
                param.put(ReqResParamKey.VC_S_DIVISION, bean.getVcSDivision());
                param.put(ReqResParamKey.DT_MRIR_DATE, bean.getDtMrirDate());
                param.put(ReqResParamKey.VC_S_MRIR_NO, bean.getVcSMrirNo());
                param.put(ReqResParamKey.VC_COMP_CODE, bean.getVcSCompCode());
                param.put(ReqResParamKey.VC_AUTH_CODE, bean.getVcAuthCode());
                param.put(ReqResParamKey.VC_S_COIL_NO, bean.getVcSCoilNo());
                param.put(ReqResParamKey.DT_S_ROUTE_DATE, bean.getDtSRouteDate());
                param.put(ReqResParamKey.DT_ORDER_DATE, bean.getDtOrderDate());
                param.put(ReqResParamKey.VC_S_GRADE_NO, bean.getVcSGradeNo());
                param.put(ReqResParamKey.DT_GATE_ENT_DATE, bean.getDtGateEntDate());
                param.put(ReqResParamKey.VC_LOCATION_NAME, bean.getItemLocationName());
                param.put(ReqResParamKey.VC_LOCATION_CODE, bean.getItemLocationCOde());
                param.put(ReqResParamKey.VC_TRAN_NO, mTranNo);
                param.put(ReqResParamKey.DT_TRAN_DATE, mTranDate);
                param.put(ReqResParamKey.VC_MATCH_UNM, bean.isItemOld());
                itemArray.put(param);
            }
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(ReceivingItemListActivity.this, Constants.RM_RECEIPT_TRAN_DETAIL_LIST_SUBMIT, itemArray.toString(), ReceivingItemListActivity.this, Constants.RM_RECEIPT_LIST_SUBMIT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(ReceivingItemListActivity.this,mUserPref.getBASEUrl()+ Constants.RM_RECEIPT_TRAN_DETAIL_LIST_SUBMIT, itemArray.toString(), ReceivingItemListActivity.this, Constants.RM_RECEIPT_LIST_SUBMIT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(ReceivingItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RM_RECEIPT_ITEM_MATCH_RESULT_REQ_ID) {
                mEtItemQrcode.setText("");
                mTotalScannedItem = mTotalScannedItem + 1;
                ReceiptTranItemDetailBean bean = data.getParcelableExtra(BundleKey.RM_RECEIPT_ITEM);
                bean.setItemScanned(true);
                bean.setItemOld("Y");
                mTranDetailList.set(mScanningItemIndex, bean);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == Constants.RM_RECEIPT_ITEM_MISMATCH_RESULT_REQ_ID) {
                mEtItemQrcode.setText("");
                mTotalScannedItem = mTotalScannedItem + 1;
                ReceiptTranItemDetailBean bean = data.getParcelableExtra(BundleKey.RM_RECEIPT_ITEM);
                bean.setItemScanned(true);
                bean.setItemOld("N");
                mTranDetailList.add(bean);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            mEtItemQrcode.setText("");
            Toast.makeText(ReceivingItemListActivity.this, "Item Not Placed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
