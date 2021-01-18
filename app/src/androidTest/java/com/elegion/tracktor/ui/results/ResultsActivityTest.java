package com.elegion.tracktor.ui.results;

import android.app.Instrumentation;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.model.Track;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ResultsActivityTest {

    @Rule
    public ActivityScenarioRule<ResultsActivity> mActivityScenarioRule = new ActivityScenarioRule<>(ResultsActivity.class);

    @Inject
    IRepository mRepository;

    @Before
    public void setUp() throws Exception {
        Scope scope = Toothpick.openScopes(App.class, ResultsActivityTest.class);
        Toothpick.inject(this, scope);
        Track track = new Track();
        track.setDate(Calendar.getInstance().getTime());
        track.setDistance(123.1);
        track.setDuration(1000L);
        track.setImageBase64("");

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            if (mRepository.getAll().isEmpty())
                mRepository.insertItem(track);
        });
        mActivityScenarioRule.getScenario().recreate();
    }

    @Test
    public void checkListShown() {
        onView(withId(R.id.recycler)).check(matches(isDisplayed()));
    }

    @Test
    public void clickListItem() {
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Toothpick.reset();
    }
}