package me.geeksploit.popularmovies.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import me.geeksploit.popularmovies.database.AppDatabase;

public final class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final String mMovieId;

    public DetailViewModelFactory(AppDatabase database, String movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mDb, mMovieId);
    }
}
