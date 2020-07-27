package com.elegion.tracktor.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.elegion.tracktor.R;
import com.elegion.tracktor.viewmodel.CounterViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CounterFragment extends Fragment {

    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.buttonStart) Button buttonStart;
    @BindView(R.id.buttonStop) Button buttonStop;

    private CounterViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_counter, container, false);
        ButterKnife.bind(this, view);
        mViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mViewModel.getStartEnabled().observe(this, buttonStart::setEnabled);
        mViewModel.getStopEnabled().observe(this, buttonStop::setEnabled);
        mViewModel.getTimeText().observe(this, s->tvTime.setText(s));
        return view;
    }

    @OnClick(R.id.buttonStart)
    void onStartClick(){
        mViewModel.startTimer();
    }

    @OnClick(R.id.buttonStop)
    void onStopClick(){
        mViewModel.stopTimer();
    }
}