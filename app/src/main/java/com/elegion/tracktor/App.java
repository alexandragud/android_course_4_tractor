package com.elegion.tracktor;

import android.app.Application;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;

public class App extends Application {

    private static Scope sAppScope;

    @Override
    public void onCreate() {
        super.onCreate();
        Toothpick.setConfiguration(Configuration.forProduction());
    }

    public static Scope getAppScope() {
        return sAppScope;
    }
}
