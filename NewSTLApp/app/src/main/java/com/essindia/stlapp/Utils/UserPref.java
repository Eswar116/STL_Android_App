package com.essindia.stlapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.essindia.stlapp.Constant.ReqResParamKey;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 27-10-2016.
 */

public class UserPref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public UserPref(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isAutoLogin() {
        return sharedPreferences.getBoolean(Constants.AUTO_LOGIN, false);
    }

    public String getToken() {
        return sharedPreferences.getString(Constants.auth_Token, "");
    }

    public void setToken(String token) {
        editor.putString(Constants.auth_Token, token).commit();
    }

    public void setActualQty(String actualQty) {
        editor.putString(Constants.ACTUAL_QTY, actualQty).commit();
    }

    public String getActualQty() {
        return sharedPreferences.getString(Constants.ACTUAL_QTY, "");
    }

    public void setAutoLogin(boolean autoLogin) {
        editor.putBoolean(Constants.AUTO_LOGIN, autoLogin).commit();
    }

    public String getPickupQrcode() {
        return sharedPreferences.getString(Constants.PICK_QR_CODE, "");
    }

    public void setPickupQrCode(String pickupQrCode) {
        editor.putString(Constants.PICK_QR_CODE, pickupQrCode).commit();
    }

//    public void setItems(HashSet<DashboardBean> item) {
//        editor.putStringSet(Constants.KEY_ITEM, item.toString()).commit();
//    }

    public void setUserDeviceId(String deviceId) {
        editor.putString(Constants.USER_DEVICE_ID, deviceId).commit();
    }

    public String getUserDeviceId() {
        return sharedPreferences.getString(Constants.USER_DEVICE_ID, "");
    }

    public void setSuggestedArea(String suggestedArea) {
        editor.putString(Constants.SUGGESTED_AREA, suggestedArea).commit();
    }

    public String getSuggestedArea() {
        return sharedPreferences.getString(Constants.SUGGESTED_AREA, "");
    }


    public void setOrgId(String orgId) {
        editor.putString(ReqResParamKey.ORG_ID, orgId).commit();
    }

    public String getOrgId() {
        return sharedPreferences.getString(ReqResParamKey.ORG_ID, "");
    }

    public void setIserId(String userId) {
        editor.putString(ReqResParamKey.USER_ID, userId).commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(ReqResParamKey.USER_ID, "");
    }




    public String getResponseData() {
        return sharedPreferences.getString(Constants.RESPONSE_DATA, "");
    }

    public void setResponseData(String responseData) {
        editor.putString(Constants.RESPONSE_DATA, responseData).commit();
    }

    public void setUserName(String userName) {
        editor.putString(Constants.USERNAME, userName).commit();
    }

    public String getUserName() {

        return sharedPreferences.getString(Constants.USERNAME, "");
    }

    public void setBASEUrl(String baseUrl) {
        editor.putString(Constants.BASE_URL_PREF, baseUrl).commit();
    }

    public String getBASEUrl() {
        return sharedPreferences.getString(Constants.BASE_URL_PREF, "");
    }


    public void setInvoiceQRString(String STR1, String str1) {
        editor.putString(STR1, str1).commit();
    }

    public String getInvoiceQRString() {

        return sharedPreferences.getString(Constants.STR1, "");
    }


    public void setKanbanQRString(String STR2, String str2) {
        editor.putString(STR2, str2).commit();
    }

    public String getKanbanQRString() {
        return sharedPreferences.getString(Constants.STR2, "");
    }

    public Set<String> getItems() {
        return sharedPreferences.getStringSet(Constants.KEY_ITEM, new HashSet<String>());
    }

    public void clearPref() {
        sharedPreferences.edit().clear().commit();
    }


}
