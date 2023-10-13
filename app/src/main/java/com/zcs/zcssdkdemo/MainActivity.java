package com.zcs.zcssdkdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private DriverManager mDriverManager;
    private Sys mSys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.demo_title));
        }
        Fragment fragment = new SettingsFragment();
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();

        mDriverManager = DriverManager.getInstance();
        mSys = mDriverManager.getBaseSysDevice();
        initSdk();
    }
    
    private void initSdk() {
        int status = mSys.sdkInit();
        if(status != SdkResult.SDK_OK) {
            mSys.sysPowerOn();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        status = mSys.sdkInit();
        if(status != SdkResult.SDK_OK) {
            //init failed.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            if (actionBar != null) {
                actionBar.setTitle(getString(R.string.demo_title));
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
