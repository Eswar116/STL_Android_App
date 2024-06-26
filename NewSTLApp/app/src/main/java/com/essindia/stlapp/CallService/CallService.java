package com.essindia.stlapp.CallService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.essindia.stlapp.Activity.LoginActivity;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.Constants;
import com.essindia.stlapp.Utils.UserPref;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 23-06-2016.
 */
public class CallService {
    private Context context;
    private Activity activity;
    private String requestedURL, param;
    private int request_id;
    private boolean isShow;
    private OnResponseFetchListener listener;
    private String errorMessage;
    private ProgressBar progressBar;
    private UserPref userPref;
    private WeakReference<Activity> weakReference;
    private ProgressDialog progressDialog;


    private static CallService instance;

    public static CallService getInstance() {
        if (instance == null)
            instance = new CallService();
        return instance;
    }

    public void getResponseUsingPOST(Context context, String url, String param, OnResponseFetchListener listener, int request_id, boolean isShow) {
        this.context = context;
        this.requestedURL = url;
        this.param = param;
        this.request_id = request_id;
        this.isShow = isShow;
        this.listener = listener;
        userPref = new UserPref(context);

        Log.e("STL_LOG", "URL: " + url);
        Log.e("STL_LOG", "Param: " + param);
        new AsyncCallerPOST().execute();
    }

    public synchronized void getResponseUsingGET(Context context, String url, OnResponseFetchListener listener, int request_id, boolean isShow) {
        this.context = context;
        this.requestedURL = url;
        this.request_id = request_id;
        this.isShow = isShow;
        this.listener = listener;
        userPref = new UserPref(context);

        Log.e("URL", "URL: " + url);
        new AsyncCallerGET().execute();
    }

    private class AsyncCallerPOST extends AsyncTask<Void, ProgressDialog, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isShow) {
                activity = (Activity) context;
                weakReference = new WeakReference<Activity>(activity);
                // addProgressBar(activity);
                showProgressBar();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {

                StringBuilder sb = new StringBuilder();
                URL url = new URL(requestedURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);//
                urlConnection.setConnectTimeout(120000);
                urlConnection.setReadTimeout(120000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty(Constants.auth_Token, userPref.getToken());

                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(param);
                out.close();

                int HttpResult = urlConnection.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("STL_LOG", " Response: " + sb.toString());
                    return sb.toString();

                } else {
                    errorMessage = urlConnection.getResponseMessage();
                    System.out.println("ResponseMsg=" + urlConnection.getResponseMessage());
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null)
                listener.webserviceResponse(request_id, result);
            else {
                listener.webserviceResponse(0, null);
                if (isShow) {
                    if (weakReference.get() != null && !weakReference.get().isFinishing()) {
                        AlertDialogManager.getInstance().simpleAlert(context, "Error", errorMessage);
                    }
                }
            }
            hideProgressBar();
        }
    }

    private class AsyncCallerGET extends AsyncTask<Void, ProgressDialog, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isShow) {
                activity = (Activity) context;
                //addProgressBar(activity);
                showProgressBar();
                weakReference = new WeakReference<Activity>(activity);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {

                StringBuilder sb = new StringBuilder();
                URL url = new URL(requestedURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);//
                urlConnection.setConnectTimeout(120000);
                urlConnection.setReadTimeout(120000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty(Constants.auth_Token, userPref.getToken());

                urlConnection.connect();

                int HttpResult = urlConnection.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("STL_LOG", " Response: " + sb.toString());
                    return sb.toString();

                } else {
                    errorMessage = urlConnection.getResponseMessage();
                    System.out.println("ResponseMsg=" + urlConnection.getResponseMessage());
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null)
                listener.webserviceResponse(request_id, result);
            else {
                listener.webserviceResponse(0, null);
                if (isShow) {
                    if (weakReference.get() != null && !weakReference.get().isFinishing()) {
                        AlertDialogManager.getInstance().simpleAlert(context, "Error", errorMessage);
                    }
                }
            }
            hideProgressBar();
        }
    }

    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public void showProgressBar() {

        if (progressDialog == null)
            progressDialog = ProgressDialog.show(context, null, "Please wait...", true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}
