package com.demo.movies.robot

import android.app.Activity
import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.movies.util.PrefsHelper
import org.hamcrest.Matchers.*

open class BaseRobot {

    protected fun context(): Context = InstrumentationRegistry.getInstrumentation().targetContext

    fun typeTextCloseKeyboard(
        @StringRes viewId: Int,
        text: String
    ): ViewInteraction =
        onViewWithId(viewId).perform(
            ViewActions.replaceText(text),
            ViewActions.closeSoftKeyboard()
        )

    fun clickView(@StringRes viewId: Int): ViewInteraction = onViewWithId(viewId).perform(click())

    fun matchText(resId: Int, text: String): ViewInteraction =
        onView(withId(resId)).check(matches(withText(text)))

    fun snackBarMessage(
        @IntegerRes viewId: Int, errorValue: String
    ): ViewInteraction =
        onView(withId(viewId)).check(matches(withText(errorValue)))

    internal fun verifyViewStatesVisibility(
        vararg viewStates: Pair<Int, Visibility>
    ) {
        for (view in viewStates) {
            onView(withId(view.first))
                .check(matches(withEffectiveVisibility(view.second)))
        }
    }

    protected open fun verifyViewStatesVisibilityById(vararg viewStates: Pair<Int?, Visibility?>) {
        for (view in viewStates) {
            onViewWithId(view.first!!).check(
                matches(
                    withEffectiveVisibility(
                        view.second
                    )
                )
            )
        }
    }

    protected fun getResourceString(@StringRes id: Int): String {
        val targetContext: Context = androidx.test.InstrumentationRegistry.getTargetContext()
        return targetContext.resources.getString(id)
    }

    protected fun onViewWithId(resId: Int): ViewInteraction = onView(withId(resId))

    protected open fun onViewWithText(textValue: String?): ViewInteraction? {
        return onView(withText(textValue))
    }

    protected open fun verifyViewStatesVisibilityByText(view: Pair<String, Visibility>) {
        onViewWithText(view.first)!!.check(
            matches(
                withEffectiveVisibility(
                    view.second
                )
            )
        )
    }

    fun hasItems() = ViewAssertion { view, noViewFoundException ->
        if (view is RecyclerView) {
            assertThat(view.adapter?.itemCount, greaterThan(0))
        } else {
            throw noViewFoundException
        }
    }

    protected fun resetAppStateBetweenTests() {
        androidx.test.InstrumentationRegistry.getTargetContext()
            .getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(PrefsHelper.IMAGE_PATH, false)
            .apply();
    }

//    protected fun verifyTargetActivityLaunched() {
//        Intents.intended(IntentMatchers.hasComponent(MoviesActivity::class.java.name))
//    }

    protected fun verifyToast(activity: Activity, toastMessage: String) {
        onViewWithText(toastMessage)
            ?.inRoot(withDecorView(not(`is`(activity.window.decorView))))
            ?.check(matches(isDisplayed()))
    }

    internal fun closeKeyboard() = ViewActions.closeSoftKeyboard()
}
