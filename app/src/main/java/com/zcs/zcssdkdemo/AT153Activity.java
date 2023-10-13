package com.zcs.zcssdkdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.card.ICCard;
import com.zcs.sdk.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AT153Activity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AT153Activity";
    protected TextView mTvLog;
    protected ScrollView mScrollView;
    protected Button mBtnRead;
    protected Button mBtnWrite;
    protected Button mBtnChangeKey;
    protected Button mBtnVerify;
    protected Button mBtnInit;

    private StringBuffer sbLog = new StringBuffer();
    private DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    private String key = "454545";
    private byte startAddr = 0;
    private byte readLen = 64;
    private byte[] readData;
    private ICCard icCard = DriverManager.getInstance().getCardReadManager().getICCard();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_at102);
        initView();
    }


    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.pref_at153);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mBtnInit = (Button) findViewById(R.id.btn_init);
        mBtnInit.setOnClickListener(AT153Activity.this);
        mBtnRead = (Button) findViewById(R.id.btn_read);
        mBtnRead.setOnClickListener(AT153Activity.this);
        mBtnWrite = (Button) findViewById(R.id.btn_write);
        mBtnWrite.setOnClickListener(AT153Activity.this);
        mBtnVerify = (Button) findViewById(R.id.btn_verify);
        mBtnVerify.setOnClickListener(AT153Activity.this);
        mBtnChangeKey = (Button) findViewById(R.id.btn_change_key);
        mBtnChangeKey.setOnClickListener(AT153Activity.this);
        mTvLog = (TextView) findViewById(R.id.tv_log);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    void init() {
        int ret = icCard.at153Init();
        showLog("AT153 init " + ret);
        if (ret != SdkResult.SDK_OK) {
            showLog(getString(R.string.init_failed));
        }
    }

    /**
     * read the card zone 1 data
     * 0x30 means read the first app zone
     */
    void read() {
        readData = new byte[readLen];
        int ret = icCard.at153ReadData((byte) 0x30, startAddr, readLen, readData);
        if (ret != SdkResult.SDK_OK) {
            readData = null;
        }
        showLog("AT153 read " + startAddr + "~" + (startAddr + readLen)
                + " ret = " + ret
                + (ret == SdkResult.SDK_OK ? ", data = " + StringUtils.convertBytesToHex(readData) : ""));
    }

    /**
     * write the origin card data to zone 1
     */
    void write() {
        if (readData == null || readData.length == 0) {
            showLog("No data to write. Click read first");
            return;
        }
        int ret = icCard.at153WriteData((byte) 0x30, startAddr, readLen, readData);
        showLog("AT153 write " + startAddr + "~" + (startAddr + readLen)
                + ", the data: " + StringUtils.convertBytesToHex(readData)
                + ", ret = " + ret
        );
    }

    /**
     * verify the key for zone 1
     */
    void verifyKey() {
        int ret = icCard.at153VerifyKey((byte) 0x30, StringUtils.convertHexToBytes(key));
        showLog("AT153 verifyKey '" + key + "' ret = " + ret);
    }

    /**
     * Must verify key before changing it.
     * keyType = 0x30 is changing zone 1 key
     */
    void changeKey() {
        int ret = icCard.at153VerifyKey((byte) 0x30, StringUtils.convertHexToBytes(key));
        if (ret != SdkResult.SDK_OK) {
            showLog("Verify key " + key + " error. Must verify key before changing it");
            return;
        }
        ret = icCard.at153ChangeKey((byte) 0x30, StringUtils.convertHexToBytes(key));
        showLog("AT153 change key to " + key + " ret = " + ret);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_init) {
            init();
        } else if (view.getId() == R.id.btn_read) {
            read();
        } else if (view.getId() == R.id.btn_write) {
            write();
        } else if (view.getId() == R.id.btn_verify) {
            verifyKey();
        } else if (view.getId() == R.id.btn_change_key) {
            changeKey();
        }
    }

    private void showLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, log);
                if (mTvLog.getLineCount() > 100) {
                    mTvLog.setText("");
                }
                Date date = new Date();
                sbLog.append(dateFormat.format(date)).append(":");
                sbLog.append(log);
                String text = mTvLog.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String[] str = text.split("\r\n");
                    for (int i = 0; i < str.length; i++) {
                        sbLog.append("\r\n");
                        sbLog.append(str[i]);
                    }
                }
                mTvLog.setText(sbLog.toString());
                sbLog.setLength(0);
            }
        });
    }
}
