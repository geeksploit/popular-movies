package me.geeksploit.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import me.geeksploit.popularmovies.database.AppDatabase;

public final class MainViewModel extends AndroidViewModel {

    private final LiveData<List<MovieModel>> mMoviesFromLocalFavorites;
    private final MutableLiveData<List<MovieModel>> mMoviesFromNetworkApi;
    private final MutableLiveData<LiveData<List<MovieModel>>> mMovieSource;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(getApplication());
        mMoviesFromLocalFavorites = db.movieDao().loadAllMovies();
        mMoviesFromNetworkApi = new MutableLiveData<>();
        mMovieSource = new MutableLiveData<>();
    }

    public MutableLiveData<LiveData<List<MovieModel>>> getMovieSource() {
        return mMovieSource;
    }
}
