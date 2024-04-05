package com.essindia.stlapp.CallService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 02-11-2016.
 */

public class GetAllService extends IntentService implements OnResponseFetchListener {
    public GetAllService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        db = new SQLiteHelper(getApplicationContext());
        Log.e("Service", "Service created");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("Service", "Service started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service", "Service Destroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void webserviceResponse(int request_id, String response) {

    }
}
