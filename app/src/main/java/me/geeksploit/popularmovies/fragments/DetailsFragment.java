package me.geeksploit.popularmovies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.database.AppDatabase;
import me.geeksploit.popularmovies.model.DetailViewModel;
import me.geeksploit.popularmovies.model.DetailViewModelFactory;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.utils.NetworkUtils;

/**
 * Activities that contain this fragment must implement the
 * {@link OnClickFavoritesListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "database";
    private static final String ARG_MOVIE = "movie";

    private MovieModel movie;

    private OnClickFavoritesListener mListener;
    private TextView favoritesButton;
    private boolean isFavorite;
    private DetailViewModel viewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(MovieModel movie) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (MovieModel) getArguments().getSerializable(ARG_MOVIE);
        }
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this,
                new DetailViewModelFactory(
                        AppDatabase.getInstance(getContext()),
                        movie.getId()))
                .get(DetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(@Nullable MovieModel movie) {
                isFavorite = movie != null;
                favoritesButton.setText(isFavorite ?
                        getString(R.string.button_favorite_remove) :
                        getString(R.string.button_favorite_mark)
                );
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel, favorite " + isFavorite);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView poster = view.findViewById(R.id.detail_poster);
        NetworkUtils.loadPoster(getContext(), movie.getPosterPath(), poster, true);

        setText(view, R.id.detail_title, movie.getTitle());
        setText(view, R.id.detail_release_date, movie.getReleaseDate());
        setText(view, R.id.detail_vote_average, String.valueOf(movie.getVoteAverage()));
        setText(view, R.id.detail_overview, movie.getOverview());

        favoritesButton = view.findViewById(R.id.detail_mark_favorite);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFavorites(null);
            }
        });

        return view;
    }

    private void setText(View root, int id, String text) {
        TextView textView = root.findViewById(id);
        textView.setText(text);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClickFavorites(Uri uri) {
        if (isFavorite)
            viewModel.favoriteRemove(movie);
        else
            viewModel.favoriteAdd(movie);

        if (mListener != null) {
            mListener.onClickFavorites(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickFavoritesListener) {
            mListener = (OnClickFavoritesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickFavoritesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnClickFavoritesListener {
        void onClickFavorites(Uri uri);
    }
}
