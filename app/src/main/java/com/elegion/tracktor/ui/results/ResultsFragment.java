package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.SortType;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.DeleteTrackEvent;
import com.elegion.tracktor.event.GetTrackResultEvent;
import com.elegion.tracktor.event.UpdateTrackEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class ResultsFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.emptyResults)
    View mEmptyResultsView;

    @Inject
    ResultsViewModel mViewModel;
    private ResultsAdapter mAdapter;

    private Menu mMenu;


    public ResultsFragment() {
    }

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScopes(App.class, ResultsFragment.class)
                .installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_results_fragment, menu);
        this.mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setVisible(false);
        switch (item.getItemId()) {
            case R.id.sort_ascending: {
                mMenu.findItem(R.id.sort_descending).setVisible(true);
                mViewModel.changeSortDirection(false);
                break;
            }
            case R.id.sort_descending: {
                mMenu.findItem(R.id.sort_ascending).setVisible(true);
                mViewModel.changeSortDirection(true);
                break;
            }
            case R.id.sort_by_date: {
                mMenu.findItem(R.id.sort_by_duration).setVisible(true);
                mViewModel.changeSortType(SortType.BY_DURATION);
                break;
            }
            case R.id.sort_by_duration: {
                mMenu.findItem(R.id.sort_by_distance).setVisible(true);
                mViewModel.changeSortType(SortType.BY_DISTANCE);
                break;
            }
            case R.id.sort_by_distance: {
                mMenu.findItem(R.id.sort_by_date).setVisible(true);
                mViewModel.changeSortType(SortType.BY_DATE);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mViewModel.loadTracks();
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
        return inflater.inflate(R.layout.fr_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ResultsAdapter();
        mViewModel.getTracks().observe(getViewLifecycleOwner(), this::showData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showData(List<Track> tracks) {
        if (tracks.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyResultsView.setVisibility(View.GONE);
            mAdapter.submitList(tracks);
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyResultsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toothpick.closeScope(ResultsFragment.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetTrackResult(GetTrackResultEvent event) {
        ResultsActivity.start(getContext(), event.getTrackId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteTrack(DeleteTrackEvent event) {
        if (mViewModel.deleteTrack(event.getTrackId()))
            mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTrack(UpdateTrackEvent event) {
        mViewModel.updateTrack(event.getTrack());
        mAdapter.notifyDataSetChanged();
    }

}
