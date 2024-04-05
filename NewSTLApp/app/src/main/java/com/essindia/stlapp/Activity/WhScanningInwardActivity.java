package com.essindia.stlapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essindia.stlapp.Bean.InvoiceBean;
import com.essindia.stlapp.Bean.InwardScanningData;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amobile.android.barcodesdk.api.IWrapperCallBack;

public class WhScanningInwardActivity extends BaseActivity implements IWrapperCallBack, OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {
    WareHouseInwardBoxScanningActivity wareHouseInwardBoxScanningActivity;
    private Toolbar toolbar;
    private Button submit, startScanning;
    private ConnectionDetector connectionDetector;
    private UserPref userPref;
    private SqliteHelper db;
    String driver_name = "", vichelNumber = "";

    EditText tvDriverName, truckNumber;
    String invoice_date, invoice_number, L_WAREHOUSE_COMP_CODE, previous_scanned, total_scanned_boxes;
    private EditText string_wh_scn_inward;
    TextView customerName, invoiceNumber, invoiceDate, totalPlanBox, warehouseScandBox, pendingBox, remarks_final, total_invoice, dri_name, truck_number_set;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private LinearLayout view_details_layout, cv;
    private ArrayList<InvoiceBean> mInvoiceList;
    private RecyclerView mRvInvoiceList;
    LinearLayout driver_details_layout, details_driver;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wh_scanning_inward);
        intializeXml();

        string_wh_scn_inward.setInputType(InputType.TYPE_NULL);

    }

    private void intializeXml() {
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        mRvInvoiceList = (RecyclerView) findViewById(R.id.mRvInvoiceList);
        driver_details_layout = findViewById(R.id.driver_details_layout);
        details_driver = findViewById(R.id.details_driver);
        submit = findViewById(R.id.submit);
        startScanning = findViewById(R.id.start_scanning);
        view_details_layout = findViewById(R.id.view_details_layout);
        cv = findViewById(R.id.cv);
        customerName = findViewById(R.id.customer_name);
        invoiceNumber = findViewById(R.id.invoice_number);
        invoiceDate = findViewById(R.id.invoice_date);
        totalPlanBox = findViewById(R.id.total_plant_boxes);
        warehouseScandBox = findViewById(R.id.warehouse_scanned_boxes);
        pendingBox = findViewById(R.id.pending_boxes);
        total_invoice = findViewById(R.id.total_invoice);
        dri_name = findViewById(R.id.dri_name);
        truck_number_set = findViewById(R.id.truck_number_set);

        truckNumber = findViewById(R.id.truck_number);
        tvDriverName = findViewById(R.id.driver_name);
        remarks_final = findViewById(R.id.remarks_final);

        string_wh_scn_inward = (EditText) findViewById(R.id.string_wh_scn_inward);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Warehouse Scanning Inward");
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


        string_wh_scn_inward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (string_wh_scn_inward.getText().toString().trim().length()>0){
                    getWarehouseLoadingInvoiceData(string_wh_scn_inward.getText().toString().trim());

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (string_wh_scn_inward.getText().toString().trim().length() > 0) {
//                    if (truckNumber.getText().toString().trim().length() > 0) {
//                        if (tvDriverName.getText().toString().trim().length() > 0) {
                    getWarehouseLoadingInvoiceData(string_wh_scn_inward.getText().toString().trim());
//                        } else {
//                            showToast("Enter the Driver Name!");
//                        }
//                    } else {
//                        showToast("Enter Truck Number!");
//                    }
                } else showToast("Scan all QR code!");
            }
        });
        startScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driver_name.trim().isEmpty() || vichelNumber.isEmpty()) {
                    if (truckNumber.getText().toString().trim().isEmpty()) {
                        showToast("Enter Truck Number");

                    } else if (tvDriverName.getText().toString().trim().isEmpty()) {
                        showToast("Enter Driver Name");
                    } else {
                        Intent intent = new Intent(WhScanningInwardActivity.this, WareHouseInwardBoxScanningActivity.class);
                        intent.putExtra("invoice_date", invoice_date);
                        intent.putExtra("invoice_number", invoice_number);
                        intent.putExtra("L_WAREHOUSE_COMP_CODE", L_WAREHOUSE_COMP_CODE);
                        intent.putExtra("total_scanned_boxes", total_scanned_boxes);
                        intent.putExtra("previous_scanned", previous_scanned);
                        intent.putExtra("driver_name", tvDriverName.getText().toString().trim());
                        intent.putExtra("truck_number", truckNumber.getText().toString().trim());
                        Constants.INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME = tvDriverName.getText().toString().trim();
                        Constants.INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER = truckNumber.getText().toString().trim();
                        int requestCode = 1; // You can use any unique code here
                        InwardScanningData inwardScanningData = new InwardScanningData();
                        inwardScanningData.setInvoice_date(invoice_date);
                        inwardScanningData.setScanned_boxed(previous_scanned);
                        inwardScanningData.setInvoice_number(invoice_number);
                        inwardScanningData.setTotal_boxes(total_scanned_boxes);
                        inwardScanningData.setL_WAREHOUSE_COMP_CODE(L_WAREHOUSE_COMP_CODE);
                        inwardScanningData.setDriver_name(tvDriverName.getText().toString().trim());
                        inwardScanningData.setTruck_number(truckNumber.getText().toString().trim());


//                        startActivityForResult(intent, requestCode);
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                        finish();

                        intent.putExtra("inwardData", inwardScanningData);
                        startActivity(intent);
//                    int requestCode = 1; // You can use any unique code here
//                    startActivityForResult(intent, requestCode);
//                    startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();

                    }
                } else {
                    Intent intent = new Intent(WhScanningInwardActivity.this, WareHouseInwardBoxScanningActivity.class);
                    intent.putExtra("invoice_date", invoice_date);
                    intent.putExtra("invoice_number", invoice_number);
                    intent.putExtra("L_WAREHOUSE_COMP_CODE", L_WAREHOUSE_COMP_CODE);
                    intent.putExtra("total_scanned_boxes", total_scanned_boxes);
                    intent.putExtra("previous_scanned", previous_scanned);
                    intent.putExtra("driver_name", vichelNumber);
                    intent.putExtra("truck_number", driver_name);


                    InwardScanningData inwardScanningData = new InwardScanningData();
                    inwardScanningData.setInvoice_date(invoice_date);
                    inwardScanningData.setScanned_boxed(previous_scanned);
                    inwardScanningData.setInvoice_number(invoice_number);
                    inwardScanningData.setL_WAREHOUSE_COMP_CODE(L_WAREHOUSE_COMP_CODE);
                    inwardScanningData.setTotal_boxes(total_scanned_boxes);
                    inwardScanningData.setDriver_name(driver_name);
                    inwardScanningData.setTruck_number(vichelNumber);

                    intent.putExtra("inwardData", inwardScanningData);
                    startActivity(intent);
//                    int requestCode = 1; // You can use any unique code here
//                    startActivityForResult(intent, requestCode);
//                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
            }
        });

    }

    private void callBackScreen() {
        Intent intent = new Intent(WhScanningInwardActivity.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void getWarehouseLoadingInvoiceData(String data) {
        JSONObject param = new JSONObject();
        try {
            param.put("user_code", userPref.getUserName());
            param.put("user_id", userPref.getUserId());
            param.put("comp_code", userPref.getOrgId());
            param.put(ReqResParamKey.WAREHOUSE_SCAN_INWARD, string_wh_scn_inward.getText().toString().trim());
            param.put(ReqResParamKey.WAREHOUSE_SCAN_INWARD_TRUCK_DRIVER, tvDriverName.getText().toString().trim());
            param.put(ReqResParamKey.WAREHOUSE_SCAN_INWARD_TRUCK_NUMBER, truckNumber.getText().toString().trim());
            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(WhScanningInwardActivity.this, userPref.getBASEUrl() + Constants.WAREHOUSE_SCANNING_INWARDS, param.toString(), WhScanningInwardActivity.this, 2, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WhScanningInwardActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void webserviceResponse(int request_id, String response) {
        hideProgressBar();

        if (request_id == 2) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);

                    Log.e("TAG", "webserviceResponse      STL : " + result.toString());
                    String responseCode = result.getString("responseCode");
                    String message = result.getString("message");
                    if (responseCode.equalsIgnoreCase("1") && message.equalsIgnoreCase("Success")) {
                        view_details_layout.setVisibility(View.VISIBLE);
                        cv.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                        JSONObject responseObj = result.getJSONObject("response");
                        String customer = responseObj.getString("customerName");
                        String invoiceNo = responseObj.getString("invoiceNo");
                        String plantBox = responseObj.getString("plantBox");
                        String plantComCode = responseObj.getString("plantComCode");
                        String whBox = responseObj.getString("whBox");
                        String invoiceDat = responseObj.getString("invoiceDate");
                        String flag = responseObj.getString("flag");
                        String REMARKS_FINAL = responseObj.getString("REMARKS_FINAL");
                        driver_name = responseObj.getString("driver_name");
                        vichelNumber = responseObj.getString("vichelNumber");
                        String invoiceCount = responseObj.getString("invoiceCount");

                        if (driver_name.trim().isEmpty() || driver_name == null || vichelNumber.trim().isEmpty() || vichelNumber.isEmpty()) {
                            driver_details_layout.setVisibility(View.VISIBLE);
                        } else {
                            details_driver.setVisibility(View.VISIBLE);
                            dri_name.setText(": " + driver_name);
                            truck_number_set.setText(": " + vichelNumber);
                            Constants.INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME = driver_name;
                            Constants.INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER = vichelNumber;

                        }


                        invoice_number = invoiceNo;
                        invoice_date = invoiceDat;
                        L_WAREHOUSE_COMP_CODE = plantComCode;

                        Constants.L_WAREHOUSE_COMP_CODE_VALUE = L_WAREHOUSE_COMP_CODE;
                        Constants.INWARDS_SCANNING_INVOICE_DATE = invoice_date;
                        Constants.INWARDS_SCANNING_INVOICE_INVOICE_GATE_PASS = invoice_number;
                        Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_SCANED = plantBox;
                        Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_ALREADY_SCANED = whBox;


                        total_scanned_boxes = plantBox;
                        previous_scanned = whBox;
                        int pending = Integer.parseInt(plantBox) - Integer.parseInt(whBox);
                        customerName.setText(": " + customer);
                        invoiceNumber.setText(": " + invoiceNo);
                        invoiceDate.setText(": " + invoiceDat);
                        totalPlanBox.setText(": " + plantBox);
                        warehouseScandBox.setText(": " + whBox);
                        total_invoice.setText(": " + invoiceCount);
                        remarks_final.setText("Loading Status :- " + REMARKS_FINAL);
                        pendingBox.setText(": " + (String.valueOf(pending)));
//                        if (Integer.parseInt(whBox) == Integer.parseInt(plantBox)) {
//                            startScanning.setVisibility(View.GONE);
////                            startScanning.setVisibility(View.VISIBLE);
//                        }
                        if (REMARKS_FINAL.equalsIgnoreCase("Loading Closed") ) {
                            startScanning.setVisibility(View.GONE);
//                            startScanning.setVisibility(View.VISIBLE);
                        }

                        InwardScanningData inwardScanningData=new InwardScanningData();
                        inwardScanningData.setScanned_boxed(plantBox);
                        inwardScanningData.setScanned_boxed(whBox);
                        inwardScanningData.setInvoice_date(invoiceDat);
                        inwardScanningData.setInvoice_number(invoiceNo);
                        inwardScanningData.setTotal_boxes(invoiceCount);


                        string_wh_scn_inward.setText("");
                        string_wh_scn_inward.requestFocus();
                        string_wh_scn_inward.isCursorVisible();
                    } else {
                        string_wh_scn_inward.setText("");
                        string_wh_scn_inward.requestFocus();
                        string_wh_scn_inward.isCursorVisible();
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                AlertDialogManager.getInstance().simpleAlert(WhScanningInwardActivity.this, "Error", "Something went wrong on Server side");
            }
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

//    @Override
//    protected void onResume() {
//        super.onResume();+
//        string_wh_scn_inward.setText(""); // for reset in  case when getting back from detail screen
//
//        try {
//            AidcManager.create(this, new AidcManager.CreatedCallback() {
//                @Override
//                public void onCreated(AidcManager aidcManager) {
//                    manager = aidcManager;
//                    try {
//                        bcr = manager.createBarcodeReader();
//                        bcr.claim();
//                        showToast(bcr.toString());
//                        barcodeScanner();
//                        if (!string_wh_scn_inward.getText().toString().trim().isEmpty()) {
//                            getWarehouseLoadingInvoiceData(string_wh_scn_inward.getText().toString().trim());
//                        }
//                    } catch (InvalidScannerNameException e) {
//                        showToast("Invalid Scanner Name Exception: " + e.getMessage());
//                    } catch (ScannerUnavailableException e) {
//                        showToast("Scanner unavailable" + e.getMessage());
//                    } catch (Exception e) {
//                        showToast("Exception: " + e.getMessage());
//                    }
//                }
//            });
//        } catch (IllegalArgumentException exp) {
//            showToast("Your device is not compatible with QR Code scanner");
//            Log.e("Loading scanner exp", exp.toString());
//        } catch (Exception exp) {
//            showToast("Your device is not compatible with QR Code scanner");
//            Log.e("Loading scanner exp1", exp.toString());
//        }
//
//
//    }

    @Override
    public void onDataReady(String strData) {
        byte[] bytes = strData.getBytes();
        System.out.println("WAREHOUSE INWARDS STRING:" + strData);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


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
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        Log.e(getCallingPackage(), "onBarcodeEvent: " + barcodeReadEvent.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                string_wh_scn_inward.setText(barcodeReadEvent.getBarcodeData());
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        callBackScreen();
    }

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


}