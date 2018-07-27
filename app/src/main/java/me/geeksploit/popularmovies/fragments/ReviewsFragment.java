package me.geeksploit.popularmovies.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.adapter.ReviewRecyclerAdapter;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.model.ReviewModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

public class ReviewsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private MovieModel mMovie;
    private ReviewRecyclerAdapter mReviewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(MovieModel movie) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovie = (MovieModel) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        initializeRecyclerView((RecyclerView) view);
        new FetchReviewsTask().execute(mMovie.getId(), PreferencesUtils.getApiKey(getContext()));
        return view;
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL)
        );

        mReviewAdapter = new ReviewRecyclerAdapter();
        recyclerView.setAdapter(mReviewAdapter);
    }

    class FetchReviewsTask extends AsyncTask<String, ReviewModel, List<ReviewModel>> {

        @Override
        protected List<ReviewModel> doInBackground(String... params) {
            String movieId = params[0];
            String apiKey = params[1];

            List<ReviewModel> models = null;
            try {
                URL queryUrl = NetworkUtils.buildUrlReviews(Integer.parseInt(movieId), apiKey);
                String response = NetworkUtils.getResponseFromHttpUrl(queryUrl);
                JSONArray results = JsonUtils.getResults(response);

                models = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); i++) {
                    models.add(JsonUtils.parseReview(results.getJSONObject(i)));
                    publishProgress(models.get(i));
                }
            } catch (JSONException | MalformedURLException | NullPointerException e) {
                e.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onProgressUpdate(ReviewModel... values) {
            mReviewAdapter.addData(values[0]);
            mReviewAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<ReviewModel> movies) {
            if (mReviewAdapter.getItemCount() == 0) {
                Snackbar.make(getView(),
                        getString(R.string.detail_reviews_empty, mMovie.getTitle()),
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
