package com.essindia.stlapp.Activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.WhareHouseInwardScanAdapter;
import com.essindia.stlapp.Bean.DataItem;
import com.essindia.stlapp.Bean.InwardScanningData;
import com.essindia.stlapp.Bean.SupervisorModel;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WareHouseInwardBoxScanningActivity extends BaseActivity implements OnResponseFetchListener {

    private ConnectionDetector connectionDetector;
    WhareHouseInwardScanAdapter whareHouseInwardScanAdapter;
    private ArrayList<DataItem> itemsArrayList;
    RecyclerView mRvInvoiceList;
    EditText string_wh_scn_inward;
    String invoice_date, invoice_number, L_WAREHOUSE_COMP_CODE, VC_PRODUCT_CODE;

    private UserPref userPref;
    //    Button submit;
    ImageView submit;
    String selectedOption = "C";
    private Toolbar toolbar;
    int submit_inward = 1;
    TextView total_boxes, scaned_boxed, remarks_product;
    String driver_name, truck_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_inward_box_scanning);

        intializeXml();
        // Receive the InwardScanningData instance from the intent
        InwardScanningData inwardScanningData = (InwardScanningData) getIntent().getSerializableExtra("inwardData");
        if (inwardScanningData != null) {
            total_boxes.setText(inwardScanningData.getTotal_boxes());
            scaned_boxed.setText(inwardScanningData.getScanned_boxed());
            L_WAREHOUSE_COMP_CODE = inwardScanningData.getL_WAREHOUSE_COMP_CODE();
            invoice_date = inwardScanningData.getInvoice_date();
            invoice_number = inwardScanningData.getInvoice_number();
            driver_name = inwardScanningData.getDriver_name();
            truck_number = inwardScanningData.getTruck_number();
        } else {

            total_boxes.setText(Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_SCANED);
            scaned_boxed.setText(Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_ALREADY_SCANED);
            L_WAREHOUSE_COMP_CODE = Constants.L_WAREHOUSE_COMP_CODE_VALUE;
            invoice_date = Constants.INWARDS_SCANNING_INVOICE_DATE;
            invoice_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GATE_PASS;
            driver_name = Constants.INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME;
            truck_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER;
        }

// Now you can use the inwardScanningData object in SecondActivity

//        Intent intent = getIntent();
//        if (intent != null) {
//            invoice_date = intent.getStringExtra("invoice_date");
//            invoice_number = intent.getStringExtra("invoice_number");
//            L_WAREHOUSE_COMP_CODE = intent.getStringExtra("L_WAREHOUSE_COMP_CODE");
//            total_boxes.setText(intent.getStringExtra("total_scanned_boxes"));
//            scaned_boxed.setText(intent.getStringExtra("previous_scanned"));
//            driver_name = intent.getStringExtra("driver_name");
//            truck_number = intent.getStringExtra("truck_number");
//
//        } else {
//            L_WAREHOUSE_COMP_CODE = Constants.L_WAREHOUSE_COMP_CODE_VALUE;
//            invoice_date = Constants.INWARDS_SCANNING_INVOICE_DATE;
//            invoice_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GATE_PASS;
//            driver_name = Constants.INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME;
//            truck_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER;
//        }


//        total_boxes.setText(Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_SCANED);
//        scaned_boxed.setText(Constants.INWARDS_SCANNING_INVOICE_INVOICE_TOTAL_ALREADY_SCANED);
//        L_WAREHOUSE_COMP_CODE = Constants.L_WAREHOUSE_COMP_CODE_VALUE;
//        invoice_date = Constants.INWARDS_SCANNING_INVOICE_DATE;
//        invoice_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GATE_PASS;
//        driver_name = Constants.INWARDS_SCANNING_INVOICE_INVOICE_DRIVER_NAME;
//        truck_number = Constants.INWARDS_SCANNING_INVOICE_INVOICE_GARI_NUMBER;


//        total_boxes.setText(inwardScanningData.getTotal_boxes());
//        scaned_boxed.setText(inwardScanningData.getScanned_boxed());
//        L_WAREHOUSE_COMP_CODE = inwardScanningData.getL_WAREHOUSE_COMP_CODE();
//        invoice_date = inwardScanningData.getInvoice_date();
//        invoice_number = inwardScanningData.getInvoice_number();
//        driver_name = inwardScanningData.getDriver_name();
//        truck_number = inwardScanningData.getTruck_number();
//
//
//        string_wh_scn_inward.setInputType(InputType.TYPE_NULL);
        getWarehouseLoadingInvoiceProductData();
    }


    private void intializeXml() {
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        itemsArrayList = new ArrayList<>();
        mRvInvoiceList = (RecyclerView) findViewById(R.id.mRvInvoiceList);
        string_wh_scn_inward = findViewById(R.id.string_wh_scn_inward);
        submit = findViewById(R.id.submit);
        total_boxes = findViewById(R.id.total_boxes);
        scaned_boxed = findViewById(R.id.scaned_boxed);
        remarks_product = findViewById(R.id.remarks_product);
        Button final_submit = findViewById(R.id.final_submit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Warehouse Scanning Inward");
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (string_wh_scn_inward.getText().toString().trim().length() > 0) {
                    remarks_product.setVisibility(View.GONE);
                    submitWarehouseInwardsProductData();
                } else {
                    showToast("Scan Product QR code ");
                }
            }
        });
        final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Integer.parseInt(total_boxes.getText().toString()) == Integer.parseInt(scaned_boxed.getText().toString())) {
//                    submitWarehouseInwardsSubmit();
//                    backOnFinalSubmit();
//
//                } else {
//                    showRadioButtonDialog();
//                }
                showRadioButtonDialog();
            }
        });


        string_wh_scn_inward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (string_wh_scn_inward.getText().length() > 0) {
                    submitWarehouseInwardsProductData();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void callBackScreen() {
        Intent i = new Intent(WareHouseInwardBoxScanningActivity.this, WhScanningInwardActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(WareHouseInwardBoxScanningActivity.this, WhScanningInwardActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    public void backOnFinalSubmit() {
        Intent intent = new Intent(WareHouseInwardBoxScanningActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void getWarehouseLoadingInvoiceProductData() {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.ORG_ID, userPref.getOrgId());
            param.put("user_code", userPref.getUserName().toUpperCase());
            param.put(ReqResParamKey.VC_INVOICE_NO, invoice_number);
            param.put("DT_INVOICE_DATE", invoice_date);
            param.put("L_WAREHOUSE_COMP_CODE", L_WAREHOUSE_COMP_CODE);
            param.put("P_VICHIEL_NO", truck_number);
            param.put("P_DRIVER_NAME", driver_name);
//            param.put(ReqResParamKey.DT_INVOICE_DATE, DateUtil.ddMMyyyyToddMMMyyyy(invoice_date));


            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(WareHouseInwardBoxScanningActivity.this, userPref.getBASEUrl() + Constants.WAREHOUSE_SCANNING_INWARDS_PRODUCT, param.toString(), WareHouseInwardBoxScanningActivity.this, Constants.GET_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WareHouseInwardBoxScanningActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void submitWarehouseInwardsProductData() {
        JSONObject param = new JSONObject();
        try {
            param.put(ReqResParamKey.ORG_ID, userPref.getOrgId());
            param.put(ReqResParamKey.USER_NAME, userPref.getUserName().toUpperCase());
            param.put(ReqResParamKey.VC_INVOICE_NO, invoice_number);
            param.put(ReqResParamKey.DT_INVOICE_DATE, invoice_date);
            param.put("VC_PRODUCT_CODE", VC_PRODUCT_CODE);
            param.put("L_WAREHOUSE_COMP_CODE", L_WAREHOUSE_COMP_CODE);
            param.put("PRODUCT_STR", string_wh_scn_inward.getText().toString().trim());
            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(WareHouseInwardBoxScanningActivity.this, userPref.getBASEUrl() + Constants.SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT, param.toString(), WareHouseInwardBoxScanningActivity.this, Constants.SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WareHouseInwardBoxScanningActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitWarehouseInwardsSubmit() {
        JSONObject param = new JSONObject();
        try {

            param.put(ReqResParamKey.P_COMP_CODE, L_WAREHOUSE_COMP_CODE);
            param.put(ReqResParamKey.P_LOADING_NO, invoice_number);
            param.put(ReqResParamKey.P_LOADING_DATE, invoice_date);
            if (selectedOption.equalsIgnoreCase("H")) {  // Hold
                param.put(ReqResParamKey.P_INWARD_FLAG, "H");
            } else if (selectedOption.equalsIgnoreCase("S")) { // Shortage Qr code
                param.put(ReqResParamKey.P_INWARD_FLAG, "S");
            } else if (selectedOption.equalsIgnoreCase("M")) {  // Mismatch Boxes
                param.put(ReqResParamKey.P_INWARD_FLAG, "M");
            } else {
                param.put(ReqResParamKey.P_INWARD_FLAG, "C");  // Final Closing if all  Boxes is Scanned
            }
            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(WareHouseInwardBoxScanningActivity.this, userPref.getBASEUrl() + Constants.SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_FINAL_SUBMIT, param.toString(), WareHouseInwardBoxScanningActivity.this, Constants.SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING_FINAL_SUBMIT, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(WareHouseInwardBoxScanningActivity.this, "Alert", "Please check your network connection");
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.GET_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arr = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

//                        String BOXES = obj.getString("BOXES");
                        VC_PRODUCT_CODE = obj.getString("VC_PRODUCT_CODE");
                        String TOT_INV_SCAN_BOX = obj.getString("TOT_INV_SCAN_BOX");
                        String TOT_PROD_SCAN_BOX = obj.getString("TOT_PROD_SCAN_BOX");
                        String VC_PLANT_SCAN_BOX = obj.getString("VC_PLANT_SCAN_BOX");
                        String NU_PRODUCT_QUANTITY = obj.getString("NU_PRODUCT_QUANTITY");
                        DataItem dataItem = new DataItem();
                        dataItem.setTOT_PROD_SCAN_BOX(TOT_PROD_SCAN_BOX);
                        dataItem.setVC_PLANT_SCAN_BOX(VC_PLANT_SCAN_BOX);
                        dataItem.setTOT_INV_SCAN_BOX(TOT_INV_SCAN_BOX);
                        dataItem.setVC_PRODUCT_CODE(VC_PRODUCT_CODE);
//                        dataItem.setNU_PACK_SIZE(NU_PACK_SIZE);
                        dataItem.setNU_PRODUCT_QUANTITY(NU_PRODUCT_QUANTITY);
                        itemsArrayList.add(dataItem);
                    }
                    whareHouseInwardScanAdapter = new WhareHouseInwardScanAdapter(getApplicationContext(), itemsArrayList);
                    mRvInvoiceList.setAdapter(whareHouseInwardScanAdapter);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        } else if (request_id == Constants.SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING) {
            if (response != null) {
                string_wh_scn_inward.setText("");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageCode = jsonObject.getString("MessageCode");
                    String message = jsonObject.getString("Message");
                    if (messageCode.equalsIgnoreCase("0")) {
                        JSONArray data = jsonObject.getJSONArray("Data");
                        JSONObject object = data.getJSONObject(0);
                        String productCode = object.getString("PRODUCT_CODE");
                        String PRODUCT_COUNT = object.getString("PRODUCT_COUNT");
                        String FLAG = object.getString("FLAG");
                        String remarks = object.getString("REMARKS");
                        remarks_product.setVisibility(View.VISIBLE);
                        remarks_product.setText(remarks);
                        if (FLAG.equalsIgnoreCase("Y")) {
                            ArrayList<DataItem> productBox = itemsArrayList;
                            for (int i = 0; i < itemsArrayList.size(); i++) {
                                if (productCode.equals(productBox.get(i).getVC_PRODUCT_CODE())) {
                                    int scannedBox = Integer.parseInt(productBox.get(i).getTOT_INV_SCAN_BOX()) + 1;
                                    productBox.get(i).setTOT_INV_SCAN_BOX(String.valueOf(scannedBox));
                                    int prevSc = Integer.parseInt(scaned_boxed.getText().toString()) + Integer.parseInt(PRODUCT_COUNT);
                                    scaned_boxed.setText(String.valueOf(prevSc));
                                    whareHouseInwardScanAdapter.notifyDataSetChanged();
                                    i = itemsArrayList.size() + 1;
                                }
                            }

                            string_wh_scn_inward.requestFocus();
                            string_wh_scn_inward.isCursorVisible();
                        }

                    } else {

                        showToast(message);
                        remarks_product.setVisibility(View.VISIBLE);
                        remarks_product.setText(message);
                        string_wh_scn_inward.requestFocus();
                        string_wh_scn_inward.isCursorVisible();

                    }
                } catch (JSONException e) {

                    string_wh_scn_inward.requestFocus();
                    string_wh_scn_inward.isCursorVisible();
                    throw new RuntimeException(e);
                }
            }
        } else if (request_id == Constants.SUBMIT_WARE_HOUSE_INWARDS_PRODUCT_BOX_SCANNING_FINAL_SUBMIT) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responseCode = jsonObject.getString("MessageCode");
                    String message = jsonObject.getString("Message");
                    if (responseCode.equalsIgnoreCase("0")) {
                        String REMARKS = jsonObject.getString("REMARKS");
                        showLongToast(REMARKS);
                        backOnFinalSubmit();
                    } else {
                        showLongToast(message);
                        backOnFinalSubmit();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }


    private void showRadioButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option");

        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_radio, null);
        builder.setView(dialogView);

        final RadioGroup radioGroup = dialogView.findViewById(R.id.select_condition);
        final RadioButton close = dialogView.findViewById(R.id.close);
        final RadioButton radioOption2 = dialogView.findViewById(R.id.radioOption2);
        final RadioButton radioOption3 = dialogView.findViewById(R.id.radioOption3);
//        final RadioButton radioOption4 = dialogView.findViewById(R.id.radioOption4);
//        final LinearLayout move_and_hold_layout = dialogView.findViewById(R.id.move_and_hold_layout);
        if (Integer.parseInt(total_boxes.getText().toString()) == Integer.parseInt(scaned_boxed.getText().toString())) {
            close.setVisibility(View.VISIBLE);
            radioOption2.setVisibility(View.INVISIBLE);
            radioOption3.setVisibility(View.INVISIBLE);
//            radioOption4.setVisibility(View.INVISIBLE);
//            move_and_hold_layout.setVisibility(View.INVISIBLE);
        } else {
//            move_and_hold_layout.setVisibility(View.VISIBLE);

            radioOption2.setVisibility(View.VISIBLE);
            radioOption3.setVisibility(View.VISIBLE);
//            radioOption4.setVisibility(View.VISIBLE);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                if (selectedRadioButton != null) {
                    String option = selectedRadioButton.getText().toString();
                    if (option.equalsIgnoreCase("Hold")) {
                        selectedOption = "H";
                        submitWarehouseInwardsSubmit();
                    } else if (option.equalsIgnoreCase("Close")) {
                        selectedOption = "C";
                        submitWarehouseInwardsSubmit();
                    }  else if (option.equalsIgnoreCase("Move to Supervisor Screen")) {
                        selectedOption = "s";
                        submitWarehouseInwardsSubmit();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                        SupervisorModel supervisorModel = new SupervisorModel();
                        supervisorModel.setInvoice_number(invoice_number);
                        supervisorModel.setInvoice_date(invoice_date);
                        supervisorModel.setL_WAREHOUSE_COMP_CODE(L_WAREHOUSE_COMP_CODE);

                        Intent intent = new Intent(WareHouseInwardBoxScanningActivity.this, InwardsSupervisorActivity.class);
                        intent.putExtra("inwardData", supervisorModel);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();

//                        intent.putExtra("invoice_date", invoice_date);
//                        intent.putExtra("invoice_number", invoice_number);
//                        intent.putExtra("L_WAREHOUSE_COMP_CODE", L_WAREHOUSE_COMP_CODE);

                    }

                } else {
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}