package me.geeksploit.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.NetworkUtils;

public class MovieGalleryAdapter extends RecyclerView.Adapter<MovieGalleryAdapter.MovieGalleryViewHolder> {

    private Context context;
    private List<MovieModel> movieList;
    private OnClickListener onClickListener;

    public MovieGalleryAdapter(Context context, OnClickListener onClickListener) {
        this.context = context.getApplicationContext();
        this.movieList = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieGalleryViewHolder(view, onClickListener);
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

    public void resetData(List<MovieModel> newData) {
        movieList.clear();
        movieList.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(MovieModel movie);
    }

    class MovieGalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePoster;
        private OnClickListener onClickListener;

        MovieGalleryViewHolder(View itemView, MovieGalleryAdapter.OnClickListener onClickListener) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieThumbnail);

            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(movieList.get(getAdapterPosition()));
        }
    }
}
