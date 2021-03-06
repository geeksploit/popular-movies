package me.geeksploit.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import me.geeksploit.popularmovies.adapter.MovieGalleryAdapter;
import me.geeksploit.popularmovies.model.MainViewModel;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View progressBar;
    private MovieGalleryAdapter movieGalleryAdapter;
    private LiveData<List<MovieModel>> movieSource;
    private FloatingActionButton fab;
    private MainViewModel model;
    private boolean haveNetworkConnection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        initializeViews();
        setupViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!haveNetworkConnection) model.updateDataSource();
    }

    private void setupViewModel() {
        final Observer<List<MovieModel>> observeMovieModels = new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(@Nullable List<MovieModel> movieModels) {
                if (movieModels == null) {
                    showFetchError(fab);
                } else {
                    showFetchSuccess(fab);
                    movieGalleryAdapter.resetData(movieModels);
                    movieGalleryAdapter.notifyDataSetChanged();
                }
            }
        };
        Observer<LiveData<List<MovieModel>>> observeMovieSource = new Observer<LiveData<List<MovieModel>>>() {
            @Override
            public void onChanged(@Nullable LiveData<List<MovieModel>> listLiveData) {
                if (movieSource != null)
                    movieSource.removeObservers(MainActivity.this);
                movieSource = listLiveData;
                movieSource.observe(MainActivity.this, observeMovieModels);
            }
        };
        model = ViewModelProviders.of(this).get(MainViewModel.class);
        model.getMovieSource().observe(this, observeMovieSource);
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
                PreferencesUtils.switchSortMode(getApplicationContext());
            }
        });
        updateFab(fab);
    }

    private void updateFab(FloatingActionButton fab) {
        fab.setImageDrawable(PreferencesUtils.getSortModeIcon(this));
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
            showSettings();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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
        haveNetworkConnection = NetworkUtils.haveNetworkConnection(getApplicationContext());
        if (haveNetworkConnection) {
            sb.setText(R.string.error_wrong_api_key);
            sb.setAction(R.string.action_enter_api_key, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSettings();
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
    }
}
