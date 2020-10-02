package com.elegion.tracktor;

import android.app.Application;

import com.elegion.tracktor.di.AppModule;

import io.realm.Realm;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Toothpick.setConfiguration(Configuration.forProduction());
        Toothpick.openScope(App.class)
                .installModules(new SmoothieApplicationModule(this), new AppModule(this));
    }
}
