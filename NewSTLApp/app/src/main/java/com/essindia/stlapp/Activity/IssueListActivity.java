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
import android.view.View;
import android.widget.TextView;

import com.essindia.stlapp.Adapter.IssueMrsNoListAdapter;
import com.essindia.stlapp.Bean.IssueItemDetailBean;
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

public class IssueListActivity extends AppCompatActivity implements OnResponseFetchListener, IssueMrsNoListAdapter.iMrsNoListClick {
    private Toolbar toolbar;
    private TextView mTvLabelMsg;
    private SqliteHelper db;
    private ConnectionDetector mConnectionDetector;
    private UserPref mUserPref;
    private RecyclerView mRvTranList;
    private LinearLayoutManager layoutManager;
    private IssueMrsNoListAdapter adapter;
    private ArrayList<IssueItemDetailBean> mTranNoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isssuance_list);
        intializeXml();
        getRmReceiptListData();
    }

    private void intializeXml() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvLabelMsg = (TextView) findViewById(R.id.tv_label_msg);
        mRvTranList = (RecyclerView) findViewById(R.id.rv_tran_list);
        toolbar.setTitle(R.string.rm_issuance);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = new SqliteHelper(this);
        mConnectionDetector = new ConnectionDetector(this);
        mUserPref = new UserPref(this);
        mTranNoList = new ArrayList<>();
    }

    private void getRmReceiptListData() {
        JSONObject param = new JSONObject();
        try {
            param.put(Constants.auth_Token, mUserPref.getToken());
            param.put(ReqResParamKey.VC_USER_CODE, mUserPref.getUserName());//Govind
            param.put(Constants.ORG_ID, mUserPref.getOrgId());

            if (mConnectionDetector.isConnectingToInternet()) {
//                CallService.getInstance().getResponseUsingPOST(IssueListActivity.this, Constants.RM_ISSUE_MRS_NO_LIST, param.toString(), IssueListActivity.this, Constants.RM_ISSUE_MRS_NO_LIST_REQ_ID, true);
                CallService.getInstance().getResponseUsingPOST(IssueListActivity.this, mUserPref.getBASEUrl()+Constants.RM_ISSUE_MRS_NO_LIST, param.toString(), IssueListActivity.this, Constants.RM_ISSUE_MRS_NO_LIST_REQ_ID, true);
            } else {
                AlertDialogManager.getInstance().simpleAlert(IssueListActivity.this, "Alert", "Please check your network connection");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(IssueListActivity.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == Constants.RM_ISSUE_MRS_NO_LIST_REQ_ID) {
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
                                IssueItemDetailBean bean = new IssueItemDetailBean();
                                JSONObject obj = array.getJSONObject(i);
                                bean.setVcDespNo(obj.optString(ReqResParamKey.VC_DESP_NO));
                                bean.setVcRouteNo(obj.optString(ReqResParamKey.VC_ROUTE_NO));
                                bean.setDtDespDate(obj.optString(ReqResParamKey.DT_DESP_DATE));

                                mTranNoList.add(bean);
                            }
                            adapter = new IssueMrsNoListAdapter(getApplicationContext(), mTranNoList, this);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRvTranList.setLayoutManager(layoutManager);
                            mRvTranList.setAdapter(adapter);
                        } else {
                            mTranNoList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mTvLabelMsg.setText("No pending transaction");
                        }
                    } else {
                        confirmAlert("Alert", message);
                        mTranNoList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        mTvLabelMsg.setText("No pending transaction");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(IssueListActivity.this, "Alert", response.toString());
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
                Intent i = new Intent(IssueListActivity.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onMrsNoListClick(int position) {
        IssueItemDetailBean bean = mTranNoList.get(position);
        Intent i = new Intent(IssueListActivity.this, IssueItemListActivity.class);
        i.putExtra(BundleKey.RM_ISSUE_MRS_DETAIL, bean);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
