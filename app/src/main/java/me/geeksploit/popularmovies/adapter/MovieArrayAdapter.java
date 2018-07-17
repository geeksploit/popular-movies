package me.geeksploit.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.NetworkUtils;

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
    public View getView(int position, View item, ViewGroup root) {
        MovieModel movie = movies[position];
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // TODO: employ View Holder pattern in Stage 2
        View view = layoutInflater.inflate(movieLayout, root, false);
        ImageView thumbnail = view.findViewById(R.id.movieThumbnail);
        NetworkUtils.loadPoster(context, movie.getPosterPath(), thumbnail, true);
        thumbnail.setContentDescription(movie.getTitle());
        return view;
    }
}
