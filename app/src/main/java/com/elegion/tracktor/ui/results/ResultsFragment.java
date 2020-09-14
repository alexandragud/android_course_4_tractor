package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.util.CustomViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsFragment extends Fragment{

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private ResultsViewModel mViewModel;
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomViewModelFactory factory = new CustomViewModelFactory(new RealmRepository());
        mViewModel = new ViewModelProvider(this, factory).get(ResultsViewModel.class);
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
        mViewModel.getTracks().observe(this, tracks -> mAdapter.submitList(tracks));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnItemClickListener = null;
    }

    public interface OnItemClickListener{
        void onClick(long trackId);
    }

}