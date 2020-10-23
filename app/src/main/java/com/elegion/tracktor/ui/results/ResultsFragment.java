package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class ResultsFragment extends Fragment{

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Inject
    ResultsViewModel mViewModel;
    private OnItemClickListener mOnItemClickListener;
    private ResultsAdapter mAdapter;


    public ResultsFragment() {
    }

    public static ResultsFragment newInstance(){
        return new ResultsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener){
            mOnItemClickListener = (OnItemClickListener) context;
        } else{
            throw new RuntimeException(context.toString()
                    + " must implement OnItemClickListener");
        }
        Scope scope = Toothpick.openScopes(App.class, ResultsFragment.class)
                .installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mAdapter = new ResultsAdapter(mOnItemClickListener);
        mViewModel.getTracks().observe(getViewLifecycleOwner(), tracks -> mAdapter.submitList(tracks));
        mViewModel.loadTracks();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnItemClickListener = null;
        Toothpick.closeScope(ResultsFragment.class);
    }

    public interface OnItemClickListener{
        void onClick(long trackId);
    }

}
