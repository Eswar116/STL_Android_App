package com.essindia.stlapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.essindia.stlapp.Adapter.DashboardAdapter;
import com.essindia.stlapp.Bean.DashboardBean;
import com.essindia.stlapp.CallService.CallService;
import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.Singleton;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements OnResponseFetchListener {
    public GridView grid;
    public DashboardAdapter adapter;
    Toolbar toolbar;
    BroadcastReceiver receiver;
    Intent serviceIntent;
    ImageView locButton;
    ImageView mMenuRefresh;
    Singleton singleton;
    private ConnectionDetector connectionDetector;
    private MenuItem menuItem;
    private UserPref userPref;
    JSONObject loginparams;
    String errorMessage;
    ArrayList<DashboardBean> list;
    public Context mContext;
    String base_url;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = this;
        intializeXml();

        singleton = Singleton.getInstance();
        singleton.autoService(getApplicationContext());
        loginparams = new JSONObject();
        try {
            loginparams.put(Constants.USERNAME, userPref.getUserName());
            loginparams.put(Constants.ORG_ID, userPref.getOrgId());
            loginparams.put(Constants.auth_Token, userPref.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        grid = (GridView) findViewById(R.id.grid);
//        adapter = new DashboardAdapter(Dashboard.this, list);
//        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DashboardBean obj = (DashboardBean) grid.getAdapter().getItem(position);
                if (obj.getName().equalsIgnoreCase("GRN verification")) {
                    Intent grn = new Intent(Dashboard.this, GRNVerificationList.class);
                    startActivity(grn);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("Put Away")) {
                    Intent putAway = new Intent(Dashboard.this, PutAwayList.class);
                    startActivity(putAway);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("Pick Up")) {
                    userPref.setPickupQrCode("");
                    Intent pickUp = new Intent(Dashboard.this, NewPickupList.class);
                    startActivity(pickUp);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("Loading Advice")) {
                    Intent loadingAdvice = new Intent(Dashboard.this, LoadingListActivity.class);
                    startActivity(loadingAdvice);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("RM RECEIPT")) {
                    Intent rmReceivingListIntent = new Intent(Dashboard.this, ReceivingListActivity.class);
                    startActivity(rmReceivingListIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("RM ISSUE")) {
                    Intent rmIssueIntent = new Intent(Dashboard.this, IssueListActivity.class);
                    startActivity(rmIssueIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("RM LOADING")) {
                    Intent rmIssueIntent = new Intent(Dashboard.this, CoilIssueListActivity.class);
                    startActivity(rmIssueIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("RM UNLOADING")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, CoilReceiveListActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("LOADING FOR FORGING")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, LoadShopFloorListActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("UNLOADING AT FORGING")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, UnloadShopFloorCOILListActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("other process unloading")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, RouteCardProcessActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else if (obj.getName().equalsIgnoreCase("WH LOADING")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, WHLoadingInvoiceActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    //finish();
                } else if (obj.getName().equalsIgnoreCase("MRS LOADING")) {
                    Intent rmReceiveIntent = new Intent(Dashboard.this, LoadingAdviceMRSActivity.class);
                    startActivity(rmReceiveIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    //finish();
                } else if (obj.getName().equalsIgnoreCase("KANBANSCANNING")) {
                    startActivity(new Intent(Dashboard.this, KanbanStringActivity.class));
                    overridePendingTransition(R.anim.come_screen_from_right_side, R.anim.out_screen_in_left_side);

                }
                else if (obj.getName().equalsIgnoreCase("WH_SCANNING_INWARDS")) {
                    startActivity(new Intent(Dashboard.this, WhScanningInwardActivity.class));
                    overridePendingTransition(R.anim.come_screen_from_right_side, R.anim.out_screen_in_left_side);

                }

            }
        });
//        if (connectionDetector.isConnectingToInternet()) {
//            CallService.getInstance().getResponseUsingPOST(Dashboard.this, Constants.POST_SYNC_DATA, loginparams.toString(), Dashboard.this, Constants.DASHBOARD_SYNC_DATA_REQ_ID, true);
//        } else {
//            AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error", "Network connection error");
//        }
    }

    private void intializeXml() {
        list = new ArrayList<>();
        connectionDetector = new ConnectionDetector(this);
        userPref = new UserPref(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.e("TAG", "BASE URL CONNECTED SERVER : " + userPref.getBASEUrl());
        String baseurl = userPref.getBASEUrl();
        if (baseurl.equalsIgnoreCase("http://10.10.30.42:7101/mobapp_wms_l_kanban/")) {
            toolbar.setTitle("DashBoard " + "\n" + "10.10.30.42:7101");
        } else if (baseurl.equalsIgnoreCase("http://10.10.2.137:7003/mobapp_wms_l_kanban/")) {
            toolbar.setTitle("DashBoard " + "\n" + "10.10.2.137:7003");
        } else if (baseurl.equalsIgnoreCase("http://10.10.2.137:7004/mobapp_wms_l_kanban/")) {
            toolbar.setTitle("DashBoard " + "\n" + "10.10.2.137:7004");
        }
        else if (baseurl.equalsIgnoreCase("http://10.10.2.137:7009/mobapp_wms_l_kanban/")) {
            toolbar.setTitle("DashBoard " + "\n" + "10.10.2.137:7009");
        }
        else if(baseurl.equalsIgnoreCase("http://10.10.2.137:7010/mobapp_wms_l_kanban/")){
            toolbar.setTitle("DashBoard " + "\n" + "10.10.2.137:7010");
        }


        toolbar.setSubtitle("(" + userPref.getUserName().toUpperCase() + ")");

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.background));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.background));
        setSupportActionBar(toolbar);

        base_url = userPref.getBASEUrl();
        /*String data = userPref.getResponseData();
        try {
            JSONArray array = new JSONArray(data);
            System.out.println("data:" + data);
            for (int i = 0; i < array.length(); i++) {
                DashboardBean bean = new DashboardBean();
                JSONObject obj = array.getJSONObject(i);
                bean.setName(obj.optString("name"));
                bean.setCode(obj.optString("code"));
                bean.setCount(obj.optString("count"));
                list.add(bean);
//                            set.add(array.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

//        list = userPref.getItems();
//        Iterator<String> it = list.iterator();
//        while (it.hasNext()) {
//            System.out.println("saved shared prefernces value:" + it.next());
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        final MenuItem item = menu.findItem(R.id.menuRefresh);
        final MenuItem logout = menu.findItem(R.id.menulogout);
        item.setActionView(R.layout.imageview);
        mMenuRefresh = (ImageView) item.getActionView().findViewById(R.id.refreshButton);
        singleton.getButtonObject(null, mMenuRefresh);
//        singleton.autoService(this);
        Log.i("onCreateOptionsMenu ", "inside");
        if (connectionDetector.isConnectingToInternet()) {
//            CallService.getInstance().getResponseUsingPOST(Dashboard.this, Constants.POST_SYNC_DATA, loginparams.toString(), Dashboard.this, Constants.DASHBOARD_SYNC_DATA_REQ_ID, true);
            CallService.getInstance().getResponseUsingPOST(Dashboard.this, base_url + Constants.POST_SYNC_DATA, loginparams.toString(), Dashboard.this, Constants.DASHBOARD_SYNC_DATA_REQ_ID, true);
        } else {
            AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error", "Network connection error");
        }
        mMenuRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("refresh button ", "clicked");
                if (connectionDetector.isConnectingToInternet()) {
//                    CallService.getInstance().getResponseUsingPOST(Dashboard.this,  Constants.POST_SYNC_DATA, loginparams.toString(), Dashboard.this, Constants.DASHBOARD_SYNC_DATA_REQ_ID, true);
                    CallService.getInstance().getResponseUsingPOST(Dashboard.this, base_url + Constants.POST_SYNC_DATA, loginparams.toString(), Dashboard.this, Constants.DASHBOARD_SYNC_DATA_REQ_ID, true);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error", "Network connection error");
                }
            }
        });
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                System.out.println("Logout called");
                logout();
                return false;
            }
        });
        return true;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (connectionDetector.isConnectingToInternet()) {
                    userPref.clearPref();
                    userPref.setAutoLogin(false);
                    Intent i = new Intent(Dashboard.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
//                    CallService.getInstance().getResponseUsingPOST(Dashboard.this, Constants.LOGOUT_POST, loginparams.toString(), Dashboard.this, 1, true);
                    CallService.getInstance().getResponseUsingPOST(Dashboard.this, base_url + Constants.LOGOUT_POST, loginparams.toString(), Dashboard.this, 1, true);
                } else {
                    AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error", "Network connection error");
                }
//                SQLiteHelper db = new SQLiteHelper(MainScreenActivity.this);
//                db.deleteTablesData();
//                userPref.clearPref();
//                userPref.setAutoLogin(false);
//
//                Intent in = new Intent(MainScreenActivity.this, LoginActivity.class);
//                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(in);
//                finish();
            }
        });

        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == 1) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        userPref.clearPref();
                        userPref.setAutoLogin(false);
                        Intent i = new Intent(Dashboard.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    } else if (statusCode.equalsIgnoreCase("2")) {
                        AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error ", result.toString());
//                        userPref.clearPref();
//                        userPref.setAutoLogin(false);
//                        Intent i = new Intent(Dashboard.this, LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error  ", result.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent i = new Intent(Dashboard.this, LoginActivity.class);
                    AlertDialogManager.getInstance().simpleAlert(Dashboard.this, "Error ", e.toString());

                }
            }
        } else if (request_id == Constants.DASHBOARD_SYNC_DATA_REQ_ID) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    String statusCode = result.optString("MessageCode");
                    String message = result.optString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        mMenuRefresh.clearAnimation();
                        ArrayList<DashboardBean> syncList = new ArrayList<>();
                        JSONArray array = result.optJSONArray("DashBoard");
                        for (int i = 0; i < array.length(); i++) {
                            DashboardBean bean = new DashboardBean();
                            JSONObject obj = array.getJSONObject(i);
                            bean.setName(obj.optString("name"));
                            bean.setCode(obj.optString("code"));
                            bean.setCount(obj.optString("count"));
                            syncList.add(bean);
                        }
                        adapter = new DashboardAdapter(Dashboard.this, syncList);
                        grid.setAdapter(adapter);
                    } else {
                        Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                    }
                    String totalCount = result.optString("TotalCount");
                    /*if (Integer.parseInt(totalCount) > 0) {
                    } else {
                    }*/
                } catch (Exception e) {
                }
            }
        }
    }
}