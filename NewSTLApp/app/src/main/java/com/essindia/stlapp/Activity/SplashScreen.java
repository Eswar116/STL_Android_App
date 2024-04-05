package com.essindia.stlapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.UserPref;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView logoIV, background;
    private TextView appNameTV;
    private Animation fade_in, zoom_in, zoom_out, sequential, moving;
    private UserPref userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userPref = new UserPref(this);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        sequential = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sequential);
        background = (ImageView) findViewById(R.id.background);
        logoIV = (ImageView) findViewById(R.id.logo);
        appNameTV = (TextView) findViewById(R.id.appnameTV);


        background.startAnimation(fade_in);
        logoIV.startAnimation(zoom_out);
        //appNameTV.startAnimation(fade_in);

        fade_in.setAnimationListener(this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent login = new Intent(SplashScreen.this, LoginActivity.class);
//                startActivity(login);
//                finish();
//            }
//        }, 3000);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (userPref.isAutoLogin()) {
            startActivity(new Intent(SplashScreen.this, Dashboard.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
