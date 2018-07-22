package me.geeksploit.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import me.geeksploit.popularmovies.R;

public class MovieGalleryRecyclerAdapter {

    class MovieGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        MovieGalleryViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieThumbnail);
        }
    }
}
