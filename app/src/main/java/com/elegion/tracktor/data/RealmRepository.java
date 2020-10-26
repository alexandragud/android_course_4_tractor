package com.elegion.tracktor.data;

import com.elegion.tracktor.data.model.ActivityType;
import com.elegion.tracktor.data.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmRepository implements IRepository<Track> {

    private Realm mRealm;

    private static AtomicLong sPrimaryId;

    public RealmRepository() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .initialData(new InitialDataRealmTransaction())
                .build();

        mRealm = Realm.getInstance(config);
        Number max = mRealm.where(Track.class).max("mId");
        sPrimaryId = max == null ? new AtomicLong(0) : new AtomicLong(max.longValue());
    }

    @Override
    public Track getItem(long id) {
        Track track = getRealmAssociatedTrack(id);
        return track != null ? mRealm.copyFromRealm(track) : null;
    }

    private Track getRealmAssociatedTrack(long id) {
        return mRealm.where(Track.class).equalTo("mId", id).findFirst();
    }

    @Override
    public List<Track> getAll() {
        List<Track> res = mRealm.where(Track.class).findAll();
        return mRealm.copyFromRealm(res);
    }

    @Override
    public long insertItem(Track track) {
        track.setId(sPrimaryId.incrementAndGet());
        if (track.getActivityType() == null)
            track.setActivityType(getDefaultActivityType());
        mRealm.beginTransaction();
        mRealm.copyToRealm(track).setSpeed(calculateSpeed(track));
        mRealm.commitTransaction();
        return sPrimaryId.longValue();
    }

    private ActivityType getDefaultActivityType() {
        return mRealm.where(ActivityType.class).findFirst();
    }

    private double calculateSpeed(Track track) {
        if (track.getDuration() > 0)
            return track.getDistance() / track.getDuration();
        else
            return 0.0;
    }

    @Override
    public boolean deleteItem(final long id) {
        boolean isDeleteSuccessful;
        mRealm.beginTransaction();
        Track track = getRealmAssociatedTrack(id);
        if (track != null) {
            track.deleteFromRealm();
            isDeleteSuccessful = true;
        } else {
            isDeleteSuccessful = false;
        }
        mRealm.commitTransaction();
        return isDeleteSuccessful;
    }

    @Override
    public void updateItem(Track track) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(track);
        mRealm.commitTransaction();
    }

    public List<String> getActivitiesList() {
        List<ActivityType> activities = mRealm.where(ActivityType.class).findAll();
        List<String> activityNames = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++)
            activityNames.add(activities.get(i).getName());
        return activityNames;
    }

    public ActivityType getActivityType(int activityId) {
        return mRealm.where(ActivityType.class).equalTo("id", activityId).findFirst();
    }
}
