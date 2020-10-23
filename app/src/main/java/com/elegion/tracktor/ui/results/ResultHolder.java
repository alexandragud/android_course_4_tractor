package com.elegion.tracktor.ui.results;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.util.StringUtil;

public class ResultHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mDateText;
    private TextView mDistanceText;
    private TextView mTimeText;
    private TextView mSpeedText;
    private TextView mCaloriesText;
    private TextView mActivityTypeText;
    private TextView mCommentText;
    private ImageButton mExpandButton;
    private View mExpandedView;
    private long mTrackId;

    private boolean isExpanded = false;

    public ResultHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mDateText = mView.findViewById(R.id.tv_date);
        mDistanceText = mView.findViewById(R.id.tv_distance);
        mTimeText = mView.findViewById(R.id.tv_time);
        mSpeedText = mView.findViewById(R.id.tv_speed);
        mCaloriesText = mView.findViewById(R.id.tv_calories);
        mActivityTypeText = mView.findViewById(R.id.tv_activity);
        mCommentText = mView.findViewById(R.id.tv_comment);
        mExpandedView = mView.findViewById(R.id.expandedItem);
        mExpandButton = mView.findViewById(R.id.expandButton);
    }

    public void bind(Track track){
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mTimeText.setText(StringUtil.getTimeText(track.getDuration()));
        mSpeedText.setText(StringUtil.getSpeedText(track.getSpeed()));
        mCaloriesText.setText(StringUtil.getCaloriesText(track.getCalories()));
        mActivityTypeText.setText(track.getActivityType().getName());
        mCommentText.setText(StringUtil.getComment(track.getComment()));

        mExpandButton.setOnClickListener(v -> {
            if (isExpanded){
                mExpandedView.setVisibility(View.GONE);
                mExpandButton.setImageResource(R.drawable.ic_expand_more);
            } else{
                mExpandedView.setVisibility(View.VISIBLE);
                mExpandButton.setImageResource(R.drawable.ic_expand_less);
            }
            isExpanded = !isExpanded;
        });
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
