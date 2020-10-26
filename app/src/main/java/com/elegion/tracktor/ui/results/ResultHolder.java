package com.elegion.tracktor.ui.results;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.event.DeleteTrackEvent;
import com.elegion.tracktor.event.GetTrackResultEvent;
import com.elegion.tracktor.event.ShareTrackInfoEvent;
import com.elegion.tracktor.event.ShowCommentDialogEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

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
    private ImageButton mDeleteButton;
    private ImageButton mEditCommentButton;
    private ImageButton mSendButton;
    private View mExpandedView;
    private long mTrackId;

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
        mExpandedView = mView.findViewById(R.id.expanded_item);
        mExpandButton = mView.findViewById(R.id.expand_button);
        mDeleteButton = mView.findViewById(R.id.delete_button);
        mEditCommentButton = mView.findViewById(R.id.add_comment_btn);
        mSendButton = mView.findViewById(R.id.send_button);
    }

    public void bind(Track track) {
        mTrackId = track.getId();
        mDateText.setText(StringUtil.getDateText(track.getDate()));
        mDistanceText.setText(StringUtil.getDistanceText(track.getDistance()));
        mTimeText.setText(StringUtil.getTimeText(track.getDuration()));
        mSpeedText.setText(StringUtil.getSpeedText(track.getSpeed()));
        mCaloriesText.setText(StringUtil.getCaloriesText(track.getCalories()));
        mActivityTypeText.setText(track.getActivityType().getName());
        mCommentText.setText(StringUtil.getComment(track.getComment()));
        updateExpandedView(track.isExpand());
        mExpandButton.setOnClickListener(v -> {
            boolean isExpanded = !track.isExpand();
            updateExpandedView(isExpanded);
            track.setExpand(isExpanded);
        });

        mDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new DeleteTrackEvent(mTrackId)));

        mView.setOnClickListener(v -> EventBus.getDefault().post(new GetTrackResultEvent(mTrackId)));

        mEditCommentButton.setOnClickListener(v-> EventBus.getDefault().post(new ShowCommentDialogEvent(track)));

        mSendButton.setOnClickListener(v-> EventBus.getDefault().post(new ShareTrackInfoEvent(track)));
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDistanceText.getText() + "'";
    }

    private void updateExpandedView(boolean isExpanded){
        if (!isExpanded) {
            mExpandedView.setVisibility(View.GONE);
            mExpandButton.setImageResource(R.drawable.ic_expand_more);
        } else {
            mExpandedView.setVisibility(View.VISIBLE);
            mExpandButton.setImageResource(R.drawable.ic_expand_less);
        }
    }

}
