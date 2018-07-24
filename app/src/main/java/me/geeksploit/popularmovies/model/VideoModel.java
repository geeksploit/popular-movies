package me.geeksploit.popularmovies.model;

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
}
