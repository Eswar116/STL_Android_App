package com.essindia.stlapp.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.IssueItemListAdapter;
import com.essindia.stlapp.Bean.IssueItemDetailBean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.DateUtil;
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

import java.util.ArrayList;

public class IssueItemListActivity extends BaseActivity implements OnResponseFetchListener, BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, View.OnClickListener {
    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private EditText mEtItemQrcode;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranDetailList;
    private LinearLayoutManager layoutManager;
    private IssueItemListAdapter adapter;
    private ArrayList<IssueItemDetailBean> mItemList;
    private static BarcodeReader bcr;
    private AidcManager manager;
    private int mScanningItemIndex = 0;
    private int mTotalScannedItem = 0;
    private String mItemLocationName = "";
    private String mItemLocationCode = "";
    private IssueItemDetailBean mMrsBean;
    String qrContent;//jonu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);
        intializeXml();
        getRmIssueMrsNoItemListData();
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mEtItemQrcode = (EditText) findViewById(R.id.et_item_qrcode);
        findViewById(R.id.img_verify).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        mRvTranDetailList = (RecyclerView) findViewById(R.id.rv_tran_detail);
        toolbar.setTitle(R.string.rm_issuance_item_list);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);

        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
    }

    private void getRmIssueMrsNoItemListData() {
        Bundle b = getIntent().getExtras();
        mMrsBean = b.getParcelable(BundleKey.RM_ISSUE_MRS_DETAIL);
        JSONObject param = new JSONObject();
        try {
            String despDate = mMrsBean.getDtDespDate();
            despDate = DateUtil.ddMMyyyyToddMMMyyyy(despDate);
            if (!despDate.isEmpty()) {
                param.put(Constants.auth_Token, mUserPref.getToken());
                param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
                param.put(Constants.ORG_ID, mUserPref.getOrgId());
                param.put(ReqResParamKey.VC_DESP_NO, mMrsBean.getVcDespNo());
                param.put(ReqResParamKey.DT_DESP_DATE, despDate);
                if (mConnectionDetector.isConnectingToInternet()) {
//                    CallService.getInstance().getResponseUsingPOST(IssueItemListActivity.this, Constants.RM_ISSUE_MRS_NO_ITEM_LIST, param.toString(), IssueItemListActivity.this, Constants.RM_ISSUE_MRS_NO_DETAIL_LIST_REQ_ID, true);
                    CallService.getInstance().getResponseUsingPOST(IssueItemListActivity.this, mUserPref.getBASEUrl()+Constants.RM_ISSUE_MRS_NO_ITEM_LIST, param.toString(), IssueItemListActivity.this, Constants.RM_ISSUE_MRS_NO_DETAIL_LIST_REQ_ID, true);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", "Please check your network connection");
                }
            } else {
                Toast.makeText(IssueItemListActivity.this, "Invalid Desp Date Format", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
                if (mItemList != null && mItemList.size() > 0) {
                    if (mTotalScannedItem < mItemList.size()) {
                        spliteData(barcodeReadEvent.getBarcodeData().toString().trim());
                    } else {
                        showToast("All item scanned, Total scanned item is equal to total assigned item.");
                    }
                } else {
                    showToast("No item for scanning");
                }
            }
        });
    }

    private void spliteData(String data) {
        mEtItemQrcode.setText(data);
        String mrirNo, mrirDate, routeCardNo, routeCardDate, coilNo, div, heatNo, coilDivWeight, grade, wireDia, itemCode, processDate;
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", "Qr code is empty cannot load data item");
        } else if (dataSp.length == 2) {
            mItemLocationName = dataSp[0].trim().toUpperCase();
            mItemLocationCode = dataSp[1].trim().toUpperCase();
            validateScanLocation(mItemLocationName, mItemLocationCode);
        } else if (dataSp.length == 12) {
            if (!mItemLocationName.isEmpty()) {
                mrirNo = dataSp[0].trim().toUpperCase();
                mrirDate = dataSp[1].trim().toUpperCase();
                routeCardNo = dataSp[2].trim().toUpperCase();
                routeCardDate = dataSp[3].trim().toUpperCase();
                coilNo = dataSp[4].trim().toUpperCase();
                div = dataSp[5].trim().toUpperCase();
                heatNo = dataSp[6].trim().toUpperCase();
                coilDivWeight = dataSp[7].trim().toUpperCase();
                grade = dataSp[8].trim().toUpperCase();
                wireDia = dataSp[9].trim().toUpperCase();
                itemCode = dataSp[10].trim().toUpperCase();
                processDate = dataSp[11].trim().toUpperCase();
                String itemUniqueId = mrirNo + mrirDate + routeCardNo + routeCardDate + coilNo + div + heatNo + grade + wireDia + itemCode /*processDate*/;
                if (mItemList != null && mItemList.size() > 0) {
                    boolean isItemMatch = false;
                    for (int i = 0; i < mItemList.size(); i++) {
                        IssueItemDetailBean rmIssueItemDetailBean = mItemList.get(i);
                        String beanMrirNo, beanMrirDate, beanRouteCardNo, beanRouteCardDate, beanDivision, beanCoilNo, beanHeatNo, beanCoilDivWeight, beanGrade, beanWireDia, beanItemCode, beanProcessDate;

                        beanMrirNo = rmIssueItemDetailBean.getVcMrirNo();
                        beanMrirDate = rmIssueItemDetailBean.getDtMrirDate();
                        beanRouteCardNo = rmIssueItemDetailBean.getVcRouteNo();
                        beanRouteCardDate = rmIssueItemDetailBean.getDtRouteDate();
                        beanCoilNo = rmIssueItemDetailBean.getVcCoilNo();
                        beanDivision = rmIssueItemDetailBean.getVcDiv();
                        if (!(beanDivision != null && !beanDivision.isEmpty() && !beanDivision.equalsIgnoreCase("null"))) {
                            beanDivision = "";
                        }
                        beanHeatNo = rmIssueItemDetailBean.getVcHeatNo();
                        beanCoilDivWeight = rmIssueItemDetailBean.getNuQtyIssued();
                        beanGrade = rmIssueItemDetailBean.getVcGradeNo();
                        beanWireDia = rmIssueItemDetailBean.getVcWireDia();
                        beanItemCode = rmIssueItemDetailBean.getVcItemCode();
//                    beanProcessDate = rmIssueItemDetailBean.getDtRouteDate();//Subroj

                        String beanItemUniqueId = beanMrirNo + beanMrirDate + beanRouteCardNo + beanRouteCardDate + beanCoilNo + beanDivision + beanHeatNo + beanGrade + beanWireDia + beanItemCode/*+ beanProcessDate*/;
                        if (itemUniqueId.equalsIgnoreCase(beanItemUniqueId)) {
                            if (!rmIssueItemDetailBean.isItemPlaced()) {
                                try {
                                    if (Double.parseDouble(coilDivWeight) >= Double.parseDouble(beanCoilDivWeight)) {
                                        mScanningItemIndex = i;
                                        isItemMatch = true;
                                        Intent in = new Intent(IssueItemListActivity.this, IssueItemDetailActivity.class);
                                        in.putExtra(BundleKey.RM_ISSUE_ITEM_DETAIL, rmIssueItemDetailBean);
                                        in.putExtra(BundleKey.RM_SCAN_ITEM_QTY, coilDivWeight);
                                        startActivityForResult(in, Constants.RM_ISSUE_ITEM_MATCH_RESULT_REQ_ID);
                                        break;
                                    } else {
                                        showToast("Scanned Item Quantity Is Less Than Required Quantity");
                                    }
                                } catch (NumberFormatException nfe) {
                                    showToast("Scanned Item Quantity Is Not In Proper Format");
                                } catch (NullPointerException npe) {
                                    showToast("Either Required Or Exist Item Quantity Is Empty");
                                }
                            } else {
                                showToast("This Item Already Scanned");
                            }
                        }
                    }
                    if (!isItemMatch) {
                        showToast("Item Not Matched");
                    }
                } else {
                    showToast("No Item For Scanning");
                }
            } else {
                showToast("First Scan Item Location");
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", "Please enter correct format !");
        }
    }

    private void validateScanLocation(String pItemLocationName, String pItemLocationCode) {
        if (mItemList != null && mItemList.size() > 0) {
            boolean isLocationMatch = false;
            for (int i = 0; i < mItemList.size(); i++) {
                IssueItemDetailBean rmIssueItemDetailBean = mItemList.get(i);
                String beanLocationName = rmIssueItemDetailBean.getVcLocationName();
                String beanLocationId = rmIssueItemDetailBean.getVcLocationCode();
                if (pItemLocationName.equalsIgnoreCase(beanLocationName) && pItemLocationCode.equalsIgnoreCase(beanLocationId)) {
                    isLocationMatch = true;
                    break;
                }
            }
            if (isLocationMatch) {
                mEtItemQrcode.setText("");
                mEtItemQrcode.setText(qrContent);
                showToast("Now Scan Required Item");
            } else {
                mItemLocationName = "";
                mItemLocationCode = "";
                showToast("Required Item Is Not Exist at Scanned Location, Please Scan Correct Location");
            }
        } else {
            showToast("No Item For Scanning");
        }
    }

    @Override
    public void onBackPressed() {
        backConfirmAlert("Record is not submit!", "Do you want to go back without submit the record");
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (response != null) {
            try {
                JSONObject result = new JSONObject(response);
                System.out.println("response:" + response);
                String statusCode = result.getString("MessageCode");
                String message = result.getString("Message");
                if (statusCode.equalsIgnoreCase("0")) {
                    if (request_id == Constants.RM_ISSUE_MRS_NO_DETAIL_LIST_REQ_ID) {
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            mItemList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                IssueItemDetailBean bean = new IssueItemDetailBean();
                                JSONObject obj = array.optJSONObject(i);

                                bean.setVcLocationCode(obj.optString(ReqResParamKey.VC_LOCATION));
                                bean.setVcLocationName(obj.optString(ReqResParamKey.VC_LOCATION_NAME));
                                bean.setVcAuthCode(obj.optString(ReqResParamKey.VC_AUTH_CODE));
                                bean.setVcMrirNo(obj.optString(ReqResParamKey.VC_MRIR_NO));
                                bean.setDtMrirDate(obj.optString(ReqResParamKey.DT_MRIR_DATE));
                                bean.setVcRouteNo(obj.optString(ReqResParamKey.VC_ROUTE_NO));
                                bean.setDtRouteDate(obj.optString(ReqResParamKey.DT_ROUTE_DATE));
                                bean.setVcCoilNo(obj.optString(ReqResParamKey.VC_COIL_NO));
                                bean.setVcHeatNo(obj.optString(ReqResParamKey.VC_HEAT_NO));
                                bean.setVcGradeNo(obj.optString(ReqResParamKey.VC_GRADE_NO));
                                bean.setVcWireDia(obj.optString(ReqResParamKey.VC_WIRE_DIA));
                                bean.setVcLotNo(obj.optString(ReqResParamKey.VC_LOT_NO));
                                bean.setNuQtyIssued(obj.optString(ReqResParamKey.NU_QTY_ISSUED));
                                bean.setVcItemCode(obj.optString(ReqResParamKey.VC_ITEM_CODE));
                                bean.setDtProcessDate(obj.optString(ReqResParamKey.DT_PROCESS_DATE));
                                bean.setVcMachineNo(obj.optString(ReqResParamKey.VC_MACHINE_NO));
                                bean.setVcMachineName(obj.optString(ReqResParamKey.VC_MACHINE_NAME));
                                bean.setVcDiv(obj.optString(ReqResParamKey.VC_DIVISION));
                                bean.setVcDespNo(mMrsBean.getVcDespNo());
                                bean.setDtDespDate(mMrsBean.getDtDespDate());
                                qrContent = bean.getVcMrirNo() + "<>" + bean.getDtMrirDate() + "<>" + bean.getVcRouteNo() + "<>" + bean.getDtRouteDate() + "<>" + bean.getVcCoilNo() + "<>" + bean.getVcDiv() + "<>" + bean.getVcHeatNo() + "<>" + bean.getNuQtyIssued() + "<>" + bean.getVcGradeNo() + "<>" + bean.getVcWireDia() + "<>" + bean.getVcItemCode()+"<>"+bean.getDtProcessDate();
                                Log.i("Item: " + i+1, qrContent);
                                mItemList.add(bean);
                            }
                            adapter = new IssueItemListAdapter(getApplicationContext(), mItemList);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranDetailList.setLayoutManager(layoutManager);
                            mRvTranDetailList.setAdapter(adapter);
                        } else {
                            showToast("No Item For Issue");
                            goBackMrsListScreen();
                        }
                    } else if (request_id == Constants.RM_ISSUE_LIST_SUBMIT_REQ_ID) {
                        showToast("Item submit succefully");
                        goBackMrsListScreen();
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", response.toString());
        }

    }

    private void goBackMrsListScreen() {
        Intent i = new Intent(IssueItemListActivity.this, IssueListActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void backConfirmAlert(String title, String msg) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_verify:
                String itemQrCode = mEtItemQrcode.getText().toString();
                if (itemQrCode != null && !itemQrCode.isEmpty()) {
                    spliteData(mEtItemQrcode.getText().toString());
                } else showToast("Please Enter Item QR Code");
                break;
            case R.id.btn_submit:
                if (mItemList != null && mItemList.size() > 0) {
                    if (mTotalScannedItem == mItemList.size()) {
                        callItemSubmitApi();
                    } else {
                        showToast("Please scan all item.");
                    }
                } else {
                    showToast("No item for submit");
                }
                break;
        }
    }

    private void callItemSubmitApi() {
        JSONArray itemArray = new JSONArray();
        try {
            for (IssueItemDetailBean bean : mItemList) {
                JSONObject param = new JSONObject();
                param.put(ReqResParamKey.TOKEN, mUserPref.getToken());
                param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
                param.put(ReqResParamKey.ORG_ID, mUserPref.getOrgId());
                param.put(ReqResParamKey.VC_MRIR_NO, bean.getVcMrirNo());
                param.put(ReqResParamKey.DT_MRIR_DATE, bean.getDtMrirDate());
                param.put(ReqResParamKey.VC_ROUTE_NO, bean.getVcRouteNo());
                param.put(ReqResParamKey.DT_ROUTE_DATE, bean.getDtRouteDate());
                param.put(ReqResParamKey.VC_COIL_NO, bean.getVcCoilNo());
                param.put(ReqResParamKey.VC_HEAT_NO, bean.getVcHeatNo());
                param.put(ReqResParamKey.VC_GRADE_NO, bean.getVcGradeNo());
                param.put(ReqResParamKey.VC_WIRE_DIA, bean.getVcWireDia());
                param.put(ReqResParamKey.NU_QTY_ISSUED, bean.getNuQtyIssued());
                param.put(ReqResParamKey.VC_DESP_NO, bean.getVcDespNo());
                param.put(ReqResParamKey.DT_DESP_DATE, bean.getDtDespDate());
                param.put(ReqResParamKey.VC_ITEM_CODE, bean.getVcItemCode());
                param.put(ReqResParamKey.VC_DIVISION, bean.getVcDiv());

                itemArray.put(param);
            }
            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(IssueItemListActivity.this, Constants.RM_ISSUE_MRS_SUBMIT, itemArray.toString(), IssueItemListActivity.this, Constants.RM_ISSUE_LIST_SUBMIT_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(IssueItemListActivity.this, mUserPref.getBASEUrl()+Constants.RM_ISSUE_MRS_SUBMIT, itemArray.toString(), IssueItemListActivity.this, Constants.RM_ISSUE_LIST_SUBMIT_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(IssueItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RM_ISSUE_ITEM_MATCH_RESULT_REQ_ID) {
            if (resultCode == RESULT_OK) {
                String issueItemQtm = data.getStringExtra(BundleKey.RM_SCAN_ITEM_QTY);
                mTotalScannedItem = mTotalScannedItem + 1;
                mItemList.get(mScanningItemIndex).setItemPlaced(true);
                mItemList.get(mScanningItemIndex).setNuQtyIssued(issueItemQtm);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(IssueItemListActivity.this, "Item Issue Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
