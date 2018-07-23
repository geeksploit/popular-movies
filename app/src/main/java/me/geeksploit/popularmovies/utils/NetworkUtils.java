package me.geeksploit.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import me.geeksploit.popularmovies.R;

public final class NetworkUtils {

    private static final String TMDB_MOVIES_URL_BASE = "http://api.themoviedb.org/3/movie";

    private static final String TMDB_POSTER_URL_BASE = "http://image.tmdb.org/t/p";
    private static final String TMDB_POSTER_URL_PART_LORES = "w185";
    private static final String TMDB_POSTER_URL_PART_HIRES = "w342";

    private static final String URL_PART_REVIEWS = "reviews";

    private static final String PARAM_API_KEY = "api_key";

    public static URL buildUrl(String sortMode, String apiKey) {
        // TODO: should url building process rely on sortMode given as a parameter?
        Uri builtUri = Uri.parse(TMDB_MOVIES_URL_BASE).buildUpon()
                .appendPath(sortMode)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildUrlReviews(Integer movieId, String apiKey) throws MalformedURLException {
        return new URL(Uri.parse(TMDB_MOVIES_URL_BASE).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(URL_PART_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build()
                .toString()
        );
    }

    public static String getResponseFromHttpUrl(URL url) {
        if (url == null) return null;

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Scanner scanner = new Scanner(urlConnection.getInputStream());
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) return scanner.next();
            else return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

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

    public static void loadPoster(Context context, String path, ImageView thumbnail, boolean hires) {
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUri(path, hires))
                .placeholder(R.drawable.placeholder_loading) //
                .error(R.drawable.placeholder_noimage) //
                .into(thumbnail);
    }

    private static Uri buildPosterUri(String posterPath, boolean hires) {
        return Uri.parse(TMDB_POSTER_URL_BASE).buildUpon()
                .appendEncodedPath(hires ? TMDB_POSTER_URL_PART_HIRES : TMDB_POSTER_URL_PART_LORES)
                .appendEncodedPath(posterPath)
                .build();
    }
}
