package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.ActivityType;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.ShareTrackInfoEvent;
import com.elegion.tracktor.event.ShowCommentDialogEvent;
import com.elegion.tracktor.event.UpdateTrackEvent;
import com.elegion.tracktor.util.CaloriesCalculator;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.elegion.tracktor.ui.results.ResultsActivity.RESULT_KEY;

public class ResultsDetailsFragment extends Fragment {

    @BindView(R.id.tvTime)
    TextView mTimeText;
    @BindView(R.id.tvDistance)
    TextView mDistanceText;
    @BindView(R.id.ivImage)
    ImageView mScreenshotImage;

    @BindView(R.id.tvSpeed)
    TextView mSpeedText;
    @BindView(R.id.tvDate)
    TextView mDateText;
    @BindView(R.id.spActivity)
    Spinner mActivityList;
      @BindView(R.id.tvCalories)
      TextView mCaloriesText;
    @BindView(R.id.tvComment)
    TextView mCommentText;


    private Bitmap mImage;
    @Inject
    ResultsViewModel mViewModel;

    public static ResultsDetailsFragment newInstance(long trackId) {
        Bundle bundle = new Bundle();
        bundle.putLong(RESULT_KEY, trackId);
        ResultsDetailsFragment fragment = new ResultsDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScopes(App.class, ResultsDetailsFragment.class)
                .installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fr_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivityList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mViewModel.getAllActivities()));
        mViewModel.selectTrack(getArguments().getLong(ResultsActivity.RESULT_KEY, 0));
        mViewModel.getSelectedDistanceText().observe(getViewLifecycleOwner(), s -> mDistanceText.setText(s));
        mViewModel.getSelectedTimeText().observe(getViewLifecycleOwner(), s -> mTimeText.setText(s));
        mViewModel.getSelectedImage().observe(getViewLifecycleOwner(), b -> {
            mImage = b;
            mScreenshotImage.setImageBitmap(b);
        });
        mViewModel.getSelectedSpeed().observe(getViewLifecycleOwner(), s -> mSpeedText.setText(s));
        mViewModel.getSelectedDate().observe(getViewLifecycleOwner(), s -> mDateText.setText(s));
        mViewModel.getSelectedActivity().observe(getViewLifecycleOwner(), this::selectActivity);
        mViewModel.getSelectedComment().observe(getViewLifecycleOwner(), s -> mCommentText.setText(StringUtil.getComment(s)));
        mViewModel.getSelectedCalories().observe(getViewLifecycleOwner(), s -> mCaloriesText.setText(s));
        mViewModel.loadSelectedTrack();
    }

    private void selectActivity(ActivityType value) {
        mActivityList.setSelection(value.getId());
    }

    private void calculateCalories() {
        CaloriesCalculator calculator = CaloriesCalculator.builder()
                .setDataFromPreferences(getContext()).build();
        mViewModel.updateCalories(calculator);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionShare) {
           EventBus.getDefault().post(new ShareTrackInfoEvent(mViewModel.getSelectedTrack()));
        } else if (item.getItemId() == R.id.actionDelete) {
            if (mViewModel.deleteSelectedTrack()) {
                getActivity().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        Toothpick.closeScope(ResultsDetailsFragment.class);
        super.onDetach();
    }

    @OnClick(R.id.add_comment_btn)
    void onCommentClick() {
        Track track = mViewModel.getSelectedTrack();
        EventBus.getDefault().post(new ShowCommentDialogEvent(track));
    }

    @OnItemSelected(R.id.spActivity)
    void onActivitySelected(int position) {
        mViewModel.updateTrackActivity(position);
        calculateCalories();
    }

    @OnItemSelected(value = R.id.spActivity, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onNothingSelected() {
        mActivityList.setSelection(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTrack(UpdateTrackEvent event) {
        mViewModel.updateTrack(event.getTrack());
        mViewModel.loadSelectedTrack();
    }
}
