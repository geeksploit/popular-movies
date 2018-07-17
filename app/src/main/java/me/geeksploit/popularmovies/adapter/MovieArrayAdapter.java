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
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
