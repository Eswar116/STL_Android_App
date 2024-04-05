/*
package com.essindia.stlapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.PickupAdapter;
import com.essindia.stlapp.Bean.PickUpBean;
import com.essindia.stlapp.Bean.PickUpSummaryData;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Sqlite.SqliteConstantData;
import com.essindia.stlapp.Sqlite.SqliteHelper;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.DateUtil;
import com.essindia.stlapp.Utils.UserPref;
import com.essindia.stlapp.Utils.ValidationChecker;
import com.google.gson.Gson;
import com.intermec.aidc.AidcManager;
import com.intermec.aidc.BarcodeReadEvent;
import com.intermec.aidc.BarcodeReadListener;
import com.intermec.aidc.BarcodeReader;
import com.intermec.aidc.BarcodeReaderException;
import com.intermec.aidc.VirtualWedge;
import com.intermec.aidc.VirtualWedgeException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickupList extends AppCompatActivity implements BarcodeReadListener, OnResponseFetchListener {

    private ImageView imageView;
    private Spinner customerSp, pickNoSp, pickDateSp;
    private RecyclerView recylerView;
    private static BarcodeReader bcr;
    private VirtualWedge wedge;
    private EditText editText;
    private JSONObject loginparams;
    private ConnectionDetector connectionDetector;
    private UserPref userPref;
    private SqliteHelper db;
    private LinearLayoutManager layoutManager;
    private PickupAdapter adapter;
    private PickUpBean bean;
    private String pickDate, pickNo, boxType;
    private Context context;
    private ArrayList<PickUpSummaryData> pickNoPickDateList;
    private ArrayList<PickUpSummaryData> pickDateListdb;
    private List<String> pickDateList = new ArrayList<>();
    //    private boolean scanFlag;
    List<String> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_list);
        intializeXml();
    }

    @Override
    protected void onResume() {
        super.onResume();

        editText.setText(""); // for reset incase when getting back from detail screen
        try {
            AidcManager.connectService(this, new AidcManager.IServiceListener() {
                @Override
                public void onConnect() {
                    try {
                        //Initial bar code reader instance
                        bcr = new BarcodeReader();
                        //disable virtual wedge
                        wedge = new VirtualWedge();
                        wedge.setEnable(false);
                        barcodeScanner();

                    } catch (BarcodeReaderException e) {
                        e.printStackTrace();
                    } catch (VirtualWedgeException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnect() {
                    System.out.println("Barcode is not connected");
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Hardware Not supported", Toast.LENGTH_LONG).show();
        }

    }

    private void intializeXml() {
        userPref = new UserPref(this);
        db = new SqliteHelper(this);
        connectionDetector = new ConnectionDetector(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pickUp);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        customerSp = (Spinner) findViewById(R.id.tv_customer_name);
        pickNoSp = (Spinner) findViewById(R.id.tv_pick_no);
        pickDateSp = (Spinner) findViewById(R.id.tv_pick_date);
        recylerView = (RecyclerView) findViewById(R.id.recylerView);
        editText = (EditText) findViewById(R.id.editText);
        editText.setHint("Scan Pick QR Code");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                if (data != null && !data.isEmpty()) {

//                        boxSplitter(data);
//                        boxVerifier();
//
//                        dataSplite(data);
                    dataSplitAndChecker(data);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Please enter valid data");
                }
            }

        });

    }

    private void dataSplitAndChecker(String data) {
        String dataSp[] = data.split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length == 2) {
            pickNo = dataSp[0].trim().toUpperCase();
            pickDate = dataSp[1].trim().toUpperCase();
            pickDate = DateUtil.ddMMyyyyToddMMMyyyy(pickDate);
            if (!pickDate.isEmpty()) {
                pickListDataCallingFromServer(userPref.getOrgId(), pickNo, pickDate);
            } else {
                Toast.makeText(PickupList.this, "Invalid Pick Date Format", Toast.LENGTH_LONG).show();
            }
        } else if (pickNo != null && !pickNo.isEmpty() && pickDate != null && !pickDate.isEmpty() && dataSp.length == 8) {
            Constants.QR_PROCESS_CODE = dataSp[0].trim().toUpperCase();
            Constants.QR_STL_PART_NO = dataSp[1].trim().toUpperCase();
            Constants.QR_ROUTE_CARD_NO = dataSp[2].trim().toUpperCase();
            Constants.QR_STD_QTY = dataSp[3].trim().toUpperCase();
            Constants.QR_ACTUAL_QTY = dataSp[4].trim().toUpperCase();
            Constants.QR_REFERENCE_NO = dataSp[5].trim().toUpperCase();
            Constants.QR_BOX_TYPE = dataSp[6].trim().toUpperCase();
//            Constants.QR_ROUTE_DATE = dataSp[7].trim().toUpperCase();
            Constants.QR_TIME = dataSp[7].trim().toUpperCase();

            if (Constants.QR_ACTUAL_QTY != null && Constants.QR_STD_QTY != null && !Constants.QR_ACTUAL_QTY.isEmpty() && !Constants.QR_STD_QTY.isEmpty()) {
                String boxType = ValidationChecker.getBoxType(this, Constants.QR_ACTUAL_QTY, Constants.QR_STD_QTY);
                Constants.QR_BOX_TYPE = Constants.QR_STL_PART_NO + "/" + Constants.QR_STD_QTY + "/" + Constants.QR_BOX_TYPE + "-" + boxType;
            }
            boxVerifier();
        } else {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Please enter correct format !");
        }
    }

    public void pickListDataCallingFromServer(String orgId, String pickNo, String pickDate) {
        loginparams = new JSONObject();
        try {
            loginparams.put(Constants.TOKEN, userPref.getToken());
            loginparams.put(ReqResParamKey.ORG_ID, orgId);
            loginparams.put(ReqResParamKey.PICK_NO, pickNo);
            loginparams.put(ReqResParamKey.PICK_DATE, pickDate);

            if (connectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(PickupList.this, Constants.POST_PICKUP_SUMMARY, loginparams.toString(), PickupList.this, Constants.PICK_UP_SUMMARY_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "No internet connection.");
            }
        } catch (Exception e) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", e.toString());
        }
    }

    private void boxVerifier() {
        if (userPref.getOrgId() != null && Constants.QR_STL_PART_NO != null && Constants.QR_BOX_TYPE != null && !userPref.getOrgId().isEmpty() && !Constants.QR_BOX_TYPE.isEmpty() && !Constants.QR_STL_PART_NO.isEmpty()) {

            context = PickupList.this;
            PickUpSummaryData bean = db.checkIsPickUpBoxDataAvailableInDb(userPref.getOrgId(), Constants.QR_BOX_TYPE, Constants.QR_STL_PART_NO, context);
            if (bean != null) {
                bean.setBoxId(Constants.QR_TIME);
                Bundle b = new Bundle();
                b.putParcelable(SqliteConstantData.KEY_PICK_UP_LIST, bean);
                editText.setText("");
                Intent form = new Intent(PickupList.this, PickupDetails.class);
                form.putExtras(b);
                startActivity(form);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } else {
//                scanFlag = false;
                AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Data doesn't exist !");
            }
        } else {
            editText.setText("");
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Data is empty");
        }
    }

    private void pickListVerifier() {
        if (userPref.getOrgId() != null && pickNo != null && pickDate != null && !userPref.getOrgId().isEmpty() && !pickNo.isEmpty() && !pickDate.isEmpty()) {

            context = PickupList.this;
            ArrayList<PickUpSummaryData> beanList = db.checkIsPickDataAvailableInDb(userPref.getOrgId(), pickNo, pickDate, context);

            if (beanList != null) {
                editText.setHint("Scan Item QR Code");
//                scanFlag = true;
//                scanFlag = true;
                ArrayList<String> cName = new ArrayList<>();
                ArrayList<String> pNo = new ArrayList<>();
                ArrayList<String> pDate = new ArrayList<>();
                for (int i = 0; i < beanList.size(); i++) {
                    cName.add(beanList.get(i).getCUSTOMERNAME());
                }
                for (int i = 0; i < beanList.size(); i++) {
                    pNo.add(beanList.get(i).getPICKNO());
                }
                for (int i = 0; i < beanList.size(); i++) {
                    pDate.add(beanList.get(i).getPICKDATE());
                }

                customerSp.setOnItemSelectedListener(null);
                pickNoSp.setOnItemSelectedListener(null);
                pickDateSp.setOnItemSelectedListener(null);

                ArrayAdapter<String> dataAdapterCustomer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cName);
                dataAdapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerSp.setAdapter(dataAdapterCustomer);

                ArrayAdapter<String> dataAdapterpickNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pNo);
                dataAdapterpickNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pickNoSp.setAdapter(dataAdapterpickNo);

                ArrayAdapter<String> dataAdapterpickDate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pDate);
                dataAdapterpickDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pickDateSp.setAdapter(dataAdapterpickDate);

                customerSp.setEnabled(false);
                pickDateSp.setEnabled(false);
                pickNoSp.setEnabled(false);

                adapter = new PickupAdapter(getApplicationContext(), beanList);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recylerView.setLayoutManager(layoutManager);
                recylerView.setAdapter(adapter);

            } else {
                editText.setHint("Scan Pick QR Code");
                editText.setText("");
//                scanFlag = false;
                AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Data doesn't exist !");
            }
        } else {
            editText.setText("");
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Data is empty!");
        }
    }

    private void barcodeScanner() {
        try {
            if (bcr != null) {
                bcr.setScannerEnable(true);
                bcr.addBarcodeReadListener(this);
            } else {
                System.out.println("Device is not QR enabled");
            }
        } catch (BarcodeReaderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void barcodeRead(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(barcodeReadEvent.getBarcodeData());
               */
/* if (scanFlag == true) {
                    boxSplitter(barcodeReadEvent.getBarcodeData());
                    boxVerifier();
                } else {
                    dataSplite(barcodeReadEvent.getBarcodeData());
                }*//*

                dataSplitAndChecker(barcodeReadEvent.getBarcodeData().toString().trim());
            }
        });
    }

    */
/*private boolean boxSplitter(String data) {

        boolean flag = false;

        String dataSp[] = data.split(",");

        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Qr code is empty cannot load data item");
            flag = false;
        } else if (dataSp.length < 9 || dataSp.length > 9) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Please enter correct format !");
            flag = false;

        } else if (dataSp.length == 9) {

            Constants.QR_PROCESS_CODE = dataSp[0].trim().toUpperCase();
            Constants.QR_STL_PART_NO = dataSp[1].trim().toUpperCase();
            Constants.QR_ROUTE_CARD_NO = dataSp[2].trim().toUpperCase();
            Constants.QR_STD_QTY = dataSp[3].trim().toUpperCase();
            Constants.QR_ACTUAL_QTY = dataSp[4].trim().toUpperCase();
            Constants.QR_REFERENCE_NO = dataSp[5].trim().toUpperCase();
            Constants.QR_BOX_TYPE = dataSp[6].trim().toUpperCase();
            Constants.QR_ROUTE_DATE = dataSp[7].trim().toUpperCase();
            Constants.QR_TIME = dataSp[8].trim().toUpperCase();

            flag = true;
        }

        return flag;
    }*//*


    private boolean dataSplite(String data) {

        boolean flag = false;

        String dataSp[] = data.split(Constants.DELIMITER);

        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Qr code is empty cannot load data item");
            flag = false;
        } else if (dataSp.length < 2 || dataSp.length > 2) {
            AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", "Please enter correct format !");
            flag = false;

        } else if (dataSp.length == 2) {

            pickNo = dataSp[0].trim().toUpperCase();
            pickDate = dataSp[1].trim().toUpperCase();
            flag = true;
            pickListDataCallingFromServer(userPref.getOrgId(), pickNo, pickDate);
        }
        return flag;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (wedge != null) {
                wedge.setEnable(true);
                wedge = null;
            }
            if (bcr != null) {
                bcr.close();
                bcr = null;
            }

        } catch (VirtualWedgeException e) {
            e.printStackTrace();
        }
        try {
            //disconnect from data collection service
            AidcManager.disconnectService();
        } catch (NullPointerException npe) {
            npe.getStackTrace();
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.PICK_UP_SUMMARY_REQ_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    Gson gson = new Gson();
                    bean = gson.fromJson(response, PickUpBean.class);
                    String messageCode = bean.getMessageCode();
                    String message = bean.getMessage();
                    if (messageCode.equalsIgnoreCase("0")) {
                        db.addPickUpList(bean.getData());
                        pickListVerifier();
                    } else {
                        confirmAlert("Alert", message);
                    }
                } catch (Exception e) {
                    AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", e.toString());
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PickupList.this, "Alert", response.toString());
            }
        }
    }

    private void dropDownData() {
        customerList = new ArrayList<>();
        customerList.add(0, "Select Customer");
        for (int i = 0; i < bean.getData().size(); i++) {
            customerList.add(bean.getData().get(i).getCUSTOMERNAME() + "(" + bean.getData().get(i).getCUSTOMERCODE() + ")");
        }

        ArrayAdapter<String> dataAdapterCustomer = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerList);
        dataAdapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSp.setAdapter(dataAdapterCustomer);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if (adapterView != null) {
//            if (i != 0) {
//                switch (adapterView.getId()) {
//                    case R.id.customerSp:
//                        customerName = adapterView.getItemAtPosition(i).toString();
//                        customerCode = bean.getData().get(i - 1).getCUSTOMERCODE().toString();
////                    pickNoPickDateList.clear();
//                        if (!customerName.equalsIgnoreCase("Select Customer")) {
//                            pickNoPickDateList = db.checkIsPickNoAvailableInDb(customerCode, context);
//                            if (pickNoPickDateList.isEmpty()) {
//                                customerSp.setEnabled(true);
//                            } else {
//                                List<String> pickNoList = new ArrayList<>();
//                                pickNoList.add(0, "Select Pick No.");
//                                for (int j = 0; j < pickNoPickDateList.size(); j++) {
//                                    pickNoList.add(pickNoPickDateList.get(j).getPICKNO());
//                                }
//                                ArrayAdapter<String> dataAdapterPickNo = new ArrayAdapter<String>(this,
//                                        android.R.layout.simple_spinner_item, pickNoList);
//                                dataAdapterPickNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                pickNoSp.setAdapter(dataAdapterPickNo);
//                                customerSp.setEnabled(false);
//                            }
//                        } else {
//                        }
//
//                        break;
//                    case R.id.pickNoSp:
//                        pickNo = adapterView.getItemAtPosition(i).toString();
//                        if (!pickNo.equalsIgnoreCase("Select Pick No.")) {
//                            pickDateListdb = db.checkIsPickDateAvailableInDb(pickNo, context);
//                            if (pickDateListdb.isEmpty()) {
//                                pickNoSp.setEnabled(true);
//                            }else {
//                                scanFlag = true;
//                                for (int k = 0; k < pickDateListdb.size(); k++) {
//                                    pickDateList.add(pickDateListdb.get(k).getPICKDATE());
//                                }
//                                ArrayAdapter<String> dataAdapterPickDate = new ArrayAdapter<String>(this,
//                                        android.R.layout.simple_spinner_item, pickDateList);
//                                dataAdapterPickDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                pickDateSp.setAdapter(dataAdapterPickDate);
//                                pickNoSp.setEnabled(false);
//                                pickDateSp.setEnabled(false);
//                            }
//                        }
//                        break;
//                    case R.id.pickDateSp:
//                        pickDate = adapterView.getItemAtPosition(i).toString();
//                        break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PickupList.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(PickupList.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        builder.create();
        builder.show();

    }
}
*/
