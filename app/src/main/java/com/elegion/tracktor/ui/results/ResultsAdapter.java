package com.elegion.tracktor.ui.results;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;

public class ResultsAdapter extends ListAdapter<Track, ResultHolder> {

    private boolean isDistanceInMiles;

    private static final DiffUtil.ItemCallback<Track> DIFF_CALLBACK = new DiffUtil.ItemCallback<Track>() {
        @Override
        public boolean areItemsTheSame(@NonNull Track oldItem, @NonNull Track newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Track oldItem, @NonNull Track newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ResultsAdapter(boolean isInMiles) {
        super(DIFF_CALLBACK);
        isDistanceInMiles = isInMiles;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_track, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        holder.bind(getItem(position), isDistanceInMiles);
    }
}
