package com.essindia.stlapp.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.essindia.stlapp.Bean.ReceiptTranItemDetailBean;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.Constants;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

public class ReceivingItemDetailActivity extends BaseActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, /*AdapterView.OnItemSelectedListener,*/ View.OnClickListener {

    private String mSelectedSpinerItem;
    private String mVcLoctionName = "", mVcLoctionId = "";
    private int mSelectedLocItemPos;
    private EditText mEtLoctionQrcode;
    private AidcManager manager;
    private static BarcodeReader bcr;
    private ReceiptTranItemDetailBean rmReceiptTranItemDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_item_detail);
        rmReceiptTranItemDetailBean = this.getIntent().getParcelableExtra(BundleKey.RM_RECEIPT_ITEM_DETAIL);
        TextView gradeNo = (TextView) findViewById(R.id.tv_grade_no);
        TextView mrirNo = (TextView) findViewById(R.id.tv_mrir_no);
        TextView coilNo = (TextView) findViewById(R.id.tv_coil_no);
        TextView coilWeight = (TextView) findViewById(R.id.tv_coil_weight);
        gradeNo.setText(rmReceiptTranItemDetailBean.getVcSGradeNo());
        mrirNo.setText(rmReceiptTranItemDetailBean.getVcSMrirNo());
        coilNo.setText(rmReceiptTranItemDetailBean.getVcSCoilNo());
        coilWeight.setText(rmReceiptTranItemDetailBean.getNuSDivQty());

        intializeXml();
    }

    private void intializeXml() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.receipt_item_detail);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);

        findViewById(R.id.img_verify).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        mEtLoctionQrcode = (EditText) findViewById(R.id.et_loction_qrcode);
      /*  Spinner spLocation = (Spinner) findViewById(R.id.sp_location);
        LocationListAdapter locAdapter = new LocationListAdapter(this, rmReceiptTranItemDetailBean.getItemLocBeen());
        spLocation.setAdapter(locAdapter);
        spLocation.setOnItemSelectedListener(this);*/
    }

   /* @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        mSelectedSpinerItem = rmReceiptTranItemDetailBean.getItemLocBeen().get(position).getVcLocationDesc();
        mSelectedLocItemPos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }*/

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

    private boolean spliteData(String data) {
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            mEtLoctionQrcode.setText("");
            AlertDialogManager.getInstance().simpleAlert(ReceivingItemDetailActivity.this, "Alert", "Location QR code is empty.");
        } else if (dataSp.length < 2 || dataSp.length > 2) {
            mEtLoctionQrcode.setText("");
            AlertDialogManager.getInstance().simpleAlert(ReceivingItemDetailActivity.this, "Alert", "Please enter correct format !");
        } else if (dataSp.length == 2) {
            mEtLoctionQrcode.setText(data);
            mVcLoctionName = dataSp[0];
            mVcLoctionId = dataSp[1];
//            loctionId = dataSp[0].trim().toUpperCase();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        confirmAlert("Record is not submit!", "Do you want to go back without submit the record");
    }

    private void confirmAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitRecord(false);
            }
        });

        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_verify:
                String qrCodeData = mEtLoctionQrcode.getText().toString();
                if (!qrCodeData.isEmpty()) {
                    spliteData(qrCodeData);
                } else showToast("Please enter Location Detail");
                break;

            case R.id.btn_submit:
                String locationQrCode = mEtLoctionQrcode.getText().toString();
                if (!locationQrCode.isEmpty()) {
                    if (!mVcLoctionName.isEmpty()) {
//                    if (mSelectedSpinerItem.equalsIgnoreCase(locationQrCode)) {
                        submitRecord(true);
//                    } else
//                        locationMismatchAlert("Alert!\nScanned Location Is Not Matched.", "Do You Want To Proceed ?");
                    } else {
                        if (spliteData(locationQrCode)) {
                            submitRecord(true);
                        }
                    }
                } else {
                    showToast("Please Scan Location Detail");
                }
                break;
        }
    }

   /* private void locationMismatchAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitRecord(true, mSelectedLocItemPos);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }*/

    private void submitRecord(boolean isSuccess) {
        Intent intent = new Intent();
        if (isSuccess) {
            rmReceiptTranItemDetailBean.setItemLocationName(mVcLoctionName);
            rmReceiptTranItemDetailBean.setItemLocationCode(mVcLoctionId);
            intent.putExtra(BundleKey.RM_RECEIPT_ITEM, rmReceiptTranItemDetailBean);
//        setResult(Constants.QR_CODE_RESULT_REQ_CODE, intent);
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}
