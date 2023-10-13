package com.zcs.zcssdkdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.View;

public class SettingsFragment extends PreferenceFragment {

    private static final String INFO_KEY = "demo_info_key";
    private static final String PRINT_KEY = "demo_print_key";
    private static final String CARD_KEY = "demo_card_key";
    private static final String PINPAD_KEY = "demo_pinpad_key";
    private static final String OTHERS_KEY = "demo_others_key";

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        addPreferencesFromResource(R.xml.pref_demo_all);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        findPreference(INFO_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchFragment(SettingsFragment.this, new InfoFragment(), mActivity.getString(R.string.pref_device_info));
                return true;
            }
        });

        findPreference(PRINT_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchFragment(SettingsFragment.this, new PrintFragment(), mActivity.getString(R.string.pref_print));
                return true;
            }
        });

        findPreference(CARD_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchFragment(SettingsFragment.this, new CardFragment(), mActivity.getString(R.string.pref_card));
                return true;
            }
        });

        findPreference(PINPAD_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchFragment(SettingsFragment.this, new PinpadFragment(), mActivity.getString(R.string.pref_pinpad));
                return true;
            }
        });

        findPreference(OTHERS_KEY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchFragment(SettingsFragment.this, new OthersFragment(), mActivity.getString(R.string.pref_others));
                return true;
            }
        });
    }

    private void switchFragment(Fragment from, Fragment to, String title) {
        MainActivity mainactivity = (MainActivity) mActivity;
        ActionBar actionBar = mainactivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (!to.isAdded()) {
            getFragmentManager().beginTransaction().addToBackStack(null).hide(from).add(R.id.frame_container, to).commit();
        } else {
            getFragmentManager().beginTransaction().addToBackStack(null).hide(from).show(to).commit();
        }
    }
}
