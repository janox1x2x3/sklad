package sklad.faurecia.sk.scanner;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sklad.faurecia.sk.scanner.api.BaseCall;

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseCall apiCall;
    protected Resources res;
    protected boolean justCreated = true;

    protected Integer navigationIcon;

    private boolean isPaused = false;

    public boolean isPaused() {
        return isPaused;
    }

    public Integer getNavigationIcon() {
        return navigationIcon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        justCreated = true;
        isPaused = false;

        res = getResources();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        isPaused = true;

        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        isPaused = false;

        super.onResume();

        if (justCreated) {
            justCreated = false;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (apiCall != null) {
            apiCall.cancel(true);
        }
    }
}
