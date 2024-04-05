package com.essindia.stlapp.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.essindia.stlapp.CallService.OnResponseFetchListener;
import com.essindia.stlapp.R;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 20-10-2016.
 */

public class Singleton implements OnResponseFetchListener {
    int i = 0;
    private static Singleton singleton;
    private Context mContext;
    private ImageView imageView;
    private Button mBtnShake;
    private JSONObject loginparams;
    private UserPref userPref;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    public synchronized void autoService(final Context context) {
        mContext = context;
        final Handler handler = new Handler();
        Timer timer = new Timer();
        userPref = new UserPref(context);
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void run() {
                        try {
                            loginparams = new JSONObject();
                            loginparams.put(Constants.auth_Token, userPref.getToken());
//                            CallService.getInstance().getResponseUsingPOST(context, Constants.POST_SYNC_DATA, loginparams.toString(), Singleton.this, 2, false);
//                            mBtnShake.startAnimation(animated(context));
//                            imageView.startAnimation(animated(context));
//                            mBtnShake.setBackgroundColor(getColor(R.color.colorPrimary));
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000); //execute in every 50000 ms
    }

    public Animation animated(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.shake);
    }

    public void getButtonObject(Button buttonRef, ImageView imageView) {
        mBtnShake = buttonRef;
        this.imageView = imageView;
    }

    @Override
    public void webserviceResponse(int request_id, String response) {
        if (request_id == 2) {
            if (response != null) {
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    System.out.println("response:" + response);
                    String statusCode = result.getString("MessageCode");
                    String message = result.getString("Message");
                    if (statusCode.equalsIgnoreCase("0")) {
                        String totalCount = result.optString("TotalCount");
                        if (!totalCount.trim().isEmpty() && Integer.parseInt(totalCount.trim()) > 0) {
                            imageView.startAnimation(animated(mContext));
                        } else {
                            imageView.clearAnimation();
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
