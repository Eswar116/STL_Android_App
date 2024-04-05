package com.essindia.stlapp.Activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.PutAwayListAdapter;
import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.Bean.PutAwaySummaryList;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;
import com.essindia.stlapp.Utils.ValidationChecker;
import com.google.gson.Gson;
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

public class PutAwayList extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private EditText mEdtQRCode;
    private JSONObject mJsonObjReqParams;
    private UserPref userPref;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private PutAwaySummaryList mPutAwaySummaryList;
    private RadioGroup radioGp;
    private RadioButton radioButton, mRBDock, mRBBond;
    private RecyclerView recylerView;
    private JSONObject loginparams;
    private ConnectionDetector connectionDetector;
    private String bondNo, dockNo;
    private String mSelectedArea = "B";
    private ArrayList<GRN_Verification_Bean> mBondItemList;
    private ArrayList<GRN_Verification_Bean> mDockItemList;
    private PutAwayListAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_away_list);
        intializeXml();
        mBondItemList = new ArrayList<>();
        mDockItemList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEdtQRCode.setText(""); // for reset incase when getting back from detail screen
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

    private void intializeXml() {
        userPref = new UserPref(this);
        connectionDetector = new ConnectionDetector(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        mEdtQRCode = (EditText) findViewById(R.id.et_putaway_qrcode);
        radioGp = (RadioGroup) findViewById(R.id.radioGp);
        int selectedId = radioGp.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        mRBDock = (RadioButton) findViewById(R.id.radioDockArea);
        mRBBond = (RadioButton) findViewById(R.id.radioBondRoom);
        recylerView = (RecyclerView) findViewById(R.id.recylerView);
        toolbar.setTitle(R.string.put_away);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.img_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qrCodeData = mEdtQRCode.getText().toString().trim();
                if (!TextUtils.isEmpty(qrCodeData)) {
                    scannedDataSpliteAndSend(qrCodeData);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", "Qr code is empty cannot load data item");
                }
            }
        });
        loginparams = new JSONObject();
        try {
            loginparams.put(Constants.auth_Token, userPref.getToken());
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(PutAwayList.this, Constants.POST_PUT_AWAY_SUGGESTED_AREA, loginparams.toString(), PutAwayList.this, Constants.PUT_AWAY_POST_SUGGESTED_AREA_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(PutAwayList.this, userPref.getBASEUrl() + Constants.POST_PUT_AWAY_SUGGESTED_AREA, loginparams.toString(), PutAwayList.this, Constants.PUT_AWAY_POST_SUGGESTED_AREA_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", "No internet connection.");
            }
        } catch (Exception e) {
            AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", e.toString());
        }
        radioGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if (radioButton.getId() == R.id.radioBondRoom) {
                    mSelectedArea = "B";
                    setListValue(mBondItemList);
                } else if (radioButton.getId() == R.id.radioDockArea) {
                    mSelectedArea = "D";
                    setListValue(mDockItemList);
                }
            }
        });

    }

    private void setListValue(ArrayList<GRN_Verification_Bean> itemList) {
        mAdapter = new PutAwayListAdapter(getApplicationContext(), itemList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(mAdapter);
    }

    private void scannedDataSpliteAndSend(String data) {

        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length < 8 || dataSp.length > 8) {
            AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 8) {
            Constants.QR_PROCESS_CODE = dataSp[0].trim().toUpperCase();
            Constants.QR_STL_PART_NO = dataSp[1].trim().toUpperCase();
            Constants.QR_ROUTE_CARD_NO = dataSp[2].trim().toUpperCase();
            Constants.QR_STD_QTY = dataSp[3].trim().toUpperCase();
            Constants.QR_ACTUAL_QTY = dataSp[4].trim().toUpperCase();
            Constants.QR_REFERENCE_NO = dataSp[5].trim().toUpperCase();
            Constants.QR_BOX_TYPE = dataSp[6].trim().toUpperCase();
//            Constants.QR_ROUTE_DATE = dataSp[7].trim().toUpperCase();
            Constants.QR_TIME = dataSp[7].trim().toUpperCase();
            if (Constants.QR_ACTUAL_QTY != null && Constants.QR_STD_QTY != null && !Constants.QR_ACTUAL_QTY.isEmpty() && !Constants.QR_STD_QTY.isEmpty()) {
                String boxType = ValidationChecker.getBoxType(this, Constants.QR_ACTUAL_QTY, Constants.QR_STD_QTY);
                Constants.QR_BOX_TYPE = Constants.QR_STL_PART_NO + "/" + Constants.QR_STD_QTY + "/" + Constants.QR_BOX_TYPE + "-" + boxType;
            }

            putVerificationListData(Constants.QR_ROUTE_CARD_NO, /* Constants.QR_ROUTE_DATE,*/ Constants.QR_STL_PART_NO, Constants.QR_REFERENCE_NO, Constants.QR_BOX_TYPE, userPref.getOrgId());
        }
    }

    private void putVerificationListData(String routeNo, /*String routeDate,*/ String itemCode, String irNo, String boxType, String orgId) {

//        String RouteDate = "12-09-2016";
        mJsonObjReqParams = new JSONObject();
        try {
            mJsonObjReqParams.put(Constants.auth_Token, userPref.getToken());
            mJsonObjReqParams.put(ReqResParamKey.ROUTE_NO, routeNo);
//            mJsonObjReqParams.put(ReqResParamKey.ROUTE_DATE, routeDate);
            mJsonObjReqParams.put(ReqResParamKey.ITEM_CODE, itemCode);
            mJsonObjReqParams.put(ReqResParamKey.IR_NO, irNo);
            mJsonObjReqParams.put(ReqResParamKey.PACK_TYPE, boxType);
            mJsonObjReqParams.put(ReqResParamKey.ORG_ID, orgId);
            mJsonObjReqParams.put(ReqResParamKey.SELECTED_AREA, mSelectedArea);
            if (ConnectionDetector.isNetworkAvailable(this)) {
                showProgressBar("Please Wait...");
//                CallService.getInstance().getResponseUsingPOST(PutAwayList.this, Constants.PUT_AWAY_SUMMARY_DATA, mJsonObjReqParams.toString(), PutAwayList.this, Constants.GET_PUT_AWAY_SUMMARY_DATA_API_REQ_ID, false);
                CallService.getInstance().getResponseUsingPOST(PutAwayList.this, userPref.getBASEUrl() + Constants.PUT_AWAY_SUMMARY_DATA, mJsonObjReqParams.toString(), PutAwayList.this, Constants.GET_PUT_AWAY_SUMMARY_DATA_API_REQ_ID, false);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", "Not an internet connectivity");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PutAwayList.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        hideProgressBar();
        Gson gson = new Gson();
        mPutAwaySummaryList = gson.fromJson(response, PutAwaySummaryList.class);
        if (request_id == Constants.GET_PUT_AWAY_SUMMARY_DATA_API_REQ_ID) {
            if (mPutAwaySummaryList != null && mPutAwaySummaryList.getMessageCode() != null && mPutAwaySummaryList.getMessageCode().equalsIgnoreCase("1")) {
                Toast.makeText(this, R.string.data_not_available, Toast.LENGTH_SHORT).show();
                mEdtQRCode.setText(""); // for reset
            } else if (mPutAwaySummaryList != null && mPutAwaySummaryList.getData() != null && mPutAwaySummaryList.getData().size() > 0) {
                Intent putawaydetail = new Intent(PutAwayList.this, PutAwayDetails.class);
                putawaydetail.putExtra(BundleKey.PUT_AWAY_ITEM_DETAILS, mPutAwaySummaryList.getData().get(0));
                putawaydetail.putExtra(Constants.SUGGESTED_AREA, radioButton.getText().toString().trim().substring(0, 4));
                startActivity(putawaydetail);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } else {
                mEdtQRCode.setText(""); // for reset
                Toast.makeText(PutAwayList.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        } else if (request_id == Constants.PUT_AWAY_POST_SUGGESTED_AREA_REQ_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        bondNo = result.optString("BOND_NO");
                        dockNo = result.optString("DOCK_NO");
                        mRBBond.setText("BOND (" + bondNo + ")");
                        mRBDock.setText("DOCK (" + dockNo + ")");
                        JSONObject itemListObj = result.optJSONObject("putAwayList");
                        JSONArray bondItemArray = itemListObj.optJSONArray("BOND");
                        JSONArray dockItemArray = itemListObj.optJSONArray("DOCK");
                        mBondItemList.clear();
                        mDockItemList.clear();
                        for (int i = 0; i < bondItemArray.length(); i++) {
                            GRN_Verification_Bean bean = new GRN_Verification_Bean();
                            JSONObject obj = bondItemArray.getJSONObject(i);
                            bean.setHO_ORG_ID(obj.optString("HO_ORG_ID"));
                            bean.setPROJ_ID(obj.optString("PROJ_ID"));
                            bean.setCLD_ID(obj.optString("CLD_ID"));
                            bean.setTRAN_NO(obj.optString("TRAN_NO"));
                            bean.setPACK_SIZE(obj.optString("PACK_SIZE"));
                            bean.setPACK_TYPE(obj.optString("PACK_TYPE"));
                            bean.setPROCESSED_BOXES("PROCESSED_BOXES");
                            bean.setIR_NO(obj.optString("IR_NO"));
                            bean.setCLOSE_FLAG(obj.optString("CLOSE_FLAG"));
                            bean.setEXCESS_PACK_QTY(obj.optString("EXCESS_PACK_QTY"));
                            bean.setNO_OF_BOXES(obj.optString("NO_OF_BOXES"));
                            bean.setROUTE_DATE(obj.optString("ROUTE_DATE"));
                            bean.setTRAN_date(obj.optString("TRAN_DATE"));
                            bean.setSLOC_ID(obj.optString("SLOC_ID"));
                            bean.setDT_MOD_DATE(obj.optString("DT_MOD_DATE"));
                            bean.setITEM_CODE(obj.optString("ITEM_CODE"));
                            bean.setROUTE_NO(obj.optString("ROUTE_NO"));
                            bean.setSCAN_FLAG(obj.optString("SCAN_FLAG"));
                            bean.setORG_ID(obj.optString("ORG_ID"));
                            bean.setEXCESS_FLAG(obj.optString("EXCESS_FLAG"));
                            bean.setVC_MACHINE_NO(obj.optString("VC_MACHINE_NO"));
                            bean.setVERIFIED_QTY(obj.optString("VERIFIED_QTY"));
                            bean.setPEND_QTY(obj.optString("PEND_QTY"));
                            bean.setITEM_DESC(obj.optString("ITEM_DESC"));
                            bean.setPUT_AWAY_ID(obj.optString("PUT_AWAY_ID"));

                            mBondItemList.add(bean);
                        }
                        for (int i = 0; i < dockItemArray.length(); i++) {
                            GRN_Verification_Bean bean = new GRN_Verification_Bean();
                            JSONObject obj = dockItemArray.getJSONObject(i);
                            bean.setHO_ORG_ID(obj.optString("HO_ORG_ID"));
                            bean.setPROJ_ID(obj.optString("PROJ_ID"));
                            bean.setCLD_ID(obj.optString("CLD_ID"));
                            bean.setTRAN_NO(obj.optString("TRAN_NO"));
                            bean.setPACK_SIZE(obj.optString("PACK_SIZE"));
                            bean.setPACK_TYPE(obj.optString("PACK_TYPE"));
                            bean.setPROCESSED_BOXES("PROCESSED_BOXES");
                            bean.setIR_NO(obj.optString("IR_NO"));
                            bean.setCLOSE_FLAG(obj.optString("CLOSE_FLAG"));
                            bean.setEXCESS_PACK_QTY(obj.optString("EXCESS_PACK_QTY"));
                            bean.setNO_OF_BOXES(obj.optString("NO_OF_BOXES"));
                            bean.setROUTE_DATE(obj.optString("ROUTE_DATE"));
                            bean.setTRAN_date(obj.optString("TRAN_DATE"));
                            bean.setSLOC_ID(obj.optString("SLOC_ID"));
                            bean.setDT_MOD_DATE(obj.optString("DT_MOD_DATE"));
                            bean.setITEM_CODE(obj.optString("ITEM_CODE"));
                            bean.setROUTE_NO(obj.optString("ROUTE_NO"));
                            bean.setSCAN_FLAG(obj.optString("SCAN_FLAG"));
                            bean.setORG_ID(obj.optString("ORG_ID"));
                            bean.setEXCESS_FLAG(obj.optString("EXCESS_FLAG"));
                            bean.setVC_MACHINE_NO(obj.optString("VC_MACHINE_NO"));
                            bean.setVERIFIED_QTY(obj.optString("VERIFIED_QTY"));
                            bean.setPEND_QTY(obj.optString("PEND_QTY"));
                            bean.setITEM_DESC(obj.optString("ITEM_DESC"));
                            bean.setPUT_AWAY_ID(obj.optString("PUT_AWAY_ID"));
                            mDockItemList.add(bean);
                        }
                        setListValue(mBondItemList);
                    } else {
                        showToast(message);
                    }
                } catch (Exception e) {

                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PutAwayList.this, "Alert", response.toString());
            }
        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        System.out.println("BarcodeReadEvent:" + barcodeReadEvent);
        System.out.println("Barocode data:" + barcodeReadEvent.getBarcodeData());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scannedDataSpliteAndSend(barcodeReadEvent.getBarcodeData());
            }
        });

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
