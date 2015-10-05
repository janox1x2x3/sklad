package sklad.faurecia.sk.scanner.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.util.HashMap;

import sklad.faurecia.sk.scanner.BaseActivity;
import sklad.faurecia.sk.scanner.listeners.OnTaskCompleteListener;

public abstract class BaseCall extends AsyncTask<Void, Integer, Integer> {

    protected HashMap<String, Object> params;
    protected ProgressDialog progress;
    protected BaseActivity activity;
    protected Resources res;

    protected OnTaskCompleteListener mListener;

    public BaseCall(Activity activity) {
        // TODO Auto-generated constructor stub
        this.activity = (BaseActivity) activity;
        this.res = this.activity.getResources();

        initProgressBar();
    }

    public BaseCall(Activity activity, HashMap<String, Object> params) {
        // TODO Auto-generated constructor stub
        this.activity = (BaseActivity) activity;
        this.params = params;
        this.res = this.activity.getResources();

        initProgressBar();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        toggleProgress();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        // TODO Auto-generated method stub

        return executeCall();
    }

    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        toggleProgress();

        if (mListener != null) mListener.OnTaskComplete(result);
    }

    protected abstract Integer executeCall();

    private void initProgressBar() {

        progress = new ProgressDialog(this.activity);
        progress.setCancelable(false);
        setProgressDialog();
    }

    protected abstract void setProgressDialog();

    public void toggleProgress() {

        if (progress == null) return;

        if (!progress.isShowing()) {
            progress.show();
        } else {
            progress.cancel();
        }
    }

    public void setOnTaskCompleteListener(OnTaskCompleteListener listener) {
        this.mListener = listener;
    }
    }
