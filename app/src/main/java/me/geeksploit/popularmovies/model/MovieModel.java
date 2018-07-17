package me.geeksploit.popularmovies.model;

/**
 * Holds detailed movie information.
 * <p>
 * Stage 1 requirements:
 * <ul>
 * <li>title
 * <li>movie poster image thumbnail
 * <li>a plot synopsis (called overview in the api)
 * <li>user rating (called vote_average in the api)
 * <li>release date
 * </ul>
 */
public class MovieModel {

    private final double voteAverage;
    private final String posterPath;
    private final String title;
    private final String overview;
    private final String releaseDate;

    public MovieModel(double voteAverage,
                      String posterPath,
                      String title,
                      String overview,
                      String releaseDate) {
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
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

}
