package com.essindia.stlapp.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.InwardsSupervisorAdapter;
import com.essindia.stlapp.Bean.ProductItem;
import com.essindia.stlapp.Bean.SupervisorBean;
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
import java.util.List;

public class InwardsSupervisorActivity extends BaseActivity implements OnResponseFetchListener, InwardsSupervisorAdapter.OnItemClickListener {
    private ConnectionDetector connectionDetector;
    private Toolbar toolbar;
    RecyclerView pendingProductRecycler;
    String invoice_date, invoice_number, L_WAREHOUSE_COMP_CODE, previous_scanned, total_scanned_boxes;
    InwardsSupervisorAdapter inwardsSupervisorAdapter;
    ArrayList<ProductItem> productBox;
    ArrayList<ProductItem> listSelected;
    private ArrayList<String> mScannedBarcodeList;
    CheckBox checkBox;
    String selectedOption;
    private TextView text_error, totalCount;
    private LinearLayout check_box_and_button;
    Button final_submit_supervisor;
    List<SupervisorBean> supervisorBeanList;

    private UserPref userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inwards_supervisor);
        productBox = new ArrayList<>();
        mScannedBarcodeList = new ArrayList<>();
        listSelected = new ArrayList<>();
        initialize();
        SupervisorModel supervisorModel = (SupervisorModel) getIntent().getSerializableExtra("inwardData");

        if (supervisorModel != null) {
            invoice_date = supervisorModel.getInvoice_date();
            invoice_number = supervisorModel.getInvoice_number();
            L_WAREHOUSE_COMP_CODE = supervisorModel.getL_WAREHOUSE_COMP_CODE();
            pendingSupervisor();
        }

    }

    public void initialize() {
        userPref = new UserPref(this);
        connectionDetector = new ConnectionDetector(this);
//        Intent intent = getIntent();
//        if (intent != null) {
//            invoice_date = intent.getStringExtra("invoice_date");
//            invoice_number = intent.getStringExtra("invoice_number");
//            L_WAREHOUSE_COMP_CODE = intent.getStringExtra("L_WAREHOUSE_COMP_CODE");
//
//        }
        pendingProductRecycler = findViewById(R.id.pending_product);
        check_box_and_button = findViewById(R.id.check_box_and_button);
        text_error = findViewById(R.id.text_error);
        totalCount = findViewById(R.id.total_issue_count);
        checkBox = findViewById(R.id.check_all);
        final_submit_supervisor = findViewById(R.id.final_submit_supervisor);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Supervisor");
        toolbar.setTitleTextColor(getResources().getColor(R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InwardsSupervisorActivity.this, WareHouseInwardBoxScanningActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
        });
        final_submit_supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSelected.isEmpty()) {
                    showToast("This is empty");
                } else {

                    showRadioButtonDialog();
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
//                ProductItem productItem;
                if (checkBox.isChecked()) {
                    for (int i = 0; i < productBox.size(); i++) {
                        productBox.get(i).setStatus(true);
                    }
                    listSelected = productBox;

                } else {
                    for (int i = 0; i < productBox.size(); i++) {
                        productBox.get(i).setStatus(false);
                    }
                }
//                inwardsSupervisorAdapter = new InwardsSupervisorAdapter(getApplicationContext(), productBox, InwardsSupervisorActivity.this);
//                pendingProductRecycler.setAdapter(inwardsSupervisorAdapter);
                inwardsSupervisorAdapter.notifyDataSetChanged();

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackPress();

    }

    private void pendingSupervisor() {
        JSONObject param = new JSONObject();
        try {
            param.put("user_code", userPref.getUserName());
            param.put("ORG_ID", userPref.getOrgId());
            param.put(ReqResParamKey.VC_INVOICE_NO, invoice_number);
            param.put(ReqResParamKey.DT_INVOICE_DATE, invoice_date);
            param.put(ReqResParamKey.L_WAREHOUSE_COMP_CODE, L_WAREHOUSE_COMP_CODE);
            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(InwardsSupervisorActivity.this, userPref.getBASEUrl() + Constants.SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_PENDING_SUPERVISOR, param.toString(), InwardsSupervisorActivity.this, 100, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(InwardsSupervisorActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitSupervisorProductStatus() {
        JSONObject param = new JSONObject();
        JSONObject jsonObject;
        JSONArray jsonArray = jsonArray = new JSONArray();

        try {
            for (int i = 0; i < listSelected.size(); i++) {
                ProductItem productItem = listSelected.get(i);
                String product_code = listSelected.get(i).getProductCode();
                String invoice_number_ = listSelected.get(i).getInvoiceNumber();
                String invoice_date_ = listSelected.get(i).getInvoice_date();
                String product_string = listSelected.get(i).getProductString();

//                String product_code =productItem.getProductCode();
//                String invoice_number_ = productItem.getInvoiceNumber();
//                String invoice_date_ = productItem.getInvoice_date();
//                String product_string = productItem.getProductString();


                jsonObject = new JSONObject();
                if (selectedOption.equalsIgnoreCase("Q")) {
                    jsonObject.put("product_status", "Q");
                } else if (selectedOption.equalsIgnoreCase("M")) {

                    jsonObject.put("product_status", "M");
                } else {
                    jsonObject.put("product_status", "S");
                }
                jsonObject.put("l_warehouse_code", L_WAREHOUSE_COMP_CODE);
                jsonObject.put("product_code", product_code);
                jsonObject.put("invoice_number", "01" + invoice_number_);
                jsonObject.put("invoice_date", invoice_date_);
                jsonObject.put("product_string", product_string);
                jsonArray.put(jsonObject);
            }
            param.put("product_list", jsonArray);
            listSelected.clear();


            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(InwardsSupervisorActivity.this, userPref.getBASEUrl() + Constants.SUBMIT_WAREHOUSE_SCANNING_INWARDS_PRODUCT_PENDING_SUPERVISOR_THIRD_PAGE, param.toString(), InwardsSupervisorActivity.this, 200, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(InwardsSupervisorActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPress() {
        startActivity(new Intent(InwardsSupervisorActivity.this, WareHouseInwardBoxScanningActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == 100) {
            if (response != null) {
                Log.e("TAG", "get pending Data For SUBMIT : " + response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("Data");
                    String massage = jsonObject.getString("Message");
                    if (data.length() > 0) {

                        check_box_and_button.setVisibility(View.VISIBLE);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            String productCode = object.getString("product_code");
                            String product_string = object.getString("product_string");
                            String invoice_number = object.getString("invoice_number");
                            String DT_INVOICE_DATE = object.getString("DT_INVOICE_DATE");
                            ProductItem productItem = new ProductItem();
                            productItem.setProductCode(productCode);
                            productItem.setProductString(product_string);
                            productItem.setInvoiceNumber(invoice_number);
                            productItem.setInvoice_date(DT_INVOICE_DATE);
                            productItem.setStatus(false);
                            productBox.add(productItem);

                        }

                        totalCount.setText(String.valueOf(productBox.size()));
                        inwardsSupervisorAdapter = new InwardsSupervisorAdapter(getApplicationContext(), productBox, InwardsSupervisorActivity.this);
                        pendingProductRecycler.setAdapter(inwardsSupervisorAdapter);
                    } else {
                        check_box_and_button.setVisibility(View.INVISIBLE);
                        text_error.setVisibility(View.VISIBLE);
                        text_error.setText(massage);
                        showToast(massage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } else if (request_id == 200) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("TAG", "webserviceResponse: THIR PAGE # " + jsonObject.toString());
                    showToast(jsonObject.toString());//
                    productBox.clear();
                    listSelected.clear();
                    pendingSupervisor();
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
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_radio_supervisor, null);
        builder.setView(dialogView);
        RadioGroup radioGroup = dialogView.findViewById(R.id.select_condition_supervisor);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);

                if (selectedRadioButton != null) {
                    String option = selectedRadioButton.getText().toString();
                    if (option.equalsIgnoreCase("Issue in QR Code")) {
                        selectedOption = "Q";

                    } else if (option.equalsIgnoreCase("Mismatch Boxes")) {
                        selectedOption = "M";
                    } else if (option.equalsIgnoreCase("Shortage Boxes Material")) {
                        selectedOption = "S";
                    }
                    showAlertConfirmation();
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


    private void showAlertConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");

        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_radio_supervisor_confirmation, null);
        builder.setView(dialogView);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listSelected.isEmpty()) {
                    showToast("Please Select the Data");
                } else {
                    submitSupervisorProductStatus();
                    listSelected.clear();
                    productBox.clear();
//                    pendingSupervisor();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listSelected.clear();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onItemClick(ProductItem data, int position) {
        ProductItem selectedRow = productBox.get(position);
        if (selectedRow.isStatus()) {
            if (!listSelected.contains(selectedRow)) {
                listSelected.add(selectedRow);
            } else {
                listSelected.remove(selectedRow);
            }
        } else {
            if (listSelected.contains(selectedRow)) {
                listSelected.remove(selectedRow);
                checkBox.setChecked(false);
            }
        }
    }



}