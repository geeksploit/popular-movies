package me.geeksploit.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.geeksploit.popularmovies.model.MovieModel;

import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.OVERVIEW;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.POSTER_PATH;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RELEASE_DATE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RESULTS;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.TITLE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VOTE_AVERAGE;

public final class JsonUtils {

    public static MovieModel[] parseTheMovieDb(String json) {
        if (json == null) return null;

        try {
            JSONArray jsonMovies = new JSONObject(json).getJSONArray(RESULTS);
            return parseMovies(jsonMovies);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static MovieModel[] parseMovies(JSONArray jsonMovies) throws JSONException {
        MovieModel[] movies = new MovieModel[jsonMovies.length()];
        for (int i = 0; i < movies.length; i++) {
            JSONObject jsonMovie = jsonMovies.getJSONObject(i);
            movies[i] = new MovieModel(
                    jsonMovie.getDouble(VOTE_AVERAGE),
                    jsonMovie.getString(POSTER_PATH),
                    jsonMovie.getString(TITLE),
                    jsonMovie.getString(OVERVIEW),
                    jsonMovie.getString(RELEASE_DATE)
            );
        }
        return movies;
    }

}

final class JsonContractTheMovieDb {
    final static String RESULTS = "results";

    final static String VOTE_AVERAGE = "vote_average";
    final static String POSTER_PATH = "poster_path";
    final static String TITLE = "title";
    final static String OVERVIEW = "overview";
    final static String RELEASE_DATE = "release_date";
}
