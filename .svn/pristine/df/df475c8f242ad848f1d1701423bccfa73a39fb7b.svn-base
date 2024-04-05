package com.essindia.stlapp.Activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.Singleton;

import java.util.Timer;
import java.util.TimerTask;

import amobile.android.barcodesdk.api.IWrapperCallBack;
import amobile.android.barcodesdk.api.Wrapper;


public class GRNVerfication extends AppCompatActivity implements View.OnClickListener, IWrapperCallBack {

    Button button;
    EditText editText;
    ImageView imageView;
    Wrapper m_wrapper;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grnverfication);
        m_wrapper = new Wrapper(this);
        intializeXml();
       /* Singleton singleton = Singleton.getInstance();
        singleton.getButtonObject(button, imageView);
        singleton.autoService(this);*/
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (m_wrapper != null) {
            if (m_wrapper.IsOpen()) {
                m_wrapper.Open();
                m_wrapper.SetDispathBarCode(false);
                m_wrapper.SetLightMode2D(Wrapper.LightMode2D.mix);
                m_wrapper.SetTimeOut(10);
            } else {
                m_wrapper = null;
                Toast.makeText(getApplicationContext(), "Device hardware not barcode enabled!", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }


//    private void autoService() {
//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    public void run() {
//                        try {
////                            Log.i("", "Counter:" + i++);
//                            System.out.println("System count:" + i++);
//                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
////                            button.startAnimation(animation);
////                            button.setBackgroundColor(getColor(R.color.colorPrimary));
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
//    }

    private void barcodeReader() {
        if (m_wrapper != null && m_wrapper.IsOpen()) {
            if (m_wrapper.Open()) {
                m_wrapper.SetDispathBarCode(false);
                m_wrapper.SetLightMode2D(Wrapper.LightMode2D.mix);
                m_wrapper.SetTimeOut(10);
                m_wrapper.Stop();
                m_wrapper.Scan();
            } else {
                m_wrapper = null;
                Toast.makeText(getApplicationContext(),
                        "Device hardware not barcode enabled!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void intializeXml() {
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        button.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                button.clearAnimation();
                button.setBackgroundColor(getColor(R.color.dashboard_color));
                break;
            case R.id.imageView:
                if (m_wrapper != null && m_wrapper.IsOpen()) {
                    m_wrapper.Stop();
                    m_wrapper.Scan();
                }
                break;
        }
    }

    @Override
    public void onDataReady(String strData) {
        byte[] bytes = strData.getBytes();
        editText.setText(strData);
    }

    @Override
    public void onServiceConnected() {
        Log.i("", "Service connected");

    }

    @Override
    public void onServiceDisConnected() {
        Log.i("", "Service Not connected");
    }
}
