package com.example.android.news;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find fragment in order to set the background color as it cannot be modified from xml
        View fragment = (View) findViewById(R.id.settings_fragment);
        int lightGray = Color.parseColor("#E0E0E0");
        fragment.getRootView().setBackgroundColor(lightGray);

    }

    public static class ArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Find Preference object in order to
            Preference topic = findPreference(getString(R.string.settings_topic_key));
            bindPreferenceSummaryToValue(topic);

            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferenceSummaryToValue(pageSize);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            Preference maxPages = findPreference(getString(R.string.settings_max_pages_key));
            bindPreferenceSummaryToValue(maxPages);
        }

        /**
         * Override the callback method from Preference.OnPreferenceChangeListener to change the
         * Preference
         * @param preference
         * @param value
         * @return
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // Check if Preference object is part of ListPreference (from settings_main.xml)
            if(preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    // Updated and saved the summary into SharedPreferences using label
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // If Preference object is not a part of ListPreference, update and saved
                // the preference summary into SharedPreferences by using key (by default)
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Helper method to call onPreferenceChange that makes the method to act as a
         * listener to Preferences
         * @param preference
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            // Retrieve string value that takes the key in Preference object that is used to
            // store values into SharedPreference
            String preferenceString = preferences.getString(preference.getKey(), "");
            // Call onPreferenceChange to change the value
            onPreferenceChange(preference, preferenceString);
        }
    }
}
