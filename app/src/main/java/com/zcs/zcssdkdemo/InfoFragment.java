package com.zcs.zcssdkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;

import java.lang.ref.WeakReference;

public class InfoFragment extends PreferenceFragment {

    private static final String TAG = "InfoFragment";

    private static final String KEY_TERMINAL_SN = "terminal_sn_key";
    private static final String KEY_FIEMWARE_VERSION = "firmware_version_key";
    private static final String KEY_BASE_SDK_VERSION = "base_sdk_version_key";
    private static final String KEY_SDK_VERSION = "sdk_version_key";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_info);
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        final InfoHandler handler = new InfoHandler(this);
        final Sys sys = DriverManager.getInstance().getBaseSysDevice();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //get sn
                String[] sn = new String[1];
                int res = sys.getSN(sn);
                Log.i(TAG, "getSN res: " + res);
                if (res == SdkResult.SDK_OK) {
                    Log.i(TAG, "getSN: " + sn[0]);
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_SN;
                    msg.obj = sn[0];
                    handler.sendMessage(msg);
                }

                //get firmware version
                String[] firmwareVersion = new String[1];
                res = sys.getFirmwareVer(firmwareVersion);
                Log.i(TAG, "getFirmwareVer res: " + res);
                if (res == SdkResult.SDK_OK) {
                    Log.i(TAG, "getFirmwareVer: " + firmwareVersion[0]);
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_FIRMWARE_VERSION;
                    msg.obj = firmwareVersion[0];
                    handler.sendMessage(msg);
                }

                //get base sdk version
                String[] baseSdkVersion = new String[1];
                res = sys.getBaseSdkVer(baseSdkVersion);
                Log.i(TAG, "getBaseSdkVer res: " + res);
                if (res == SdkResult.SDK_OK) {
                    Log.i(TAG, "getBaseSdkVer: " + baseSdkVersion[0]);
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_BASE_SDK_VERSION;
                    msg.obj = baseSdkVersion[0];
                    handler.sendMessage(msg);
                }

                //get sdk version
                String sdkVersion = sys.getSdkVersion();
                Message msg = handler.obtainMessage();
                msg.what = MSG_SDK_VERSION;
                msg.obj = sdkVersion;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshSummary(@NonNull String key, String summary) {
        refreshSummary(findPreference(key), summary);
    }

    private void refreshSummary(Preference preference, String summary) {
        SpannableString spannableString = new SpannableString(summary);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, summary.length(), 0);
        preference.setSummary(spannableString);
    }

    private static final int MSG_SN = 1001;
    private static final int MSG_FIRMWARE_VERSION = 1002;
    private static final int MSG_BASE_SDK_VERSION = 1003;
    private static final int MSG_SDK_VERSION = 1004;

    private static class InfoHandler extends Handler {
        WeakReference<InfoFragment> mFragment;

        InfoHandler(InfoFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            InfoFragment infoFragment = mFragment.get();
            if (infoFragment == null || !infoFragment.isAdded())
                return;
            switch (msg.what) {
                case MSG_SN:
                    infoFragment.refreshSummary(KEY_TERMINAL_SN, (String)msg.obj);
                    break;
                case MSG_FIRMWARE_VERSION:
                    infoFragment.refreshSummary(KEY_FIEMWARE_VERSION, (String)msg.obj);
                    break;
                case MSG_BASE_SDK_VERSION:
                    infoFragment.refreshSummary(KEY_BASE_SDK_VERSION, (String)msg.obj);
                    break;
                case MSG_SDK_VERSION:
                    infoFragment.refreshSummary(KEY_SDK_VERSION, (String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    }
}
