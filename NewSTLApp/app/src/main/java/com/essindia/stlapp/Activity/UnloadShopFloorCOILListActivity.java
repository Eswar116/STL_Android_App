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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.UnloadShopFloorCoilDetailListAdapter;
import com.essindia.stlapp.Bean.CoilIssueItemDetailBean;
import com.essindia.stlapp.Bean.UnloadShopFloorDataBean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
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

public class UnloadShopFloorCOILListActivity extends BaseActivity implements OnResponseFetchListener, View.OnClickListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranList;
    private LinearLayoutManager layoutManager;
    private EditText mEtItemQrcode;
    private ImageView mIvVerify;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private RadioGroup radioGroup;
    private Button btn_proceed;
    private Button btn_submit;
    private RadioButton rbtn_route, rbtn_machine, rbtn_coil;
    private UnloadShopFloorDataBean unloadShopGetSet;
    private UnloadShopFloorCoilDetailListAdapter adapter;
    private ArrayList<CoilIssueItemDetailBean> mTranDetailList;
    private String routeCardNo = "";
    private String routecardDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload_shop_floor_coillist);
        intializeXml();
    }

    private void getUnLoadingShopFloorCoilListData() {
        JSONObject param = new JSONObject();
        try {

            String dataSp[] = unloadShopGetSet.getRoute_str().trim().split(Constants.DELIMITER);
            String productNo = dataSp[0];
            String orgId = dataSp[1];
            routeCardNo = dataSp[2];
            routecardDate = dataSp[3];

            String machineSp[] = unloadShopGetSet.getMachine_str().trim().split(Constants.DELIMITER);
            String processCode = machineSp[0];
            String machineCode = machineSp[1];

            String coilSp[] = unloadShopGetSet.getCoil_str().trim().split(Constants.DELIMITER);
            String mrirNo = coilSp[0];
            String mrirDate = coilSp[1];
            String vcRouteNo = coilSp[2];
            String dtRouteDate = coilSp[3];

            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.ROUTE_CARD_NO, routeCardNo);
            param.put(ReqResParamKey.ROUTE_CARD_DATE, routecardDate);
            param.put(ReqResParamKey.MRIR_NO, mrirNo);
            param.put(ReqResParamKey.MRIR_DATE, mrirDate);
            param.put(ReqResParamKey.MACHINE_CODE, machineCode);
            param.put(ReqResParamKey.VC_ROUTE_NO, vcRouteNo);
            param.put(ReqResParamKey.DT_ROUTE_DATE, dtRouteDate);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());

            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this,  Constants.UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_LIST_DETAIL_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this, mUserPref.getBASEUrl() + Constants.UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_LIST_DETAIL_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verifyUnLoadingShopFloorListData() {
        JSONObject param = new JSONObject();
        try {

            String dataSp[] = unloadShopGetSet.getRoute_str().trim().split(Constants.DELIMITER);
            String productNo = dataSp[0];
            String orgId = dataSp[1];
            routeCardNo = dataSp[2];
            routecardDate = dataSp[3];

            param.put(ReqResParamKey.P_STR, unloadShopGetSet.getCoil_str());
            param.put(ReqResParamKey.R_STR, unloadShopGetSet.getRoute_str());
            param.put(ReqResParamKey.M_STR, unloadShopGetSet.getMachine_str());
            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.ROUTE_CARD_NO, routeCardNo);
            param.put(ReqResParamKey.ROUTE_CARD_DATE, routecardDate);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//


            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this, Constants.VERIFY_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this, mUserPref.getBASEUrl() + Constants.VERIFY_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_SCAANED_ITEM_UPDATE_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitUnLoadingShopFloorCoilListData() {
        JSONObject param = new JSONObject();
        try {

            String dataSp[] = unloadShopGetSet.getRoute_str().trim().split(Constants.DELIMITER);
            String productNo = dataSp[0];
            String orgId = dataSp[1];
            routeCardNo = dataSp[2];
            routecardDate = dataSp[3];

            String machineSp[] = unloadShopGetSet.getMachine_str().trim().split(Constants.DELIMITER);
            String process_code = machineSp[0];
            String machine_code = machineSp[1];

            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.ROUTE_CARD_NO, routeCardNo);
            param.put(ReqResParamKey.ROUTE_CARD_DATE, routecardDate);
            if (mTranDetailList.size() > 0) {
                param.put(ReqResParamKey.TRAN_NO, mTranDetailList.get(0).getTranNo());
                param.put(ReqResParamKey.TRAN_DATE, mTranDetailList.get(0).getTranDate());
            }
            param.put(ReqResParamKey.MACHINE_CODE, machine_code);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//

            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this,  Constants.SUBMIT_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(UnloadShopFloorCOILListActivity.this,mUserPref.getBASEUrl() + Constants.SUBMIT_UNLOADING_SHOP_FLOOR_TRAN_COIL_LIST, param.toString(), UnloadShopFloorCOILListActivity.this, Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                                bean.setLoadStatus(obj.optString(ReqResParamKey.UNLOAD_STATUS));
                                bean.setTagNo(obj.optString(ReqResParamKey.VC_TAG_NO));
                                bean.setVcDivision(obj.optString(ReqResParamKey.VC_DIVISION));
                                bean.setRouteCardNo(routeCardNo);
                                bean.setRouteCardDate(routecardDate);

                                mTranDetailList.add(bean);
                            }
                            adapter = new UnloadShopFloorCoilDetailListAdapter(getApplicationContext(), mTranDetailList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranList.setLayoutManager(layoutManager);
                            mRvTranList.setAdapter(adapter);
                            for (CoilIssueItemDetailBean coil : mTranDetailList) {
                                coil.setLoadStatus("Y");
                            }
                            adapter.notifyDataSetChanged();
                            //verifyUnLoadingShopFloorListData();
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
                        getUnLoadingShopFloorCoilListData();
                        btn_submit.setVisibility(View.VISIBLE);
                    } else {
                        btn_submit.setVisibility(View.GONE);
                        AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", message);
                    }
                } else if (request_id == Constants.RM_COIL_ISSUE_LIST_SUBMIT_REQ_ID) {
                    if (statusCode.equalsIgnoreCase("0")) {
                        showPostSuccessAlert("Success", "Unloading done successfully");
                        showLongToast(message);
                        goBackTranListScreen();
                    } else {
                        showPostFailureAlert("Alert", message);
                        showLongToast(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Server not responding.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_verify:
                String itemQrCode = mEtItemQrcode.getText().toString().trim();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    isDataValid(itemQrCode);
                } else showToast("Please Enter Item QR Code");
                break;
            case R.id.btn_proceed:
                if (isEdittextvalid()) {
                    if (isSubmitDataValid()) {
                        //getUnLoadingShopFloorCoilListData();
                        verifyUnLoadingShopFloorListData();
                    } else {
                        showToast("Data Invalid");
                    }

                } else {
                    showToast("Please select the radiobutton first");
                }
                break;
            case R.id.btn_submit:
                if (isEdittextvalid()) {
                    if (isSubmitDataValid()) {
                        submitUnLoadingShopFloorCoilListData();
                    } else {
                        showToast("Data Invalid");
                    }
                } else {
                    showToast("Please select the radiobutton first");
                }
                break;

        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (barcodeReadEvent.getBarcodeData() != null && !barcodeReadEvent.getBarcodeData().equals("")) {
                    Log.v("QR_read_data", barcodeReadEvent.getBarcodeData());
                    isDataValid(barcodeReadEvent.getBarcodeData().toString().trim());
                } else {
                    showToast("Item not Scanned.Try Again");
                }
            }
        });

    }

    private boolean spliteRouteData(String data, boolean isvalid) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            unloadShopGetSet.setRouteValid(false);
            unloadShopGetSet.setRoute_str("");
            isvalid = false;
            rbtn_route.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            unloadShopGetSet.setRouteValid(true);
            unloadShopGetSet.setRoute_str(data);
            isvalid = true;
            rbtn_route.setTextColor(getResources().getColor(R.color.green));
            if (isvalid) {
                rbtn_machine.setChecked(true);
            }
        }

        /*else if (dataSp.length < 11 || dataSp.length > 11) {
            unloadShopGetSet.setRouteValid(false);
            unloadShopGetSet.setRoute_str("");
            mEtItemQrcode.setText("");
            isvalid = false;
            rbtn_route.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 11) {
            unloadShopGetSet.setRouteValid(true);
            unloadShopGetSet.setRoute_str(data);
            isvalid = true;
            rbtn_route.setTextColor(getResources().getColor(R.color.green));
            if (isvalid) {
                rbtn_machine.setChecked(true);
            }
        }*/
        return isvalid;
    }

    private boolean spliteMachineData(String data, boolean isvalid) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            unloadShopGetSet.setMachineValid(false);
            unloadShopGetSet.setMachine_str("");
            isvalid = false;
            rbtn_machine.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length < 2 || dataSp.length > 2) {
            unloadShopGetSet.setMachineValid(false);
            unloadShopGetSet.setMachine_str("");
            mEtItemQrcode.setText("");
            isvalid = false;
            rbtn_machine.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 2) {
            unloadShopGetSet.setMachineValid(true);
            unloadShopGetSet.setMachine_str(data);
            isvalid = true;
            rbtn_machine.setTextColor(getResources().getColor(R.color.green));
            if (isvalid) {
                rbtn_coil.setChecked(true);
            }
        }
        return isvalid;
    }

    private boolean spliteCoilData(String data, boolean isvalid) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            unloadShopGetSet.setCoilValid(false);
            unloadShopGetSet.setCoil_str("");
            isvalid = false;
            rbtn_coil.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            unloadShopGetSet.setCoilValid(true);
            unloadShopGetSet.setCoil_str(data);
            isvalid = true;
            rbtn_coil.setTextColor(getResources().getColor(R.color.green));
        }
        /*else if (dataSp.length < 13 || dataSp.length > 13) {
            unloadShopGetSet.setCoilValid(false);
            unloadShopGetSet.setCoil_str("");
            mEtItemQrcode.setText("");
            isvalid = false;
            rbtn_coil.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialogManager.getInstance().simpleAlert(UnloadShopFloorCOILListActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 13) {
            unloadShopGetSet.setCoilValid(true);
            unloadShopGetSet.setCoil_str(data);
            isvalid = true;
            rbtn_coil.setTextColor(getResources().getColor(R.color.green));
        }*/
        return isvalid;
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

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(UnloadShopFloorCOILListActivity.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (mTranDetailList.size() > 0) {
            confirmAlert("Record is not submit!", "Do you want to go back without submit the record");
        } else {
            goBackTranListScreen();
        }
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvTranList = (RecyclerView) findViewById(R.id.rv_tran_list);
        mEtItemQrcode = (EditText) findViewById(R.id.et_item_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        mIvVerify.setOnClickListener(this);
        toolbar.setTitle(R.string.unload_shop_floor);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackTranListScreen();

            }
        });

        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbtn_route = (RadioButton) findViewById(R.id.rbtn_route);
        rbtn_machine = (RadioButton) findViewById(R.id.rbtn_machine);
        rbtn_coil = (RadioButton) findViewById(R.id.rbtn_coil);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_proceed.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        unloadShopGetSet = new UnloadShopFloorDataBean();
        btn_submit.setVisibility(View.GONE);
        rbtn_route.setChecked(true);
        mTranDetailList = new ArrayList<>();
    }

    public boolean isEdittextvalid() {
        boolean isvalid = false;
        int id = radioGroup.getCheckedRadioButtonId();
        if (id != R.id.rbtn_route && id != R.id.rbtn_machine && id != R.id.rbtn_coil) {
            mEtItemQrcode.setEnabled(false);
            isvalid = false;
        } else {
            mEtItemQrcode.setEnabled(true);
            isvalid = true;
        }
        return isvalid;
    }

    public boolean isDataValid(String data) {
        boolean isvalid = false;
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == R.id.rbtn_route) {
            isvalid = spliteRouteData(data, isvalid);
        } else if (id == R.id.rbtn_machine) {
            isvalid = spliteMachineData(data, isvalid);
        } else if (id == R.id.rbtn_coil) {
            isvalid = spliteCoilData(data, isvalid);
        } else {
            showToast("Please select the radiobutton first");
            isvalid = false;
        }
        return isvalid;
    }

    public boolean isSubmitDataValid() {
        boolean isValid = false;
        if (unloadShopGetSet.isRouteValid() && unloadShopGetSet.getRoute_str() != null && !unloadShopGetSet.getRoute_str().equals("")) {
            isValid = true;
        } else {
            isValid = false;
            showToast("Please scan route data");
            rbtn_route.setChecked(true);
        }

        if (isValid) {
            if (unloadShopGetSet.isMachineValid() && unloadShopGetSet.getMachine_str() != null && !unloadShopGetSet.getMachine_str().equals("")) {
                isValid = true;
            } else {
                isValid = false;
                showToast("Please scan machine data");
                rbtn_machine.setChecked(true);
            }
        }

        if (isValid) {
            if (unloadShopGetSet.isCoilValid() && unloadShopGetSet.getCoil_str() != null && !unloadShopGetSet.getCoil_str().equals("")) {
                isValid = true;
            } else {
                isValid = false;
                showToast("Please scan coil data");
                rbtn_coil.setChecked(true);
            }
        }
        return isValid;
    }

    private void showPostSuccessAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //submitRecord(true);
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

    private void goBackTranListScreen() {
        Intent i = new Intent(UnloadShopFloorCOILListActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

}
