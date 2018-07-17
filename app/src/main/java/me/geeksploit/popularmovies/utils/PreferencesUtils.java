package me.geeksploit.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private static final String PREF_KEY_API = "api_key";
    private static final String PREF_VALUE_API_DEFAULT = "";

    private static final String PREF_KEY_SORT_MODE = "sort_mode";
    private static final String PREF_VALUE_SORT_POPULAR = "popular";
    private static final String PREF_VALUE_SORT_TOP_RATED = "top_rated";
    private static final String PREF_VALUE_SORT_DEFAULT = PREF_VALUE_SORT_POPULAR;

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
