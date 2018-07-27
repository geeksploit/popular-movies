package me.geeksploit.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.database.AppDatabase;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public final class MainViewModel extends AndroidViewModel {

    private final LiveData<List<MovieModel>> mMoviesFromLocalFavorites;
    private final MutableLiveData<List<MovieModel>> mMoviesFromNetworkApi;
    private final MutableLiveData<LiveData<List<MovieModel>>> mMovieSource;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(getApplication());
        mMoviesFromLocalFavorites = db.movieDao().loadAllMovies();
        mMoviesFromNetworkApi = new MutableLiveData<>();
        mMovieSource = new MutableLiveData<>();
        updateDataSource();
    }

    public MutableLiveData<LiveData<List<MovieModel>>> getMovieSource() {
        return mMovieSource;
    }

    public void updateDataSource() {
        Context c = getApplication();
        String sortMode = PreferencesUtils.getSortMode(c);
        if (c.getString(R.string.pref_sort_mode_value_popular).equals(sortMode)) {
            new FetchMoviesTask().execute(sortMode, PreferencesUtils.getApiKey(c));
            mMovieSource.setValue(mMoviesFromNetworkApi);
        } else if (c.getString(R.string.pref_sort_mode_value_top_rated).equals(sortMode)) {
            new FetchMoviesTask().execute(sortMode, PreferencesUtils.getApiKey(c));
            mMovieSource.setValue(mMoviesFromNetworkApi);
        } else if (c.getString(R.string.pref_sort_mode_value_favorite).equals(sortMode)) {
            mMovieSource.setValue(mMoviesFromLocalFavorites);
        }
    }

    class FetchMoviesTask extends AsyncTask<String, MovieModel, List<MovieModel>> {

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            String sortMode = params[0];
            String apiKey = params[1];

            List<MovieModel> models = null;
            try {
                URL queryUrl = NetworkUtils.buildUrl(sortMode, apiKey);
                String response = NetworkUtils.getResponseFromHttpUrl(queryUrl);
                JSONArray results = JsonUtils.getResults(response);
                models = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); i++) {
                    models.add(JsonUtils.parseMovie(results.getJSONObject(i)));
                    publishProgress(models.get(i));
                }
            } catch (JSONException | MalformedURLException | NullPointerException e) {
                e.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onPostExecute(List<MovieModel> movies) {
            mMoviesFromNetworkApi.setValue(movies);
        }
    }
}
