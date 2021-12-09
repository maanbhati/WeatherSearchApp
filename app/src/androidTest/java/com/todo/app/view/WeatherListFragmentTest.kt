package com.todo.app.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.todo.app.R
import com.todo.app.view.activity.WeatherSearchActivity
import com.todo.app.view.adapter.WeatherListAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WeatherListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_list_fragment_when_activity_launched_displayed_fragment() {
        mockActivityScenario()

        onView(withId(R.id.listFragment)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_list_fragment_when_launched_displayed_search_view() {
        mockActivityScenario()

        onView(withId(R.id.editSearch)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_list_fragment_when_launched_displayed_recycler_view() {
        mockActivityScenario()

        onView(withId(R.id.rv_list_items)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_list_fragment_when_searched_weather_displayed_progressBar() {
        mockActivityScenario()
        onView(withId(R.id.editSearch))
            .perform(clearText(), typeText("Wales"))

        mockThreadSleep(1000)

        onView(withId(R.id.progressBar)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_list_fragment_when_item_searched_displayed_current_date_text() {
        mockSearchQuery()

        onView(withId(R.id.current_date)).check(matches(not(withText(""))))
    }

    @Test
    fun test_list_fragment_when_item_searched_displayed_current_weather_text() {
        mockSearchQuery()

        onView(withId(R.id.current_weather)).check(matches(not(withText(""))))
    }

    @Test
    fun test_list_fragment_when_item_searched_displayed_current_temperature_text() {
        mockSearchQuery()

        onView(withId(R.id.current_temperature)).check(matches(not(withText(""))))
    }

    @Test
    fun test_list_fragment_when_item_clicked_displayed_current_date_text() {
        mockSearchQuery()

        onView(withId(R.id.rv_list_items)).perform(
            RecyclerViewActions.actionOnItemAtPosition<WeatherListAdapter.WeatherListViewHolder>(
                1,
                ViewActions.click()
            )
        )
        onView(withId(R.id.current_date)).check(matches(not(withText(""))))
    }

    private fun mockSearchQuery() {
        mockActivityScenario()
        onView(withId(R.id.editSearch))
            .perform(clearText(), typeText("London"))
        mockThreadSleep(2000)
    }

    private fun mockActivityScenario() {
        ActivityScenario.launch(WeatherSearchActivity::class.java)
        mockThreadSleep(1000)
    }

    private fun mockThreadSleep(milliSeconds: Long) {
        try {
            Thread.sleep(milliSeconds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}