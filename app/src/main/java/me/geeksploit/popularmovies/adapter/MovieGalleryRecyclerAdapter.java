package me.geeksploit.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.model.MovieModel;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieGalleryViewHolder holder, int position) {

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
