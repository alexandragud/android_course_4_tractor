package com.elegion.tracktor.ui.results;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;

public class ResultHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mDateText;
    private TextView mDistanceText;
    private long mTrackId;

    public ResultHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mDateText = mView.findViewById(R.id.tv_date);
        mDistanceText = mView.findViewById(R.id.tv_distance);
    }

    public void bind(Track track){
        mTrackId = track.getId();
        mDateText.setText(String.valueOf(track.getDate()));
        mDistanceText.setText(String.valueOf(track.getDistance()));
    }

    public void setListener(ResultsFragment.OnItemClickListener listener){
        mView.setOnClickListener(v -> {
            if (null!=listener){
                listener.onClick(mTrackId);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

}
