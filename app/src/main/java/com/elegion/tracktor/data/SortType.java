package com.elegion.tracktor.data;

public enum SortType {

    BY_DATE("mDate"),
    BY_DURATION("mDuration"),
    BY_DISTANCE("mDistance");


    private String fieldName;

    SortType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
