package com.demo.movies.testrobot

import android.app.Activity
import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.movies.util.PrefsHelper
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not

open class TestRobot {

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

    protected fun resetAppStateBetweenTests() {
        androidx.test.InstrumentationRegistry.getTargetContext()
            .getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(PrefsHelper.HAS_SUBMITTED_CREDENTIALS, false)
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
