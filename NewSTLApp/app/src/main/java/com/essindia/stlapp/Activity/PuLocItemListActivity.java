package com.essindia.stlapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.PuLocItemAdapter;
import com.essindia.stlapp.Bean.PickUpBean;
import com.essindia.stlapp.Bean.PickUpSummaryData;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class PuLocItemListActivity extends BaseActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, OnResponseFetchListener {

    private ImageView imageView;
    private RecyclerView recylerView;
    private static BarcodeReader bcr;
    private EditText editText;
    private TextInputLayout usernameET;
    private JSONObject jsonObjReqParam;
    private ConnectionDetector connectionDetector;
    private UserPref userPref;
    private LinearLayoutManager layoutManager;
    private PuLocItemAdapter adapter;
    private PickUpBean bean;
    private String pickDate, pickNo;
    private Context context;
    private ArrayList<PickUpSummaryData> mItemList;
    private SqliteHelper mSqliteHelper;
    private String mQrCodeData, mQrStlPartNo, mPackSize, mActualQty, mQrTime, mOrgId, mPickno, mPickDate;
    private PickUpSummaryData mMatchedBean;
    private String mLocationCode;
    private AidcManager manager;
//    private String mLotNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pu_loc_item_list);
        mItemList = this.getIntent().getParcelableArrayListExtra(BundleKey.KEY_PU_ITEM_LIST);
        mLocationCode = this.getIntent().getStringExtra(BundleKey.KEY_PU_LOCATION_CODE);
        intializeXml();
        pickListVerifier();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.setText(""); // for reset incase when getting back from detail screen
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
                    }
                    catch (ScannerUnavailableException e) {
                        showToast("Scanner unavailable" + e.getMessage());
                    }catch (Exception e) {
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

    private void intializeXml() {
        userPref = new UserPref(this);
        connectionDetector = new ConnectionDetector(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pickUp);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSqliteHelper = new SqliteHelper(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        recylerView = (RecyclerView) findViewById(R.id.recylerView);
        editText = (EditText) findViewById(R.id.editText);
        usernameET = (TextInputLayout) findViewById(R.id.usernameET);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText.getText().toString();
                if (data != null && !data.isEmpty()) {
                    dataSplitAndChecker(data);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Please enter valid data");
                }
            }

        });

    }

    private void getPickUpItemQrCodeValidate(String qrCodeData, String qrStlPartNo, String packSize, String actualQty, String qrTime, String orgId, String pickno, String pickDate) {
        jsonObjReqParam = new JSONObject();
        try {
            jsonObjReqParam.put(ReqResParamKey.QR_CODE_DATA, qrCodeData);
            jsonObjReqParam.put(ReqResParamKey.PRODUCT_CODE, qrStlPartNo);
            jsonObjReqParam.put(ReqResParamKey.PACK_SIZE, Integer.parseInt(packSize));
            jsonObjReqParam.put(ReqResParamKey.ACTUAL_BOX_QTY, Integer.parseInt(actualQty));
            jsonObjReqParam.put(ReqResParamKey.TIME_STAMP, qrTime);
            jsonObjReqParam.put(ReqResParamKey.ORG_ID, orgId);
            jsonObjReqParam.put(ReqResParamKey.PICK_NO, pickno);
            jsonObjReqParam.put(ReqResParamKey.PICK_DATE, pickDate);
            jsonObjReqParam.put(ReqResParamKey.BIN_LOCATION, mLocationCode);
            if (connectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(PuLocItemListActivity.this, Constants.GET_PICKUP_ITEM_CODE_VALIDATE, jsonObjReqParam.toString(), PuLocItemListActivity.this, Constants.PICK_UP_ITEM_CODE_VALIDATE_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(PuLocItemListActivity.this, userPref.getBASEUrl()+ Constants.GET_PICKUP_ITEM_CODE_VALIDATE, jsonObjReqParam.toString(), PuLocItemListActivity.this, Constants.PICK_UP_ITEM_CODE_VALIDATE_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "No internet connection.");
            }
        } catch (Exception e) {
            AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", e.toString());
        }
    }

    private void dataSplitAndChecker(String data) {
        String dataSp[] = data.split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length >= 8) {
            mQrCodeData = data;
            Constants.QR_PROCESS_CODE = dataSp[0].trim().toUpperCase();
            Constants.QR_STL_PART_NO = dataSp[1].trim().toUpperCase();
            Constants.QR_ROUTE_CARD_NO = dataSp[2].trim().toUpperCase();
            Constants.QR_STD_QTY = dataSp[3].trim().toUpperCase();
            Constants.QR_ACTUAL_QTY = dataSp[4].trim().toUpperCase();
            Constants.QR_REFERENCE_NO = dataSp[5].trim().toUpperCase();
            Constants.QR_BOX_TYPE = dataSp[6].trim().toUpperCase();
            Constants.QR_TIME = dataSp[7].trim().toUpperCase();
            boxVerifier();
        } else {
            AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Please enter correct format !");
        }
    }

    private void boxVerifier() {
        boolean isItemExist = false;
        boolean isProductCodeExist = false;
        boolean isPackSizeExist = false;
        boolean isActualQtyExist = false;
        boolean isBoxNonStd = false;
        ArrayList<PickUpSummaryData> productCodeList = new ArrayList<>();
        ArrayList<PickUpSummaryData> packSizeList = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            PickUpSummaryData bean = mItemList.get(i);
            if (bean.getPRODUCTCODE().equalsIgnoreCase(Constants.QR_STL_PART_NO)) {
                productCodeList.add(bean);
            }
        }
        if (productCodeList.size() > 0) {
            for (int i = 0; i < productCodeList.size(); i++) {
                PickUpSummaryData bean = productCodeList.get(i);
                if (bean.getPACKSIZE().equalsIgnoreCase(Constants.QR_STD_QTY)) {
                    packSizeList.add(bean);
                }
            }
            if (packSizeList.size() > 0) {
                try {
                    if (Integer.parseInt(Constants.QR_ACTUAL_QTY) < Integer.parseInt(Constants.QR_STD_QTY)) {
                        isBoxNonStd = true;
                    }
                } catch (Exception e) {
                    showToast("Error : Box Actual Qty Or Pack Size is not matching");
                }
                boolean scannedItemStatus = isScannedItemExistInRefineList(isBoxNonStd, packSizeList);
                if (scannedItemStatus) {
                    for (int i = 0; i < packSizeList.size(); i++) {
                        PickUpSummaryData bean = packSizeList.get(i);
                        if (isBoxNonStd) {
                            if (bean.getPEND_QTY().equalsIgnoreCase(Constants.QR_ACTUAL_QTY)) {
                                mMatchedBean = bean;
                                getPickUpItemQrCodeValidate(mQrCodeData, Constants.QR_STL_PART_NO, Constants.QR_STD_QTY, Constants.QR_ACTUAL_QTY, Constants.QR_TIME, userPref.getOrgId(), bean.getPICKNO(), bean.getPICKDATE());
                                isItemExist = true;
                                break;
                            }
                        } else {
                            if ((Integer.parseInt(Constants.QR_ACTUAL_QTY) == Integer.parseInt(bean.getPACKSIZE())) || Integer.parseInt(Constants.QR_ACTUAL_QTY) <= Integer.parseInt(bean.getPACKSIZE()) + 20) {
                                mMatchedBean = bean;
                                getPickUpItemQrCodeValidate(mQrCodeData, Constants.QR_STL_PART_NO, Constants.QR_STD_QTY, Constants.QR_ACTUAL_QTY, Constants.QR_TIME, userPref.getOrgId(), bean.getPICKNO(), bean.getPICKDATE());
                                isItemExist = true;
                                break;
                            }
                        }
                    }
                }
                if (!isItemExist) {
                    editText.setText("");
                    AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Error : Box Actual Qty Is not Matching");
                }
            } else {
                editText.setText("");
                AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Error : Pack size Is not Matching");
            }
        } else {
            editText.setText("");
            AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", "Error : Product Code Is not Matching");
        }
    }

    private boolean isScannedItemExistInRefineList(boolean isBoxNonStd, ArrayList<PickUpSummaryData> packSizeList) {
        for (int i = 0; i < packSizeList.size(); i++) {
            PickUpSummaryData bean = packSizeList.get(i);
            if (isBoxNonStd) {
                if (bean.getBOX_TYPE().endsWith("NONSTD")) {
                    return true;
                }
            } else {
                if (bean.getBOX_TYPE().endsWith("-STD")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void pickListVerifier() {
        adapter = new PuLocItemAdapter(getApplicationContext(), mItemList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(adapter);
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
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(barcodeReadEvent.getBarcodeData());
                dataSplitAndChecker(barcodeReadEvent.getBarcodeData().toString().trim());
            }
        });
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
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.PICK_UP_ITEM_CODE_VALIDATE_REQ_ID) {
            if (response != null) {
                try {
                    JSONObject result = new JSONObject(response);
                    String messageCode = result.optString(ReqResParamKey.MessageCode);
                    String message = result.optString(ReqResParamKey.Message);
                    if (message.length() > 0 && messageCode.equalsIgnoreCase("0")) {
                        String[] binQtyAndLotList = message.split(Constants.DELIMITER);
                        if (binQtyAndLotList.length == 2) {
                            String binQty = binQtyAndLotList[1];
//                            mLotNo = binQtyAndLotList[2];
                            PickUpSummaryData pickUpSummaryDataBean = new PickUpSummaryData();
                            pickUpSummaryDataBean.setPICKNO(mMatchedBean.getPICKNO());
                            pickUpSummaryDataBean.setPICKDATE(mMatchedBean.getPICKDATE());
                            pickUpSummaryDataBean.setPRODUCTNAME(Constants.QR_PROCESS_CODE);
                            pickUpSummaryDataBean.setPRODUCTCODE(Constants.QR_STL_PART_NO);
                            pickUpSummaryDataBean.setPACKSIZE(Constants.QR_STD_QTY);
                            pickUpSummaryDataBean.setPEND_QTY(Constants.QR_ACTUAL_QTY);
                            pickUpSummaryDataBean.setPEND_BOXES(mMatchedBean.getPEND_BOXES());
                            pickUpSummaryDataBean.setBIN_QTY(binQty);
                            pickUpSummaryDataBean.setVC_BIN_NO(mLocationCode);
//                            pickUpSummaryDataBean.setVC_LOT_NO(mLotNo);
                            Bundle b = new Bundle();
                            b.putParcelable(BundleKey.KEY_PU_SCANNED_ITEM_BEAN, pickUpSummaryDataBean);
                            b.putParcelable(BundleKey.KEY_PU_LIST_ITEM_BEAN, mMatchedBean);
                            b.putString(BundleKey.KEY_PU_ITEM_QR_TIMESTAMP, Constants.QR_TIME);
                            b.putString(BundleKey.KEY_PU_ITEM_QR_CODE, mQrCodeData);
                            editText.setText("");
                            Intent form = new Intent(PuLocItemListActivity.this, PickupDetails.class);
                            form.putExtras(b);
                            startActivity(form);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        } else {
                            showToast("Total boxes not found at scanned location");
                        }
                    } else {
                        editText.setText("");
                        showAlert("Alert", message);
                    }
                } catch (Exception e) {
                    AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", e.toString());
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(PuLocItemListActivity.this, "Alert", response.toString());
            }
        }
    }

    private void showAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
