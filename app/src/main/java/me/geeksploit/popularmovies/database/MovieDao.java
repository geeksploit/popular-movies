package me.geeksploit.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.geeksploit.popularmovies.model.MovieModel;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieModel>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieModel> loadMovieById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieModel movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieModel movie);

    @Delete
    void deleteMovie(MovieModel movie);
}
