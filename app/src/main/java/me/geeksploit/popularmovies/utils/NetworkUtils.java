package me.geeksploit.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public final class NetworkUtils {

    private static final String TMDB_POSTER_URL_BASE = "http://image.tmdb.org/t/p";
    private static final String TMDB_POSTER_URL_PART_LORES = "w185";
    private static final String TMDB_POSTER_URL_PART_HIRES = "w342";

    public static boolean haveNetworkConnection(Context context) {
        // TODO: is there a better way to check if the Internet connection is up and running?

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getType() == ConnectivityManager.TYPE_WIFI)
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private static Uri buildPosterUri(String posterPath, boolean hires) {
        return Uri.parse(TMDB_POSTER_URL_BASE).buildUpon()
                .appendEncodedPath(hires ? TMDB_POSTER_URL_PART_HIRES : TMDB_POSTER_URL_PART_LORES)
                .appendEncodedPath(posterPath)
                .build();
    }
}
