package me.geeksploit.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.geeksploit.popularmovies.R;
import me.geeksploit.popularmovies.model.ReviewModel;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    private final List<ReviewModel> mValues = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAuthorView.setText(mValues.get(position).getAuthor());
        holder.mContentView.setText(mValues.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addData(ReviewModel value) {
        mValues.add(value);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mAuthorView;
        final TextView mContentView;

        ReviewModel mItem;

        ViewHolder(View view) {
            super(view);
            mAuthorView = view.findViewById(R.id.review_item_author);
            mContentView = view.findViewById(R.id.review_item_content);
        }
    }
}
