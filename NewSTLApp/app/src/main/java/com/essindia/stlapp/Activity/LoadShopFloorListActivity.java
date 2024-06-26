package com.essindia.stlapp.Activity;

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

import com.essindia.stlapp.Adapter.LoadShopFloorListAdapter;
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

public class LoadShopFloorListActivity extends BaseActivity implements OnResponseFetchListener, LoadShopFloorListAdapter.iTranNoListClick, View.OnClickListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranList;
    private LinearLayoutManager layoutManager;
    private LoadShopFloorListAdapter adapter;
    private ArrayList<ReceiptItemBean> mTranNoList;
    private EditText mEtItemQrcode;
    private ImageView mIvVerify;
    private static BarcodeReader bcr;
    private String mRouteCardNo = "";
    private String mRouteCardDate = "";
    private String r_str = "";
    private AidcManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_shop_floor_list);
        intializeXml();
    }

    private void getLoadingShopFloorListData(String data) {
        JSONObject param = new JSONObject();
        try {
            param.put(Constants.auth_Token, mUserPref.getToken());
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());
            param.put(Constants.ORG_ID, mUserPref.getOrgId());
            param.put(ReqResParamKey.QR_CODE_DATA, data);

            String dataSp[] = data.trim().split(Constants.DELIMITER);
            mRouteCardNo = dataSp[2];
            mRouteCardDate = dataSp[3];
            r_str = data;

            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(LoadShopFloorListActivity.this, Constants.LOADING_SHOP_FLOOR_TRAN_NO_LIST, param.toString(), LoadShopFloorListActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(LoadShopFloorListActivity.this,mUserPref.getBASEUrl()+ Constants.LOADING_SHOP_FLOOR_TRAN_NO_LIST, param.toString(), LoadShopFloorListActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadShopFloorListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.RM_COIL_ISSUE_LIST_REQ_ID) {
            if (mTranNoList != null && mTranNoList.size() > 0) {
                mTranNoList.clear();
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
                                ReceiptItemBean bean = new ReceiptItemBean();
                                JSONObject obj = array.getJSONObject(i);
                                bean.setVcCompCode(obj.optString(ReqResParamKey.VC_COMP_CODE));
                                bean.setVcTranNo(obj.optString(ReqResParamKey.VC_TRAN_NO));
                                bean.setTranDate(obj.optString(ReqResParamKey.DT_TRAN_DATE));
                                bean.setMachineCode(obj.optString(ReqResParamKey.VC_MACHINE_CODE));
                                bean.setRouteCardNo(mRouteCardNo);
                                bean.setRouteCardDate(mRouteCardDate);
                                mTranNoList.add(bean);
                            }
                            adapter = new LoadShopFloorListAdapter(getApplicationContext(), mTranNoList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranList.setLayoutManager(layoutManager);
                            mRvTranList.setAdapter(adapter);
                        } else {
                            mTranNoList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");
                        }
                    } else {
                        confirmAlert("Alert", message);
                        mTranNoList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(LoadShopFloorListActivity.this, "Alert", response.toString());
            }
        }
    }

    @Override
    public void onTranNoListClick(int position) {
        if (mTranNoList != null && mTranNoList.size() > 0) {
            ReceiptItemBean bean = mTranNoList.get(position);
            if (bean.getTranDate() != null && !bean.getTranDate().isEmpty() && !bean.getTranDate().equalsIgnoreCase("null")) {
                String tranDate = DateUtil.ddMMyyyyToddMMMyyyy(bean.getTranDate());
                if (!tranDate.isEmpty()) {
                    Intent i = new Intent(LoadShopFloorListActivity.this, LoadShopFloorItemListActivity.class);
                    i.putExtra(BundleKey.RM_RECEIPT_TRAN_NO, bean.getVcTranNo());
                    i.putExtra(BundleKey.RM_RECEIPT_TRAN_DATE, tranDate);
                    i.putExtra(BundleKey.ROUTE_CARD_NO, mRouteCardNo);
                    i.putExtra(BundleKey.ROUTE_CARD_DATE, mRouteCardDate);
                    i.putExtra(BundleKey.R_STR, r_str);
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
                    getLoadingShopFloorListData(itemQrCode);
                }
            } else {
                showToast("Items Not Submit, Please try again !");
            }
        } else if (resultCode == RESULT_CANCELED) {
//            showToast("You cancelled the transaction, Please try again !");
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

   /* private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length < 11 || dataSp.length > 11) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorListActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 11) {
            getLoadingShopFloorListData(data);
        }
    }*/

    private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadShopFloorListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else {
            getLoadingShopFloorListData(data);
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
                Intent i = new Intent(LoadShopFloorListActivity.this, Dashboard.class);
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

        Intent i = new Intent(LoadShopFloorListActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvTranList = (RecyclerView) findViewById(R.id.rv_tran_list);
        mEtItemQrcode = (EditText) findViewById(R.id.et_item_qrcode);
        mIvVerify = (ImageView) findViewById(R.id.img_verify);
        mIvVerify.setOnClickListener(this);
        toolbar.setTitle(R.string.load_shop_floor);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        mTranNoList = new ArrayList<>();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

}
