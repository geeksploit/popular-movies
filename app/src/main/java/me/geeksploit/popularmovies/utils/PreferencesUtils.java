package me.geeksploit.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import java.util.Arrays;

import me.geeksploit.popularmovies.R;

public class PreferencesUtils {

    private static final String PREF_KEY_API = "api_key";
    private static final String PREF_VALUE_API_DEFAULT = "";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getApiKey(Context c) {
        return getPreferences(c).getString(PREF_KEY_API, PREF_VALUE_API_DEFAULT);
    }

    public static void setApiKey(Context c, String apiKey) {
        getPreferences(c).edit().putString(PREF_KEY_API, apiKey).apply();
    }

    public static String getSortMode(Context c) {
        String modeKey = c.getString(R.string.pref_sort_mode_key);
        String modeDefault = c.getString(R.string.pref_sort_mode_value_popular);
        return getPreferences(c).getString(modeKey, modeDefault);
    }

    private static void setSortMode(Context c, String sortMode) {
        String modeKey = c.getString(R.string.pref_sort_mode_key);
        getPreferences(c).edit().putString(modeKey, sortMode).apply();
    }

    public static void switchSortMode(Context c) {
        String[] sortModes = getSortModeValues(c);
        int nextSortMode = (getSortModeIndex(c) + 1) % sortModes.length;
        setSortMode(c, sortModes[nextSortMode]);
    }

    public static String getSortModeLabel(Context c) {
        return c.getResources().getStringArray(R.array.pref_sort_mode_labels)[getSortModeIndex(c)];
    }

    public static Drawable getSortModeIcon(Context c) {
        return c.getResources().obtainTypedArray(R.array.pref_sort_mode_icons).getDrawable(getSortModeIndex(c));
    }

    private static String[] getSortModeValues(Context c) {
        return c.getResources().getStringArray(R.array.pref_sort_mode_values);
    }

    private static int getSortModeIndex(Context c) {
        return Arrays.asList(getSortModeValues(c)).indexOf(getSortMode(c));
    }
}
