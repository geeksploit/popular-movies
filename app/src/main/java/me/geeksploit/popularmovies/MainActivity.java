package me.geeksploit.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.net.URL;

import me.geeksploit.popularmovies.adapter.MovieArrayAdapter;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private GridView moviesGrid;
    private FloatingActionButton fab;

    private MovieModel[] mMovies = new MovieModel[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchMoviesData();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.movies_progress);

        moviesGrid = findViewById(R.id.movies_grid);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent movieDetail = new Intent(MainActivity.this, DetailActivity.class);
                movieDetail.putExtra(DetailActivity.EXTRA_MOVIE, mMovies[position]);
                startActivity(movieDetail);
            }
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            moviesGrid.setNumColumns(5);
        else
            moviesGrid.setNumColumns(3);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFab(fab);
            }
        });
        updateFab(fab);
    }

    private void updateFab(FloatingActionButton fab) {
        fab.setImageDrawable(ContextCompat.getDrawable(
                getApplicationContext(),
                PreferencesUtils.isSortModePopular(getApplicationContext()) ?
                        R.drawable.sort_popular :
                        R.drawable.sort_top_rated)
        );
    }

    private void fetchMoviesData() {
        new FetchMoviesTask().execute(
                PreferencesUtils.getSortMode(getApplicationContext()),
                PreferencesUtils.getApiKey(getApplicationContext())
        );
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
                                PreferencesUtils.setPrefApiKey(
                                        getApplicationContext(),
                                        apiKey.getText().toString()
                                );
                                fetchMoviesData();
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
                PreferencesUtils.isSortModePopular(getApplicationContext()) ?
                        getString(R.string.message_order_by_popularity) :
                        getString(R.string.message_order_by_rating),
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void setStateFetchingMovies(boolean inProgress) {
        if (inProgress) {
            fab.setEnabled(false);
            moviesGrid.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            fab.setEnabled(true);
            moviesGrid.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
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
                return;
            }

            showFetchSuccess(fab);
            mMovies = movies;
            moviesGrid.setAdapter(new MovieArrayAdapter(
                    getApplicationContext(),
                    R.layout.movie_grid_item, movies)
            );
        }
    }
}
