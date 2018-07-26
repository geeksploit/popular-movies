package me.geeksploit.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Holds detailed movie information.
 * <ul>
 * <li>title
 * <li>movie poster image thumbnail
 * <li>a plot synopsis (called overview in the api)
 * <li>user rating (called vote_average in the api)
 * <li>release date
 * <li>movie id
 * </ul>
 */
@Entity(tableName = "movie")
public class MovieModel implements Serializable {
// TODO: Implement Parcelable in Stage 2

    private final double voteAverage;
    private final String posterPath;
    private final String title;
    private final String overview;
    private final String releaseDate;
    @NonNull
    @PrimaryKey
    private final String id;

    public MovieModel(double voteAverage,
                      String posterPath,
                      String title,
                      String overview,
                      String releaseDate,
                      @NonNull String id) {
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
