package me.geeksploit.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.geeksploit.popularmovies.R;

public class MovieGalleryRecyclerAdapter extends RecyclerView.Adapter<MovieGalleryRecyclerAdapter.MovieGalleryViewHolder> {

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
        return 0;
    }

    class MovieGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        MovieGalleryViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieThumbnail);
        }
    }
}
