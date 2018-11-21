package com.dennisce.recorder.tools;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by dennis on 2018/11/21
 */
public class FormatDateTools {
    public static String formatDate(long time, String format) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);

    }
}
