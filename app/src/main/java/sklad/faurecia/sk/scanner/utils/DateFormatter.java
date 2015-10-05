package sklad.faurecia.sk.scanner.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    // ========================
    // Date formatting
    // ========================

    public static String getTimeInISOFormat() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        return sdf.format(new Date());
    }

    public static String formatToISO(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        return sdf.format(date);
    }

    @SuppressLint("DefaultLocale")
    public static String formatToDateNotification(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd", Locale.US);
        String s = format.format(date);

        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String formatToDateTitleShort(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String s = format.format(date).toUpperCase();

        return s;
    }


    @SuppressLint("DefaultLocale")
    public static String formatToDatePickerTitle(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US);
        String s = format.format(date);

        return s;
    }


    @SuppressLint("DefaultLocale")
    public static String formatToDateWithTime(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy, h:mm a", Locale.US);
        String s = format.format(date);

        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String formatToTimelineDate(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, h:mm a", Locale.US);
        String s = format.format(date);

        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String formatToDateTitleLong(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String s = format.format(date);

        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String formatToDateTitleHistory(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy", Locale.US);
        String s = format.format(date).toUpperCase();

        return s;
    }

    public static String formatToTime(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
        String s = format.format(date).toLowerCase();

        return s;
    }
}
