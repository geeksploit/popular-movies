package me.geeksploit.popularmovies.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.adapter.VideoRecyclerAdapter;
import me.geeksploit.popularmovies.model.VideoModel;

/**
 * A fragment representing a list of trailer videos.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnClickVideoItemListener}
 * interface.
 */
public class VideoFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnClickVideoItemListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {
    }

    public static VideoFragment newInstance(int columnCount) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        initializeRecyclerView((RecyclerView) view);
        return view;
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new VideoRecyclerAdapter(new ArrayList<VideoModel>(), mListener));
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
}
