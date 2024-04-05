package com.essindia.stlapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.essindia.stlapp.Adapter.GRNVerificationAdapter;
import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Sqlite.SqliteConstantData;
import com.essindia.stlapp.Sqlite.SqliteHelper;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;
import com.essindia.stlapp.Utils.ValidationChecker;
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

import amobile.android.barcodesdk.api.IWrapperCallBack;


public class GRNVerificationList extends BaseActivity implements IWrapperCallBack, OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private RecyclerView recylerView;
    private LinearLayoutManager layoutManager;
    private GRNVerificationAdapter adapter;
    private ArrayList<GRN_Verification_Bean> beanList;
    private ConnectionDetector connectionDetector;
    private JSONObject loginparams;
    private UserPref userPref;
    private SqliteHelper db;
    private EditText editText;
    private static BarcodeReader bcr;
    private AidcManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grnverification_list);
        intializeXml();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText(""); // for reset incase when getting back from detail screen
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
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        beanList = new ArrayList<>();
        recylerView = (RecyclerView) findViewById(R.id.recylerView);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.grn_verify);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        grnVerificationListData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                if (data != null && !data.isEmpty()) {
                    if (dataSplite(data) == true) boxVerifier();
                    else {
                        AlertDialogManager.getInstance().simpleAlert(GRNVerificationList.this, "Alert", "Please enter valid data");
                    }
                } else {
                }
            }
        });
    }

    private void boxVerifier() {

        if (userPref.getOrgId() != null && Constants.QR_ROUTE_CARD_NO != null /*&& Constants.QR_ROUTE_DATE != null*/ && Constants.QR_STL_PART_NO != null && Constants.QR_REFERENCE_NO != null && Constants.QR_BOX_TYPE != null && !userPref.getOrgId().isEmpty() && !Constants.QR_REFERENCE_NO.isEmpty() && !Constants.QR_ROUTE_CARD_NO.isEmpty()
                /*&& !Constants.QR_ROUTE_DATE.isEmpty() */ && !Constants.QR_STL_PART_NO.isEmpty() && !Constants.QR_BOX_TYPE.isEmpty()) {

            //use below method for each single validation in route no .iteam code and route date
            //updated where clause sequence org id,grn no,grn date,item code,ir no,box type
            Context context = GRNVerificationList.this;
            GRN_Verification_Bean bean = db.checkIsDataDuplicateeAvailableInDb(userPref.getOrgId(), Constants.QR_ROUTE_CARD_NO, /*Constants.QR_ROUTE_DATE,*/ Constants.QR_STL_PART_NO, Constants.QR_REFERENCE_NO, Constants.QR_BOX_TYPE, context);
            if (bean != null) {
                Bundle b = new Bundle();
                b.putParcelable(SqliteConstantData.KEY_GRN_VERIFICATION_LIST, bean);
                editText.setText("");
                Intent form = new Intent(GRNVerificationList.this, GRNVerificationDetails.class);
                form.putExtras(b);
                startActivity(form);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } else {
                editText.setText("");
                System.out.println("data doesn't exist");
            }
        } else {
            editText.setText("");
            AlertDialogManager.getInstance().simpleAlert(GRNVerificationList.this, "Alert", "Data is empty !");
        }
    }

    private boolean dataSplite(String data) {

        boolean flag = false;

        String dataSp[] = data.split(Constants.DELIMITER);

        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(GRNVerificationList.this, "Alert", "Qr code is empty cannot load data item");
            flag = false;
        } else if (dataSp.length < 8 || dataSp.length > 8) {
            AlertDialogManager.getInstance().simpleAlert(GRNVerificationList.this, "Alert", "Please enter correct format !");
            flag = false;

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

            userPref.setActualQty(Constants.QR_ACTUAL_QTY);
            if (Constants.QR_ACTUAL_QTY != null && Constants.QR_STD_QTY != null && !Constants.QR_ACTUAL_QTY.isEmpty() && !Constants.QR_STD_QTY.isEmpty()) {
                String boxType = ValidationChecker.getBoxType(this, Constants.QR_ACTUAL_QTY, Constants.QR_STD_QTY);
                Constants.QR_BOX_TYPE = Constants.QR_STL_PART_NO + "/" + Constants.QR_STD_QTY + "/" + Constants.QR_BOX_TYPE + "-" + boxType;
            }
            flag = true;
        }
        return flag;
    }

    private void grnVerificationListData() {
        loginparams = new JSONObject();
        try {
            loginparams.put(Constants.auth_Token, userPref.getToken());
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(GRNVerificationList.this, Constants.GRN_VERIFICATION_LIST, loginparams.toString(), GRNVerificationList.this, 2, true);
                CallService.getInstance().getResponseUsingPOST(GRNVerificationList.this,userPref.getBASEUrl()+ Constants.GRN_VERIFICATION_LIST, loginparams.toString(), GRNVerificationList.this, 2, true);
            } else {
                beanList = db.getGRNVerificationList();
                adapter = new GRNVerificationAdapter(getApplicationContext(), beanList);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recylerView.setLayoutManager(layoutManager);
                recylerView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(GRNVerificationList.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onDataReady(final String strData) {
        byte[] bytes = strData.getBytes();
        System.out.println("data:" + strData);
        dataSplite(strData.trim());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(strData);
                boxVerifier();
            }
        });

    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onServiceDisConnected() {

    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == 2) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        JSONArray array = result.getJSONArray("Data");
                        for (int i = 0; i < array.length(); i++) {

                            GRN_Verification_Bean bean = new GRN_Verification_Bean();

                            JSONObject obj = array.getJSONObject(i);

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
                            System.out.println("DT mo date:" + obj.optString("DT_MOD_DATE"));
                            bean.setITEM_CODE(obj.optString("ITEM_CODE"));
                            bean.setROUTE_NO(obj.optString("ROUTE_NO"));
                            bean.setSCAN_FLAG(obj.optString("SCAN_FLAG"));
                            bean.setORG_ID(obj.optString("ORG_ID"));
                            bean.setEXCESS_FLAG(obj.optString("EXCESS_FLAG"));
                            bean.setVC_MACHINE_NO(obj.optString("VC_MACHINE_NO"));

                            bean.setVERIFIED_QTY(obj.optString("VERIFIED_QTY"));
                            bean.setPEND_QTY(obj.optString("PEND_QTY"));
                            bean.setITEM_DESC(obj.optString("ITEM_DESC"));

                            beanList.add(bean);
                        }
                        db.addGRNVerificationList(beanList);
                        adapter = new GRNVerificationAdapter(getApplicationContext(), beanList);
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recylerView.setLayoutManager(layoutManager);
                        recylerView.setAdapter(adapter);
                    } else {
                        confirmAlert("Alert", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(GRNVerificationList.this, "Alert", response.toString());
            }
        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataSplite(barcodeReadEvent.getBarcodeData().toString().trim());
                editText.setText(barcodeReadEvent.getBarcodeData());
                boxVerifier();
            }
        });
    }

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(GRNVerificationList.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.create();
        builder.show();

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
