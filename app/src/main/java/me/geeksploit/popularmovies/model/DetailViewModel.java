package me.geeksploit.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import me.geeksploit.popularmovies.database.AppDatabase;

public class DetailViewModel extends ViewModel {

    private LiveData<MovieModel> movie;
    private AppDatabase db;

    DetailViewModel(AppDatabase database, String movieId) {
        db = database;
        movie = db.movieDao().loadMovieById(movieId);
    }

    public LiveData<MovieModel> getMovie() {
        return movie;
    }

    public void favoriteRemove(MovieModel movie) {
        db.movieDao().deleteMovie(movie);
    }

    public void favoriteAdd(MovieModel movie) {
        db.movieDao().insertMovie(movie);
    }
}
