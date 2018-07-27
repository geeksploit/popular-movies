package me.geeksploit.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.fragments.VideoFragment.OnClickShareVideoItemListener;
import me.geeksploit.popularmovies.fragments.VideoFragment.OnClickVideoItemListener;
import me.geeksploit.popularmovies.model.VideoModel;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.ViewHolder> {

    private final List<VideoModel> mValues;
    private final OnClickVideoItemListener mListener;
    private final OnClickShareVideoItemListener mShareListener;

    public VideoRecyclerAdapter(List<VideoModel> items, OnClickVideoItemListener listener, OnClickShareVideoItemListener shareListener) {
        mValues = items;
        mListener = listener;
        mShareListener = shareListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClickVideoItem(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addData(VideoModel value) {
        mValues.add(value);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mNameView;
        final ImageView mShareView;
        public VideoModel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.video_item_name);
            mShareView = view.findViewById(R.id.video_item_share);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
