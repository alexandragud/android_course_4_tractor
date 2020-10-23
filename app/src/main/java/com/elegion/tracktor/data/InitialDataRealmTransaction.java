package com.elegion.tracktor.data;

import com.elegion.tracktor.data.model.ActivityType;

import io.realm.Realm;

public class InitialDataRealmTransaction implements Realm.Transaction {
    private static final String[] ACTIVITIES_LIST = new String[]{"Шаг", "Бег", "Велосипед"};
    private static final Double[] ACTIVITIES_MET = new Double[]{4.3, 9.0, 7.5};

    @Override
    public void execute(Realm realm) {
        initActivityTypes(realm);
    }

    private void initActivityTypes(Realm realm) {
        for (int i = 0; i < ACTIVITIES_LIST.length; i++) {
            ActivityType newActivity = realm.createObject(ActivityType.class, i);
            newActivity.setName(ACTIVITIES_LIST[i]);
            newActivity.setMet(ACTIVITIES_MET[i]);
        }
    }
}
