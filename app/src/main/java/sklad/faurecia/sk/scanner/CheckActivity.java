package sklad.faurecia.sk.scanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.decodemanager.DecodeManager;
import com.honeywell.decodemanager.barcode.DecodeResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import sklad.faurecia.sk.scanner.api.SavePartCall;
import sklad.faurecia.sk.scanner.listeners.OnTaskCompleteListener;
import sklad.faurecia.sk.scanner.utils.Constants;
import sklad.faurecia.sk.scanner.utils.DateFormatter;
import sklad.faurecia.sk.scanner.utils.Utils;

public class CheckActivity extends BaseActivity {

    private final int ID_SCANSETTING = 0x12;

    private DecodeManager mDecodeManager;
    private TextView mDecodeResultCompare;
    private TextView mDecodeResultCheck;
    private ImageView statusImage;
    private Button btnReset;

    private final int SCANKEY = 0x94;
    private final int SCANTIMEOUT = 5000;
    long mScanAccount = 0;
    private boolean mbKeyDown = true;

    private String compareCode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check);
        initializeUI();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                try {
                    if (mbKeyDown) {
                        DoScan();
                        mbKeyDown = false;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
            case KeyEvent.KEYCODE_UNKNOWN:
                if (event.getScanCode() == SCANKEY || event.getScanCode() == 87 || event.getScanCode() == 88) {
                    try {
                        if (mbKeyDown) {
                            DoScan();
                            mbKeyDown = false;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                try {
                    mbKeyDown = true;
                    cancleScan();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;

            case KeyEvent.KEYCODE_UNKNOWN:
                if (event.getScanCode() == SCANKEY || event.getScanCode() == 87 || event.getScanCode() == 88) {
                    try {
                        mbKeyDown = true;
                        cancleScan();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDecodeManager == null) {
            mDecodeManager = new DecodeManager(this, ScanResultHandler);
        }

        SoundManager.getInstance();
        SoundManager.initSounds(getBaseContext());
        SoundManager.loadSounds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ID_SCANSETTING, 0, R.string.SymbologySettingMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ID_SCANSETTING: {
                mDecodeManager.getSymConfigActivityOpeartor().start();
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDecodeManager != null) {
            try {
                mDecodeManager.release();
                mDecodeManager = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SoundManager.cleanup();
        if (mDecodeManager != null) {
            try {

                mDecodeManager.release();
                mDecodeManager = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void initializeUI() {
        mDecodeResultCompare = (TextView) findViewById(R.id.scan1);
        mDecodeResultCheck = (TextView) findViewById(R.id.scan2);
        statusImage = (ImageView) findViewById(R.id.img_status);
        btnReset = (Button) findViewById(R.id.btn_reset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareCode = null;
                mDecodeResultCompare.setText("Odskenuj prvú položku.");
                mDecodeResultCheck.setText("");
                statusImage.setImageDrawable(null);
            }
        });
    }

    private void DoScan() throws Exception {
        try {
            mDecodeManager.doDecode(SCANTIMEOUT);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DecodeManager.MESSAGE_DECODER_COMPLETE:
                    mScanAccount++;
                    DecodeResult decodeResult = (DecodeResult) msg.obj;

                    if(compareCode == null) {
                        compareCode = decodeResult.barcodeData;
                        mDecodeResultCompare.setText(compareCode);
                        mDecodeResultCheck.setText("Odskenuj druhú položku.");
                        SoundManager.playSound(1, 1);
                    } else {
                        mDecodeResultCheck.setText(decodeResult.barcodeData);
                        if(decodeResult.barcodeData.contains(compareCode)) {
                            SoundManager.playSound(1, 1);
                            statusImage.setImageResource(R.drawable.img_ok);
                        } else {
                            SoundManager.playSound(2, 1);
                            statusImage.setImageResource(R.drawable.img_error);
                        }
                    }
                    break;

                case DecodeManager.MESSAGE_DECODER_FAIL: {
                    SoundManager.playSound(2, 1);
                    if(compareCode == null) {
                        mDecodeResultCompare.setText(getString(R.string.title_scan_failed));
                    } else {
                        mDecodeResultCheck.setText(getString(R.string.title_scan_failed));
                    }
                }
                break;
                case DecodeManager.MESSAGE_DECODER_READY: {
                    ArrayList<Integer> arry = mDecodeManager.getSymConfigActivityOpeartor().getAllSymbologyId();
                    boolean b = arry.isEmpty();
                }
                break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private void cancleScan() throws Exception {
        mDecodeManager.cancelDecode();
    }

    private void compare() {

    }
}
