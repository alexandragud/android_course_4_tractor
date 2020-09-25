package com.elegion.tracktor.ui.results;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ResultsActivityTest {

    @Rule
    public ActivityScenarioRule<ResultsActivity> mActivityScenarioRule = new ActivityScenarioRule<>(ResultsActivity.class);

    private static IRepository<Track> mRepository = new RealmRepository();
    private static long trackId = -1;

    @BeforeClass
    public static void addResults(){
        if (mRepository.getAll().isEmpty()){
            Track track = new Track();
            track.setDate(Calendar.getInstance().getTime());
            track.setDistance(new Random().nextDouble());
            track.setDuration(new Random().nextLong());
            track.setImageBase64("");
            trackId = mRepository.insertItem(track);
        }
    }

    @Test
    public void checkListShown(){
        onView(withId(R.id.recycler)).check(matches(isDisplayed()));
    }

    @Test
    public void clickListItem(){
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
    }

    @AfterClass
    public static void deleteResults(){
        if (trackId>0)
            mRepository.deleteItem(trackId);
    }

}