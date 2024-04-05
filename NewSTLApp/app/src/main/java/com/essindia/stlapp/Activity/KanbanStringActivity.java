package com.essindia.stlapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import amobile.android.barcodesdk.api.IWrapperCallBack;

public class KanbanStringActivity extends BaseActivity implements IWrapperCallBack, OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, View.OnClickListener {
    private static final String TAG = "KanbanStringActivity";
    private ImageView imageView,iv_status;
    private ConnectionDetector connectionDetector;
    private JSONObject loginparams;
    private UserPref userPref;
    private EditText wh_invoice_qr_code, plant_invoice_qr_code, scan_kanban_qr_code, product_label_qr_code;
    private static BarcodeReader bcr;
    private AidcManager manager;

    int i = 0;
    int j = 0;
    Button submit, saveWhButton, final_submit;
    RelativeLayout save_button_layout;
    LinearLayout layout_all_field,cv_info;
    String baseUrl;
    private Timer myTimer;
    private TextView scanBoxes, totalBoxes, errorMessage, allStringError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stl);
        intializeXml();
        wh_invoice_qr_code.setInputType(InputType.TYPE_NULL);
        plant_invoice_qr_code.setInputType(InputType.TYPE_NULL);
        scan_kanban_qr_code.setInputType(InputType.TYPE_NULL);
        product_label_qr_code.setInputType(InputType.TYPE_NULL);

    }


    private void intializeXml() {
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);

        baseUrl = userPref.getBASEUrl();
        imageView = (ImageView) findViewById(R.id.imageView);
        errorMessage = findViewById(R.id.error_message);
        allStringError = findViewById(R.id.other_string);
        scanBoxes = findViewById(R.id.scanned_boxes);
        totalBoxes = findViewById(R.id.total_boxes_);
        iv_status = findViewById(R.id.iv_status);
        cv_info = findViewById(R.id.cv_info);

        wh_invoice_qr_code = findViewById(R.id.wh_invoice_qr_code);
        plant_invoice_qr_code = findViewById(R.id.plant_invoice_qr_code);
        scan_kanban_qr_code = findViewById(R.id.scan_kanban_qr_code);
        product_label_qr_code = findViewById(R.id.product_label_qr_code);
        submit = findViewById(R.id.submit);
        saveWhButton = findViewById(R.id.start_kanban_scanning);
        final_submit = findViewById(R.id.final_submit);
        save_button_layout = findViewById(R.id.save_button_layout);
        layout_all_field = findViewById(R.id.layout_all_field);
        submit.setOnClickListener(this);
        saveWhButton.setOnClickListener(this);
        final_submit.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kanban Scanning");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> callBackScreen());

        wh_invoice_qr_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allStringError.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (wh_invoice_qr_code.getText().toString().trim().length()>0){
                    validateWhQrString();
                }else {
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        product_label_qr_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                allStringError.setVisibility(View.GONE);
                if (plant_invoice_qr_code.getText().toString().trim().length() > 0 && scan_kanban_qr_code.getText().toString().trim().length() > 0 && product_label_qr_code.getText().toString().trim().length() > 0) {
                    allStringError.setVisibility(View.GONE);
                    qrCodeVerification();

                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        wh_invoice_qr_code.setText(""); // for reset in  case when getting back from detail screen
        try {
            AidcManager.create(this, aidcManager -> {
                manager = aidcManager;
                try {
                    bcr = manager.createBarcodeReader();
                    bcr.claim();
                    barcodeScanner();
                    if (!wh_invoice_qr_code.getText().toString().trim().isEmpty()) {
                        validateWhQrString();
                    }
                } catch (InvalidScannerNameException e) {
                    showToast("Invalid Scanner Name Exception: " + e.getMessage());
                } catch (ScannerUnavailableException e) {
                    showToast("Scanner unavailable" + e.getMessage());
                } catch (Exception e) {
                    showToast("Exception: " + e.getMessage());
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
    public void onDataReady(String strData) {
        byte[] bytes = strData.getBytes();
        System.out.println("data Kanban:" + strData);
        Log.e(TAG, "onDataReady: " + strData);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (wh_invoice_qr_code.getText().toString().trim().length() > 0) {
                  validateWhQrString();
                }else {
                    qrCodeVerification();
                }


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
        Log.e(TAG, "onBarcodeEvent: " + barcodeReadEvent.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wh_invoice_qr_code.setText(barcodeReadEvent.getBarcodeData());
                if (j == 0) {
                    plant_invoice_qr_code.setText(barcodeReadEvent.getBarcodeData());
                    scan_kanban_qr_code.requestFocus();
                    scan_kanban_qr_code.isCursorVisible();
                    j = 1;

                } else if (j == 1) {
                    scan_kanban_qr_code.setText(barcodeReadEvent.getBarcodeData());
                    product_label_qr_code.requestFocus();
                    product_label_qr_code.isCursorVisible();
                    j = 2;

                } else if (j == 2) {
                    product_label_qr_code.setText(barcodeReadEvent.getBarcodeData());
                    plant_invoice_qr_code.requestFocus();
                    plant_invoice_qr_code.isCursorVisible();
                    j = 0;
                    if (plant_invoice_qr_code.getText().toString().trim().length() > 0 && scan_kanban_qr_code.getText().toString().trim().length() > 0 && product_label_qr_code.getText().toString().trim().length() > 0)
                        qrCodeVerification();

                }


//                boxVerifier();
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {


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

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (plant_invoice_qr_code.getText().length() < 1 || scan_kanban_qr_code.getText().length() < 1 || product_label_qr_code.length() < 1) {
//                    showToast("Scan all QR code ");
                    allStringError.setVisibility(View.VISIBLE);
                    allStringError.setText("Please scan all required QR Code");
                } else {
                    qrCodeVerification();
                }
                break;
            case R.id.start_kanban_scanning:
                if (wh_invoice_qr_code.getText().length() < 1) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please Scan Invoice String");

                } else {
                    validateWhQrString();
//                    errorMessage.setVisibility(View.GONE);
//                    plant_invoice_qr_code.setText("");
//                    scan_kanban_qr_code.setText("");
//                    product_label_qr_code.setText("");
//                    save_button_layout.setVisibility(View.GONE);
//                    layout_all_field.setVisibility(View.VISIBLE);
//                    plant_invoice_qr_code.isCursorVisible();
//                    plant_invoice_qr_code.requestFocus();

                }
                break;

            case R.id.final_submit:
                save_button_layout.setVisibility(View.VISIBLE);
                layout_all_field.setVisibility(View.INVISIBLE);
                wh_invoice_qr_code.setText("");
                plant_invoice_qr_code.setText("");
                scan_kanban_qr_code.setText("");
                product_label_qr_code.setText("");
                wh_invoice_qr_code.isCursorVisible();
                wh_invoice_qr_code.requestFocus();
                break;

        }
    }

    private void validateWhQrString() {
        showProgressBar("Please wait...");
        loginparams = new JSONObject();
        try {
            loginparams.put("user_code", userPref.getUserName());
            loginparams.put("comp_code", userPref.getOrgId());
            loginparams.put("invoice_string", wh_invoice_qr_code.getText().toString().trim());

            Log.e(TAG, "PARAMS : " + loginparams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (connectionDetector.isConnectingToInternet()) {
            CallService.getInstance().getResponseUsingPOST(KanbanStringActivity.this, baseUrl + Constants.CHECK_WH_QR_CODE_INVOICE_STRING_KANBAN, loginparams.toString(), KanbanStringActivity.this, 1, true);
        } else {
            AlertDialogManager.getInstance().simpleAlert(KanbanStringActivity.this, "Error", "Something went wrong on Server side");
        }
    }


    private void qrCodeVerification() {
        showProgressBar("Please wait...");
        loginparams = new JSONObject();
        try {
            loginparams.put("user_code", userPref.getUserName());
            loginparams.put("comp_code", userPref.getOrgId());
            loginparams.put("invoice_string", plant_invoice_qr_code.getText().toString().trim());
            loginparams.put("kanban_string", scan_kanban_qr_code.getText().toString().trim());
            loginparams.put("Wh_invoice_string", wh_invoice_qr_code.getText().toString().trim());
            loginparams.put("Product_label_string", product_label_qr_code.getText().toString().trim());

            Log.e(TAG, "PARAMS : " + loginparams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (connectionDetector.isConnectingToInternet()) {
            CallService.getInstance().getResponseUsingPOST(KanbanStringActivity.this, baseUrl + Constants.CHECK_SCANNED_MATCH, loginparams.toString(), KanbanStringActivity.this, 2, true);
        } else {
            AlertDialogManager.getInstance().simpleAlert(KanbanStringActivity.this, "Error", "Something went wrong on Server side");
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
//        editText.setText("");
        plant_invoice_qr_code.setText("");
        scan_kanban_qr_code.setText("");
        product_label_qr_code.setText("");
        plant_invoice_qr_code.requestFocus();
        hideProgressBar();

        if (request_id == 2) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String message = result.getString("Message");
                    String messageCode = result.getString("MessageCode");
                    if (messageCode.equalsIgnoreCase("0") && message.equalsIgnoreCase("success")) {
                        int currentScan = Integer.parseInt(scanBoxes.getText().toString());
                        int totBox = Integer.parseInt(scanBoxes.getText().toString());
                        currentScan = currentScan + 1;
                        scanBoxes.setText(String.valueOf(currentScan));
                        if (currentScan==0){
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_white));
                        }else if (currentScan<totBox){
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_24dp));
                        }else {
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_green));
                        }

                    } else {
                        allStringError.setVisibility(View.VISIBLE);
                        allStringError.setText(message);
                    }
                    j = 0;
                    plant_invoice_qr_code.requestFocus();
                    plant_invoice_qr_code.isCursorVisible();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                AlertDialogManager.getInstance().simpleAlert(KanbanStringActivity.this, "Error", "Something went wrong on Server side");
            }
        } else if (request_id == 1) {

            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String message = result.getString("Message");
                    String messageCode = result.getString("MessageCode");
                    if (messageCode.equalsIgnoreCase("0")) {
                        cv_info.setVisibility(View.VISIBLE);
                        errorMessage.setVisibility(View.GONE);
                        plant_invoice_qr_code.setText("");
                        scan_kanban_qr_code.setText("");
                        product_label_qr_code.setText("");
                        save_button_layout.setVisibility(View.GONE);
                        layout_all_field.setVisibility(View.VISIBLE);
                        int scanBox = result.getInt("scanBosex");
                        int totalBox = result.getInt("totalBoxes");
                        if (scanBox==0){
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_white));
                        }else if (scanBox<totalBox){
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_24dp));
                        }else {
                            iv_status.setImageDrawable(getResources().getDrawable(R.drawable.ic_brightness_1_theme_green));
                        }
                        scanBoxes.setText(String.valueOf(scanBox));
                        totalBoxes.setText(String.valueOf(totalBox));
                        plant_invoice_qr_code.isCursorVisible();
                        plant_invoice_qr_code.requestFocus();
                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText(message);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else {
                AlertDialogManager.getInstance().simpleAlert(KanbanStringActivity.this, "Error", "Something went wrong on Server side");
            }

            wh_invoice_qr_code.setText("");
            wh_invoice_qr_code.isCursorVisible();
            wh_invoice_qr_code.requestFocus();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    private void callBackScreen() {
        Intent i = new Intent(KanbanStringActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callBackScreen();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
