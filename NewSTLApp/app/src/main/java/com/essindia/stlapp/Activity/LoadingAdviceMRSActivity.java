package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.LoadShopFloorListAdapter;
import com.essindia.stlapp.Adapter.LoadingAdviceMRSListAdapter;
import com.essindia.stlapp.Bean.LoadingAdviceMRS;
import com.essindia.stlapp.Bean.ReceiptItemBean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.DateUtil;
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

public class LoadingAdviceMRSActivity extends BaseActivity implements OnResponseFetchListener, LoadingAdviceMRSListAdapter.iTranNoListClick, View.OnClickListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

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
    private ArrayList<LoadingAdviceMRS> mMrsList;
    private LoadingAdviceMRSListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_advice_mrsactivity);
        intializeXml();
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RM_COIL_ISSUE_LIST_SUBMIT_RESULT_REQ_ID) {
                String itemQrCode = mEtItemQrcode.getText().toString();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    getLoadingAdviceMRSListData(itemQrCode);
                }
            } else {
                showToast("Items Not Submit, Please try again !");
            }
        } else if (resultCode == RESULT_CANCELED) {
           showToast("You cancelled the transaction, Please try again !");
        }
    }

    @Override
    public void onTranNoListClick(int position) {
        if (mMrsList != null && mMrsList.size() > 0) {
            LoadingAdviceMRS bean = mMrsList.get(position);
            if (bean.getTRAN_DATE() != null && !bean.getTRAN_DATE().isEmpty() && !bean.getTRAN_DATE().equalsIgnoreCase("null")) {
                if (!bean.getTRAN_DATE().isEmpty()) {
                    Intent i = new Intent(LoadingAdviceMRSActivity.this, LoadingAdviceMRSCOILActivity.class);
                    i.putExtra(BundleKey.TAG_OBJ, bean);
                    startActivityForResult(i, Constants.RM_COIL_ISSUE_LIST_SUBMIT_RESULT_REQ_ID);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    showToast("Invalid Transaction Date");
                }
            } else {
                showToast("Transaction Date Is Empty");
            }
        }

    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.RM_COIL_ISSUE_LIST_REQ_ID) {
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
                                LoadingAdviceMRS bean = new LoadingAdviceMRS();

                                JSONObject obj = array.getJSONObject(i);
                                bean.setTRAN_NO(obj.optString(ReqResParamKey.TRAN_NO));
                                bean.setTRAN_DATE(obj.optString(ReqResParamKey.TRAN_DATE));
                                bean.setGRAD(obj.optString(ReqResParamKey.GRAD));
                                bean.setLOCATION_NAME(obj.optString(ReqResParamKey.LOCATION_NAME));
                                mMrsList.add(bean);
                            }
                            adapter = new LoadingAdviceMRSListAdapter(getApplicationContext(), mMrsList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranList.setLayoutManager(layoutManager);
                            mRvTranList.setAdapter(adapter);
                        } else {
                            mMrsList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");
                        }
                    } else {
                        confirmAlert("Alert", message);
                        mMrsList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSActivity.this, "Alert", response.toString());
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
            AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            getLoadingAdviceMRSListData(data);
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

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(LoadingAdviceMRSActivity.this, Dashboard.class);
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
       super.onBackPressed();
        onBackPres();
    }

    public void onBackPres(){
        Intent i = new Intent(LoadingAdviceMRSActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    private void getLoadingAdviceMRSListData(String data) {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.MRS_STR, data);

            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSActivity.this,  Constants.LOADING_ADVICE_MRS_LIST, param.toString(), LoadingAdviceMRSActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(LoadingAdviceMRSActivity.this,mUserPref.getBASEUrl()+ Constants.LOADING_ADVICE_MRS_LIST, param.toString(), LoadingAdviceMRSActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadingAdviceMRSActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvTranList = (RecyclerView) findViewById(R.id.rv_mrs_list);
        mEtItemQrcode = (EditText) findViewById(R.id.et_mrs_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        mIvVerify.setOnClickListener(this);
        toolbar.setTitle(R.string.load_advice_mrs);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(android.R.color.white);
        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        mMrsList = new ArrayList<>();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        // or call onBackPressed()
        return true;
    }

}