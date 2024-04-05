package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.essindia.stlapp.Bean.PickUpSummaryData;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Sqlite.SqliteConstantData;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.DateUtil;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.essindia.stlapp.Utils.Constants.PICK_UP_DETAIL_REASON_ID;


public class PickupDetails extends BaseActivity implements View.OnClickListener, OnResponseFetchListener {

    private PickUpSummaryData mPuScannedDataBean, mPuListItemBean;
    private EditText productCodeEt, packTypeEt, packSizeEt, binQtyEt, pickedQtyEt, itemDescTv, pickingQtyEt, et_location_desc;
    private Button buttonPickUp;
    private String mBoxType;
    private String mItemQrTimestamp, mItemQrCode;
    private LinearLayout reasonLL;
    private JSONObject loginparams;
    private UserPref userPref;
    private DateFormat dateFormat;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_details);
        mPuScannedDataBean = this.getIntent().getParcelableExtra(BundleKey.KEY_PU_SCANNED_ITEM_BEAN);
        mPuListItemBean = this.getIntent().getParcelableExtra(BundleKey.KEY_PU_LIST_ITEM_BEAN);
        mItemQrTimestamp = this.getIntent().getStringExtra(BundleKey.KEY_PU_ITEM_QR_TIMESTAMP);
        mItemQrCode = this.getIntent().getStringExtra(BundleKey.KEY_PU_ITEM_QR_CODE);
        intializeXml();
        currentDateTime();
        setValuesFromBundle();
    }

    private void intializeXml() {
        userPref = new UserPref(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pickUpdetail);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        productCodeEt = (EditText) findViewById(R.id.productCodeEt);
        pickingQtyEt = (EditText) findViewById(R.id.pickingQtyEt);
        reasonLL = (LinearLayout) findViewById(R.id.reasonLL);
        packTypeEt = (EditText) findViewById(R.id.packTypeEt);
        packSizeEt = (EditText) findViewById(R.id.packSizeEt);
        binQtyEt = (EditText) findViewById(R.id.binQtyEt);
        et_location_desc = (EditText) findViewById(R.id.et_location_desc);
        pickedQtyEt = (EditText) findViewById(R.id.pickedQtyEt);
        itemDescTv = (EditText) findViewById(R.id.itemDescTv);
        buttonPickUp = (Button) findViewById(R.id.buttonPickUp);
        buttonPickUp.setOnClickListener(this);
        pickingQtyEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String enteredQty = s.toString().trim();
                if (!enteredQty.startsWith("0")) {
                    if (enteredQty.length() > 0) {
                        if (Integer.parseInt(enteredQty) > Integer.parseInt(mPuScannedDataBean.getBIN_QTY())) {
                            pickingQtyEt.setText("");
                            pickingQtyEt.setError("Picking boxes can't be greater than available boxes(" + mPuScannedDataBean.getBIN_QTY() + ")");
                            showToast("Picking boxes can't be greater than available boxes(" + mPuScannedDataBean.getBIN_QTY() + ")");
                        } else {
                            if (Integer.parseInt(enteredQty) > Integer.parseInt(mPuScannedDataBean.getPEND_BOXES())) {
                                pickingQtyEt.setText("");
                                pickingQtyEt.setError("Picking boxes can't be greater than pending boxes(" + mPuScannedDataBean.getPEND_BOXES() + ")");
                                showToast("Picking boxes can't be greater than pending boxes(" + mPuScannedDataBean.getPEND_BOXES() + ")");
                            } else {
                                pickingQtyEt.setError(null);
                            }
                        }
                    } else {
                        pickingQtyEt.setError(null);
                    }
                } else {
                    pickingQtyEt.setError("Pick boxes can not be zero");
                    pickingQtyEt.setText("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void currentDateTime() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();
        System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
    }

    private void setValuesFromBundle() {
        productCodeEt.setText(mPuListItemBean.getPRODUCTCODE());
        try {
            if (Integer.parseInt(mPuScannedDataBean.getPEND_QTY()) < Integer.parseInt(mPuScannedDataBean.getPACKSIZE())) {
                mBoxType = "NONSTD";
                packTypeEt.setText("Non Standard");
            } else {
                mBoxType = "STD";
                packTypeEt.setText("Standard");
            }
        } catch (Exception e) {
            packTypeEt.setText("");
        }
        packSizeEt.setText(mPuScannedDataBean.getPACKSIZE());
        binQtyEt.setText(mPuScannedDataBean.getBIN_QTY());
        et_location_desc.setText(mPuScannedDataBean.getVC_BIN_NO());
//        itemDescTv.setText(mPuScannedDataBean.getPRODUCTNAME());
        pickedQtyEt.setText(mPuScannedDataBean.getPEND_BOXES());
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  loginparams = new JSONObject();
        if (connectionDetector.isConnectingToInternet()) {
            try {
                loginparams.put(Constants.auth_Token, userPref.getToken());
                loginparams.put(Constants.ORG_ID, mPuScannedDataBean.getORGID());
                CallService.getInstance().getResponseUsingPOST(PickupDetails.this, Constants.POST_PICKUP_REASON, loginparams.toString(), PickupDetails.this, PICK_UP_DETAIL_REASON_ID, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", "No internet connection.");
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPickUp:
                if (pickingQtyEt.getText().toString().trim().length() > 0) {
                    postPickUpLogData();
                } else {
                    showToast("Please enter picked qty");
                }
                break;
        }
    }
/*
    private void callingPickUpReserveAPI(String scannedBinLocation) {
        loginparams = new JSONObject();
        if (connectionDetector.isConnectingToInternet()) {
            try {
                loginparams.put(Constants.ORG_ID, mPuScannedDataBean.getORGID());
                loginparams.put(Constants.ITEM_CODE, mPuScannedDataBean.getPRODUCTCODE());
                loginparams.put(Constants.BIN_DESC, scannedBinLocation);
                loginparams.put(Constants.PACK_SIZE, mPuScannedDataBean.getPACKSIZE());
                loginparams.put(Constants.BOX_TYPE, mPuScannedDataBean.getBOX_TYPE());
                CallService.getInstance().getResponseUsingPOST(PickupDetails.this, Constants.POST_PICKUP_RESERVE_SUMMARY, loginparams.toString(), PickupDetails.this, Constants.PICK_UP_SUMMARY_RESERVE_ID, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", "No internet connection.");
        }
    }*/

    private boolean dataSplit(String data) {

        boolean flag = false;
        String dataSp[] = data.split(Constants.DELIMITER);

        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", "Qr code is empty cannot load data item");
            flag = false;
        } else if (dataSp.length < 1 || dataSp.length > 1) {
            AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", "Please verify correct location format !");
            flag = false;
        } else if (dataSp.length == 1) {
//            mBinLocation = dataSp[0];
//            mEtBinLocation.setText(mBinLocation);
            flag = true;
        }

        return flag;
    }

    private void postPickUpLogData() {
        JSONObject jsonObjReqParams = new JSONObject();
        try {
            jsonObjReqParams.put(Constants.auth_Token, userPref.getToken());
            jsonObjReqParams.put(ReqResParamKey.ORG_ID, mPuListItemBean.getORGID());
            jsonObjReqParams.put(ReqResParamKey.CUSTOMER_CODE, mPuListItemBean.getCUSTOMERCODE());
            jsonObjReqParams.put(ReqResParamKey.CUSTOMER_NAME, mPuListItemBean.getCUSTOMERNAME());
            jsonObjReqParams.put(ReqResParamKey.PRODUCT_NAME, mPuListItemBean.getPRODUCTNAME());
            jsonObjReqParams.put(ReqResParamKey.PACK_SIZE, mPuScannedDataBean.getPACKSIZE());
            jsonObjReqParams.put(SqliteConstantData.KEY_BOX_TYPE, mBoxType);
//            jsonObjReqParams.put(ReqResParamKey.LOT_NO, mPuScannedDataBean.getVC_LOT_NO());
            jsonObjReqParams.put(ReqResParamKey.VC_USER_CODE, userPref.getUserName());
            jsonObjReqParams.put(ReqResParamKey.DEVICE_ID, userPref.getUserDeviceId());
            jsonObjReqParams.put(ReqResParamKey.PICK_TIMESTAMP, mItemQrTimestamp);
            jsonObjReqParams.put(ReqResParamKey.PICK_NO, mPuListItemBean.getPICKNO());
            String pickDate = DateUtil.ddMMMyyyyToddMMyyyy(mPuListItemBean.getPICKDATE());
            if (!pickDate.isEmpty()) {
                jsonObjReqParams.put(ReqResParamKey.PICK_DATE, pickDate);
            } else {
                Toast.makeText(PickupDetails.this, "Invalid Pick Date Format", Toast.LENGTH_LONG).show();
                return;
            }
            jsonObjReqParams.put(ReqResParamKey.PRODUCT_CODE, mPuListItemBean.getPRODUCTCODE());
            jsonObjReqParams.put(ReqResParamKey.BIN_DESC, mPuScannedDataBean.getVC_BIN_NO());
            jsonObjReqParams.put(ReqResParamKey.TIMESTAMP, dateFormat.format(date));
            jsonObjReqParams.put(ReqResParamKey.PICK_QTY, mPuScannedDataBean.getPEND_QTY());
            jsonObjReqParams.put(ReqResParamKey.BOX_ID, mItemQrCode);
            jsonObjReqParams.put(ReqResParamKey.PICKED_BOXES, pickingQtyEt.getText().toString().trim());

            if (ConnectionDetector.isNetworkAvailable(this)) {
//                CallService.getInstance().getResponseUsingPOST(PickupDetails.this, Constants.POST_PICKUP_DETAILS, jsonObjReqParams.toString(), PickupDetails.this, Constants.PICK_UP_DETAIL_POST_RESERVE_ID, true);
                CallService.getInstance().getResponseUsingPOST(PickupDetails.this,userPref.getBASEUrl()+ Constants.POST_PICKUP_DETAILS, jsonObjReqParams.toString(), PickupDetails.this, Constants.PICK_UP_DETAIL_POST_RESERVE_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, getString(R.string.alert), getString(R.string.not_an_internet_connectivity));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
      /*  if (request_id == Constants.PICK_UP_SUMMARY_RESERVE_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String message = result.optString(ReqResParamKey.Message);
                    String messageCode = result.optString(ReqResParamKey.MessageCode);
                    if (messageCode.equalsIgnoreCase("0")) {
                        String putQty = result.optString(ReqResParamKey.PUT_QTY);
                        vcBinNo = result.optString(ReqResParamKey.VC_BIN_NO);
                        String binDesc = result.optString(ReqResParamKey.BIN_DESC);
                        binQtyEt.setText(putQty);
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", message);
                    }
                } catch (Exception e) {
                    AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", e.toString());
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", response.toString());
            }
        } else*/
        if (request_id == Constants.PICK_UP_DETAIL_POST_RESERVE_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String message = result.optString(ReqResParamKey.Message);
                    String messageCode = result.optString(ReqResParamKey.MessageCode);
                    if (messageCode.equalsIgnoreCase("0")) {
                        String[] statusList = message.split(Constants.DELIMITER);
                        if (statusList.length == 2) {
                            String itemSubmitStatus = statusList[0];
                            if (itemSubmitStatus.equalsIgnoreCase("Y")) {
                                String pickSubmitStatus = statusList[1];
                                if (pickSubmitStatus.equalsIgnoreCase("Y")) {
                                    userPref.setPickupQrCode("");
                                    showToast("Pick Up submit successfully");
                                    showAlertPuSubmitSuccess("Success", "Pick Up submit successfully");
                                } else {
                                    showToast("Pick Up item submit successfully");
                                    finish();
                                }
                            } else {
                                AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", message);
                            }
                        }
/*
                        Intent i = new Intent(PickupDetails.this, NewPickupList.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();*/
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", message);
                    }
                } catch (Exception e) {
                    AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", e.toString());
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", response.toString());
            }
        } /*else if (request_id == Constants.PICK_UP_DETAIL_REASON_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String message = result.optString(ReqResParamKey.Message);
                    String messageCode = result.optString(ReqResParamKey.MessageCode);
                    if (messageCode.equalsIgnoreCase("0")) {
                        reasonList.clear();
                        JSONArray array = result.getJSONArray(ReqResParamKey.VC_REASON_DESC);
                        for (int i = 0; i < array.length(); i++) {
                            reasonList.add(array.getJSONObject(i).optString(ReqResParamKey.PICKUP_REASON));
                        }
                        ArrayAdapter<String> dataAdapterReason = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reasonList);
                        dataAdapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        reasonSp.setAdapter(dataAdapterReason);
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", message);
                    }
                } catch (Exception e) {
                    AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", e.toString());
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupDetails.this, "Alert", response.toString());
            }
        }*/
    }

    private void showAlertPuSubmitSuccess(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(PickupDetails.this, Dashboard.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        builder.create();
        builder.show();

    }
}
