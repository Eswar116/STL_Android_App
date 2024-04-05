package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.DockAdapter;
import com.essindia.stlapp.Bean.GRNBean;
import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Sqlite.SqliteConstantData;
import com.essindia.stlapp.Sqlite.SqliteHelper;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GRNVerificationDetails extends AppCompatActivity implements OnResponseFetchListener {

    private Toolbar toolbar;
    private Button button2;
    private EditText grnNoEt, grnDateEt, productEt, irNoEt, packSizeEt, actualPackageSizeEt, VerifiedEt, veriyingEt, pendingEt, grnQtyEt, productDescriptionEt;
    private ConnectionDetector connectionDetector;
    private JSONObject loginparams;
    private UserPref userPref;
    GRN_Verification_Bean bean;
    private String pendingQty, binDesc, binNo;
    private SqliteHelper db;
    private String actualQty;
    private DateFormat dateFormat;
    private Date date;
    //    private RadioGroup radioGp;
    private LinearLayout locationUI;
    //    private RadioButton radioButton;
//    private Spinner spLocation;
    private ArrayList<GRNBean> dockList;
    private DockAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_test);
        currentDateTime();
        intializeXml();
        System.out.println("device id:" + userPref.getUserDeviceId());
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!veriyingEt.getText().toString().isEmpty()) {
                    int verifyingInt = Integer.parseInt(veriyingEt.getText().toString());
                    if (verifyingInt > 0) {
                        if (isPendingBoxChecked()) {
                            if (isVerifiedBoxChecker()) {

                                if (isActualQtyChecker()) {
                                    if (isPartiallyChecked()) {
                                        confirmAlert(bean);
//                                        db.addGRNVerificationLog(bean); as per resolving uat bug
                                    } else {
                                        syncingData();
                                        db.addGRNVerificationLog(bean);
                                    }
                                } else {
                                    AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "Actual Qty cannot more than Excess Qty:" + " " + bean.getEXCESS_PACK_QTY());
                                }
                            } else {
                                AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "Verifying box cannot be greater than GRN Qty.");
                            }
                        } else {
                            AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "Verifying box cannot be greater than pending box.");
                        }
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "GRN quantity cannot be Zero");
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "Verifying Qty cannot be empty.");
                }
            }
        });
    }

    private void confirmAlert(final GRN_Verification_Bean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Error");
        builder.setMessage(R.string.alert_grn);
        builder.setNegativeButton(R.string.grn_alert_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), R.string.grn_verification_cancelled, Toast.LENGTH_LONG).show();
            }
        });
        builder.setPositiveButton(R.string.grn_alert_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isPartiallyFlagChecked()) {
                    syncingData();
                    db.addGRNVerificationLog(bean);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "User have no rights to do partial verification!");
                }
            }
        });
//        builder.setPositiveButton(R.string.grn_alert_no, null);
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    private boolean isVerifiedBoxChecker() {
        int grnBox = Integer.parseInt(grnQtyEt.getText().toString());
        int verified = Integer.parseInt(veriyingEt.getText().toString());

        if (grnBox >= verified) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isActualQtyChecker() {
        int actualQtyInInt = Integer.parseInt(actualQty);
        int excessQty = Integer.parseInt(bean.getEXCESS_PACK_QTY());
        if (excessQty >= actualQtyInInt) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPendingBoxChecked() {
        int pendingBox = Integer.parseInt(pendingEt.getText().toString());
        int verified = Integer.parseInt(veriyingEt.getText().toString());
        if (pendingBox >= verified) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPartiallyChecked() {
        int pendingBox = Integer.parseInt(pendingEt.getText().toString().trim());
        int verifying = Integer.parseInt(veriyingEt.getText().toString().trim());
        int remainQty = pendingBox - verifying;
        pendingQty = String.valueOf(remainQty);
        if (remainQty > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPartiallyFlagChecked() {
        System.out.println(bean.getEXCESS_FLAG());
        if (bean.getEXCESS_FLAG().equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }


    private void syncingData() {
//        pendingQty = String.valueOf(Integer.parseInt(grnQtyEt.getText().toString()) - Integer.parseInt(veriyingEt.getText().toString()));
        loginparams = new JSONObject();

//        Map mp = new HashMap();
//        mp.put("auth_Token", userPref.getToken().toString());

        try {
            loginparams.put(Constants.auth_Token, userPref.getToken());

            loginparams.put("HO_ORG_ID", bean.getHO_ORG_ID());
            loginparams.put("ORG_ID", bean.getORG_ID());
            loginparams.put("SLOC_ID", bean.getSLOC_ID());
            loginparams.put("PROJ_ID", bean.getPROJ_ID());
            loginparams.put("CLD_ID", bean.getCLD_ID());

            loginparams.put("VERIFICATION_TIMESTAMP", dateFormat.format(date));
            loginparams.put("USER_ID", userPref.getUserDeviceId());
            loginparams.put("DEVICE_ID", userPref.getUserDeviceId());

            loginparams.put("ROUTE_NO", bean.getROUTE_NO());
            loginparams.put("ROUTE_DATE", bean.getROUTE_DATE());
            loginparams.put("TRAN_NO", bean.getTRAN_NO());
            loginparams.put("TRAN_DATE", bean.getTRAN_date());

            loginparams.put("IR_NO", bean.getIR_NO());
            loginparams.put("ITEM_CODE", bean.getITEM_CODE());
            loginparams.put("PACK_TYPE", bean.getPACK_TYPE());
            loginparams.put("PACK_SIZE", bean.getPACK_SIZE());
            loginparams.put("NO_OF_BOXES", veriyingEt.getText().toString());
            loginparams.put("BOX_ID", "BOX1");
            loginparams.put("DT_MOD_DATE", bean.getDT_MOD_DATE());
            loginparams.put("PEND_QTY", pendingQty.toString());
            loginparams.put(ReqResParamKey.EXCESS_PACK_QTY,bean.getEXCESS_PACK_QTY());

            //additional params after discussion with client
//            loginparams.put("Dock_Location","");
//            loginparams.put("Bin_No","");


//            mp.put("log_Data", loginparams);
//            System.out.println("data:" + mp);
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(GRNVerificationDetails.this, Constants.GRN_VERIFICATION_LOG_POST, loginparams.toString(), GRNVerificationDetails.this, 3, true);
                CallService.getInstance().getResponseUsingPOST(GRNVerificationDetails.this, userPref.getBASEUrl()+Constants.GRN_VERIFICATION_LOG_POST, loginparams.toString(), GRNVerificationDetails.this, 3, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert!!", "Not an internet connection");
            }
        } catch (Exception e) {
            AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert!!", e.toString());
        }
    }

    private void intializeXml() {
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        dockList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button2 = (Button) findViewById(R.id.button2);
        toolbar.setTitle(R.string.grn_verify_details);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        setSupportActionBar(toolbar);


//        locationUI = (LinearLayout) findViewById(R.id.locationUI);
//        spLocation = (Spinner) findViewById(spLocation);
        grnNoEt = (EditText) findViewById(R.id.grnNoEt);
        grnDateEt = (EditText) findViewById(R.id.grnDateEt);
        productEt = (EditText) findViewById(R.id.productEt);
        irNoEt = (EditText) findViewById(R.id.irNoEt);
        packSizeEt = (EditText) findViewById(R.id.packSizeEt);
        actualPackageSizeEt = (EditText) findViewById(R.id.actualPackageSizeEt);
        VerifiedEt = (EditText) findViewById(R.id.VerifiedEt);
        veriyingEt = (EditText) findViewById(R.id.verifyingEt);
        pendingEt = (EditText) findViewById(R.id.pendingEt);
        grnQtyEt = (EditText) findViewById(R.id.grnQtyEt);
//        radioGp = (RadioGroup) findViewById(radioGp);
        productDescriptionEt = (EditText) findViewById(R.id.productDescriptionEt);

        beanData();
//        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                binDesc = dockList.get(i).getBIN_DESC();
//                binNo = dockList.get(i).getBIN_NO();
//                System.out.println("sea:" + binDesc + "bin no:" + binNo);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        radioGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                radioButton = (RadioButton) findViewById(selectedId);
//                if (radioButton.getText().equals("Bond Room")) {
//                    locationUI.setVisibility(View.GONE);
//                } else {
//                    loginparams = new JSONObject();
//                    locationUI.setVisibility(View.VISIBLE);
//                    try {
//                        loginparams.put(Constants.auth_Token, userPref.getToken());
//                        loginparams.put(Constants.GRN_VERIFICATION_DOCK_ITEM_CODE, bean.getITEM_CODE());
//                        loginparams.put(Constants.GRN_VERIFICATION_DOCK_PACK_SIZE, bean.getPACK_SIZE());
//                        loginparams.put(Constants.GRN_VERIFICATION_DOCK_ORG_ID, bean.getORG_ID());
//                        if (connectionDetector.isConnectingToInternet()) {
//                            CallService.getInstance().getResponseUsingPOST(GRNVerificationDetails.this, Constants.POST_DOCK_LOCATION, loginparams.toString(), GRNVerificationDetails.this, Constants.POST_DOCK_LOCATION_REQ_ID, true);
//                        } else {
//                            AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert", "No Internet Connection");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    private void beanData() {
        Bundle b = getIntent().getExtras();
        if (userPref != null && b != null) {
            actualQty = userPref.getActualQty().toString().trim();
            bean = b.getParcelable(SqliteConstantData.KEY_GRN_VERIFICATION_LIST);
            grnNoEt.setText(bean.getROUTE_NO());
            grnDateEt.setText(bean.getROUTE_DATE());
            productEt.setText(bean.getITEM_CODE());
            irNoEt.setText(bean.getIR_NO());
            packSizeEt.setText(bean.getPACK_SIZE());
            actualPackageSizeEt.setText(actualQty);
            VerifiedEt.setText(bean.getVERIFIED_QTY());
//            veriyingEt.setText("0");
            pendingEt.setText(bean.getPEND_QTY());
            grnQtyEt.setText(bean.getNO_OF_BOXES());
            productDescriptionEt.setText(bean.getITEM_DESC());
            System.out.println(bean.getEXCESS_FLAG());
        }
    }

    private void currentDateTime() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();
        System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(GRNVerificationDetails.this, GRNVerificationList.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == 3) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String statusCode = result.optString("MessageCode");
                    String message = result.optString("Message");
                    System.out.println("response:" + response);
                    if (statusCode.equalsIgnoreCase("0")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(GRNVerificationDetails.this, GRNVerificationList.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,
                                android.R.anim.slide_out_right);
                        finish();
                    } else if (statusCode.equalsIgnoreCase("1")) {
                        AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert!!", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
//        else if (request_id == Constants.POST_DOCK_LOCATION_REQ_ID) {
//            if (response != null) {
//                System.out.println("response:" + response);
//                JSONObject result = null;
//                try {
//                    result = new JSONObject(response);
//                    String statusCode = result.optString("MessageCode");
//                    String message = result.optString("Message");
//                    dockList.clear();
//                    if (statusCode.equalsIgnoreCase("0")) {
//                        JSONArray array = result.getJSONArray("Data");
//                        for (int i = 0; i < array.length(); i++) {
//                            GRNBean bean = new GRNBean();
//                            JSONObject obj = array.getJSONObject(i);
//                            bean.setBIN_DESC(obj.optString("BIN_DESC"));
//                            bean.setBIN_NO(obj.optString("BIN_NO"));
//                            dockList.add(bean);
//                        }
////                        adapter = new DockAdapter(dockList);
////                        spLocation.setAdapter(adapter);
////                        Intent i = new Intent(GRNVerificationDetails.this, GRNVerificationList.class);
////                        startActivity(i);
////                        finish();
//                    } else if (statusCode.equalsIgnoreCase("1")) {
//                        AlertDialogManager.getInstance().simpleAlert(GRNVerificationDetails.this, "Alert!!", message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
