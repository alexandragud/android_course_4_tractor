package com.elegion.tracktor.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.elegion.tracktor.R;
import com.elegion.tracktor.ui.map.MainActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddAndDeleteTrackTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void checkAddAndDeleteTrack() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.buttonStart)).perform(click());
        Thread.sleep(15000);
        onView(withId(R.id.buttonStop)).perform(click());

        onView(withId(R.id.tvDate)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDistance)).check(matches(isDisplayed()));
        onView(withId(R.id.tvSpeed)).check(matches(isDisplayed()));

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        selectMenu(R.string.share);
        Intents.intended(IntentMatchers.hasExtra(Intent.EXTRA_TITLE, "Результаты маршрута"));

        pressBack();
        selectMenu(R.string.statistic);
        int countOfTracks = getItemsCount(R.id.recycler);
        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        selectMenu(R.string.delete);
        onView(Matchers.anyOf(withChild(withId(R.id.recycler)), withChild(withId(R.id.emptyResults))))
                .check(matches(isDisplayed()));
        assertThat(getItemsCount(R.id.recycler), Matchers.is(countOfTracks-1));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    private void selectMenu(int menuTextId) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(menuTextId)).perform(click());
    }

    private int getItemsCount(int recyclerId) {
        final int[] result = {0};
        ViewAssertion assertion = (view, noViewFoundException) -> {
                    if (view!=null) {
                        result[0] = ((RecyclerView) view).getAdapter().getItemCount();
                    } else{
                        result[0] = 0;
                    }
                };

        onView(Matchers.allOf(withId(recyclerId), isDisplayed())).check(assertion);
        return result[0];
    }


}
