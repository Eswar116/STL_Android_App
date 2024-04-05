package com.essindia.stlapp.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.WarehouseLoadingListAdapter;
import com.essindia.stlapp.Bean.InvoiceBean;
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

public class WHLoadingInvoiceActivity extends BaseActivity implements OnResponseFetchListener, WarehouseLoadingListAdapter.iTranNoListClick, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    //    private ImageView imageView;
    private Button imageView;
    private LinearLayoutManager layoutManager;
    private WarehouseLoadingListAdapter adapter;
    private ConnectionDetector connectionDetector;
    private JSONObject loginparams;
    private UserPref userPref;
    private SqliteHelper db;
    private EditText editText, editText2;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private ArrayList<InvoiceBean> mInvoiceList;
    private RecyclerView mRvInvoiceList;
    int i = 0;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_loading);
        intializeXml();
        editText.setInputType(InputType.TYPE_NULL);

        String token = getResources().getString(R.string.location_id);


    }

    private void intializeXml() {
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        mRvInvoiceList = (RecyclerView) findViewById(R.id.mRvInvoiceList);
        imageView = findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText_product);
        error_message = findViewById(R.id.error_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.warehouse_loading);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackScreen();
            }
        });

        mInvoiceList = new ArrayList<>();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (editText.getText().toString().length() > 0) {
                    spliteData(editText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
//                String data_ = editText2.getText().toString();
                if (data != null && !data.isEmpty()) {
                    spliteData(data);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceActivity.this, "Alert", "Please enter valid data");
                }
            }
        });
    }

    private void spliteData(String data) {
        String dataSp[] = data.split(Constants.DELIMITER);

        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceActivity.this, "Alert", "Qr code is empty cannot load data invoice");
        } else {
            getWarehouseLoadingInvoiceData(data);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.RM_COIL_ISSUE_LIST_REQ_ID) {
            if (mInvoiceList != null && mInvoiceList.size() > 0) {
                mInvoiceList.clear();
            }
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        error_message.setVisibility(View.GONE);
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                Gson gson = new Gson();
                                InvoiceBean invoiceBean = gson.fromJson(String.valueOf(obj), InvoiceBean.class);
                                mInvoiceList.add(invoiceBean);
                            }
                            adapter = new WarehouseLoadingListAdapter(getApplicationContext(), mInvoiceList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvInvoiceList.setLayoutManager(layoutManager);
                            mRvInvoiceList.setAdapter(adapter);
                            editText.getText().clear();
                            editText2.getText().clear();
                            i = 0;
                        } else {
                            mInvoiceList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            showToast("No Invoices");
                        }
                    } else {
//                        confirmAlert("Alert", message);
                        mInvoiceList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        showToast("No Invoices");
                        error_message.setVisibility(View.VISIBLE);
                        error_message.setText(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceActivity.this, "Alert", response.toString());
            }
        }
        editText.getText().clear();
        editText.setText("");
        editText.isCursorVisible();
        editText.requestFocus();


    }

    private void getWarehouseLoadingInvoiceData(String data) {
        JSONObject param = new JSONObject();
        try {
            param.put(Constants.auth_Token, userPref.getToken());
            param.put(ReqResParamKey.VC_USER_CODE, userPref.getUserName());
            param.put(Constants.ORG_ID, userPref.getOrgId());
            param.put(ReqResParamKey.INVOICE_STR, data);
//            param.put(ReqResParamKey.PLANT_INVOICE_QR_CODE, editText2.getText().toString().trim());
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceActivity.this, Constants.GET_WAREHOUSE_LOADING_INVOICE_API, param.toString(), WHLoadingInvoiceActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceActivity.this, userPref.getBASEUrl() + Constants.GET_WAREHOUSE_LOADING_INVOICE_API, param.toString(), WHLoadingInvoiceActivity.this, Constants.RM_COIL_ISSUE_LIST_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RM_COIL_ISSUE_LIST_SUBMIT_RESULT_REQ_ID) {
                /*String itemQrCode = mEtItemQrcode.getText().toString();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    getLoadingShopFloorListData(itemQrCode);
                }*/
            } else {
                showToast("Items Not Submit, Please try again !");
            }
        } else if (resultCode == RESULT_CANCELED) {
//            showToast("You cancelled the transaction, Please try again !");
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

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(WHLoadingInvoiceActivity.this, Dashboard.class);
                Intent i = new Intent(WHLoadingInvoiceActivity.this, WHLoadingInvoiceActivity.class);
                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onTranNoListClick(int position) {
        if (mInvoiceList != null && mInvoiceList.size() > 0) {
            InvoiceBean bean = mInvoiceList.get(position);
            if (bean.getVC_INVOICE_NO() != null && !bean.getVC_INVOICE_NO().isEmpty() && !bean.getVC_INVOICE_NO().equalsIgnoreCase("null")) {
                Intent i = new Intent(WHLoadingInvoiceActivity.this, WHLoadingInvoiceProductActivity.class);
                i.putExtra(BundleKey.TAG_OBJ, bean);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            } else {
                showToast("Transaction Date Is Empty");
            }
        }

    }


    private void callBackScreen() {
        Intent i = new Intent(WHLoadingInvoiceActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callBackScreen();
    }
}