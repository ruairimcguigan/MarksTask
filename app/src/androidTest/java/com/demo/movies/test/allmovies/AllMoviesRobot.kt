package com.demo.movies.test.allmovies

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.annotation.IntegerRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.rule.ActivityTestRule
import com.demo.movies.robot.BaseRobot
import com.demo.movies.rules.DisableAnimationsRule
import com.demo.movies.ui.allmovies.AllMoviesActivity
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Rule

class AllMoviesRobot: BaseRobot() {

    @get:Rule
    val activityRule = ActivityTestRule(
        AllMoviesActivity::class.java,
        true,
        false
    )

    @Rule
    @JvmField
    val disableAnimationRule: DisableAnimationsRule = DisableAnimationsRule()

    @Rule
    @JvmField
    var intentsRule: IntentsTestRule<AllMoviesActivity> =
        IntentsTestRule(AllMoviesActivity::class.java)

    private val mockWebServer = MockWebServer()

    internal fun startServer() = mockWebServer.start(PORT)

    internal fun launchActivity() = activityRule.launchActivity(null)

    private val activityIntent = Intent(
        InstrumentationRegistry.getTargetContext(), AllMoviesActivity::class.java
    )

    internal fun getApiResponse(responseCode: Int, responseBody: String) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(responseBody)
            }
        }
    }

    internal fun verifyHasData(@IntegerRes moviesList: Int) = hasItems()

    internal fun verifyMoviesState(vararg views: Pair<Int, Visibility>) = verifyViewStatesVisibility(*views)

    internal fun verifyImagePathIsStoredToSharedPrefs(){
        val actual = context()
            .getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            .getString(IMAGE_PATH, null)
        assertEquals(POSTER_URL, actual)
    }

    internal fun stopServer() = mockWebServer.shutdown()

    companion object {
        const val PORT = 8080
        const val HTTP_OK = 200
        const val PREF_NAME = "prefs"
        const val IMAGE_PATH = "imageUrl"
        const val POSTER_URL = "https://image.tmdb.org/t/p/w300"
    }
}