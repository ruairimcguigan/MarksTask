package com.demo.movies.test.allmovies

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import com.demo.movies.R
import com.demo.movies.R.id.*
import com.demo.movies.api.OkHttpProvider
import com.demo.movies.robot.BaseTest
import com.demo.movies.test.allmovies.AllMoviesRobot.Companion.HTTP_OK
import com.demo.movies.util.ResponseReader.readJson
import com.jakewharton.espresso.OkHttp3IdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class AllMoviesTest: BaseTest() {

    private val allMoviesRobot = createRobotRunner(AllMoviesRobot::class)

    @Before
    fun setup() {
        allMoviesRobot {
            startServer()

            IdlingRegistry.getInstance().register(
                OkHttp3IdlingResource.create(
                    "okhttp",
                    OkHttpProvider.getOkHttpClient()
                )
            )
        }
    }

    @Test
    fun shouldFetchAndStoreImagePath() {
        allMoviesRobot {
            verifyImagePathIsStoredToSharedPrefs()
        }
    }

    @Test
    fun shouldFetchAndDisplayMovies() {
        allMoviesRobot {
            // given
            val moviesList = Pair(moviesList, VISIBLE)
            val errorView = Pair(moviesErrorView, GONE)

            // when
            launchActivity()

            getApiResponse(
                responseCode = HTTP_OK,
                responseBody = readJson("movies_response.json")
            )

            // then
            verifyMoviesState(moviesList, errorView)
            verifyHasData(R.id.moviesList)
        }
    }

    @Test
    fun testFailedIResourceMovedErrorResponse() {

        allMoviesRobot {

            // given
            val progressBar = Pair(progressBar, VISIBLE)
            val repoListView = Pair(moviesList, GONE)
            val noReposView = Pair(moviesErrorView, VISIBLE)

            // when
            launchActivity()

            getApiResponse(
                responseCode = 301,
                responseBody = ""
            )

            // then
            verifyMoviesState(
                progressBar,
                repoListView,
                noReposView
            )

            verifyCorrectErrorMessageShown(
                viewId = R.id.moviesErrorView,
                errorValue = "Requested resource has been changed permanently"
            )
        }
    }

    @After
    fun tearDown() {
        allMoviesRobot {
            stopServer()
        }
    }
}