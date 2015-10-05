package sklad.faurecia.sk.scanner.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static String getString(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.getString(name);
        else return null;
    }

    public static String optString(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.optString(name);
        else return null;
    }

    public static Integer getInt(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.getInt(name);
        else return null;
    }

    public static Integer optInt(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.optInt(name);
        else return null;
    }

    public static Long getLong(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.getLong(name);
        else return null;
    }

    public static Long optLong(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.optLong(name);
        else return null;
    }

    public static Boolean getBoolean(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.getBoolean(name);
        else return null;
    }

    public static Boolean optBoolean(JSONObject jsonObject, String name) throws JSONException {
        if (!jsonObject.isNull(name)) return jsonObject.optBoolean(name);
        else return null;
    }
}
