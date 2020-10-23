package com.elegion.tracktor.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ActivityType extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private double met;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMet() {
        return met;
    }

    public void setMet(double met) {
        this.met = met;
    }
}
