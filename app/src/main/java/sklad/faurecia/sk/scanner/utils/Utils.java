package sklad.faurecia.sk.scanner.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

public class Utils {

    public static final float MILES_IN_METER = 0.000621371192f;
    public static final float METRES_IN_MILE = 1609.344000614692f;
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final String EMPTY_STRING = "";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    // ========================
    // Decimal formatting
    // ========================
    public final static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();

    static {
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
    }

    private static final DecimalFormat DECIMAL0 = new DecimalFormat("0", otherSymbols);
    private static final DecimalFormat DECIMAL1 = new DecimalFormat("0.0", otherSymbols);
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "Google Play Services";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static void appendLog(String TAG, String text) {

        if (text.length() > 4000) {
            Log.v(TAG, "sb.length = " + text.length());
            int chunkCount = text.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= text.length()) {
                    Log.i(TAG, "chunk " + i + " of " + chunkCount + ":" + text.substring(4000 * i));
                } else {
                    Log.i(TAG, "chunk " + i + " of " + chunkCount + ":" + text.substring(4000 * i, max));
                }
            }
        } else {
            Log.i(TAG, text);
        }

    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static String MD5Hash(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public static void hideKeyboard(Context context, View editText) {
        if (editText == null) return;

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }

    public static void hideKeyboard(Context context, View editText, View requestFocusView) {
        if (editText != null && requestFocusView != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            requestFocusView.requestFocus();
        }
    }

    public static void showKeyboard(Context context, View editText) {
        if (editText == null) return;

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        imm.restartInput(editText);
    }

    public static void showToast(Context context, int testRedId) {
        Toast.makeText(context, context.getString(testRedId), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text, boolean longer) {
        int duration = longer ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    public static void showToast(Context context, int textResId, boolean longer) {
        int duration = longer ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, context.getResources().getString(textResId), duration).show();
    }

    public static int getPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.density * dp + 0.5f);
    }

    public static int getSpPixels(Context context, int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context
                .getResources().getDisplayMetrics()) + 0.5f);
    }

    public static String formatDistance(double distance) {
        if (distance < 10) return DECIMAL1.format(distance);
        else return DECIMAL0.format(distance);
    }

    public static String getUniqueId(Activity activity) {
        final TelephonyManager tm = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32)
                | tmSerial.hashCode());

        return deviceUuid.toString().substring(9);
    }
}
