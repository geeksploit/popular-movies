package me.geeksploit.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.model.ReviewModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MOVIE_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent.getExtras() == null) return;

        final MovieModel movie = (MovieModel) intent.getExtras().get(EXTRA_MOVIE);
        if (movie == null) return;

        ImageView poster = findViewById(R.id.detail_poster);
        NetworkUtils.loadPoster(this, movie.getPosterPath(), poster, true);

        setText(R.id.detail_title, movie.getTitle());
        setText(R.id.detail_release_date, movie.getReleaseDate());
        setText(R.id.detail_vote_average, String.valueOf(movie.getVoteAverage()));
        setText(R.id.detail_overview, movie.getOverview());
    }

    private void setText(int id, String text) {
        TextView textView = findViewById(id);
        textView.setText(text);
    }

    class FetchReviewsTask extends AsyncTask<String, ReviewModel, List<ReviewModel>> {

        @Override
        protected List<ReviewModel> doInBackground(String... params) {
            String movieId = params[0];
            String apiKey = params[1];

            List<ReviewModel> models = null;
            try {
                URL queryUrl = NetworkUtils.buildUrlReviews(Integer.parseInt(movieId), apiKey);
                String response = NetworkUtils.getResponseFromHttpUrl(queryUrl);
                JSONArray results = JsonUtils.getResults(response);

                models = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); i++) {
                    models.add(JsonUtils.parseReview(results.getJSONObject(i)));
                    publishProgress(models.get(i));
                }
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onProgressUpdate(ReviewModel... values) {
            // TODO: update the UI
        }

        @Override
        protected void onPostExecute(List<ReviewModel> movies) {
            // TODO: update the UI
        }
    }
}
