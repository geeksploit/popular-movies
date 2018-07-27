package me.geeksploit.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.net.URL;
import java.util.Arrays;

import me.geeksploit.popularmovies.adapter.MovieGalleryAdapter;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View progressBar;
    private MovieGalleryAdapter movieGalleryAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        initializeViews();
        fetchMoviesData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.movies_progress);

        movieGalleryAdapter = new MovieGalleryAdapter(
                getApplicationContext(),
                new MovieGalleryAdapter.OnClickListener() {
                    @Override
                    public void onClick(MovieModel movie) {
                        Intent movieDetail = new Intent(MainActivity.this, DetailActivity.class);
                        movieDetail.putExtra(DetailActivity.EXTRA_MOVIE, movie);
                        startActivity(movieDetail);
                    }
                }
        );

        RecyclerView movieGallery = findViewById(R.id.movie_gallery);
        movieGallery.setAdapter(movieGalleryAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchSortMode();
                updateFab(fab);
            }
        });
        updateFab(fab);
    }

    private void updateFab(FloatingActionButton fab) {
        fab.setImageDrawable(PreferencesUtils.getSortModeIcon(this));
    }

    private void switchSortMode() {
        PreferencesUtils.switchSortMode(getApplicationContext());
        fetchMoviesData();
    }

    private void fetchMoviesData() {
        String sortMode = PreferencesUtils.getSortMode(getApplicationContext());
        if (sortMode.equals(getString(R.string.pref_sort_mode_value_popular))) {
            new FetchMoviesTask().execute(sortMode, PreferencesUtils.getApiKey(this));
        } else if (sortMode.equals(getString(R.string.pref_sort_mode_value_top_rated))) {
            new FetchMoviesTask().execute(sortMode, PreferencesUtils.getApiKey(this));
        } else {
            Snackbar.make(fab,
                    getString(R.string.error_not_implemented, PreferencesUtils.getSortModeLabel(this)),
                    Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showApiKeyDialog(MainActivity.this);
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showApiKeyDialog(Context context) {
        View view = getLayoutInflater().inflate(R.layout.dialog_api_key, null);
        final EditText apiKey = view.findViewById(R.id.api_key);
        apiKey.setText(PreferencesUtils.getApiKey(getApplicationContext()));
        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(R.string.dialog_api_key_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PreferencesUtils.setApiKey(
                                        getApplicationContext(),
                                        apiKey.getText().toString()
                                );
                                fetchMoviesData();
                            }
                        })
                .create()
                .show();
    }

    private void showAboutDialog(Context context) {
        new AlertDialog.Builder(context)
                .setView(getLayoutInflater().inflate(R.layout.dialog_about, null))
                .setTitle(getString(R.string.dialog_about_title, getString(R.string.app_name)))
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
    }

    private void showFetchError(View view) {
        Snackbar sb = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {
            sb.setText(R.string.error_wrong_api_key);
            sb.setAction(R.string.action_enter_api_key, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showApiKeyDialog(MainActivity.this);
                }
            });
        } else {
            sb.setText(R.string.error_no_internet_connection);
            sb.setAction(R.string.action_enable_wifi, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
        }
        sb.show();
    }

    private void showFetchSuccess(FloatingActionButton fab) {
        Snackbar.make(fab,
                getString(R.string.message_order_by, PreferencesUtils.getSortModeLabel(this)),
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void setStateFetchingMovies(boolean inProgress) {
        if (inProgress) {
            fab.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            fab.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_mode_key))) {
            updateFab(fab);
        }
        fetchMoviesData();
    }

    class FetchMoviesTask extends AsyncTask<String, Void, MovieModel[]> {

        @Override
        protected void onPreExecute() {
            setStateFetchingMovies(true);
        }

        @Override
        protected MovieModel[] doInBackground(String... params) {
            String sortMode = params[0];
            String apiKey = params[1];
            URL movieQueryUrl = NetworkUtils.buildUrl(sortMode, apiKey);
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieQueryUrl);
            return JsonUtils.parseTheMovieDb(jsonMovieResponse);
        }

        @Override
        protected void onPostExecute(MovieModel[] movies) {
            setStateFetchingMovies(false);

            if (movies == null) {
                showFetchError(fab);
            } else {
                showFetchSuccess(fab);
                movieGalleryAdapter.resetData(Arrays.asList(movies));
            }
        }
    }
}
