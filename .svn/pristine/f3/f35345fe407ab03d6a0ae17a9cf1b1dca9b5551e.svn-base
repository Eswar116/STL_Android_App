package com.essindia.stlapp.Utils;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Administrator on 18-10-2016.
 */

public class UpdaterService extends Service {

    BroadcastReceiver broadcaster;
    Intent intent;
    static final public String BROADCAST_ACTION = "com.test.broadcast";
    Updater updater;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updater = new Updater();
        Log.d("acd", "Created");
        showMSg("Created");
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        if (!updater.isRunning()) {
            updater.start();
            Log.d("acd", "Started");
            showMSg("Started");
            updater.isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();

//        if (updater.isRunning) {
//            updater.interrupt();
//            Log.d("acd", "Destroyed");
//            showMSg("Destroyed");
//            updater.isRunning = false;
//            updater = null;
//        }
    }


    class Updater extends Thread {

        public boolean isRunning = false;
        public long DELAY = 30000;

        int i = 0;

        @Override
        public void run() {
            super.run();

            isRunning = true;
            while (isRunning) {
                Log.d("acd", "Running...");
                // sendbroadcast
                sendResult(i + "");
                i++;
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            } // while end
        } // run end

        public boolean isRunning() {
            return this.isRunning;
        }

    } // inner class end

    public void sendResult(String message) {
        intent.putExtra("counter", message);
        intent.putExtra("time", new Date().toLocaleString());
        sendBroadcast(intent);
    }

    public void showMSg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
