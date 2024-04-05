package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.LoadingAdviceMRSCoilListAdapter;
import com.essindia.stlapp.Adapter.LoadingAdviceMRSListAdapter;
import com.essindia.stlapp.Bean.LoadingAdviceMRS;
import com.essindia.stlapp.Bean.LoadingAdviceMRSCoil;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Sqlite.SqliteHelper;
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

public class LoadingAdviceMRSCOILActivity extends BaseActivity implements OnResponseFetchListener, LoadingAdviceMRSCoilListAdapter.iTranNoListClick, View.OnClickListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranList;
    private LinearLayoutManager layoutManager;
    private EditText mEtItemQrcode;
    private ImageView mIvVerify;
    private Button btn_submit;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private ArrayList<LoadingAdviceMRSCoil> mMrsList;
    private LoadingAdviceMRSCoilListAdapter adapter;
    private LoadingAdviceMRS bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_advice_mrscoilactivity);
        bean = this.getIntent().getParcelableExtra(BundleKey.TAG_OBJ);
        if (bean != null) {
            intializeXml();
            getLoadingAdviceMRSCOILListData(bean);
            try {
                //int pending = Integer.parseInt(bean.getNU_TOTAL_CASES()) - Integer.parseInt(bean.getTOT_PROD_SCAN_BOX());
                //tv_pending_boxes.setText("Total Pending Boxes : " + pending);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
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
                if (mMrsList != null && mMrsList.size() > 0) {
                    for (LoadingAdviceMRSCoil coil : mMrsList) {
                        if (coil.getLOAD_STATUS().equalsIgnoreCase("Y")) {
                            callListItemSubmitApi(coil.getVC_ROUTE_NO(), coil.getDT_ROUTE_DATE());
                        }
                    }
                } else {
                    showToast("No item for submit");
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RM_COIL_ISSUE_LIST_SUBMIT_RESULT_REQ_ID) {
                String itemQrCode = mEtItemQrcode.getText().toString();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    // getLoadingAdviceMRSListData(itemQrCode);
                }
            } else {
                showToast("Items Not Submit, Please try again !");
            }
        } else if (resultCode == RESULT_CANCELED) {
            showToast("You cancelled the transaction, Please try again !");
        }
    }


    private void getLoadingAdviceMRSCOILListData(LoadingAdviceMRS bean) {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.TRAN_NO, bean.getTRAN_NO());
            param.put(ReqResParamKey.TRAN_DATE, bean.getTRAN_DATE());
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_LIST, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this, mUserPref.getBASEUrl()+Constants.LOADING_ADVICE_MRS_COIL_LIST, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.LOADING_ADVICE_MRS_COIL_REQ_ID) {
            if (mMrsList != null && mMrsList.size() > 0) {
                mMrsList.clear();
            }
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                LoadingAdviceMRSCoil bean = new LoadingAdviceMRSCoil();
                                JSONObject obj = array.getJSONObject(i);
                                bean.setGRAD(obj.optString(ReqResParamKey.GRAD));
                                bean.setVc_TAG_NO(obj.optString(ReqResParamKey.VC_TAG_NO));
                                bean.setVC_ROUTE_NO(obj.optString(ReqResParamKey.VC_ROUTE_NO));
                                bean.setVC_DIVISION(obj.optString(ReqResParamKey.VC_DIVISION));
                                bean.setMRIR_NO(obj.optString(ReqResParamKey.MRIR_NO));
                                bean.setMRIR_DATE(obj.optString(ReqResParamKey.MRIR_DATE));
                                bean.setCOIL_WEIGHT(obj.optString(ReqResParamKey.COIL_WEIGHT));
                                bean.setREQ_QUANTITY(obj.optString(ReqResParamKey.REQ_QUANTITY));
                                bean.setDT_ROUTE_DATE(obj.optString(ReqResParamKey.DT_ROUTE_DATE));
                                bean.setVC_S_HEAT_NO(obj.optString(ReqResParamKey.VC_S_HEAT_NO));
                                bean.setLOCATION_NAME(obj.optString(ReqResParamKey.LOCATION_NAME));
                                bean.setLOAD_STATUS(obj.optString(ReqResParamKey.LOAD_STATUS));
                                mMrsList.add(bean);
                            }
                            adapter = new LoadingAdviceMRSCoilListAdapter(getApplicationContext(), mMrsList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranList.setLayoutManager(layoutManager);
                            mRvTranList.setAdapter(adapter);
                            if (mMrsList != null && mMrsList.size() > 0) {
                                for (LoadingAdviceMRSCoil coil : mMrsList) {
                                    if (coil.getLOAD_STATUS().equalsIgnoreCase("Y")) {
                                        btn_submit.setVisibility(View.VISIBLE);
                                        break;
                                    }
                                }
                            } else {
                                showToast("No item for submit");
                            }
                        } else {
                           /* mMrsList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");*/
                        }
                    } else {
                        confirmAlert("Alert", message);
                       /* mMrsList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", response.toString());
            }
        } else if (request_id == Constants.LOADING_ADVICE_MRS_COIL_VERIFY) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        mEtItemQrcode.setText("");
                        if (statusCode.equalsIgnoreCase("0")) {
                            if (bean != null) {
                                getLoadingAdviceMRSCOILListData(bean);
                            }
                        } else {
                            AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", message);
                        }
                    } else {
                        confirmAlert("Alert", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (request_id == Constants.LOADING_ADVICE_MRS_COIL_SUBMIT) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        confirmAlert("Success", "MRS submit successfully");
                        showLongToast(message);
                    } else {
                        confirmAlert("Alert", message);
                       /* mMrsList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (barcodeReadEvent.getBarcodeData() != null && !barcodeReadEvent.getBarcodeData().equals("")) {
                    spliteData(barcodeReadEvent.getBarcodeData().trim());
                } else {
                    showToast("Item not Scanned.Try Again");
                }
            }
        });

    }

    private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            verifyLoadingAdviceMrsCoil(data);
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

    private void confirmAlert(final String alert, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (alert.equalsIgnoreCase("success")) {
                    Intent i = new Intent(LoadingAdviceMRSCOILActivity.this, Dashboard.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else {
                    dialog.dismiss();
                }

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }


    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvTranList = (RecyclerView) findViewById(R.id.rv_coil_list);
        mEtItemQrcode = (EditText) findViewById(R.id.et_coil_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        mIvVerify.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        toolbar.setTitle(R.string.load_advice_coil);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);
        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        mMrsList = new ArrayList<>();
    }

    @Override
    public void onTranNoListClick(int position) {

    }

    private void verifyLoadingAdviceMrsCoil(String qrCodeData) {
        JSONObject param = new JSONObject();
        try {

            param.put(ReqResParamKey.QR_CODE_DATA, qrCodeData);
            param.put(ReqResParamKey.GRAD, bean.getGRAD());
            param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.TRAN_NO, bean.getTRAN_NO());
            param.put(ReqResParamKey.TRAN_DATE, bean.getTRAN_DATE());
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            if (mConnectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this,  Constants.VERIFY_LOADING_ADVICE_MRS_COIL, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_VERIFY, true);
                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this, mUserPref.getBASEUrl()+Constants.VERIFY_LOADING_ADVICE_MRS_COIL, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_VERIFY, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callListItemSubmitApi(String route_no, String route_date) {
        try {
            JSONObject param = new JSONObject();
            param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.TRAN_NO, bean.getTRAN_NO());
            param.put(ReqResParamKey.TRAN_DATE, bean.getTRAN_DATE());
            param.put(ReqResParamKey.ROUTE_NO, route_no);
            param.put(ReqResParamKey.ROUTE_DATE, route_date);
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this,  Constants.SUBMIT_LOADING_ADVICE_MRS_COIL, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_SUBMIT, true);
                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSCOILActivity.this, mUserPref.getBASEUrl()+Constants.SUBMIT_LOADING_ADVICE_MRS_COIL, param.toString(), LoadingAdviceMRSCOILActivity.this, Constants.LOADING_ADVICE_MRS_COIL_SUBMIT, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSCOILActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}