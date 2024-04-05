package com.essindia.stlapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import com.essindia.stlapp.R;
import com.essindia.stlapp.listeners.DialogListener;

/**
 * Created by Administrator on 23-06-2016.
 */
public class AlertDialogManager {
    private static AlertDialogManager instance;
    private static int DIALOG_TEXT_SIZE = 19;
    private static int DIALOG_BUTTON_SIZE = 20;
    public static AlertDialogManager getInstance() {
        if (instance == null) instance = new AlertDialogManager();
        return instance;
    }

    public void simpleAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.create();
        if (!((Activity) context).isFinishing()) builder.show();
    }

    public static void showYesNoAlert(final Context activity, String userMessage, final DialogListener listener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(userMessage);
            builder.setCancelable(false);

            builder.setPositiveButton(activity.getString(R.string.Yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    if (listener != null) listener.onBtnClick();
                }
            });

            builder.setNegativeButton(activity.getString(R.string.No), null);

            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            TextView textView = (TextView) alert.findViewById(android.R.id.message);
            textView.setTextSize(DIALOG_TEXT_SIZE);
            Button btn1 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            Button btn2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            btn1.setTextSize(DIALOG_BUTTON_SIZE);
            btn2.setTextSize(DIALOG_BUTTON_SIZE);
        } catch (Exception e) {
            // some time windowmanager$badtokenexception throw if activity argument is incorrect.
            e.printStackTrace();
        }
    }

    public static void showDialogSingleBtnWithNoCallBack(final Context activity, String btnName, String userMessage, boolean isError) {
        AlertDialog.Builder builder;
        try {
            if (isError) {
                builder = new AlertDialog.Builder(activity, R.style.AppTheme);
            } else {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setMessage(userMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(btnName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            TextView textView = (TextView) alert.findViewById(android.R.id.message);
            textView.setTextSize(DIALOG_TEXT_SIZE);
            Button btn1 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            btn1.setTextSize(DIALOG_BUTTON_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
