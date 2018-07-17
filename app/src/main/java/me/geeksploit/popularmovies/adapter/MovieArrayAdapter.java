package me.geeksploit.popularmovies.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import me.geeksploit.popularmovies.model.MovieModel;

public class MovieArrayAdapter extends BaseAdapter {

    private final Context context;
    private final int movieLayout;
    private final MovieModel[] movies;

    public MovieArrayAdapter(Context context, int movieLayout, MovieModel[] movies) {
        this.context = context;
        this.movieLayout = movieLayout;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
