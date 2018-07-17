package me.geeksploit.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.net.URL;

import me.geeksploit.popularmovies.adapter.MovieArrayAdapter;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public class MainActivity extends AppCompatActivity {

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

    private void initializeViews() {
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

    class FetchMoviesTask extends AsyncTask<String, Void, MovieModel[]> {

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

            if (movies == null) {
                return;
            }

            mMovies = movies;
            moviesGrid.setAdapter(new MovieArrayAdapter(
                    getApplicationContext(),
                    R.layout.movie_grid_item, movies)
            );
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
