package me.geeksploit.popularmovies.model;

/**
 * @see <span>The Movie Database API / <a href='https://developers.themoviedb.org/3/movies/get-movie-videos'>Get Videos</a></span>
 */
public final class VideoModel {

    private final String key;
    private final String name;
    private final String site;

    public VideoModel(String key,
                      String name,
                      String site) {
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }
}
