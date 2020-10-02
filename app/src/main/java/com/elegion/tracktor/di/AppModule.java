package com.elegion.tracktor.di;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.util.CustomViewModelFactory;

import toothpick.config.Module;

public class AppModule extends Module {

    public AppModule(App app) {
        bind(App.class).toInstance(app);
        RealmRepository realmRepository = new RealmRepository();
        bind(IRepository.class).toInstance(realmRepository);
        bind(RealmRepository.class).toInstance(realmRepository);
        bind(CustomViewModelFactory.class).singleton();
    }
}
