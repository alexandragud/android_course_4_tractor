package com.elegion.tracktor.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.StartBtnClickedEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;
import com.elegion.tracktor.event.StopCountEvent;
import com.elegion.tracktor.util.DistanceConverter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

public class CounterFragment extends Fragment {

    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.buttonStart)
    Button buttonStart;
    @BindView(R.id.buttonStop)
    Button buttonStop;

    @Inject
    MainViewModel mViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScopes(App.class, CounterFragment.class)
                .installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.isDistanceInMiles(DistanceConverter.isDistanceInMiles(getContext()));
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_counter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getStartEnabled().observe(getViewLifecycleOwner(), buttonStart::setEnabled);
        mViewModel.getStopEnabled().observe(getViewLifecycleOwner(), buttonStop::setEnabled);
        mViewModel.getTimeText().observe(getViewLifecycleOwner(), s -> tvTime.setText(s));
        mViewModel.getDistanceText().observe(getViewLifecycleOwner(), s -> tvDistance.setText(s));
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.buttonStart)
    void onStartClick() {
        EventBus.getDefault().post(new StartBtnClickedEvent());
        mViewModel.onStartCount();
        mViewModel.clear();
    }

    @OnClick(R.id.buttonStop)
    void onStopClick() {
        EventBus.getDefault().post(new StopBtnClickedEvent());
        mViewModel.onStopCount();
    }

    @Override
    public void onDetach() {
        Toothpick.closeScope(CounterFragment.class);
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStopCount(StopCountEvent event) {
        mViewModel.onStopCount();
        EventBus.getDefault().post(new StopBtnClickedEvent());
    }
}
