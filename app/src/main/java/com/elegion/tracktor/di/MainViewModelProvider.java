package com.elegion.tracktor.di;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.elegion.tracktor.ui.map.MainViewModel;
import com.elegion.tracktor.util.CustomViewModelFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class MainViewModelProvider implements Provider<MainViewModel> {

    @Inject
    CustomViewModelFactory factory;

    @Inject
    ViewModelStoreOwner owner;

    @Override
    public MainViewModel get() {
        return new ViewModelProvider(owner, factory).get(MainViewModel.class);
    }
}
