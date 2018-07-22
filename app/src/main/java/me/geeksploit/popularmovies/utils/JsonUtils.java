package me.geeksploit.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.geeksploit.popularmovies.model.MovieModel;

import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.*;

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
