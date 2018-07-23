package me.geeksploit.popularmovies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.geeksploit.popularmovies.R;
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

    private static final String ARG_MOVIE = "movie";

    private MovieModel movie;

    private OnClickFavoritesListener mListener;

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
