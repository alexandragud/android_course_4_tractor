package com.elegion.tracktor.di;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;

import com.elegion.tracktor.ui.map.MainViewModel;
import com.elegion.tracktor.ui.results.ResultsViewModel;

import toothpick.config.Module;

public class ViewModelModule extends Module {

    public ViewModelModule(Fragment fragment) {
        bind(ViewModelStoreOwner.class).toInstance(fragment);
        bind(MainViewModel.class).toProvider( MainViewModelProvider.class);
        bind(ResultsViewModel.class).toProvider( ResultsViewModelProvider.class);
    }
}
