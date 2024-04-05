package com.essindia.stlapp.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.essindia.stlapp.Bean.GRN_Verification_Bean;
import com.essindia.stlapp.Bean.LoadingAdviceDataBean;
import com.essindia.stlapp.Bean.LoadingAdviceDetailBean;
import com.essindia.stlapp.Bean.LoadingAdviceInvoiceBean;
import com.essindia.stlapp.Bean.LoadingAdviceItemBean;
import com.essindia.stlapp.Bean.PickUpSummaryData;
import com.essindia.stlapp.Utils.AlertDialogManager;
import com.essindia.stlapp.Utils.UserPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_BOX_CATEGORY;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_BOX_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_CLD_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_CLOSE_FLAG;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_CUSTOMER_CODE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_CUSTOMER_NAME;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_DRIVER_NAME;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_HO_ORG_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_INVOICE_DATE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_INVOICE_NO;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_INVOICE_QTY;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_IR_NO;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_ITEM_CODE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_ITEM_DESC;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_NO_OF_BOXES;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_NU_PRIORITY;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_ORG_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_PACK_SIZE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_PEND_QTY;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_PRE_ADVICE_DATE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_PRE_ADVICE_NO;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_PROJ_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_RECEIVED_QTY;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_SLOC_ID;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_VC_USER_CODE;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.KEY_VEHICLE_NUMBER;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.TABLE_LOADING_ADVICE_LIST;
import static com.essindia.stlapp.Sqlite.SqliteConstantData.TABLE_LOADING_SCANNED_BOX;

/**
 * Created by Administrator on 08-11-2016.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "STL_Database";
    private ContentValues values;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SqliteConstantData.CREATE_GRN_VERIFICATION_LIST_TABLE);
        sqLiteDatabase.execSQL(SqliteConstantData.CREATE_GRN_VERIFICATION_LOG_TABLE);
        sqLiteDatabase.execSQL(SqliteConstantData.CREATE_PICK_UP_LIST_TABLE);
        sqLiteDatabase.execSQL(SqliteConstantData.CREATE_LOADING_ADVICE_LIST_TABLE);
        sqLiteDatabase.execSQL(SqliteConstantData.CREATE_LOADING_SCANNED_BOX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteConstantData.CREATE_GRN_VERIFICATION_LIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteConstantData.CREATE_GRN_VERIFICATION_LOG_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void insertLoadingAdviceList(LoadingAdviceDataBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Long> idsList = new ArrayList<>();
        db.delete(TABLE_LOADING_ADVICE_LIST, null, null);
        values = new ContentValues();
        values.put(KEY_VC_USER_CODE, bean.getVcUserCode());
        values.put(KEY_DRIVER_NAME, bean.getDriverName());
        values.put(KEY_PRE_ADVICE_DATE, bean.getPreAdviceDate());
        values.put(KEY_CLD_ID, bean.getCldId());
        values.put(KEY_PROJ_ID, bean.getProjId());
        values.put(KEY_VEHICLE_NUMBER, bean.getVehicleNumber());
        values.put(KEY_PRE_ADVICE_NO, bean.getPreAdviceNo());
        values.put(KEY_SLOC_ID, bean.getSlocId());
        values.put(KEY_HO_ORG_ID, bean.getHoOrgId());
        values.put(KEY_ORG_ID, bean.getOrgId());
        for (int i = 0; i < bean.getInvoiceList().size(); i++) {
            LoadingAdviceInvoiceBean invoiceBean = bean.getInvoiceList().get(i);
            values.put(KEY_CUSTOMER_NAME, invoiceBean.getCustomerName());
            values.put(KEY_CUSTOMER_CODE, invoiceBean.getCustomerCode());
            values.put(KEY_INVOICE_NO, invoiceBean.getInvoiceNo());
            values.put(KEY_INVOICE_DATE, invoiceBean.getInvoiceDate());

            for (int j = 0; j < invoiceBean.getLoadingAdviceItemlist().size(); j++) {
                LoadingAdviceItemBean itemBean = invoiceBean.getLoadingAdviceItemlist().get(j);
                values.put(KEY_ITEM_DESC, itemBean.getItemDesc());
                values.put(KEY_PEND_QTY, itemBean.getPendQty());
                values.put(KEY_INVOICE_QTY, itemBean.getInvoiceQty());
                values.put(KEY_ITEM_CODE, itemBean.getItemCode());
                values.put(KEY_NU_PRIORITY, itemBean.getNuPriority());
                values.put(KEY_RECEIVED_QTY, itemBean.getReceivedQty());
                values.put(KEY_CLOSE_FLAG, itemBean.getCloseFlag());
                values.put(KEY_PACK_SIZE, itemBean.getPackSize());
                long id = db.insert(TABLE_LOADING_ADVICE_LIST, null, values);
                idsList.add(id);
            }
        }

        db.close();
    }

    public boolean isLoadingBoxExistInDb(String boxId) {

        String query1 = "Select * from " + TABLE_LOADING_SCANNED_BOX + " where " + KEY_BOX_ID + " = '" + boxId + "'";
        Cursor cursor1 = cursorChecker(query1);
        if (cursor1.getCount() > 0) {
            System.out.println("isLoadingBoxExistInDb: already exist in db");
            return true;
        } else
            return false;
    }


   /* public ArrayList<PickUpSummaryData> checkIsLoadingListDataAvailableInDb(String companyCode, String invoiceNo, String invoiceDate, Context context) {

        ArrayList<LoadingAdviceDetailBean> list = new ArrayList<>();
        String query1 = "Select * from " + SqliteConstantData.TABLE_LOADING_ADVICE_LIST + " where " + SqliteConstantData.KEY_ORG_ID + " = '" + companyCode.toUpperCase() + "' COLLATE NOCASE ";
        Cursor cursor1 = cursorChecker(query1);
        if (cursor1.getCount() > 0) {
            String query2 = "Select * from " + SqliteConstantData.TABLE_LOADING_ADVICE_LIST + " where " + SqliteConstantData.KEY_INVOICE_NO + " = '" + invoiceNo.toUpperCase() + "' COLLATE NOCASE ";
            Cursor cursor2 = cursorChecker(query2);
            if (cursor2.getCount() > 0) {
                String query3 = "Select * from " + SqliteConstantData.TABLE_LOADING_ADVICE_LIST + " where " + SqliteConstantData.KEY_INVOICE_DATE + " = '" + invoiceDate.toUpperCase() + "' COLLATE NOCASE ";
                Cursor cursor3 = cursorChecker(query3);
                if (cursor3.getCount() > 0) {
                    String query4 = "Select * from " + SqliteConstantData.TABLE_LOADING_ADVICE_LIST + " where " + SqliteConstantData.KEY_CUSTOMER_CODE + " = '" + customerCode.toUpperCase() + "' AND " + SqliteConstantData.KEY_PICK_NO + " = '" + pickNo.toUpperCase() + "' AND " + SqliteConstantData.KEY_PICK_DATE + " = '" + pickDate.toUpperCase() + "'";
                    Cursor cursor4 = cursorChecker(query4);
                    if (cursor4.getCount() > 0) {
                        if (cursor4.moveToFirst()) {
                            do {
                                PickUpSummaryData bean = new PickUpSummaryData();


                                list.add(bean);
                            } while (cursor4.moveToNext());
                        }
                        cursor4.close();
                        return list;
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Pick Date");
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Pick No.");
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Customer Name");
        }
        return list;
    }*/

    public LoadingAdviceDetailBean getLoadingScannedBoxCompleteData(String orgId, String invoiceNo, String invoiceDate, String customerCode, String customerName, String itemCode, String packSize, String noOfBox, String boxId) {

        LoadingAdviceDetailBean bean;
        String selectQuery = "SELECT " +
                KEY_HO_ORG_ID + " ," +
                KEY_SLOC_ID + " ," +
                KEY_PROJ_ID + " ," +
                KEY_CLD_ID + " ," +
                KEY_PRE_ADVICE_NO + " ," +
                KEY_PRE_ADVICE_DATE + " ," +
                " FROM " +
                TABLE_LOADING_ADVICE_LIST +
                " where " +
                KEY_ORG_ID + " = '" + orgId + "'" +
                KEY_INVOICE_NO + " = '" + invoiceNo + "'" +
                KEY_INVOICE_DATE + " = '" + invoiceDate + "'" +
                KEY_CUSTOMER_CODE + " = '" + customerCode + "'" +
                KEY_CUSTOMER_NAME + " = '" + customerName + "'" +
                KEY_ITEM_CODE + " = '" + itemCode + "'" +
                KEY_PACK_SIZE + " = '" + packSize + "'" +
                KEY_NO_OF_BOXES + " = '" + noOfBox + "'" +
                KEY_BOX_ID + " = '" + boxId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            bean = new LoadingAdviceDetailBean();

            bean.setHoOrgId(cursor.getString(cursor.getColumnIndex(KEY_HO_ORG_ID)));
            bean.setSlocId(cursor.getString(cursor.getColumnIndex(KEY_SLOC_ID)));
            bean.setProjId(cursor.getString(cursor.getColumnIndex(KEY_PROJ_ID)));
            bean.setCldId(cursor.getString(cursor.getColumnIndex(KEY_CLD_ID)));
            bean.setPreAdviceNo(cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_NO)));
            bean.setPreAdviceDate(cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_DATE)));
            bean.setOrgId(orgId);
            bean.setInvoiceNo(invoiceNo);
            bean.setInvoiceDate(invoiceDate);
            bean.setCustomerCode(customerCode);
            bean.setCustomerName(customerName);
            bean.setItemCode(itemCode);
            bean.setPackSize(packSize);
            bean.setPackSize(noOfBox);
            bean.setPackSize(boxId);
            cursor.close();
            return bean;
        } else
            return null;
    }

    public boolean insertScannedLoadingBox(LoadingAdviceDetailBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();

        values = new ContentValues();

           /* values.put(KEY_HO_ORG_ID, "hoOrgId" + i);
            values.put(KEY_ORG_ID, "orgId" + i);
            values.put(KEY_SLOC_ID, "slocId" + i);
            values.put(KEY_PROJ_ID, "projId" + i);
            values.put(KEY_CLD_ID, "cldId" + i);
            values.put(KEY_DEVICE_ID, "deviceId" + i);
            values.put(KEY_INVOICE_NO, "invoiceNo" + i);
            values.put(KEY_INVOICE_DATE, "invoiceDate" + i);
            values.put(KEY_PRE_ADVICE_NO, "preAdviceNo" + i);
            values.put(KEY_PRE_ADVICE_DATE, "preAdviceDate" + i);
            values.put(KEY_CUSTOMER_CODE, "custCode" + i);
            values.put(KEY_CUSTOMER_NAME, "customerName" + i);
            values.put(KEY_ITEM_CODE, "itemCode" + i);
            values.put(KEY_PACK_SIZE, "packSize" + i);
            values.put(KEY_NO_OF_BOXES, "noOfBox" + i);
//            values.put(SqliteConstantData.KEY_BOX_CATEGORY, "category");
            values.put(KEY_BOX_ID, "boxId" + i);*/

        values.put(KEY_VC_USER_CODE, bean.getVcUserCode());
        values.put(KEY_DRIVER_NAME, bean.getDriverName());
        values.put(KEY_PRE_ADVICE_DATE, bean.getPreAdviceDate());
        values.put(KEY_CLD_ID, bean.getCldId());
        values.put(KEY_PROJ_ID, bean.getProjId());
        values.put(KEY_VEHICLE_NUMBER, bean.getVehicleNo());
        values.put(KEY_PRE_ADVICE_NO, bean.getPreAdviceNo());
        values.put(KEY_CUSTOMER_CODE, bean.getCustomerCode());
        values.put(KEY_CUSTOMER_NAME, bean.getCustomerName());
        values.put(KEY_INVOICE_NO, bean.getInvoiceNo());
        values.put(KEY_INVOICE_QTY, bean.getInvoiceQty());
        values.put(KEY_ITEM_CODE, bean.getItemCode());
        values.put(KEY_PACK_SIZE, bean.getPackSize());
        values.put(KEY_INVOICE_DATE, bean.getInvoiceDate());
        values.put(KEY_SLOC_ID, bean.getSlocId());
        values.put(KEY_HO_ORG_ID, bean.getHoOrgId());
        values.put(KEY_ORG_ID, bean.getOrgId());
//        values.put(KEY_NO_OF_BOXES, bean.getNoOfBoxes());
//        values.put(KEY_BOX_CATEGORY, loadingAdviceDetailBean.getOrgId() + "_" + loadingAdviceDetailBean.getCustomerCode() + "_" + loadingAdviceDetailBean.getInvoiceNo() + "_" + loadingAdviceDetailBean.getInvoiceDate() + "_" + loadingAdviceDetailBean.getItemCode() + "_" + loadingAdviceDetailBean.getPackSize());
        values.put(KEY_BOX_ID, bean.getBoxId());

        long rowInserted = db.insert(TABLE_LOADING_SCANNED_BOX, null, values);

        db.close();
        return (rowInserted == -1) ? false : true;
    }

    public int noOfUploadedScannedBoxAvailableInDb(String orgId, String customerCode, String invoiceNo, String invoiceDate, String itemCode, String packSize) {

        String query1 = "Select * from " + TABLE_LOADING_SCANNED_BOX + " where " + KEY_BOX_CATEGORY + " = '" + orgId + "_" + customerCode + "_" + invoiceNo + "_" + invoiceDate + "_" + itemCode + "_" + packSize + "'";
        Cursor cursor1 = cursorChecker(query1);
        cursor1.close();
        return cursor1.getCount();

    }

    public void clearAllBoxData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_LOADING_SCANNED_BOX, null, null);
    }

    public synchronized JSONArray getBoxDataJsonArray(Context context) {

        JSONArray data = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_LOADING_SCANNED_BOX;
        Cursor cursor = db.rawQuery(sql, null);
        UserPref userPref = new UserPref(context);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                JSONObject row = new JSONObject();
                try {

                    row.put(KEY_VC_USER_CODE, cursor.getString(cursor.getColumnIndex(KEY_VC_USER_CODE)));
                    row.put(KEY_DRIVER_NAME, cursor.getString(cursor.getColumnIndex(KEY_DRIVER_NAME)));
                    row.put(KEY_PRE_ADVICE_DATE, cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_DATE)));
                    row.put(KEY_CLD_ID, cursor.getString(cursor.getColumnIndex(KEY_CLD_ID)));
                    row.put(KEY_PROJ_ID, cursor.getString(cursor.getColumnIndex(KEY_PROJ_ID)));
                    row.put(KEY_VEHICLE_NUMBER, cursor.getString(cursor.getColumnIndex(KEY_VEHICLE_NUMBER)));
                    row.put(KEY_PRE_ADVICE_NO, cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_NO)));
                    row.put(KEY_CUSTOMER_CODE, cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
                    row.put(KEY_CUSTOMER_NAME, cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                    row.put(KEY_INVOICE_NO, cursor.getString(cursor.getColumnIndex(KEY_INVOICE_NO)));
                    row.put(KEY_INVOICE_QTY, cursor.getString(cursor.getColumnIndex(KEY_INVOICE_QTY)));
                    row.put(KEY_ITEM_CODE, cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                    row.put(KEY_PACK_SIZE, cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE)));
                    row.put(KEY_INVOICE_DATE, cursor.getString(cursor.getColumnIndex(KEY_INVOICE_DATE)));
                    row.put(KEY_SLOC_ID, cursor.getString(cursor.getColumnIndex(KEY_SLOC_ID)));
                    row.put(KEY_HO_ORG_ID, cursor.getString(cursor.getColumnIndex(KEY_HO_ORG_ID)));
                    row.put(KEY_ORG_ID, cursor.getString(cursor.getColumnIndex(KEY_ORG_ID)));
//                    row.put(KEY_DEVICE_ID, cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
//                    row.put(KEY_NO_OF_BOXES, cursor.getString(cursor.getColumnIndex(KEY_NO_OF_BOXES)));
                    row.put(KEY_BOX_ID, cursor.getString(cursor.getColumnIndex(KEY_BOX_ID)));
                    row.put(KEY_NO_OF_BOXES, "1");

                    data.put(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        //db.close();
        return data;
    }

    public ArrayList<LoadingAdviceDetailBean> getLoadingScannedBoxList() {
        ArrayList<LoadingAdviceDetailBean> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOADING_SCANNED_BOX;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LoadingAdviceDetailBean bean = new LoadingAdviceDetailBean();

                bean.setHoOrgId(cursor.getString(cursor.getColumnIndex(KEY_HO_ORG_ID)));
                bean.setOrgId(cursor.getString(cursor.getColumnIndex(KEY_ORG_ID)));
                bean.setSlocId(cursor.getString(cursor.getColumnIndex(KEY_SLOC_ID)));
                bean.setProjId(cursor.getString(cursor.getColumnIndex(KEY_PROJ_ID)));
                bean.setCldId(cursor.getString(cursor.getColumnIndex(KEY_CLD_ID)));
                bean.setInvoiceNo(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_NO)));
                bean.setInvoiceDate(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_DATE)));
                bean.setPreAdviceNo(cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_NO)));
                bean.setPreAdviceDate(cursor.getString(cursor.getColumnIndex(KEY_PRE_ADVICE_DATE)));
                bean.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
                bean.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                bean.setItemCode(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                bean.setPackSize(cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE)));
                bean.setPackSize(cursor.getString(cursor.getColumnIndex(KEY_NO_OF_BOXES)));
                bean.setPackSize(cursor.getString(cursor.getColumnIndex(KEY_BOX_ID)));

                list.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void addGRNVerificationList(ArrayList<GRN_Verification_Bean> beanList) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SqliteConstantData.TABLE_GRN_VERIFICATION_LIST, null, null);
        for (int i = 0; i < beanList.size(); i++) {
            values = new ContentValues();
            values.put(KEY_HO_ORG_ID, beanList.get(i).getHO_ORG_ID());
            values.put(KEY_PROJ_ID, beanList.get(i).getPROJ_ID());
            values.put(KEY_CLD_ID, beanList.get(i).getCLD_ID());
            values.put(SqliteConstantData.KEY_TRAN_NO, beanList.get(i).getTRAN_NO());
            values.put(KEY_PACK_SIZE, beanList.get(i).getPACK_SIZE());
            values.put(SqliteConstantData.KEY_PACK_TYPE, beanList.get(i).getPACK_TYPE());
            values.put(SqliteConstantData.KEY_PROCESSED_BOXES, beanList.get(i).getPROCESSED_BOXES());
            values.put(KEY_IR_NO, beanList.get(i).getIR_NO());
            values.put(SqliteConstantData.KEY_CLOSE_FLAG, beanList.get(i).getCLOSE_FLAG());
            values.put(SqliteConstantData.KEY_EXCESS_PACK_QTY, beanList.get(i).getEXCESS_PACK_QTY());
            values.put(KEY_NO_OF_BOXES, beanList.get(i).getNO_OF_BOXES());
            values.put(SqliteConstantData.KEY_ROUTE_DATE, beanList.get(i).getROUTE_DATE());
            values.put(SqliteConstantData.KEY_TRAN_date, beanList.get(i).getTRAN_date());
            values.put(KEY_SLOC_ID, beanList.get(i).getSLOC_ID());
            values.put(SqliteConstantData.KEY_DT_MOD_DATE, beanList.get(i).getDT_MOD_DATE());
            values.put(KEY_ITEM_CODE, beanList.get(i).getITEM_CODE());
            values.put(SqliteConstantData.KEY_ROUTE_NO, beanList.get(i).getROUTE_NO());
            values.put(SqliteConstantData.KEY_SCAN_FLAG, beanList.get(i).getSCAN_FLAG());
            values.put(KEY_ORG_ID, beanList.get(i).getORG_ID());
            values.put(SqliteConstantData.KEY_VERIFIED_QTY, beanList.get(i).getVERIFIED_QTY());
            values.put(SqliteConstantData.KEY_PEND_QTY, beanList.get(i).getPEND_QTY());
            values.put(SqliteConstantData.KEY_ITEM_DESC, beanList.get(i).getITEM_DESC());
            values.put(SqliteConstantData.KEY_EXCESS_FLAG, beanList.get(i).getEXCESS_FLAG());
            values.put(SqliteConstantData.KEY_VC_MACHINE_NO, beanList.get(i).getVC_MACHINE_NO());

            db.insert(SqliteConstantData.TABLE_GRN_VERIFICATION_LIST, null, values);
        }
        db.close();
    }


    public void addGRNVerificationLog(GRN_Verification_Bean bean) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SqliteConstantData.TABLE_GRN_VERIFICATION_LOG, null, null);
        values = new ContentValues();
        values.put(KEY_HO_ORG_ID, bean.getHO_ORG_ID());
        values.put(KEY_PROJ_ID, bean.getPROJ_ID());
        values.put(KEY_CLD_ID, bean.getCLD_ID());
        values.put(SqliteConstantData.KEY_TRAN_NO, bean.getTRAN_NO());
        values.put(KEY_PACK_SIZE, bean.getPACK_SIZE());
        values.put(SqliteConstantData.KEY_PACK_TYPE, bean.getPACK_TYPE());
        values.put(SqliteConstantData.KEY_PROCESSED_BOXES, bean.getPROCESSED_BOXES());
        values.put(KEY_IR_NO, bean.getIR_NO());
        values.put(SqliteConstantData.KEY_CLOSE_FLAG, bean.getCLOSE_FLAG());
        values.put(SqliteConstantData.KEY_EXCESS_PACK_QTY, bean.getEXCESS_PACK_QTY());
        values.put(KEY_NO_OF_BOXES, bean.getNO_OF_BOXES());
        values.put(SqliteConstantData.KEY_ROUTE_DATE, bean.getROUTE_DATE());
        values.put(SqliteConstantData.KEY_TRAN_date, bean.getTRAN_date());
        values.put(KEY_SLOC_ID, bean.getSLOC_ID());
        values.put(SqliteConstantData.KEY_DT_MOD_DATE, bean.getDT_MOD_DATE());
        values.put(KEY_ITEM_CODE, bean.getITEM_CODE());
        values.put(SqliteConstantData.KEY_ROUTE_NO, bean.getROUTE_NO());
        values.put(SqliteConstantData.KEY_SCAN_FLAG, bean.getSCAN_FLAG());
        values.put(KEY_ORG_ID, bean.getORG_ID());
        values.put(SqliteConstantData.KEY_EXCESS_FLAG, bean.getEXCESS_FLAG());

        db.insert(SqliteConstantData.TABLE_GRN_VERIFICATION_LOG, null, values);
        db.close();
    }

    public ArrayList<GRN_Verification_Bean> getGRNVerificationList() {
        ArrayList<GRN_Verification_Bean> list = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GRN_Verification_Bean bean = new GRN_Verification_Bean();

                bean.setHO_ORG_ID(cursor.getString(cursor.getColumnIndex(KEY_HO_ORG_ID)));
                bean.setPROJ_ID(cursor.getString(cursor.getColumnIndex(KEY_PROJ_ID)));
                bean.setCLD_ID(cursor.getString(cursor.getColumnIndex(KEY_CLD_ID)));
                bean.setTRAN_NO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_TRAN_NO)));
                bean.setPACK_SIZE(cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE)));
                bean.setPACK_TYPE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PACK_TYPE)));
                bean.setPROCESSED_BOXES(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PROCESSED_BOXES)));
                bean.setIR_NO(cursor.getString(cursor.getColumnIndex(KEY_IR_NO)));
                bean.setCLOSE_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_CLOSE_FLAG)));
                bean.setEXCESS_PACK_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_EXCESS_PACK_QTY)));
                bean.setNO_OF_BOXES(cursor.getString(cursor.getColumnIndex(KEY_NO_OF_BOXES)));
                bean.setROUTE_DATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ROUTE_DATE)));
                bean.setTRAN_date(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_TRAN_date)));
                bean.setSLOC_ID(cursor.getString(cursor.getColumnIndex(KEY_SLOC_ID)));
                bean.setDT_MOD_DATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_DT_MOD_DATE)));
                bean.setITEM_CODE(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                bean.setROUTE_NO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ROUTE_NO)));
                bean.setSCAN_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_SCAN_FLAG)));
                bean.setORG_ID(cursor.getString(cursor.getColumnIndex(KEY_ORG_ID)));
                bean.setVERIFIED_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_VERIFIED_QTY)));
                bean.setPEND_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PEND_QTY)));
                bean.setITEM_DESC(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ITEM_DESC)));

                bean.setEXCESS_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_EXCESS_FLAG)));
                bean.setVC_MACHINE_NO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_VC_MACHINE_NO)));
                // Adding contact to list
                list.add(bean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //      created by himanshu
//(orgId, Constants.QR_ROUTE_CARD_NO, Constants.QR_ROUTE_DATE, Constants.QR_STL_PART_NO, Constants.QR_ROUTE_CARD_NO, Constants.QR_BOX_TYPE, context);
    public GRN_Verification_Bean checkIsDataDuplicateeAvailableInDb(String orgId, String routNO/*, String routeDate*/, String itemCode, String referenceNo, String boxType, Context context) {
        GRN_Verification_Bean bean = new GRN_Verification_Bean();
        String query1 = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where " + SqliteConstantData.KEY_ORG_ID + " = '" + orgId + "' COLLATE NOCASE ";
        Cursor cursor1 = cursorChecker(query1);
        if (cursor1.getCount() > 0) {
            String query2 = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where " + SqliteConstantData.KEY_ROUTE_NO + " = '" + routNO.toUpperCase() + "' COLLATE NOCASE ";
            Cursor cursor2 = cursorChecker(query2);
            if (cursor2.getCount() > 0) {
               /* String query3 = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where " + SqliteConstantData.KEY_ROUTE_DATE + " = '" + routeDate.toUpperCase() + "' COLLATE NOCASE ";
                Cursor cursor3 = cursorChecker(query3);
                if (cursor3.getCount() > 0) {*/
                    String query4 = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where " + SqliteConstantData.KEY_ITEM_CODE + " = '" + itemCode.toUpperCase() + "' COLLATE NOCASE ";
                    Cursor cursor4 = cursorChecker(query4);
                    if (cursor4.getCount() > 0) {
                        String query5 = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where " + KEY_IR_NO + " = '" + referenceNo.toUpperCase() + "' COLLATE NOCASE ";
                        Cursor cursor5 = cursorChecker(query5);
                        if (cursor5.getCount() > 0) {
                            String finalQuery = "Select * from " + SqliteConstantData.TABLE_GRN_VERIFICATION_LIST + " where "
                                    + SqliteConstantData.KEY_ORG_ID + " = '" + orgId + "' COLLATE NOCASE "
                                    + " AND " + SqliteConstantData.KEY_ROUTE_NO + " = '" + routNO.toUpperCase() + "' COLLATE NOCASE "
                                    /*+ " AND " + SqliteConstantData.KEY_ROUTE_DATE + " = '" + routeDate.toUpperCase() + "' COLLATE NOCASE "*/
                                    + " AND " + KEY_ITEM_CODE + " = '" + itemCode.toUpperCase() + "' COLLATE NOCASE "
                                    + " AND " + KEY_IR_NO + " = '" + referenceNo.toUpperCase() + "' COLLATE NOCASE "
                                    + " AND " + SqliteConstantData.KEY_PACK_TYPE + " = '" + boxType.toUpperCase() + "' COLLATE NOCASE "
                                    + "";
                            Cursor cursor = cursorChecker(finalQuery);
                            if (cursor.getCount() > 0) {
                                bean.setHO_ORG_ID(cursor.getString(cursor.getColumnIndex(KEY_HO_ORG_ID)));
                                bean.setPROJ_ID(cursor.getString(cursor.getColumnIndex(KEY_PROJ_ID)));
                                bean.setCLD_ID(cursor.getString(cursor.getColumnIndex(KEY_CLD_ID)));
                                bean.setTRAN_NO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_TRAN_NO)));
                                bean.setPACK_SIZE(cursor.getString(cursor.getColumnIndex(KEY_PACK_SIZE)));
                                bean.setPACK_TYPE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PACK_TYPE)));
                                bean.setPROCESSED_BOXES(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PROCESSED_BOXES)));
                                bean.setIR_NO(cursor.getString(cursor.getColumnIndex(KEY_IR_NO)));
                                bean.setCLOSE_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_CLOSE_FLAG)));
                                bean.setEXCESS_PACK_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_EXCESS_PACK_QTY)));
                                bean.setNO_OF_BOXES(cursor.getString(cursor.getColumnIndex(KEY_NO_OF_BOXES)));
                                bean.setROUTE_DATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ROUTE_DATE)));
                                bean.setTRAN_date(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_TRAN_date)));
                                bean.setSLOC_ID(cursor.getString(cursor.getColumnIndex(KEY_SLOC_ID)));
                                bean.setDT_MOD_DATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_DT_MOD_DATE)));
                                bean.setITEM_CODE(cursor.getString(cursor.getColumnIndex(KEY_ITEM_CODE)));
                                bean.setROUTE_NO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ROUTE_NO)));
                                bean.setSCAN_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_SCAN_FLAG)));
                                bean.setORG_ID(cursor.getString(cursor.getColumnIndex(KEY_ORG_ID)));
                                bean.setVERIFIED_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_VERIFIED_QTY)));
                                bean.setPEND_QTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PEND_QTY)));
                                bean.setITEM_DESC(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_ITEM_DESC)));
                                bean.setEXCESS_FLAG(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_EXCESS_FLAG)));
                                cursor.close();
                                return bean;
                            } else {
                                AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Box Type");
                            }
                        } else {
                            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid IR No.");
                        }
                    } else {
                        AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid ItemCode");
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid GRN Date");
                }
            /*} else {
                AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid GRN Number");
            }*/
        } else {
            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Org Id");
        }
        return null;
    }


    public ArrayList<PickUpSummaryData> checkIsPickDataAvailableInDb(String orgId, String pickNo, String pickDate, Context context) {

        ArrayList<PickUpSummaryData> list = new ArrayList<>();
        String query1 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + KEY_ORG_ID + " = '" + orgId + "' COLLATE NOCASE ";
        Cursor cursor1 = cursorChecker(query1);
        if (cursor1.getCount() > 0) {
            String query2 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + SqliteConstantData.KEY_PICK_NO + " = '" + pickNo.toUpperCase() + "' COLLATE NOCASE ";
            Cursor cursor2 = cursorChecker(query2);
            if (cursor2.getCount() > 0) {
                String query3 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + SqliteConstantData.KEY_PICK_DATE + " = '" + pickDate.toUpperCase() + "' COLLATE NOCASE ";
                Cursor cursor3 = cursorChecker(query3);
                if (cursor3.getCount() > 0) {
                    String query4 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + KEY_ORG_ID + " = '" + orgId + "' AND " + SqliteConstantData.KEY_PICK_NO + " = '" + pickNo.toUpperCase() + "' AND " + SqliteConstantData.KEY_PICK_DATE + " = '" + pickDate/*.toUpperCase()*/ + "'";
                    Cursor cursor4 = cursorChecker(query4);
                    if (cursor4.getCount() > 0) {
                        if (cursor4.moveToFirst()) {
                            do {
                                PickUpSummaryData bean = new PickUpSummaryData();
                                bean.setCUSTOMERCODE(cursor4.getString(cursor4.getColumnIndex(KEY_CUSTOMER_CODE)));
                                bean.setCUSTOMERNAME(cursor4.getString(cursor4.getColumnIndex(KEY_CUSTOMER_NAME)));
                                bean.setREASON(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_REASON)));
                                bean.setPRODUCTNAME(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PRODUCT_NAME)));
                                bean.setPICKQTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_QTY)));
                                bean.setPRODUCTCODE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PRODUCT_CODE)));
                                bean.setCLOSEFLAG(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_CLOSE_FLAG)));
                                bean.setHOORGID(cursor4.getString(cursor4.getColumnIndex(KEY_HO_ORG_ID)));
                                bean.setPickBoxes(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_ACTUAL_PICK_QTY)));
                                bean.setORGID(cursor4.getString(cursor4.getColumnIndex(KEY_ORG_ID)));
                                bean.setPACKSIZE(cursor4.getString(cursor4.getColumnIndex(KEY_PACK_SIZE)));
                                bean.setVCUSERCODE(cursor4.getString(cursor4.getColumnIndex(KEY_VC_USER_CODE)));
                                bean.setCLDID(cursor4.getString(cursor4.getColumnIndex(KEY_CLD_ID)));
                                bean.setPICKNO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_NO)));
                                bean.setPROJID(cursor4.getString(cursor4.getColumnIndex(KEY_PROJ_ID)));
                                bean.setPICKDATE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_DATE)));
                                bean.setEXCESSLESSQTYFLAG(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_EXCESS_LESS_QTY_FLAG)));
                                bean.setSLOCID(cursor4.getString(cursor4.getColumnIndex(KEY_SLOC_ID)));

                                bean.setVC_BIN_NO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_BIN_NO)));
                                bean.setVC_BIN_DESC(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_BIN_DESC)));
                                bean.setVC_LOT_NO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_LOT_NO)));
                                bean.setBIN_QTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_BIN_QTY)));
                                bean.setPEND_QTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PEND_QTY)));
                                bean.setBOX_TYPE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_BOX_TYPE)));

                                list.add(bean);
                            } while (cursor4.moveToNext());
                        }
                        cursor4.close();
                        return list;
                    }
                    else{
                        AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Query Format");
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Pick Date");
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Pick No.");
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Organisation");
        }
        return list;
    }

    public PickUpSummaryData checkIsPickUpBoxDataAvailableInDb(String orgId, String boxType, String productCode, Context context) {

        PickUpSummaryData bean = new PickUpSummaryData();

        String query1 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + KEY_ORG_ID + " = '" + orgId + "' COLLATE NOCASE ";
        Cursor cursor1 = cursorChecker(query1);
        if (cursor1.getCount() > 0) {
            String query2 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + SqliteConstantData.KEY_BOX_TYPE + " = '" + boxType.toUpperCase() + "' COLLATE NOCASE ";
            Cursor cursor2 = cursorChecker(query2);
            if (cursor2.getCount() > 0) {
                String query3 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + SqliteConstantData.KEY_PRODUCT_CODE + " = '" + productCode.toUpperCase() + "' COLLATE NOCASE ";
                Cursor cursor3 = cursorChecker(query3);
                if (cursor3.getCount() > 0) {
                    String query4 = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where "
                            + KEY_ORG_ID + " = '" + orgId
                            + "' AND " + SqliteConstantData.KEY_BOX_TYPE + " = '" + boxType.toUpperCase()
                            + "' AND " + SqliteConstantData.KEY_PRODUCT_CODE + " = '" + productCode.toUpperCase()
                            + "'";
                    Cursor cursor4 = cursorChecker(query4);
                    if (cursor4.getCount() > 0) {
                        bean.setCUSTOMERCODE(cursor4.getString(cursor4.getColumnIndex(KEY_CUSTOMER_CODE)));
                        bean.setCUSTOMERNAME(cursor4.getString(cursor4.getColumnIndex(KEY_CUSTOMER_NAME)));
                        bean.setREASON(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_REASON)));
                        bean.setPRODUCTNAME(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PRODUCT_NAME)));
                        bean.setPICKQTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_QTY)));
                        bean.setPRODUCTCODE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PRODUCT_CODE)));
                        bean.setCLOSEFLAG(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_CLOSE_FLAG)));
                        bean.setHOORGID(cursor4.getString(cursor4.getColumnIndex(KEY_HO_ORG_ID)));
                        bean.setPickBoxes(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_ACTUAL_PICK_QTY)));
                        bean.setORGID(cursor4.getString(cursor4.getColumnIndex(KEY_ORG_ID)));
                        bean.setPACKSIZE(cursor4.getString(cursor4.getColumnIndex(KEY_PACK_SIZE)));
                        bean.setVCUSERCODE(cursor4.getString(cursor4.getColumnIndex(KEY_VC_USER_CODE)));
                        bean.setCLDID(cursor4.getString(cursor4.getColumnIndex(KEY_CLD_ID)));
                        bean.setPICKNO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_NO)));
                        bean.setPROJID(cursor4.getString(cursor4.getColumnIndex(KEY_PROJ_ID)));
                        bean.setPICKDATE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PICK_DATE)));
                        bean.setEXCESSLESSQTYFLAG(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_EXCESS_LESS_QTY_FLAG)));
                        bean.setSLOCID(cursor4.getString(cursor4.getColumnIndex(KEY_SLOC_ID)));
                        bean.setVC_BIN_NO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_BIN_NO)));
                        bean.setVC_BIN_DESC(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_BIN_DESC)));
                        bean.setVC_LOT_NO(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_VC_LOT_NO)));
                        bean.setBIN_QTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_BIN_QTY)));
                        bean.setPEND_QTY(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_PEND_QTY)));
                        bean.setBOX_TYPE(cursor4.getString(cursor4.getColumnIndex(SqliteConstantData.KEY_BOX_TYPE)));

                        cursor4.close();
                        return bean;
                    }
                } else {
                    AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Product Code");
                }
            } else {
                AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Box Type");
            }
        } else {
            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Org Id");
        }
        return null;
    }

    public ArrayList<PickUpSummaryData> checkIsPickNoAvailableInDb(String customerCode, Context context) {

        ArrayList<PickUpSummaryData> list = new ArrayList<>();

        String query = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + KEY_CUSTOMER_CODE + " = '" + customerCode.toUpperCase() + "' COLLATE NOCASE ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    PickUpSummaryData bean = new PickUpSummaryData();

                    bean.setPICKQTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PICK_QTY)));
                    bean.setPICKNO(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PICK_NO)));
                    bean.setPICKDATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PICK_DATE)));
                    list.add(bean);
                } while (cursor.moveToNext());
            }
        } else {
//            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Customer Code.");
        }
        cursor.close();
        return list;
    }

    public ArrayList<PickUpSummaryData> checkIsPickDateAvailableInDb(String pickNo, Context context) {

        ArrayList<PickUpSummaryData> list = new ArrayList<>();

        String query = "Select * from " + SqliteConstantData.TABLE_PICK_UP_LIST + " where " + SqliteConstantData.KEY_PICK_NO + " = '" + pickNo.toUpperCase() + "' COLLATE NOCASE ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    PickUpSummaryData bean = new PickUpSummaryData();

                    bean.setPICKQTY(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PICK_QTY)));
                    bean.setPICKDATE(cursor.getString(cursor.getColumnIndex(SqliteConstantData.KEY_PICK_DATE)));
                    list.add(bean);
                } while (cursor.moveToNext());
            }
        } else {
//            AlertDialogManager.getInstance().simpleAlert(context, "Alert", "Invalid Pick Date.");
        }
        cursor.close();
        return list;
    }

    private Cursor cursorChecker(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private Cursor cursorrChecker(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        //cursor.close();
        return cursor;
    }

    //
    public void addPickUpList(List<PickUpSummaryData> beanList) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SqliteConstantData.TABLE_PICK_UP_LIST, null, null);
        for (int i = 0; i < beanList.size(); i++) {
            values = new ContentValues();
            values.put(KEY_CUSTOMER_CODE, beanList.get(i).getCUSTOMERCODE());
            values.put(KEY_CUSTOMER_NAME, beanList.get(i).getCUSTOMERNAME());
            values.put(SqliteConstantData.KEY_REASON, beanList.get(i).getREASON());
            values.put(SqliteConstantData.KEY_PRODUCT_NAME, beanList.get(i).getPRODUCTNAME());
            values.put(SqliteConstantData.KEY_PICK_QTY, beanList.get(i).getPICKQTY());
            values.put(SqliteConstantData.KEY_PRODUCT_CODE, beanList.get(i).getPRODUCTCODE());
            values.put(SqliteConstantData.KEY_CLOSE_FLAG, beanList.get(i).getCLOSEFLAG());
            values.put(KEY_HO_ORG_ID, beanList.get(i).getHOORGID());
            values.put(SqliteConstantData.KEY_ACTUAL_PICK_QTY, beanList.get(i).getPickBoxes());
            values.put(KEY_ORG_ID, beanList.get(i).getORGID());
            values.put(KEY_PACK_SIZE, beanList.get(i).getPACKSIZE());
            values.put(KEY_VC_USER_CODE, beanList.get(i).getVCUSERCODE());
            values.put(KEY_CLD_ID, beanList.get(i).getCLDID());
            values.put(SqliteConstantData.KEY_PICK_NO, beanList.get(i).getPICKNO());
            values.put(KEY_PROJ_ID, beanList.get(i).getPROJID());
            values.put(SqliteConstantData.KEY_PICK_DATE, beanList.get(i).getPICKDATE());
            values.put(SqliteConstantData.KEY_EXCESS_LESS_QTY_FLAG, beanList.get(i).getEXCESSLESSQTYFLAG());
            values.put(KEY_SLOC_ID, beanList.get(i).getSLOCID());
            values.put(SqliteConstantData.KEY_VC_BIN_NO, beanList.get(i).getVC_BIN_NO());
            values.put(SqliteConstantData.KEY_VC_BIN_DESC, beanList.get(i).getVC_BIN_DESC());
            values.put(SqliteConstantData.KEY_VC_LOT_NO, beanList.get(i).getVC_LOT_NO());
            values.put(SqliteConstantData.KEY_PEND_QTY, beanList.get(i).getPEND_QTY());
            values.put(SqliteConstantData.KEY_BIN_QTY, beanList.get(i).getBIN_QTY());
            values.put(SqliteConstantData.KEY_BOX_TYPE, beanList.get(i).getBOX_TYPE());

            long id = db.insert(SqliteConstantData.TABLE_PICK_UP_LIST, null, values);
        }
        System.out.println("Status:" + "Successfully inserted");
        db.close();
    }
}
