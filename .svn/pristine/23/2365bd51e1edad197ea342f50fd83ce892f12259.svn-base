package com.essindia.stlapp.Utils;

import android.content.Context;

import com.essindia.stlapp.R;
public class ValidationChecker {

    public static String getBoxType(Context pContxt, String actualQty, String stdQty) {
        int boxActualQty = Integer.parseInt(actualQty);
        int boxStdQty = Integer.parseInt(stdQty);
        if (boxActualQty >= boxStdQty) return pContxt.getString(R.string.std);
        else return pContxt.getString(R.string.nonstd);
    }
}
