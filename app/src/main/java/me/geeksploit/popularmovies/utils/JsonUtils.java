package me.geeksploit.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.model.ReviewModel;
import me.geeksploit.popularmovies.model.VideoModel;

import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.MOVIE_ID;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.OVERVIEW;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.POSTER_PATH;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RELEASE_DATE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.RESULTS;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_AUTHOR;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_CONTENT;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.REVIEW_URL;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.TITLE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VIDEO_KEY;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VIDEO_NAME;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VIDEO_SITE;
import static me.geeksploit.popularmovies.utils.JsonContractTheMovieDb.VOTE_AVERAGE;

public final class JsonUtils {

    public static JSONArray getResults(String json) throws JSONException {
        return new JSONObject(json).getJSONArray(RESULTS);
    }

    public static MovieModel parseMovie(JSONObject jsonObject) throws JSONException {
        return new MovieModel(
                jsonObject.getDouble(VOTE_AVERAGE),
                jsonObject.getString(POSTER_PATH),
                jsonObject.getString(TITLE),
                jsonObject.getString(OVERVIEW),
                jsonObject.getString(RELEASE_DATE),
                jsonObject.getString(MOVIE_ID));
    }

    public static ReviewModel parseReview(JSONObject jsonObject) throws JSONException {
        return new ReviewModel(
                jsonObject.getString(REVIEW_AUTHOR),
                jsonObject.getString(REVIEW_CONTENT),
                jsonObject.getString(REVIEW_URL)
        );
    }

    public static VideoModel parseVideo(JSONObject jsonObject) throws JSONException {
        return new VideoModel(
                jsonObject.getString(VIDEO_KEY),
                jsonObject.getString(VIDEO_NAME),
                jsonObject.getString(VIDEO_SITE)
        );
    }
}
