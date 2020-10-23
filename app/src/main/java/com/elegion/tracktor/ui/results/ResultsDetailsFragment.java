package com.elegion.tracktor.ui.results;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.ActivityType;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.util.CaloriesCalculator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
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
        mViewModel.getSelectedComment().observe(getViewLifecycleOwner(), this::setComment);
        mViewModel.getSelectedCalories().observe(getViewLifecycleOwner(), s -> mCaloriesText.setText(s));
        mViewModel.loadSelectedTrack();
    }

    private void setComment(String comment) {
        if (comment == null || comment.isEmpty())
            comment = getString(R.string.comment);
        mCommentText.setText(comment);
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
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), mImage, "Мой маршрут", null);
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, getTrackInfo());
                startActivity(Intent.createChooser(intent, "Результаты маршрута"));
                return true;
            } else {
                Toast.makeText(getContext(), R.string.permissions_denied, Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.actionDelete) {
            if (mViewModel.deleteSelectedTrack()) {
                getActivity().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getTrackInfo() {
        return String.format("Дата: %s\nВремя: %s\nРасстояние: %s\nСредняя скорость: %s\nДеятельность: %s\nКалории: %s\nКомментарий: %s",
                mDateText.getText(),
                mTimeText.getText(),
                mDistanceText.getText(),
                mSpeedText.getText(),
                mActivityList.getSelectedItem().toString(),
                mCaloriesText.getText(),
                mCommentText.getText());
    }

    @Override
    public void onDetach() {
        Toothpick.closeScope(ResultsDetailsFragment.class);
        super.onDetach();
    }

    @OnClick(R.id.add_comment_btn)
    void onCommentClick() {
        AlertDialog.Builder commentDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.comment_title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dilaog_add_comment, null);
        EditText comment = view.findViewById(R.id.et_comment);
        mViewModel.getSelectedComment().observe(getViewLifecycleOwner(), comment::setText);
        commentDialog.setView(view);
        commentDialog.setPositiveButton(R.string.comment_button_ok, ((dialog, which) ->
                mViewModel.updateTrackComment(comment.getText().toString()))
        );
        commentDialog.setNegativeButton(R.string.comment_button_cancel, (dialog, which) -> dialog.cancel());
        commentDialog.create().show();
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
}
