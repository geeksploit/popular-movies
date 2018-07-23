package me.geeksploit.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.model.ReviewModel;

import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.MOVIE_ID;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.OVERVIEW;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.POSTER_PATH;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RELEASE_DATE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RESULTS;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_AUTHOR;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_CONTENT;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_URL;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.TITLE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VOTE_AVERAGE;

public final class JsonUtils {

    public static JSONArray getResults(String json) throws JSONException {
        return new JSONObject(json).getJSONArray(RESULTS);
    }

    public static ReviewModel parseReview(JSONObject jsonObject) throws JSONException {
        return new ReviewModel(
                jsonObject.getString(REVIEW_AUTHOR),
                jsonObject.getString(REVIEW_CONTENT),
                jsonObject.getString(REVIEW_URL)
        );
    }

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
                    jsonMovie.getString(RELEASE_DATE),
                    jsonMovie.getString(MOVIE_ID));
        }
        return movies;
    }
}
