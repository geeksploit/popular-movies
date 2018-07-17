package me.geeksploit.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import me.geeksploit.popularmovies.model.MovieModel;
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
}
