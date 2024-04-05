package com.essindia.stlapp.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.WarehouseLoadingProductListAdapter;
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
import com.essindia.stlapp.Utils.DateUtil;
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

public class WHLoadingInvoiceProductActivity extends BaseActivity implements OnResponseFetchListener, WarehouseLoadingProductListAdapter.iTranNoListClick, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private LinearLayoutManager layoutManager;
    private WarehouseLoadingProductListAdapter adapter;
    private ConnectionDetector connectionDetector;
    private JSONObject loginparams;
    private UserPref userPref;
    private SqliteHelper db;
    private EditText editText, plant_product_invoice;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private ArrayList<InvoiceBean> mInvoicePartList;
    private RecyclerView mRvInvoiceList;
    private InvoiceBean bean;
    private TextView tv_boxes, tv_scanned_boxes, tv_pending_boxes,error_message;
    int total_counter = 0;
    int PRODUCT_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whloading_invoice_product);

    

        bean = this.getIntent().getParcelableExtra(BundleKey.TAG_OBJ);
        if (bean != null) {
            intializeXml();

            getWarehouseLoadingInvoiceProductData();
            tv_boxes.setText("Total Invoice Boxes : " + bean.getNU_TOTAL_CASES());
            tv_scanned_boxes.setText("Total Scanned Boxes : " + bean.getTOT_PROD_SCAN_BOX());

            total_counter = Integer.parseInt(bean.getTOT_PROD_SCAN_BOX());
            try {
                int pending = Integer.parseInt(bean.getNU_TOTAL_CASES()) - Integer.parseInt(bean.getTOT_PROD_SCAN_BOX());
                tv_pending_boxes.setText("Total Pending Boxes : " + pending);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        plant_product_invoice.setInputType(InputType.TYPE_NULL);
        editText.setInputType(InputType.TYPE_NULL);
        plant_product_invoice.requestFocus();
        plant_product_invoice.isCursorVisible();
    }

    private void intializeXml() {
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        mRvInvoiceList = (RecyclerView) findViewById(R.id.mRvInvoiceList);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        plant_product_invoice = findViewById(R.id.plant_product_invoice);
        tv_boxes = (TextView) findViewById(R.id.tv_boxes);
        error_message = (TextView) findViewById(R.id.error_message);
        tv_scanned_boxes = (TextView) findViewById(R.id.tv_scanned_boxes);
        tv_pending_boxes = (TextView) findViewById(R.id.tv_pending_boxes);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.warehouse_loading);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackScreen();
            }
        });
        mInvoicePartList = new ArrayList<>();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                String data_ = plant_product_invoice.getText().toString();
                if (data != null && !data.isEmpty() && data_ != null && !data_.isEmpty()) {
                    error_message.setVisibility(View.INVISIBLE);
                    spliteData(data);

                } else {
                    AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", "Please enter valid data");
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String data = editText.getText().toString();
//                String data_ = plant_product_invoice.getText().toString();
//                if (data != null && !data.isEmpty() && data_ != null && !data_.isEmpty()) {
//                    error_message.setVisibility(View.INVISIBLE);
//                    spliteData(data);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String data = editText.getText().toString();
                String data_ = plant_product_invoice.getText().toString();
                if (data != null && !data.isEmpty() && data_ != null && !data_.isEmpty()) {
                    error_message.setVisibility(View.INVISIBLE);
                    spliteData(data);
                }
            }
        });


    }


    private void spliteData(String data) {
        String dataSp[] = data.split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", "Qr code is empty cannot load data invoice");
        } else {
            submitWarehouseLoadingInvoiceProductData(data);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.GET_WH_INVOICE_PRODUCT_REQ_ID) {
            if (mInvoicePartList != null && mInvoicePartList.size() > 0) {
                mInvoicePartList.clear();
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
                                tv_scanned_boxes.setText("Total Scanned Boxes : " + invoiceBean.getTOT_PROD_SCAN_BOX());
                                mInvoicePartList.add(invoiceBean);

                            }
                            adapter = new WarehouseLoadingProductListAdapter(getApplicationContext(), mInvoicePartList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvInvoiceList.setLayoutManager(layoutManager);
                            mRvInvoiceList.setAdapter(adapter);
                        } else {
                            mInvoicePartList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            showToast("No Products for Invoice");
                        }
                    } else {
//                        confirmAlert("Alert", message);
                        error_message.setVisibility(View.VISIBLE);
                        error_message.setText(message);
                        mInvoicePartList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        showToast("No Products for Invoice");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", response.toString());
            }
        } else if (request_id == Constants.SUBMIT_WH_INVOICE_PRODUCT_REQ_ID) {
            plant_product_invoice.setText("");
            plant_product_invoice.requestFocus();
            plant_product_invoice.isCursorVisible();
            plant_product_invoice
                    .getText().clear();
            editText.getText().clear();
            if (mInvoicePartList != null && mInvoicePartList.size() > 0) {
                editText.getText().clear();
                if (response != null) {
                    JSONObject result = null;
                    Log.d("TAG", "webserviceResponse SUBMIT : SUBMIT_WH_INVOICE_PRODUCT_REQ_ID ");
                    try {
                        editText.getText().clear();
                        result = new JSONObject(response);
                        System.out.println("response:" + response);
                        String statusCode = result.getString("MessageCode");
                        String message = result.getString("Message");
                        if (statusCode.equalsIgnoreCase("0")) {
                            showToast("Item submit successfully");
                            JSONArray array = result.getJSONArray("Data");
                            if (array != null && array.length() > 0) {
                                JSONObject obj = array.getJSONObject(0);
                                PRODUCT_COUNT = obj.optInt(ReqResParamKey.PRODUCT_COUNT);
                                String PRODUCT_CODE = obj.optString(ReqResParamKey.PRODUCT_CODE);
                                String FLAG = obj.optString(ReqResParamKey.FLAG);
                                for (InvoiceBean objBean : mInvoicePartList) {
                                    if (objBean.getVC_PRODUCT_CODE().equalsIgnoreCase(PRODUCT_CODE)) {
                                        objBean.setTOT_PROD_SCAN_BOX(String.valueOf(PRODUCT_COUNT));
                                        total_counter = total_counter + PRODUCT_COUNT;
                                        tv_scanned_boxes.setText("Total Scanned Boxes : " + total_counter);
                                        try {
//                                            int pending = Integer.parseInt(bean.getNU_TOTAL_CASES()) - total_counter;
                                            int pending = Integer.parseInt(bean.getNU_TOTAL_CASES()) - total_counter;
                                            tv_pending_boxes.setText("Total Pending Boxes : " + pending);
//                                            adapter.notifyDataSetChanged();

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
//                                        adapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        } else {
//                            confirmAlert("Alert", message);

                            error_message.setVisibility(View.VISIBLE);
                            error_message.setText(message);
                            editText.getText().clear();
                            editText.setText("");
//                            mInvoicePartList.clear();
//                            if (adapter != null) {
//                                adapter.notifyDataSetChanged();
//                            }
                            showToast(message);
                        }
                    } catch (JSONException e) {
                        editText.getText().clear();
                        e.printStackTrace();
                    }
                } else {
                    editText.getText().clear();
                    AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", response.toString());
                }
            }
        }


    }

    private void getWarehouseLoadingInvoiceProductData() {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.ORG_ID, userPref.getOrgId());
            param.put(ReqResParamKey.VC_INVOICE_NO, bean.getVC_INVOICE_NO());
            param.put(ReqResParamKey.DT_INVOICE_DATE, DateUtil.ddMMyyyyToddMMMyyyy(bean.getDT_INVOICE_DATE()));
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceProductActivity.this, Constants.GET_WAREHOUSE_LOADING_INVOICE_PRODUCT_API, param.toString(), WHLoadingInvoiceProductActivity.this, Constants.GET_WH_INVOICE_PRODUCT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceProductActivity.this, userPref.getBASEUrl() + Constants.GET_WAREHOUSE_LOADING_INVOICE_PRODUCT_API, param.toString(), WHLoadingInvoiceProductActivity.this, Constants.GET_WH_INVOICE_PRODUCT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitWarehouseLoadingInvoiceProductData(String data) {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.ORG_ID, userPref.getOrgId());
            param.put(ReqResParamKey.VC_INVOICE_NO, bean.getVC_INVOICE_NO());
            param.put(ReqResParamKey.DT_INVOICE_DATE, DateUtil.ddMMyyyyToddMMMyyyy(bean.getDT_INVOICE_DATE()));
            param.put(ReqResParamKey.PRODUCT_STR, data);
            param.put("plant_string", plant_product_invoice.getText().toString().trim());



            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceProductActivity.this, Constants.SUBMIT_WAREHOUSE_LOADING_INVOICE_PRODUCT_API, param.toString(), WHLoadingInvoiceProductActivity.this, Constants.SUBMIT_WH_INVOICE_PRODUCT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(WHLoadingInvoiceProductActivity.this, userPref.getBASEUrl() + Constants.SUBMIT_WAREHOUSE_LOADING_INVOICE_PRODUCT_API, param.toString(), WHLoadingInvoiceProductActivity.this, Constants.SUBMIT_WH_INVOICE_PRODUCT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WHLoadingInvoiceProductActivity.this, "Alert", "Please check your network connection");
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
                submitWarehouseLoadingInvoiceProductData(editText.getText().toString().trim());

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
                //Intent i = new Intent(WHLoadingInvoiceProductActivity.this, Dashboard.class);
                //startActivity(i);
                /*overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();*/
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onTranNoListClick(int position) {
    }

    private void callBackScreen() {
        Intent i = new Intent(WHLoadingInvoiceProductActivity.this, WHLoadingInvoiceActivity.class);
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