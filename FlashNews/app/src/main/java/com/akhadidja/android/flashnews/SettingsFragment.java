package com.akhadidja.android.flashnews;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    public final static String PREF_TEXT_SIZE_VALUE = "pref_text_size";
    private ListPreference mListPreference;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        String savedValue = FlashNewsApplication.readFromPreferences(getActivity(),
                PREF_TEXT_SIZE_VALUE, getString(R.string.pref_text_size_default_value));

        mListPreference = (ListPreference) findPreference(getString(R.string.text_size_pref_key));
        mListPreference.setValue(savedValue);
        int index = mListPreference.findIndexOfValue(savedValue);
        mListPreference.setSummary(getEntry(index));
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value = newValue.toString();
                int index = mListPreference.findIndexOfValue(value);
                String entry = getEntry(index);
                preference.setSummary(entry);
                FlashNewsApplication.saveToPreferences(getActivity(), PREF_TEXT_SIZE_VALUE, value);
                return true;
            }
        });
    }

    private String getEntry(int index) {
        if (index >= 0)
            return (String) mListPreference.getEntries()[index];
        else
            return (String) mListPreference.getEntry();
    }
}