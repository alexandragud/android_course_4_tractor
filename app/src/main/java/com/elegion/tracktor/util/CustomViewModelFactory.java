package com.elegion.tracktor.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.ui.map.MainViewModel;
import com.elegion.tracktor.ui.results.ResultsViewModel;

import javax.inject.Inject;

import toothpick.Toothpick;

public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    IRepository mRepository;

    public CustomViewModelFactory(){
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ResultsViewModel.class)) {
            return (T) Toothpick.openScope(App.class).getInstance(ResultsViewModel.class);
        } else if (modelClass.isAssignableFrom(MainViewModel.class))
            return (T) Toothpick.openScope(App.class).getInstance(MainViewModel.class);
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
