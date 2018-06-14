package com.udacity.nd.projects.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    private static final String TAG = MainActivityInstrumentedTest.class.getSimpleName();

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        Log.d(TAG, "registerIdlingResource");
        mIdlingResource = activityActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickGridViewItem_OpensRecipeDetailsActivity() {
        Log.d(TAG, "clickGridViewItem_OpensRecipeDetailsActivity");
        Espresso.onView(ViewMatchers.withId(R.id.btn_fetch_recipes))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));

        Espresso.onView(ViewMatchers.withId(R.id.rv_steps))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        Log.d(TAG, "unregisterIdlingResource");
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
