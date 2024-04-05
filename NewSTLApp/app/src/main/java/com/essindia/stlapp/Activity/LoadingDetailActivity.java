package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.essindia.stlapp.Bean.LoadingAdviceDataBean;
import com.essindia.stlapp.Bean.LoadingAdviceDetailBean;
import com.essindia.stlapp.Bean.LoadingAdviceInvoiceBean;
import com.essindia.stlapp.Bean.LoadingAdviceItemBean;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadingDetailActivity extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private static BarcodeReader bcr;
    private AidcManager manager;
    //    private RecyclerView mRvLoadingList;
    private EditText mEdtpendingQty, mEdtBoxQrCode, itemCode, invoiceEt, invoiceDateEt, customerNameEt, itemDescEt, invoiceQty, loadedQty;
    private UserPref userPref;
    //    private CoordinatorLayout coordinatorLayout;
    private SqliteHelper mDb;
    private LoadingAdviceDataBean mLoadingAdviceDataBean;
    private HashMap<String, PendTotalQty> mPendingTask;
//    private HashMap<String, Integer> pendingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_detail);
        intializeXml();
//        pendingTask = new HashMap<>();
        mLoadingAdviceDataBean = this.getIntent().getParcelableExtra(BundleKey.LOADING_LIST_SUMMARY_DATA);
        mDb = new SqliteHelper(this);
        createHashMapForCategory();

        findViewById(R.id.img_verify).
                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           String qrCodeData = mEdtBoxQrCode.getText().toString();
                                           if (!qrCodeData.isEmpty()) {
                                               spliteData(qrCodeData);
                                           } else {
                                               showToast("Please enter Loading Advice List data");
                                           }
                                       }
                                   }
                );

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPendBoxExist()) {
                    showProgressBar("getting data");
                    new Thread(new Task()).start();
                } else {
                    showToast("You have some pending box, Please scan all boxes");
                }
            }
        });
    }

    private boolean isPendBoxExist() {
        for (Map.Entry<String, PendTotalQty> entry : mPendingTask.entrySet()) {
            PendTotalQty pendTotalQty = entry.getValue();
            if (pendTotalQty.getTotalQty() - pendTotalQty.getScannedQty() > 0) {
                return true;
            }
        }
        return false;
    }

    private void createHashMapForCategory() {
        mPendingTask = new HashMap<>();
        for (int i = 0; i < mLoadingAdviceDataBean.getInvoiceList().size(); i++) {
            LoadingAdviceInvoiceBean invoicebean = mLoadingAdviceDataBean.getInvoiceList().get(i);
            if (invoicebean.getLoadingAdviceItemlist() != null && invoicebean.getLoadingAdviceItemlist().size() > 0) {
                for (int j = 0; j < invoicebean.getLoadingAdviceItemlist().size(); j++) {
                    LoadingAdviceItemBean itembean = invoicebean.getLoadingAdviceItemlist().get(j);
                    String orgId = mLoadingAdviceDataBean.getOrgId();
                    String invoiceNo = invoicebean.getInvoiceNo();
                    String invoiceDate = invoicebean.getInvoiceDate();
                    String customerCode = invoicebean.getCustomerCode();
                    String itemCode = itembean.getItemCode();
                    String packSize = itembean.getPackSize();
                    String keyCategory = orgId + "_" + invoiceNo + "_" + invoiceDate + "_" + customerCode + "_" + itemCode + "_" + packSize;
                    if (!mPendingTask.containsKey(keyCategory)) {
                        PendTotalQty pendTotalQty = new PendTotalQty();
                        if (!itembean.getInvoiceQty().isEmpty()) {
                            pendTotalQty.setTotalQty(Integer.parseInt(itembean.getInvoiceQty()));
                            pendTotalQty.setScannedQty(0);
                            mPendingTask.put(keyCategory, pendTotalQty);
                        }
                    }
                }
            }
        }
    }

    class Task implements Runnable {
        @Override
        public void run() {
//            mLoadingScannedBoxList = mDb.getLoadingScannedBoxList();
            JSONArray array = mDb.getBoxDataJsonArray(LoadingDetailActivity.this);
            callSendAllScannedBoxAPI(array.toString());
        }
    }

    private void callSendAllScannedBoxAPI(String boxDataArray) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(boxDataArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject reqParamsLoading = new JSONObject();
        try {
            reqParamsLoading.put(Constants.auth_Token, userPref.getToken());
            reqParamsLoading.put(ReqResParamKey.DEVICE_ID, userPref.getUserDeviceId());
            reqParamsLoading.put("Data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ConnectionDetector.isNetworkAvailable(this)) {
            showProgressBar(getString(R.string.please_wait));
//            CallService.getInstance().getResponseUsingPOST(LoadingDetailActivity.this, Constants.LOADING_POST_BOX_DATA, reqParamsLoading.toString(), LoadingDetailActivity.this, Constants.LOADING_POST_BOX_REQ_ID, false);
            CallService.getInstance().getResponseUsingPOST(LoadingDetailActivity.this, userPref.getBASEUrl()+Constants.LOADING_POST_BOX_DATA, reqParamsLoading.toString(), LoadingDetailActivity.this, Constants.LOADING_POST_BOX_REQ_ID, false);
        } else {
            showLongToast(getString(R.string.not_an_internet_connectivity));
//            Snackbar.make(coordinatorLayout, R.string.not_an_internet_connectivity, Snackbar.LENGTH_LONG).show();
        }
    }


    private void intializeXml() {
        userPref = new UserPref(this);
        //itemCode,invoiceEt,invoiceDateEt,customerNameEt,itemDescEt,invoiceQty,loadedQty,pendingQtyEt
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.loading_advice_detail);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);
//        mRvLoadingList = (RecyclerView) findViewById(R.id.rv_loading_list);
        mEdtpendingQty = (EditText) findViewById(R.id.pendingQtyEt);
        mEdtBoxQrCode = (EditText) findViewById(R.id.et_loading_box_qrcode);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        itemCode = (EditText) findViewById(R.id.itemCode);
        invoiceEt = (EditText) findViewById(R.id.invoiceEt);
        invoiceDateEt = (EditText) findViewById(R.id.invoiceDateEt);
        customerNameEt = (EditText) findViewById(R.id.customerNameEt);
        itemDescEt = (EditText) findViewById(R.id.itemDescEt);
        invoiceQty = (EditText) findViewById(R.id.invoiceQty);
        loadedQty = (EditText) findViewById(R.id.loadedQty);
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
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spliteData(barcodeReadEvent.getBarcodeData().toString().trim());
            }
        });

    }

    private void spliteData(String data) {
        mEdtBoxQrCode.setText(data);
        String orgId, invoiceNo, invoiceDate, customerCode, customerName, itemCode, packSize, noOfBox, boxId;
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadingDetailActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length < 8 || dataSp.length > 8) {
            AlertDialogManager.getInstance().simpleAlert(LoadingDetailActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 8) {
            orgId = dataSp[0].trim().toUpperCase();
            invoiceNo = dataSp[1].trim().toUpperCase();
            invoiceDate = dataSp[2].trim().toUpperCase();
            customerCode = dataSp[3].trim().toUpperCase();
            customerName = dataSp[4].trim().toUpperCase();
            itemCode = dataSp[5].trim().toUpperCase();
            packSize = dataSp[6].trim().toUpperCase();
//            noOfBox = dataSp[7].trim().toUpperCase();
            boxId = dataSp[7].trim().toUpperCase();
            checkAndInsertData(orgId, invoiceNo, invoiceDate, customerCode, customerName, itemCode, packSize, boxId);
        }
    }

    private void checkAndInsertData(String orgId, String invoiceNo, String invoiceDate, String customerCode, String customerName, String itemCode, String packSize, String boxId) {
        boolean isAlreadyExist = mDb.isLoadingBoxExistInDb(boxId);
        LoadingAdviceDetailBean bean = null;
        if (!isAlreadyExist) {
            if (mLoadingAdviceDataBean.getOrgId().equalsIgnoreCase(orgId)) {
                boolean isInvoiceAvailableInLoadingList = false;
                for (int i = 0; i < mLoadingAdviceDataBean.getInvoiceList().size(); i++) {
                    LoadingAdviceInvoiceBean invoice = mLoadingAdviceDataBean.getInvoiceList().get(i);
                    if (invoice.getInvoiceNo().equalsIgnoreCase(invoiceNo) && invoice.getInvoiceDate().equalsIgnoreCase(invoiceDate) && invoice.getCustomerCode().equalsIgnoreCase(customerCode)) {
                        List<LoadingAdviceItemBean> loadingAdviceItemlist = invoice.getLoadingAdviceItemlist();
                        for (int j = 0; j < loadingAdviceItemlist.size(); j++) {
                            LoadingAdviceItemBean itemBean = loadingAdviceItemlist.get(j);
                            if (itemBean.getItemCode().equalsIgnoreCase(itemCode) && itemBean.getPackSize().equalsIgnoreCase(packSize)) {
                                isInvoiceAvailableInLoadingList = true;
                                String keyCategory = orgId + "_" + invoiceNo + "_" + invoiceDate + "_" + customerCode + "_" + itemCode + "_" + packSize;
                                if (isBoxWithinLimit(keyCategory)) {
//                                LoadingAdviceDetailBean loadingAdviceDetailBean = mDb.getLoadingScannedBoxCompleteData(orgId, invoiceNo, invoiceDate, customerCode, customerName, itemCode, packSize, noOfBox, boxId);
                                    bean = new LoadingAdviceDetailBean();
                                    bean.setVcUserCode(mLoadingAdviceDataBean.getVcUserCode());
                                    bean.setDriverName(mLoadingAdviceDataBean.getDriverName());
                                    bean.setPreAdviceDate(mLoadingAdviceDataBean.getPreAdviceDate());
                                    bean.setCldId(mLoadingAdviceDataBean.getCldId());
                                    bean.setProjId(mLoadingAdviceDataBean.getProjId());
                                    bean.setVehicleNo(mLoadingAdviceDataBean.getVehicleNumber());
                                    bean.setPreAdviceNo(mLoadingAdviceDataBean.getPreAdviceNo());
                                    bean.setCustomerCode(customerCode);
                                    bean.setCustomerName(customerName);
                                    bean.setInvoiceNo(invoiceNo);
                                    bean.setInvoiceQty(itemBean.getInvoiceQty());
                                    bean.setItemCode(itemCode);
                                    bean.setPackSize(packSize);
                                    bean.setInvoiceDate(invoiceDate);
                                    bean.setSlocId(mLoadingAdviceDataBean.getSlocId());
                                    bean.setHoOrgId(mLoadingAdviceDataBean.getHoOrgId());
                                    bean.setOrgId(orgId);
                                    bean.setBoxId(boxId);
                                    bean.setItemDesc(itemBean.getItemDesc());

                                    boolean isInserted = mDb.insertScannedLoadingBox(bean);
                                    if (!isInserted) {
                                        showToast("This Item is not store properly, Please scan the box again");
                                    } else {
                                        addLoadedDataInCategory(keyCategory);
                                        setPendingQty(keyCategory);
                                        displayDataOfScannedBox(bean);
                                    }
                                    return;
                                } else {
                                    showToast("This Item is already loaded for respective customer");
                                }
                            }
                        }
                    }
                }
                if (!isInvoiceAvailableInLoadingList) {
                    showToast("Invoice no. is not matched with scanned Loading List");
                }
            } else
                showToast("This box is not belongs to selected Loading Advice List, Please scan another box");
        } else
            showToast("This box is already scanned, Please scan another box");

       /* if (bean != null) {
            mDb.insertScannedLoadingBox(bean, LoadingDetailActivity.this);
//                    int i = mDb.noOfUploadedScannedBoxAvailableInDb("orgId4", "custCode4", "invoceDate4", "itemCode4", "itemCode4", "400");
            int i = mDb.noOfUploadedScannedBoxAvailableInDb(orgId, customerCode, invoiceNo, invoiceDate, itemCode, packSize);
            if (mLoadingAdviceListDataBean != null && mLoadingAdviceListDataBean.getInvoiceQty() != null && !mLoadingAdviceListDataBean.getInvoiceQty().isEmpty()) {
                setPendingQty(Integer.parseInt(mLoadingAdviceListDataBean.getInvoiceQty()) - i);
//                    loadingAdviceDetailBean.getOrgId() + "_" + loadingAdviceDetailBean.getCustomerCode() + "_" + loadingAdviceDetailBean.getInvoiceNo() + "_" + loadingAdviceDetailBean.getInvoiceDate() + "_" + loadingAdviceDetailBean.getItemCode() + "_" + loadingAdviceDetailBean.getPackSize()
//                    pendingTask.put(mLoadingAdviceListDataBean.getOrgId() + "_" + mLoadingAdviceListDataBean.getCustomerCode() + "_" + mLoadingAdviceListDataBean.getInvoiceNo() + "_" + mLoadingAdviceListDataBean.getInvoiceDate() + "_" + mLoadingAdviceListDataBean.getItmeCode() + "_" + mLoadingAdviceListDataBean.getPackSize(), Integer.parseInt(mLoadingAdviceListDataBean.getInvoiceQty()) - i);
            }
        } else
            showToast("Data is not properly stored, Please try again.");*/


    }

    private void displayDataOfScannedBox(LoadingAdviceDetailBean bean) {
        itemCode.setText(bean.getItemCode());
        invoiceEt.setText(bean.getInvoiceNo());
        invoiceDateEt.setText(bean.getInvoiceDate());
        customerNameEt.setText(bean.getCustomerName());
        itemDescEt.setText(bean.getItemDesc());
        invoiceQty.setText(bean.getInvoiceQty());
//        loadedQty.setText(bean.get);
    }

    private void addLoadedDataInCategory(String keyCategory) {
        if (mPendingTask.containsKey(keyCategory)) {
            PendTotalQty pendTotalQty = mPendingTask.get(keyCategory);
            long scannedQty = pendTotalQty.getScannedQty();
            pendTotalQty.setScannedQty(scannedQty + 1);
            mPendingTask.put(keyCategory, pendTotalQty);
        }
    }

    private boolean isBoxWithinLimit(String categoryKey) {
        if (mPendingTask != null && mPendingTask.size() > 0) {
            for (Map.Entry<String, PendTotalQty> entry : mPendingTask.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase(categoryKey)) {
                    PendTotalQty pendTotalQty = entry.getValue();
                    if (pendTotalQty.getTotalQty() > pendTotalQty.getScannedQty()) {
                        return true;
                    }
                }
            }
        } else {
            showToast("Please scan box");
            return false;
        }
        return false;
    }

    private void setPendingQty(String keyCategory) {
        if (mPendingTask.containsKey(keyCategory)) {
            PendTotalQty pendTotalQty = mPendingTask.get(keyCategory);
            long scannedQty = pendTotalQty.getScannedQty();
            long totalQty = pendTotalQty.getTotalQty();
            mEdtpendingQty.setText("" + (totalQty - scannedQty));
            loadedQty.setText("" + scannedQty);
        }
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        hideProgressBar();
        if (response != null) {
            if (request_id == Constants.LOADING_POST_BOX_REQ_ID) {
                try {
                    JSONObject result = new JSONObject(response);
                    String statusCode = result.optString("MessageCode");
                    String message = result.optString("Message");

                    if (statusCode.equalsIgnoreCase("0")) {
                        showToast(message);
//                        mDb.clearAllBoxData();
                        Intent i = new Intent(LoadingDetailActivity.this, LoadingListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    } else
                        AlertDialogManager.getInstance().simpleAlert(LoadingDetailActivity.this, "Alert", message);
                } catch (JSONException e) {
                    AlertDialogManager.getInstance().simpleAlert(LoadingDetailActivity.this, "Error", e.toString());
                }
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(LoadingDetailActivity.this, "Error", "Something went wrong on Server side");
        }
    }

    @Override
    public void onBackPressed() {
        confirmAlert();
    }

    private void confirmAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Error");
        builder.setMessage("Data not saved, Do you really want to go back ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(LoadingDetailActivity.this, LoadingListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();

    }

    private class PendTotalQty {
        long totalQty;
        long scannedQty;

        private long getTotalQty() {
            return totalQty;
        }

        private void setTotalQty(long totalQty) {
            this.totalQty = totalQty;
        }

        private long getScannedQty() {
            return scannedQty;
        }

        private void setScannedQty(long scannedQty) {
            this.scannedQty = scannedQty;
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
