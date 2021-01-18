package com.elegion.tracktor;

import android.app.Application;

import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Toothpick.setConfiguration(Configuration.forProduction());
        Toothpick.openScope(App.class)
                .installModules(new SmoothieApplicationModule(this));
    }
}
