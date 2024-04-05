package com.essindia.stlapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.ReturnToCageItemListAdapter;
import com.essindia.stlapp.Bean.ReceiptItemBean;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReturnNotUsedItemListActivity extends AppCompatActivity implements OnResponseFetchListener, ReturnToCageItemListAdapter.iReturnToCageListClick {
    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private SqliteHelper db;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvItemList;
    private LinearLayoutManager layoutManager;
    private ReturnToCageItemListAdapter adapter;
    private ArrayList<ReceiptItemBean> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_to_cage_item_list);
        intializeXml();
        getRmReturnToCageItemListData();
    }

    private void getRmReturnToCageItemListData() {
        JSONObject param = new JSONObject();
        try {
            param.put(Constants.auth_Token, mUserPref.getToken());
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
            param.put(Constants.ORG_ID, mUserPref.getOrgId());

            if (mConnectionDetector.isConnectingToInternet()) {
                CallService.getInstance().getResponseUsingPOST(ReturnNotUsedItemListActivity.this, Constants.RM_RECEIPT_TRAN_NO_LIST, param.toString(), ReturnNotUsedItemListActivity.this, Constants.RM_RECEIPT_LIST_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(ReturnNotUsedItemListActivity.this,mUserPref.getBASEUrl()+ Constants.RM_RECEIPT_TRAN_NO_LIST, param.toString(), ReturnNotUsedItemListActivity.this, Constants.RM_RECEIPT_LIST_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(ReturnNotUsedItemListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvItemList = (RecyclerView) findViewById(R.id.rv_tran_list);
        toolbar.setTitle(R.string.return_not_used_item);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);

        db = new SqliteHelper(this);
        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        mItemList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ReturnNotUsedItemListActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.RM_RECEIPT_LIST_REQ_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        JSONArray array = result.getJSONArray("Data");
                        if (array != null && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                ReceiptItemBean bean = new ReceiptItemBean();
                                JSONObject obj = array.getJSONObject(i);
                                bean.setVcCompCode(obj.optString(ReqResParamKey.VC_COMP_CODE));
                                bean.setVcTranNo(obj.optString(ReqResParamKey.VC_TRAN_NO));
                                bean.setTranDate(obj.optString(ReqResParamKey.DT_TRAN_DATE));
                                mItemList.add(bean);
                            }
                            adapter = new ReturnToCageItemListAdapter(getApplicationContext(), mItemList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvItemList.setLayoutManager(layoutManager);
                            mRvItemList.setAdapter(adapter);
                        } else {
                            mItemList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");
                        }
                    } else {
                        confirmAlert("Alert", message);
                        mItemList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(ReturnNotUsedItemListActivity.this, "Alert", response.toString());
            }
        }
    }

    private void confirmAlert(String alert, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alert);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ReturnNotUsedItemListActivity.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onReturnToCageListItemClick(int position) {
        ReceiptItemBean bean = mItemList.get(position);
        Intent i = new Intent(ReturnNotUsedItemListActivity.this, ReceivingItemListActivity.class);
        i.putExtra(BundleKey.RM_RECEIPT_TRAN_NO, bean.getVcTranNo());
        i.putExtra(BundleKey.RM_RECEIPT_TRAN_DATE, bean.getTranDate());
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
