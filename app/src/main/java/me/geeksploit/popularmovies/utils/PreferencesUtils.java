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

    public static String getApiKey(Context c) {
        return getPreferences(c).getString(PREF_KEY_API,PREF_VALUE_API_DEFAULT);
    }

    public static void setPrefApiKey(Context c, String apiKey) {
        getPreferences(c).edit().putString(PREF_KEY_API, apiKey).apply();
    }

    public static String getSortMode(Context c) {
        return getPreferences(c).getString(PREF_KEY_SORT_MODE, PREF_VALUE_SORT_DEFAULT);
    }

    private static void setSortMode(Context c, String sortMode) {
        getPreferences(c).edit().putString(PREF_KEY_SORT_MODE, sortMode).apply();
    }

}
