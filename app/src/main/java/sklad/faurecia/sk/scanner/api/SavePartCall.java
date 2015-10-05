package sklad.faurecia.sk.scanner.api;

import android.app.Activity;

import java.util.HashMap;

public class SavePartCall extends BaseCall {

    public static final String url = ApiUrl.PARTS + ApiUrl.DEPLETED;

    public SavePartCall(Activity activity, HashMap<String, Object> params) {
        super(activity, params);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Integer executeCall() {
        // TODO Auto-generated method stub

        HttpRetriever retr = new HttpRetriever(activity, HttpMethods.POST);
        retr.addCallParam(params);

        return retr.makeApiCall(activity, url);
    }

    @Override
    protected void setProgressDialog() {
        // TODO Auto-generated method stub
        progress.setMessage("Odosielam...");
    }
}
