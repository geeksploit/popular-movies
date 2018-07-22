package me.geeksploit.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.NetworkUtils;

public class MovieGalleryRecyclerAdapter extends RecyclerView.Adapter<MovieGalleryRecyclerAdapter.MovieGalleryViewHolder> {

    private Context context;
    private List<MovieModel> movieList;

    public MovieGalleryRecyclerAdapter(Context context, List<MovieModel> movieList) {
        this.context = context.getApplicationContext();
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieGalleryViewHolder holder, int position) {
        NetworkUtils.loadPoster(context,
                movieList.get(position).getPosterPath(),
                holder.moviePoster,
                true);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        MovieGalleryViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieThumbnail);
        }
    }
}
