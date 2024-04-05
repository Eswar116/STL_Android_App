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

import com.essindia.stlapp.Bean.IssueItemDetailBean;
import com.essindia.stlapp.Constant.BundleKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.Constants;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;

public class IssueItemDetailActivity extends BaseActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener, View.OnClickListener {

    private String mVcMachineId = "";
    private static BarcodeReader bcr;
    private IssueItemDetailBean mIssueItemDetailBean;
    private String mIssueItemQty;
    private AidcManager manager;
    private TextView mTvMachineName;
    private TextView mTvMachineNo;
    private EditText mEtLoctionQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_item_detail);
        mIssueItemDetailBean = this.getIntent().getParcelableExtra(BundleKey.RM_ISSUE_ITEM_DETAIL);
        mIssueItemQty = this.getIntent().getStringExtra(BundleKey.RM_SCAN_ITEM_QTY);
        intializeXml();
        setWidgetValue();
    }

    private void intializeXml() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.issue_machine_detail);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
            setSupportActionBar(toolbar);
        }

        findViewById(R.id.btn_verify).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        mEtLoctionQrcode = (EditText) findViewById(R.id.et_loction_qrcode);
        mTvMachineName = (TextView) findViewById(R.id.tv_machine_name);
        mTvMachineNo = (TextView) findViewById(R.id.tv_machine_no);
    }

    private void setWidgetValue() {
        mTvMachineName.setText(mIssueItemDetailBean.getVcMachineName());
        mTvMachineNo.setText(mIssueItemDetailBean.getVcMachineNo());
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
        String dataSp[] = data.trim().split(Constants.DELIMITER);
        if (dataSp.length == 0) {
            mEtLoctionQrcode.setText("");
            showToast("Please Scan Machine Details");
        } else if (dataSp.length == 2) {
            mVcMachineId = dataSp[1];
            if (mVcMachineId.equalsIgnoreCase(mIssueItemDetailBean.getVcMachineNo())) {
                mEtLoctionQrcode.setText(data);
                showToast("Machine Code Match. Please Press Done Button.");
            } else {
                mEtLoctionQrcode.setText("");
                showToast("Machine Code Does'nt Match. Please Scan Valid Machine Code.");
            }
        } else {
            mEtLoctionQrcode.setText("");
            showToast("Please Scan Valid Machine Code.");
        }
    }

    @Override
    public void onBackPressed() {
        backConfirmAlert("Record is not submit!", "Do you want to go back without submit the record");
    }

    private void backConfirmAlert(String title, String msg) {
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
            case R.id.btn_verify:
                String qrCodeData = mEtLoctionQrcode.getText().toString();
                if (!qrCodeData.isEmpty()) {
                    spliteData(qrCodeData);
                } else showToast("Please Scan Machine Details");
                break;

            case R.id.btn_submit:
                if (mVcMachineId.equalsIgnoreCase(mIssueItemDetailBean.getVcMachineNo())) {
                    submitRecord(true);
                } else showToast("Please Scan Valid Machine Details");
                break;
        }
    }

    private void submitRecord(boolean isSuccess) {
        Intent intent = new Intent();
        if (isSuccess) {
            intent.putExtra(BundleKey.RM_SCAN_ITEM_QTY, mIssueItemQty);
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
