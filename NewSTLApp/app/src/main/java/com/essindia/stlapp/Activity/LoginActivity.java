package com.essindia.stlapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.essindia.stlapp.Constant.ReqResParamKey;
import com.essindia.stlapp.R;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.ConnectionDetector;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Button loginBT, scanned;
    private TextInputLayout usernameET, passwordET;
    String username, password, deviceId, errorMessage;
    private JSONObject loginparams;
    EditText inputName, inputPassword;
    private CoordinatorLayout coordinatorLayout;
    private UserPref userPref;
    private Vibrator vib;
    Animation animShake;
    private ConnectionDetector connectionDetector;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intializeXml();
        runTimePermission();
    }

    private void runTimePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            loginBT.setEnabled(false);
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        } else {
            loginBT.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loginBT.setEnabled(true);
                } else {
                    loginBT.setEnabled(false);
                }
                return;
            }
        }
    }

    private void intializeXml() {
        userPref = new UserPref(this);
        connectionDetector = new ConnectionDetector(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        usernameET = (TextInputLayout) findViewById(R.id.usernameET);
        passwordET = (TextInputLayout) findViewById(R.id.passwordET);
        inputName = (EditText) findViewById(R.id.inputName);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        loginBT = (Button) findViewById(R.id.loginBT);
        scanned = findViewById(R.id.scanned);
        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_login);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mRadioGroup = findViewById(R.id.radioGrp);

//        inputName.addTextChangedListener(new MyTextWatcher(inputName));
//        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
// ANIL 29-06-2023
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet()) {
                    int selectedId = mRadioGroup.getCheckedRadioButtonId();
                    // Constants constants =new Constants();
                    if (selectedId == R.id.rb_live_server_137) {
                        //----------------Primary server(MUMBAI)-----------------
                        Log.d("LOGIN", "onClick: " + Constants.BASE_URL);
                        userPref.setBASEUrl("http://10.10.2.137:7003/mobapp_wms_l_kanban/");

                    } else if (selectedId == R.id.rb_live_server_secondry) {
//                      //----------------SECONDARY (HYDERABAD) server-----------------
                        Log.d("TAG", "LOGIN URL ELSE: " + Constants.BASE_URL);
                        userPref.setBASEUrl("http://10.10.2.137:7004/mobapp_wms_l_kanban/");
                    }
                    else if (selectedId == R.id.rb_live_testing_server) {
                        //----------------TEST server-----------------
                        Log.d("TAG", "LOGIN URL ELSE: " + Constants.BASE_URL);
                        userPref.setBASEUrl("http://10.10.2.137:7009/mobapp_wms_l_kanban/");
                    }
                    else if (selectedId == R.id.rb_live_server_demo) {
   //----------------DEMO server-----------------
                        Log.d("TAG", "LOGIN URL ELSE: " + Constants.BASE_URL);
                        userPref.setBASEUrl("http://10.10.30.42:7101/mobapp_wms_l_kanban/");
                    } else if (selectedId==R.id.rb_live_testing_server_hyd) {
                        Log.d("TAG", "LOGIN URL ELSE: " + Constants.BASE_URL);
                        userPref.setBASEUrl("http://10.10.2.137:7010/mobapp_wms_l_kanban/");


                    }
                    submitForm();
                } else {
                    AlertDialogManager.getInstance().simpleAlert(LoginActivity.this, "Error", "Network connection error");
                }
            }
        });
        scanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, KanbanStringActivity.class));
            }
        });
    }


    private void submitForm() {
        if (!validateName()) {
            inputName.setAnimation(animShake);
            inputName.startAnimation(animShake);
            vib.vibrate(120);
            return;
        } else if (!validatePassword()) {
            inputPassword.setAnimation(animShake);
            inputPassword.startAnimation(animShake);
            vib.vibrate(120);
            return;
        } else {
            usernameET.setErrorEnabled(false);
            passwordET.setErrorEnabled(false);
            getDeviceID();
            loginAttempt();
        }
    }

    private boolean validateName() {

        if (inputName.getText().toString().trim().isEmpty()) {
            usernameET.setErrorEnabled(true);
            usernameET.setError(getString(R.string.err_msg_name));
            inputName.setError(getString(R.string.err_msg_required));
//            requestFocus(inputName);
            return false;
        }
        usernameET.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            passwordET.setError(getString(R.string.err_msg_password));
            inputPassword.setError(getString(R.string.err_msg_required));
            requestFocus(inputPassword);
            return false;
        }
        passwordET.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @SuppressLint("MissingPermission")
    private void getDeviceID() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getDeviceId() != null) {
                deviceId = telephonyManager.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            userPref.setUserDeviceId(deviceId);
            System.out.println("device id:" + deviceId);
        } else {
            deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            userPref.setUserDeviceId(deviceId);
            System.out.println("device id:" + deviceId);
        }
    }

    private void loginAttempt() {
        loginBT.setText("Please wait...");
        username = inputName.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        try {
            loginparams = new JSONObject();
            loginparams.put("usrName", username);
            loginparams.put("password", password);
            loginparams.put("devId", deviceId);
//            loginparams.put("devId", "10C303D526BC379D");
//            loginparams.put("devId", "452c9cdc6907cc33"); // intermec white listed device id Jonu
//            loginparams.put("devId", "di12345678"); // intermec white listed device id Jonu
//            loginparams.put("devId", "8ead13f592c81259"); // intermec white listed device id test1
//            loginparams.put("devId", "452c9cdc6907cc33"); // intermec white listed device id test
            loginparams.put("devTyp", "micromax");

            Log.e("STL_LOG Param", loginparams.toString());
            new LoginAttempt().execute();
            //CallService.getInstance().getResponseUsingPOST(LoginActivity.this, Constants.LoginPost, jobj.toString(), LoginActivity.this, 1, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class LoginAttempt extends AsyncTask<Void, ProgressDialog, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, null, "Please wait...", true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                StringBuilder sb = new StringBuilder();
                Log.e("LOGIN", "LOGIN URL OF STL SERVER CONNECTED : "+userPref.getBASEUrl()+ Constants.LOGIN_POST);

//                URL url = new URL(Constants.BASE_URL + Constants.LOGIN_POST); //Working
                URL url = new URL(userPref.getBASEUrl()+ Constants.LOGIN_POST);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);//
                urlConnection.setConnectTimeout(60000);
                urlConnection.setReadTimeout(60000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty(Constants.TOKEN, userPref.getToken());

                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(loginparams.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("STL_LOG Response", sb.toString());
                    return sb.toString();

                } else {
                    errorMessage = urlConnection.getResponseMessage();
                }
            } catch (java.io.IOException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
            } catch (Exception e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject result = new JSONObject(response);
                    String statusCode = result.optString("MessageCode");
                    String message = result.optString("Message");
                    String authToken = result.optString("auth_Token");

                    if (statusCode.equalsIgnoreCase("0")) {                     //ie success
                        System.out.println("response:" + result);
                        userPref.setToken(authToken);
                        JSONArray array = result.getJSONArray("DashBoard");
                        String dashboardData = array.toString();
                        userPref.setResponseData(dashboardData);
                        userPref.setUserName(username);
                        userPref.setOrgId(result.getString(ReqResParamKey.ORG_ID));
                        userPref.setAutoLogin(true);
                        userPref.setIserId(result.getString("ORG_ID"));
                        Intent dashboard = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(dashboard);
                        finish();
                    } else {
                        loginBT.setText("Sign in");
                        AlertDialogManager.getInstance().simpleAlert(LoginActivity.this, "Alert", message);
                    }
                } catch (JSONException e) {
                    loginBT.setText("Sign in");
                    AlertDialogManager.getInstance().simpleAlert(LoginActivity.this, "Error", e.toString());
                }
            } else {
                loginBT.setText("Sign in");
                AlertDialogManager.getInstance().simpleAlert(LoginActivity.this, "Error", "It seems server is down, Please try after some time");
            }
            //CallService.getInstance().hideProgressBar();
            if (progressDialog != null) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
