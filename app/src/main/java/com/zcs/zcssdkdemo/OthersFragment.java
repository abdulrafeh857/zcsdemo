package com.zcs.zcssdkdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zcs.sdk.Beeper;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Led;
import com.zcs.sdk.LedLightModeEnum;
import com.zcs.sdk.Printer;
import com.zcs.sdk.Sys;

public class OthersFragment extends PreferenceFragment {

    private static final String KEY_LED = "others_led_key";
    private static final String KEY_BEEPER = "others_beeper_key";
    private static final String KEY_LCD = "others_lcd_key";
    private static final String KEY_CASHDRAWER = "others_cashdrawer_key";

    private DriverManager mDriverManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_others);
        mDriverManager = DriverManager.getInstance();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findPreference(KEY_LED).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final Led led = mDriverManager.getLedDriver();
                ListPreference listPreference = (ListPreference) preference;
                final int index = listPreference.findIndexOfValue((String) newValue);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        led.setLed(LedLightModeEnum.ALL, false);
                        if (index == 0) {
                            led.setLed(LedLightModeEnum.RED, true);
                        } else if (index == 1) {
                            led.setLed(LedLightModeEnum.GREEN, true);
                        } else if (index == 2) {
                            led.setLed(LedLightModeEnum.YELLOW, true);
                        } else if (index == 3) {
                            led.setLed(LedLightModeEnum.BLUE, true);
                        } else if (index == 4) {
                            led.setLed(LedLightModeEnum.ALL, true);
                        }
                    }
                }).start();
                return true;
            }
        });

        findPreference(KEY_BEEPER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final Beeper beeper = mDriverManager.getBeeper();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int ret = beeper.beep(4000, 600);
                    }
                }).start();
                return true;
            }
        });

        findPreference(KEY_LCD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //testLcdBitmap();
                testLcdString();
                return true;
            }
        });

        findPreference(KEY_CASHDRAWER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Printer printer = mDriverManager.getPrinter();
                printer.openBox();
                return true;
            }
        });
    }

    private void testLcdBitmap() {
        Sys sys = mDriverManager.getBaseSysDevice();
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.test_lcd);
        sys.showBitmapOnLcd(bitmap, true);
    }

    private void testLcdString() {
        Sys sys = mDriverManager.getBaseSysDevice();
        String toShow = "Welcome";
        sys.showStringOnLcd(3, 6, toShow, true);
    }
}
