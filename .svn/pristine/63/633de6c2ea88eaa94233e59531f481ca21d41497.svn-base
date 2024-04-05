package com.essindia.stlapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.essindia.stlapp.Activity.GRNVerificationList;


/**
 * Created by Administrator on 27-10-2016.
 */

public class SyncService extends Service {
    private static final String TAG = "SyncService";

    private static final Object sSyncAdapterLock = new Object();
//    private static GRNVerificationList.SyncAdapter sSyncAdapter = null;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        synchronized (sSyncAdapterLock) {
//            if (sSyncAdapter == null) {
                GRNVerificationList l = new GRNVerificationList();
//                sSyncAdapter = l.new SyncAdapter(getApplicationContext(), true);
//            }
        }
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

