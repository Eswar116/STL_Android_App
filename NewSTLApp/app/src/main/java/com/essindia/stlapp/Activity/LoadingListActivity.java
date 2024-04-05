package com.essindia.stlapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.ExpandableListAdapter;
import com.essindia.stlapp.Bean.LoadingAdviceDataBean;
import com.essindia.stlapp.Bean.LoadingAdviceInvoiceBean;
import com.essindia.stlapp.Bean.LoadingAdviceItemBean;
import com.essindia.stlapp.Bean.LoadingAdviceListBean;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadingListActivity extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private static BarcodeReader bcr;
//    private CoordinatorLayout coordinatorLayout;
    private RecyclerView mRvLoadingList;
    private AidcManager manager;
    private EditText mEdtQrCode;
    private UserPref userPref;
    private LoadingAdviceDataBean mLoadingDataList;
    private LoadingAdviceListBean mLoadingAdviceListBean;
    private SqliteHelper mDb;
    private ExpandableListView el_expandableview;
    private TextView txv_driver_name, txv_truck_no/*, orgId, preAdviceNo, preAdviceDate*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_list);
        intializeXml();
        mDb = new SqliteHelper(this);

        findViewById(R.id.img_verify).
                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
//                                           String qrCodeData = "02,1,15-09-2016";
//                                           Intent i = new Intent(LoadingListActivity.this,LoadingDetailActivity.class);
//                                           startActivity(i);
                                           String qrCodeData = mEdtQrCode.getText().toString();
                                           if (!qrCodeData.isEmpty()) {
                                               scannedDataSplite(qrCodeData);
                                           } else {
                                               showToast("Please enter Loading Advice List data");
                                           }
                                       }
                                   }
                );
        findViewById(R.id.btn_start_scanning).
                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if (mLoadingAdviceListBean != null) {
                                               if (mLoadingAdviceListBean.getData() != null) {
                                                   if (mLoadingAdviceListBean.getData().size() > 0) {
                                                       LoadingAdviceDataBean loadingAdviceDataBean = mLoadingAdviceListBean.getData().get(0);
                                                       if (loadingAdviceDataBean.getInvoiceList() != null && loadingAdviceDataBean.getInvoiceList().size() > 0) {
                                                           mDb.insertLoadingAdviceList(loadingAdviceDataBean);
                                                           Intent intent = new Intent(LoadingListActivity.this, LoadingBoxDetailActivity.class);
                                                           intent.putExtra(BundleKey.LOADING_LIST_SUMMARY_DATA, loadingAdviceDataBean);
                                                           startActivity(intent);
                                                           overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                                           finish();
                                                       } else
                                                           showToast("Invoice no. is not available in scanned Loading Advice List");
                                                   } else
                                                       showToast("This Loading Advice List completed");
                                               } else
                                                   showToast(mLoadingAdviceListBean.getMessage());
                                           } else
                                               showToast("Please scan Loading Advice List");
                                       }
                                   }
                );
    }

    private void callLoadingSummaryAPI(String companyCode, String loadingNo, String loadingDate) {

        loadingDate = DateUtil.ddMMyyyyToddMMMyyyy(loadingDate);
        if (!loadingDate.isEmpty()) {
            JSONObject reqParamsLoadingList = new JSONObject();
            try {
                reqParamsLoadingList.put(Constants.auth_Token, userPref.getToken());
                reqParamsLoadingList.put(ReqResParamKey.ORG_ID, companyCode);
                reqParamsLoadingList.put(ReqResParamKey.PRE_ADVICE_NO, loadingNo);
                reqParamsLoadingList.put(ReqResParamKey.PRE_ADVICE_DATE, loadingDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ConnectionDetector.isNetworkAvailable(this)) {
                showProgressBar(getString(R.string.please_wait));
//                CallService.getInstance().getResponseUsingPOST(LoadingListActivity.this, Constants.LOADING_SUMMARY_LIST, reqParamsLoadingList.toString(), LoadingListActivity.this, Constants.LOADING_SUMMARY_LIST_REQ_ID, false);
                CallService.getInstance().getResponseUsingPOST(LoadingListActivity.this, userPref.getBASEUrl()+Constants.LOADING_SUMMARY_LIST, reqParamsLoadingList.toString(), LoadingListActivity.this, Constants.LOADING_SUMMARY_LIST_REQ_ID, false);
            } else {
                showLongToast(getString(R.string.not_an_internet_connectivity));
//                Snackbar.make(coordinatorLayout, R.string.not_an_internet_connectivity, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoadingListActivity.this, "Invalid Loading Date Format", Toast.LENGTH_LONG).show();
        }

    }

    private void intializeXml() {
        userPref = new UserPref(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.loading_advice);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mEdtQrCode = (EditText) findViewById(R.id.et_loading_qrcode);
        el_expandableview = (ExpandableListView) findViewById(R.id.el_expandableview);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        txv_truck_no = (TextView) findViewById(R.id.txv_truck_no);
        txv_driver_name = (TextView) findViewById(R.id.txv_driver_name);
      /*  preAdviceNo = (TextView) findViewById(R.id.preAdviceNo);
        orgId = (TextView) findViewById(R.id.orgId);
        preAdviceDate = (TextView) findViewById(R.id.preAdviceDate);*/
        el_expandableview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Doing nothing
                return true;
            }
        });
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
                scannedDataSplite(barcodeReadEvent.getBarcodeData().toString().trim());
            }
        });

    }

    private void scannedDataSplite(String qrCodeData) {
        mEdtQrCode.setText(qrCodeData);
        String companyCode, invoiceNo, invoiceDate;
        String dataSp[] = qrCodeData.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(LoadingListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length == 3) { // for loading list scan
            companyCode = dataSp[0].trim().toUpperCase();
            invoiceNo = dataSp[1].trim().toUpperCase();
            invoiceDate = dataSp[2].trim().toUpperCase();
            callLoadingSummaryAPI(companyCode, invoiceNo, invoiceDate);
        } else {
            AlertDialogManager.getInstance().simpleAlert(LoadingListActivity.this, "Alert", "Please enter correct format !");
        }

    }

    private void setTruckDetail(LoadingAdviceDataBean bean) {
        txv_driver_name.setText(bean.getDriverName());
        txv_truck_no.setText(bean.getVehicleNumber());
      /*  preAdviceNo.setText(bean.getPreAdviceNo());
        orgId.setText(bean.getOrgId());
        preAdviceDate.setText(bean.getPreAdviceDate());*/
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        hideProgressBar();
        Gson gson = new Gson();

        if (response != null) {
            if (request_id == Constants.LOADING_SUMMARY_LIST_REQ_ID) {
                mLoadingAdviceListBean = gson.fromJson(response, LoadingAdviceListBean.class);
                if (mLoadingAdviceListBean != null)
                    if (!mLoadingAdviceListBean.getMessageCode().equalsIgnoreCase("1"))
                        if (mLoadingAdviceListBean.getData() != null) {
                            if (mLoadingAdviceListBean.getData().size() > 0) {
                                mDb.clearAllBoxData();
//                                showList(mLoadingAdviceListBean.getData().get(0));
                                setTruckDetail(mLoadingAdviceListBean.getData().get(0));
                                splittingParentChildFromRoot();
                            } else {
//                                showToast("No Loading data available for this Advice list");
                                showToast(mLoadingAdviceListBean.getMessage());
                            }
                        } else
                            showToast(mLoadingAdviceListBean.getMessage());
                    else
                        showToast(mLoadingAdviceListBean.getMessage());
                else
                    showToast(mLoadingAdviceListBean.getMessage());
            }
        }
    }

    public void splittingParentChildFromRoot() {
        List<LoadingAdviceDataBean> rootList = mLoadingAdviceListBean.getData();
        List<LoadingAdviceInvoiceBean> parentList = new ArrayList<>();
        List<LoadingAdviceItemBean> childList = new ArrayList<>();

        HashMap<LoadingAdviceInvoiceBean, List<LoadingAdviceItemBean>> parentChildMappedData = new HashMap<>();
        for (int j = 0; j < rootList.size(); j++) {
            parentList.addAll(rootList.get(j).getInvoiceList());
            System.out.println("parentList:" + parentList);
        }
        for (int i = 0; i < parentList.size(); i++) {
            childList.addAll(parentList.get(i).getLoadingAdviceItemlist());
            System.out.println("childList:" + childList);
        }
//            for (int i = 0; i < parentList.size(); i++) {
//                for (int j = 0; j < childList.size(); j++) {
//                    parentChildMappedData.put(parentList, childList);
//                }
        //}
        for (int i = 0; i < parentList.size(); i++) {
            for (int j = 0; j < childList.size(); j++) {
                parentChildMappedData.put(parentList.get(i), childList);
            }
        }

        System.out.println("Hashmap internal data:" + parentChildMappedData);
        ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), parentList, parentChildMappedData);
        el_expandableview.setAdapter(adapter);
    }

  /*  void showList(LoadingAdviceDataBean bean) {
        LoadingListAdapter adapter = new LoadingListAdapter(getApplicationContext(), bean);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRvLoadingList.setLayoutManager(layoutManager);
        mRvLoadingList.setAdapter(adapter);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoadingListActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

}
