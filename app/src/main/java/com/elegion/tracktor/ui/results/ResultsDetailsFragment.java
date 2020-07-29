package com.elegion.tracktor.ui.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.R;
import com.elegion.tracktor.util.StringUtil;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsDetailsFragment extends Fragment {

    @BindView(R.id.tvTime)
    TextView mTimeText;
    @BindView(R.id.tvDistance)
    TextView mDistanceText;

    public static ResultsDetailsFragment newInstance(Bundle bundle){
        Bundle args = new Bundle();
        args.putAll(bundle);
        ResultsDetailsFragment fragment = new ResultsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        double distance = getArguments().getDouble(ResultsActivity.DISTANCE_KEY, 0.0);
        long time = getArguments().getLong(ResultsActivity.TIME_KEY, 0);
        ArrayList<LatLng> route = (ArrayList<LatLng>) getArguments().getSerializable(ResultsActivity.ROUTE_KEY);

        mTimeText.setText(StringUtil.getTimeText(time));
        mDistanceText.setText(StringUtil.getDistanceText(distance));
    }
}
