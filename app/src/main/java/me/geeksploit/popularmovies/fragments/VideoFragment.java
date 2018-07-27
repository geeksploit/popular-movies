package me.geeksploit.popularmovies.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import me.geeksploit.popularmovies.adapter.VideoRecyclerAdapter;
import me.geeksploit.popularmovies.model.MovieModel;
import me.geeksploit.popularmovies.model.VideoModel;
import me.geeksploit.popularmovies.utils.JsonUtils;
import me.geeksploit.popularmovies.utils.NetworkUtils;
import me.geeksploit.popularmovies.utils.PreferencesUtils;

/**
 * A fragment representing a list of trailer videos.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnClickVideoItemListener}
 * interface.
 */
public class VideoFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private MovieModel mMovie;
    private int mColumnCount = 1;
    private OnClickVideoItemListener mListener;
    private VideoRecyclerAdapter mVideoAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {
    }

    public static VideoFragment newInstance(MovieModel movie, int columnCount) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovie = (MovieModel) getArguments().getSerializable(ARG_MOVIE);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        initializeRecyclerView((RecyclerView) view);
        new FetchVideosTask().execute(mMovie.getId(), PreferencesUtils.getApiKey(getContext()));
        return view;
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        mVideoAdapter = new VideoRecyclerAdapter(new ArrayList<VideoModel>(), mListener);
        recyclerView.setAdapter(mVideoAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickVideoItemListener) {
            mListener = (OnClickVideoItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickVideoItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnClickVideoItemListener {
        void onClickVideoItem(VideoModel item);
    }

    class FetchVideosTask extends AsyncTask<String, VideoModel, List<VideoModel>> {

        @Override
        protected List<VideoModel> doInBackground(String... params) {
            String movieId = params[0];
            String apiKey = params[1];

            List<VideoModel> models = null;
            try {
                URL queryUrl = NetworkUtils.buildUrlVideos(movieId, apiKey);
                String response = NetworkUtils.getResponseFromHttpUrl(queryUrl);
                JSONArray results = JsonUtils.getResults(response);

                models = new ArrayList<>(results.length());
                for (int i = 0; i < results.length(); i++) {
                    models.add(JsonUtils.parseVideo(results.getJSONObject(i)));
                    publishProgress(models.get(i));
                }
            } catch (JSONException | MalformedURLException | NullPointerException e ) {
                e.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onProgressUpdate(VideoModel... values) {
            mVideoAdapter.addData(values[0]);
            mVideoAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<VideoModel> models) {
            if (mVideoAdapter.getItemCount() == 0) {
                Snackbar.make(getView(),
                        getString(R.string.detail_videos_empty, mMovie.getTitle()),
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
