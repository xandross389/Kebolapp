package com.inbitart.kebolapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XandrOSS on 30/08/2016.
 */
public class Utils {
    public static String formatDate(String date, String sourceDateFormat, String endDateFormat) throws java.text.ParseException {
        Date initDate = new SimpleDateFormat(sourceDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
                                // 2016-08-26 12:13:45.593
        return formatter.format(initDate);
    }
}
