package com.essindia.stlapp.Activity;

import android.app.Activity;
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

import com.essindia.stlapp.Adapter.CoilIssueDetailListAdapter;
import com.essindia.stlapp.Bean.CoilIssueItemDetailBean;
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

public class LoadShopFloorItemListActivity extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, View.OnClickListener {

    private Toolbar toolbar;
    private TextView mTvLabelMsg, mTvTranNo;
    private EditText mEtItemQrcode;
    private ImageView mIvVerify;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranDetailList;
    private LinearLayoutManager layoutManager;
    private CoilIssueDetailListAdapter adapter;
    private ArrayList<CoilIssueItemDetailBean> mTranDetailList;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private int mScanningItemIndex = 0;
    private int mTotalScannedItem = 0;
    private int mTotalScanableItem = 0;
    private String mTranNo = "";
    private String mTranDate = "";
    private String mRouteCardNo = "";
    private String mRouteCardDate = "";
    private String r_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_shop_floor_item_list);
        intializeXml();
        getLoadingShopFloorItemListData();
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mTvTranNo = (TextView) findViewById(R.id.tv_tran_no);
        mEtItemQrcode = (EditText) findViewById(R.id.et_item_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        mIvVerify.setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mRvTranDetailList = (RecyclerView) findViewById(R.id.rv_tran_detail);
        toolbar.setTitle("Loading Shop Floor List");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);
        mUserPref = new UserPref(this);
        mConnectionDetector = new ConnectionDetector(this);
    }

    private void getLoadingShopFloorItemListData() {
        Bundle b = getIntent().getExtras();
        mTranNo = b.getString(BundleKey.RM_RECEIPT_TRAN_NO);
        mTranDate = b.getString(BundleKey.RM_RECEIPT_TRAN_DATE);
        mRouteCardNo = b.getString(BundleKey.ROUTE_CARD_NO);
        mRouteCardDate = b.getString(BundleKey.ROUTE_CARD_DATE);
        r_str = b.getString(BundleKey.R_STR);
        if (mTranNo != null && mTranNo.length() > 0 && mTranDate != null && mTranDate.length() > 0) {
            mTvTranNo.setText("MRS No/MRS Date: " + mTranNo + " / " + mTranDate);
            JSONObject param = new JSONObject();
            try {
                param.put(ReqResParamKey.TOKEN, mUserPref.getToken());
                param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
                param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
                param.put(ReqResParamKey.VC_TRAN_NO, mTranNo);
                param.put(ReqResParamKey.DT_TRAN_DATE, mTranDate);
                if (mConnectionDetector.isConnectingToInternet()) {
//                    CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this, Constants.LOADING_SHOP_FLOOR_TRAN_DETAIL_LIST, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_LIST_DETAIL_REQ_ID, true);
                    CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this,mUserPref.getBASEUrl()+ Constants.LOADING_SHOP_FLOOR_TRAN_DETAIL_LIST, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_LIST_DETAIL_REQ_ID, true);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Please check your network connection");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showLongToast("Tran No/Tran Date is mandatory");
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

   /* private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length < 13 || dataSp.length > 13) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 13) {
            callItemScanSubmitApi(data);
        }
    }*/

    private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            callItemScanSubmitApi(data);
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
                if (request_id == Constants.RM_COIL_ISSUE_LIST_DETAIL_REQ_ID) {
                    if (statusCode.equalsIgnoreCase("0")) {
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            mTranDetailList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                CoilIssueItemDetailBean bean = new CoilIssueItemDetailBean();
                                JSONObject obj = array.optJSONObject(i);
                                bean.setTranNo(obj.optString(ReqResParamKey.TRAN_NO));
                                bean.setTranDate(obj.optString(ReqResParamKey.TRAN_DATE));
                                bean.setMrirNo(obj.optString(ReqResParamKey.MRIR_NO));
                                bean.setMrirDate(obj.optString(ReqResParamKey.MRIR_DATE));
                                bean.setGrad(obj.optString(ReqResParamKey.GRAD));
                                bean.setCoilWeight(obj.optString(ReqResParamKey.COIL_WEIGHT));
                                bean.setVcSHeatNo(obj.optString(ReqResParamKey.VC_S_HEAT_NO));
                                bean.setVcSCoilNo(obj.optString(ReqResParamKey.VC_S_COIL_NO));
                                bean.setLoadStatus(obj.optString(ReqResParamKey.LOAD_STATUS));
                                bean.setTagNo(obj.optString(ReqResParamKey.VC_TAG_NO));
                                bean.setLocationName(obj.optString(ReqResParamKey.VC_LOCATION_NAME));
                                bean.setLocationCode(obj.optString(ReqResParamKey.VC_LOCATION_CODE));
                                bean.setVcDivision(obj.optString(ReqResParamKey.VC_DIVISION, ""));

                                mTranDetailList.add(bean);
                            }
                            mTotalScanableItem = mTranDetailList.size();
                            adapter = new CoilIssueDetailListAdapter(getApplicationContext(), mTranDetailList);
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
                        goBackTranListScreen();
                    }
                } else if (request_id == Constants.RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID) {
                    mEtItemQrcode.setText("");
                    if (statusCode.equalsIgnoreCase("0")) {
                        showLongToast(message);
                        getLoadingShopFloorItemListData();
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", message);
                    }
                } else if (request_id == Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID) {
                    if (statusCode.equalsIgnoreCase("0")) {
                        showPostSuccessAlert("Success", "Loading done successfully");
                        showLongToast(message);
                    } else {
                        showPostFailureAlert("Alert", message);
                        showLongToast(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Server not responding.");
        }
    }

    private void goBackTranListScreen() {
        Intent i = new Intent(LoadShopFloorItemListActivity.this, LoadShopFloorListActivity.class);
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
                submitRecord(true);
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();
    }

    private void showPostFailureAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();
    }

    private void submitRecord(boolean isSuccess) {
        Intent intent = new Intent();
        if (isSuccess) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }

    private void confirmAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitRecord(false);
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
                    callListItemSubmitApi();
                } else {
                    showToast("No item for submit");
                }
//                confirmAlert("All Record is not submit!", "Do you want to proceed without submit the record");
                break;
        }
    }

    private void callItemScanSubmitApi(String qrCOdeData) {
        try {
            JSONObject param = new JSONObject();
            param.put(ReqResParamKey.QR_CODE_DATA, qrCOdeData);
            param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.TRAN_NO, mTranNo);
            param.put(ReqResParamKey.TRAN_DATE, mTranDate);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            param.put(ReqResParamKey.R_STR, r_str);
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this, Constants.LOADING_SHOP_FLOOR_UPDATESCANNED_ITEM, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this, mUserPref.getBASEUrl()+Constants.LOADING_SHOP_FLOOR_UPDATESCANNED_ITEM, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callListItemSubmitApi() {
        try {
            JSONObject param = new JSONObject();
            param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.TRAN_NO, mTranNo);
            param.put(ReqResParamKey.TRAN_DATE, mTranDate);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            param.put(ReqResParamKey.ROUTE_CARD_NO, mRouteCardNo);
            param.put(ReqResParamKey.ROUTE_CARD_DATE, mRouteCardDate);
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this, Constants.LOADING_SHOP_FLOOR_SUBMIT_ITEM_LIST, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(LoadShopFloorItemListActivity.this, mUserPref.getBASEUrl()+Constants.LOADING_SHOP_FLOOR_SUBMIT_ITEM_LIST, param.toString(), LoadShopFloorItemListActivity.this, Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadShopFloorItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
