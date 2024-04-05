package com.essindia.stlapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 18-10-2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
//        String time = intent.getStringExtra("time");
//        String counter = intent.getStringExtra("counter");
//        Log.i("", "Timer:" + time);
        Log.i("", "Counter:" + i++);
        Log.i("", "Counter:");
    }
}
