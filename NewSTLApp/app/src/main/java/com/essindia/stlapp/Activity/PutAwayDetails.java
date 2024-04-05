package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.essindia.stlapp.Bean.PutAwaySummaryData;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class PutAwayDetails extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private UserPref userPref;
    private static BarcodeReader bcr;
    private PutAwaySummaryData mPutAwaySummaryData;
    private EditText mEdtItemDesc, mEdtItemCode, mEdtCurrCapacity, mEdtPendingQty, mEdtVcBinDesc, mEdtVcBinNo, mEdtHoOrgId, mEdtOrgId, edt_areaType, mEdtRouteDate, mEdtRouteNo, mEdtPackSize, mEdtPackType, mEdtIrNo, mEdtProjectId, mEdtCldId, mEdtLotNo, mEdtSlocId, mEdtDtModDate, mEdtPutAwayQty;
    private AidcManager manager;
    private String mScanedBinDesc, mSuggestedArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_away_details);
        mPutAwaySummaryData = this.getIntent().getParcelableExtra(BundleKey.PUT_AWAY_ITEM_DETAILS);
        mSuggestedArea = this.getIntent().getStringExtra(Constants.SUGGESTED_AREA);
        intializeXml();
        setValuesFromBundle();
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
        final String scannedBinDesc = barcodeReadEvent.getBarcodeData().toString().trim();
//        String binNoFromApi = mEdtVcBinNo.getText().toString().trim();
//        mEdtVcBinDesc.setText(scannedBinDesc);
//        if (binNoFromApi.equals(scannedBinNo)) {
//            postPutAwayLogData();
//        } else {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dockLocationForScannedBin(mPutAwaySummaryData.getIrNo(), mPutAwaySummaryData.getOrgId(), mPutAwaySummaryData.getItemCode(), scannedBinDesc, mPutAwaySummaryData.getPackSize());
            }
        });

//        }
    }

    private void setValuesFromBundle() {
        mEdtItemDesc.setText(mPutAwaySummaryData.getItemDesc());
        mEdtItemCode.setText(mPutAwaySummaryData.getItemCode());
        mEdtCurrCapacity.setText(mPutAwaySummaryData.getCurrCapacity());
        mEdtPendingQty.setText(mPutAwaySummaryData.getPendQty());
        mEdtVcBinDesc.setText(mPutAwaySummaryData.getVcBinDesc());
        mEdtVcBinNo.setText(mPutAwaySummaryData.getVcBinNo());
        mEdtHoOrgId.setText(mPutAwaySummaryData.getHoOrgId());
        mEdtOrgId.setText(mPutAwaySummaryData.getOrgId());
        edt_areaType.setText(mSuggestedArea);
        mEdtRouteDate.setText(mPutAwaySummaryData.getRouteDate());
        mEdtSlocId.setText(mPutAwaySummaryData.getSlocId());
        mEdtRouteNo.setText(mPutAwaySummaryData.getRouteNo());
        mEdtPackSize.setText(mPutAwaySummaryData.getPackSize());
        mEdtPackType.setText(mPutAwaySummaryData.getPackType());
        mEdtIrNo.setText(mPutAwaySummaryData.getIrNo());
        mEdtProjectId.setText(mPutAwaySummaryData.getProjId());
        mEdtCldId.setText(mPutAwaySummaryData.getCldId());
        mEdtLotNo.setText(mPutAwaySummaryData.getLotNo());
        mEdtDtModDate.setText(mPutAwaySummaryData.getDtModDate());
    }

    private void intializeXml() {
        userPref = new UserPref(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.put_away_detail);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        mEdtItemDesc = (EditText) findViewById(R.id.edt_item_desc);
        mEdtItemCode = (EditText) findViewById(R.id.edt_item_code);
        mEdtCurrCapacity = (EditText) findViewById(R.id.edt_curr_capacity);
        mEdtPendingQty = (EditText) findViewById(R.id.edt_pend_qty);
        mEdtVcBinDesc = (EditText) findViewById(R.id.edt_vc_bin_desc);
        mEdtVcBinNo = (EditText) findViewById(R.id.edt_vc_bin_no);
        mEdtHoOrgId = (EditText) findViewById(R.id.edt_ho_org_id);
        mEdtOrgId = (EditText) findViewById(R.id.edt_org_id);
        edt_areaType = (EditText) findViewById(R.id.edt_areaType);
        mEdtRouteDate = (EditText) findViewById(R.id.edt_route_date);
        mEdtRouteNo = (EditText) findViewById(R.id.edt_route_no);
        mEdtPackSize = (EditText) findViewById(R.id.edt_pack_size);
        mEdtPackType = (EditText) findViewById(R.id.edt_pack_type);
        mEdtIrNo = (EditText) findViewById(R.id.edt_ir_no);
        mEdtProjectId = (EditText) findViewById(R.id.edt_proj_id);
        mEdtCldId = (EditText) findViewById(R.id.edt_cld_id);
        mEdtLotNo = (EditText) findViewById(R.id.edt_lot_no);
        mEdtSlocId = (EditText) findViewById(R.id.edt_sloc_id);
        mEdtDtModDate = (EditText) findViewById(R.id.edt_dt_mode_date);
        mEdtPutAwayQty = (EditText) findViewById(R.id.edt_put_away_qty);
        findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEdtPutAwayQty.getText().toString().trim()))
                    if (!TextUtils.isEmpty(mEdtVcBinDesc.getText().toString().trim())) {
                        if (isValidQty()) {
                            if (capacityChecker()) {
                                if (isPartialPutAway()) {
                                    confirmAlert();
                                } else {
                                    postPutAwayLogData();
                                }
                            } else {
                                AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Alert", "There is not enough space for put away quantity");
                            }
                        } else {
                            AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Alert", "Put away quantity is not valid");
                        }
                    } else showToast(getString(R.string.please_scan_bin_location));
                else showToast(getString(R.string.please_enter_put_away_qty));
            }
        });
        findViewById(R.id.iv_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qrCodeData = mEdtVcBinDesc.getText().toString().trim();
                if (!TextUtils.isEmpty(qrCodeData)) {
                    dockLocationForScannedBin(mPutAwaySummaryData.getIrNo(), mPutAwaySummaryData.getOrgId(), mPutAwaySummaryData.getItemCode(), qrCodeData, mPutAwaySummaryData.getPackSize());
                } else {
                    AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Alert", "Qr code is empty cannot load data item");
                }
            }
        });

        setSupportActionBar(toolbar);
    }

    private void confirmAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Error");
        builder.setMessage(R.string.alert_putAway);
        builder.setNegativeButton(R.string.putAway_alert_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Alert", "Canceled Partial Verification");
            }
        });
        builder.setPositiveButton(R.string.putAway_alert_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postPutAwayLogData();
            }
        });
//        builder.setPositiveButton(R.string.grn_alert_no, null);
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    private boolean isPartialPutAway() {
        int putAwayQty = Integer.parseInt(mEdtPutAwayQty.getText().toString().trim());
        int pendQty = Integer.parseInt(mPutAwaySummaryData.getPendQty().trim());
        if (putAwayQty < pendQty) {
            return true;
        } else {
            return false;
        }

    }

    private boolean capacityChecker() {
        int putAwayQty = Integer.parseInt(mEdtPutAwayQty.getText().toString().trim());
        int binCapacity = Integer.parseInt(mPutAwaySummaryData.getCurrCapacity().trim());
        if (putAwayQty <= binCapacity) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isValidQty() {
        int putAwayQty = Integer.parseInt(mEdtPutAwayQty.getText().toString().trim());
        int pendQty = Integer.parseInt(mPutAwaySummaryData.getPendQty().trim());
        if (putAwayQty > 0 && putAwayQty <= pendQty) {
            return true;
        } else {
            return false;
        }
    }

    private void postPutAwayLogData() {
        JSONObject jsonObjReqParams = new JSONObject();
        try {
            jsonObjReqParams.put(Constants.auth_Token, userPref.getToken());
            jsonObjReqParams.put(ReqResParamKey.HO_ORG_ID, mPutAwaySummaryData.getHoOrgId());
            jsonObjReqParams.put(ReqResParamKey.PUT_AWAY_ID, mPutAwaySummaryData.getPutAwayId());
            jsonObjReqParams.put(ReqResParamKey.ORG_ID, mPutAwaySummaryData.getOrgId());
            jsonObjReqParams.put(ReqResParamKey.SLOC_ID, mPutAwaySummaryData.getSlocId());
            jsonObjReqParams.put(ReqResParamKey.PROJ_ID, mPutAwaySummaryData.getProjId());
            jsonObjReqParams.put(ReqResParamKey.CLD_ID, mPutAwaySummaryData.getCldId());
            String timeStamp = new Timestamp(System.currentTimeMillis()).toString();
            String[] split = timeStamp.split(java.util.regex.Pattern.quote("."));
            jsonObjReqParams.put(ReqResParamKey.VERIFICATION_TIMESTAMP, split[0]);
            jsonObjReqParams.put(ReqResParamKey.USER_ID, userPref.getUserDeviceId());
            jsonObjReqParams.put(ReqResParamKey.DEVICE_ID, userPref.getUserDeviceId());
            jsonObjReqParams.put(ReqResParamKey.ROUTE_NO, mPutAwaySummaryData.getRouteNo());
            jsonObjReqParams.put(ReqResParamKey.ROUTE_DATE, mPutAwaySummaryData.getRouteDate());
            jsonObjReqParams.put(ReqResParamKey.TRAN_NO, mPutAwaySummaryData.getTranNo());
            jsonObjReqParams.put(ReqResParamKey.TRAN_DATE, mPutAwaySummaryData.getTranDate());
            jsonObjReqParams.put(ReqResParamKey.IR_NO, mPutAwaySummaryData.getIrNo());
            jsonObjReqParams.put(ReqResParamKey.ITEM_CODE, mPutAwaySummaryData.getItemCode());
            jsonObjReqParams.put(ReqResParamKey.PACK_TYPE, mPutAwaySummaryData.getPackType());
            jsonObjReqParams.put(ReqResParamKey.PACK_SIZE, mPutAwaySummaryData.getPackSize());
            jsonObjReqParams.put(ReqResParamKey.NO_OF_BOXES, mEdtPutAwayQty.getText().toString().trim());
            jsonObjReqParams.put(ReqResParamKey.BOX_ID, ""); // as discussed with Govind, pass empty value
            jsonObjReqParams.put(ReqResParamKey.DT_MOD_DATE, mPutAwaySummaryData.getDtModDate());
            jsonObjReqParams.put(ReqResParamKey.PEND_QTY, mPutAwaySummaryData.getPendQty());
            jsonObjReqParams.put(ReqResParamKey.SUGGESTED_AREA, mSuggestedArea);
            jsonObjReqParams.put(ReqResParamKey.LOT_NO, mPutAwaySummaryData.getLotNo());
            jsonObjReqParams.put(ReqResParamKey.CURR_CAPACITY, mPutAwaySummaryData.getCurrCapacity());
            jsonObjReqParams.put(ReqResParamKey.DOCK_AREA, mPutAwaySummaryData.getVcBinNo());
            jsonObjReqParams.put(ReqResParamKey.EXCESS_PACK_QTY,mPutAwaySummaryData.getExcessPackQty());

            if (ConnectionDetector.isNetworkAvailable(this)) {
                showProgressBar(getString(R.string.please_wait));
//                CallService.getInstance().getResponseUsingPOST(PutAwayDetails.this, Constants.POST_PUT_AWAY_LOG, jsonObjReqParams.toString(), PutAwayDetails.this, Constants.POST_PUT_AWAY_LOG_DATA_API_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(PutAwayDetails.this, userPref.getBASEUrl()+Constants.POST_PUT_AWAY_LOG, jsonObjReqParams.toString(), PutAwayDetails.this, Constants.POST_PUT_AWAY_LOG_DATA_API_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, getString(R.string.alert), getString(R.string.not_an_internet_connectivity));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dockLocationForScannedBin(String irNo, String orgId, String itemCode, String scanBinDesc, String packSize) {
        JSONObject jsonObjReqParams = new JSONObject();
        mScanedBinDesc = scanBinDesc;
        try {
            jsonObjReqParams.put(Constants.auth_Token, userPref.getToken());
            jsonObjReqParams.put(ReqResParamKey.IR_NO, irNo);
            jsonObjReqParams.put(ReqResParamKey.ORG_ID, orgId);
            jsonObjReqParams.put(ReqResParamKey.ITEM_CODE, itemCode);
            jsonObjReqParams.put(ReqResParamKey.BIN_DESC, scanBinDesc);
            jsonObjReqParams.put(ReqResParamKey.PACK_SIZE, packSize);
            if (ConnectionDetector.isNetworkAvailable(this)) {
                showProgressBar(getString(R.string.please_wait));
//                CallService.getInstance().getResponseUsingPOST(PutAwayDetails.this, Constants.PUT_AWAY_DOCK_LOCATION, jsonObjReqParams.toString(), PutAwayDetails.this, Constants.PUT_AWAY_BIN_LOCATION_DATA_API_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(PutAwayDetails.this,userPref.getBASEUrl()+ Constants.PUT_AWAY_DOCK_LOCATION, jsonObjReqParams.toString(), PutAwayDetails.this, Constants.PUT_AWAY_BIN_LOCATION_DATA_API_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Alert", "Not an internet connectivity");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        hideProgressBar();
        if (response != null) {
            try {
                JSONObject result = new JSONObject(response);
                if (request_id == Constants.PUT_AWAY_BIN_LOCATION_DATA_API_REQ_ID) {
                    String message = result.optString(ReqResParamKey.Message);
                    String messageCode = result.optString(ReqResParamKey.MessageCode);
                    if (messageCode.equalsIgnoreCase("0")) {
                        String binNoForNewBin = result.optString(ReqResParamKey.VC_BIN_NO);
                        if (!TextUtils.isEmpty(binNoForNewBin)) {
                            mPutAwaySummaryData.setVcBinNo(binNoForNewBin);
                        }
                        String binQtyForNewBin = result.optString(ReqResParamKey.BIN_QTY);
                        if (!TextUtils.isEmpty(binQtyForNewBin)) {
                            mEdtCurrCapacity.setText(binQtyForNewBin);
                            mPutAwaySummaryData.setCurrCapacity(binQtyForNewBin);
                        }
                        mEdtVcBinDesc.setText(mScanedBinDesc);
                    } else {
                        mEdtVcBinDesc.setText("");
                        mEdtCurrCapacity.setText("");
                        mPutAwaySummaryData.setCurrCapacity("");
                        showToast(message);
                    }

                } else if (request_id == Constants.POST_PUT_AWAY_LOG_DATA_API_REQ_ID) {
                    String msg = result.optString(ReqResParamKey.Message);
                    String msgCode = result.optString(ReqResParamKey.MessageCode);
                    if (msgCode.equalsIgnoreCase("0")) {
                        Toast.makeText(PutAwayDetails.this, msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PutAwayDetails.this, PutAwayList.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    }
                    else{
                        Toast.makeText(PutAwayDetails.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                AlertDialogManager.getInstance().simpleAlert(PutAwayDetails.this, "Error", e.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PutAwayDetails.this, PutAwayList.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
