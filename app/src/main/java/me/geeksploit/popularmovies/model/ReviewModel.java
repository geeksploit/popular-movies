package me.geeksploit.popularmovies.model;

public final class ReviewModel {

    private final String author;
    private final String content;
    private final String url;

    public ReviewModel(String author,
                       String content,
                       String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
