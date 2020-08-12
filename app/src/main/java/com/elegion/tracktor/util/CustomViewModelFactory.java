package com.elegion.tracktor.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.ui.results.ResultsViewModel;

public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private IRepository mRepository;

    public CustomViewModelFactory(IRepository repository){
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ResultsViewModel.class)) {
            return (T) new ResultsViewModel(mRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
