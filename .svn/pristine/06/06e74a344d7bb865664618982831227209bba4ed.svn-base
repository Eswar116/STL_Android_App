package com.essindia.stlapp.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Jonu.Kumar on 24-11-2017.
 */

public class DateUtil {

    public static String ddMMyyyyToddMMMyyyy(String date) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        String str = "";
        try {
            Date outputDate = inputFormat.parse(date);
            str = outputFormat.format(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String ddMMMyyyyToddMMyyyy(String date) {
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        String str = "";
        try {
            Date outputDate = inputFormat.parse(date);
            str = outputFormat.format(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
