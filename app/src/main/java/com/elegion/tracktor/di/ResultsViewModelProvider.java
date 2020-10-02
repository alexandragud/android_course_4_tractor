package com.elegion.tracktor.di;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.elegion.tracktor.ui.results.ResultsViewModel;
import com.elegion.tracktor.util.CustomViewModelFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class ResultsViewModelProvider implements Provider<ResultsViewModel> {

    @Inject
    CustomViewModelFactory factory;

    @Inject
    ViewModelStoreOwner owner;

    @Override
    public ResultsViewModel get() {
        return new ViewModelProvider(owner, factory).get(ResultsViewModel.class);
    }
}
