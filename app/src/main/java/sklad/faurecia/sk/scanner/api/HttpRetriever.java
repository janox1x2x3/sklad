package sklad.faurecia.sk.scanner.api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import sklad.faurecia.sk.scanner.BaseActivity;
import sklad.faurecia.sk.scanner.R;
import sklad.faurecia.sk.scanner.utils.Constants;
import sklad.faurecia.sk.scanner.utils.Utils;

public class HttpRetriever {

    JSONArray errors = null;
    private String error;
    private Activity activity;
    /**
     * Default error runnable
     */
    private Runnable FailedMsg = new Runnable() {

        @Override
        public void run() {
            Utils.showToast(activity, error);
        }
    };
    private HttpMethods httpMethods;
    private int httpMethodType;

    /**
     * Default constructor
     *
     * @param context
     * @param httpMethodType
     */
    public HttpRetriever(Context context, int httpMethodType) {
        super();

        httpMethods = new HttpMethods(context);
        this.httpMethodType = httpMethodType;
    }

    /**
     * Make apiCall
     *
     * @param activity
     * @param callUri
     * @return null if request failed, or requestbody with jsonObject
     */
    public Integer makeApiCall(Activity activity, String callUri) {

        if (activity == null) return null;

        this.activity = activity;
        error = activity.getResources().getString(R.string.server_error_message);

        if (!this.hasConnection(activity)) {
            activity.runOnUiThread(FailedMsg);
            error = activity.getResources().getString(R.string.no_connection_error_message);
            Utils.appendLog(Constants.TAG_RETRIEVER, "No connection! Call aborted.");

            return null;
        }

        // Utils.appendLog(Constants.TAG_RETRIEVER,"Has connection, continue to post");
        HttpParams httpParameters = new BasicHttpParams();
        int toC = 10000;
        int toS = 10000;

        HttpConnectionParams.setConnectionTimeout(httpParameters, toC);
        HttpConnectionParams.setSoTimeout(httpParameters, toS);

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient(httpParameters);

        Integer response = null;

        try {

            switch (httpMethodType) {
                case HttpMethods.POST:
                    response = httpMethods.makePOSTrequest(httpclient, callUri);
                    break;

                case HttpMethods.GET:
                    response = httpMethods.makeGETrequest(httpclient, callUri);
                    break;

                default:
                    break;
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Utils.appendLog(Constants.TAG_RETRIEVER, "CPE");

            if (activity instanceof BaseActivity)
                if (!((BaseActivity) activity).isPaused()) activity
                        .runOnUiThread(FailedMsg);

            e.printStackTrace();
            return null;

        } catch (ConnectTimeoutException e) {
            // TODO Auto-generated catch block
            Utils.appendLog(Constants.TAG_RETRIEVER, "CTOE");

            if (activity instanceof BaseActivity)
                if (!((BaseActivity) activity).isPaused()) activity
                        .runOnUiThread(FailedMsg);

            e.printStackTrace();
            return null;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Utils.appendLog(Constants.TAG_RETRIEVER, "IOE");

            if (activity instanceof BaseActivity) {
                if (!((BaseActivity) activity).isPaused()) {
                    activity.runOnUiThread(FailedMsg);
                }
            }

            e.printStackTrace();
            return null;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utils.appendLog(Constants.TAG_RETRIEVER, "JSONE");

            if (activity instanceof BaseActivity)
                if (!((BaseActivity) activity).isPaused()) activity
                        .runOnUiThread(FailedMsg);

            return null;
        } catch (NullPointerException e) {
            Utils.appendLog(Constants.TAG_RETRIEVER, "NE");
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            Utils.appendLog(Constants.TAG_RETRIEVER, "IAE");
            e.printStackTrace();
            return null;
        }

        return response;
    }

    /**
     * @param ctx
     * @return true if connection is available
     */
    public static boolean hasConnection(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) return true;

        return false;
    }

    public void addCallParam(String paramName, String value) {
        httpMethods.addCallParam(paramName, value);
    }

    public void addCallParam(HashMap<String, Object> params) {
        if (params != null) httpMethods.addCallParam(params);
    }

    public void clearCallParams() {
        httpMethods.clearCallParams();
    }
}
