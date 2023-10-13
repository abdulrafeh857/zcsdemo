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

public class AT24CXXActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SLE4442Activity";
    protected TextView mTvLog;
    protected ScrollView mScrollView;
    protected Button mBtnRead;
    protected Button mBtnWrite;
    protected Button mBtnInit;

    private StringBuffer sbLog = new StringBuffer();
    private DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    private byte startAddr = 0;
    private byte readLen = 127;
    private byte[] readData;
    private ICCard icCard = DriverManager.getInstance().getCardReadManager().getICCard();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_at24);
        initView();
    }


    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.pref_at24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mBtnInit = (Button) findViewById(R.id.btn_init);
        mBtnInit.setOnClickListener(AT24CXXActivity.this);
        mBtnRead = (Button) findViewById(R.id.btn_read);
        mBtnRead.setOnClickListener(AT24CXXActivity.this);
        mBtnWrite = (Button) findViewById(R.id.btn_write);
        mBtnWrite.setOnClickListener(AT24CXXActivity.this);
        mTvLog = (TextView) findViewById(R.id.tv_log);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    void init() {
        int ret = icCard.at24cInit();
        showLog("AT24CXX init " + ret);
        if (ret != SdkResult.SDK_OK) {
            showLog(getString(R.string.init_failed));
        }
    }

    /**
     * Read AT24CXX card
     * Card type
     * 0X30: AT24C01
     * 0X31: AT24C02
     * 0X32: AT24C04
     * 0X33: AT24C08
     * 0X34: AT24C16
     * 0X35: AT24C32
     * 0X36: AT24C64
     */
    void read() {
        readData = new byte[readLen];
        int ret = icCard.at24cReadData((byte) 0x36, startAddr, readLen, readData);
        showLog("AT24C64 read" + startAddr + "~" + (startAddr + readLen)
                + " ret = " + ret
                + (ret == SdkResult.SDK_OK ? ", data = " + StringUtils.convertBytesToHex(readData) : ""));
    }

    /**
     * Write AT24CXX card
     * 0X30: AT24C01
     * 0X31: AT24C02
     * 0X32: AT24C04
     * 0X33: AT24C08
     * 0X34: AT24C16
     * 0X35: AT24C32
     * 0X36: AT24C64
     */
    void write() {
        if (readData == null || readData.length == 0) {
            showLog("No data to write. Click read first");
            return;
        }
        int ret = icCard.at24cWriteData((byte) 0x36, startAddr, readLen, readData);
        showLog("AT24C64 write " + startAddr + "~" + (startAddr + readLen)
                + ", the data: " + StringUtils.convertBytesToHex(readData)
                + ", ret = " + ret
        );
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_init) {
            init();
        } else if (view.getId() == R.id.btn_read) {
            read();
        } else if (view.getId() == R.id.btn_write) {
            write();
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
