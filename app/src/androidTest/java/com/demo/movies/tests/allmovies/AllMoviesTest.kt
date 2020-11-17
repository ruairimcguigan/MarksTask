package com.demo.movies.tests.allmovies

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import com.demo.movies.R
import com.demo.movies.R.id.moviesErrorView
import com.demo.movies.R.id.moviesList
import com.demo.movies.R.string.*
import com.demo.movies.api.OkHttpProvider
import com.demo.movies.robot.BaseTest
import com.demo.movies.testdata.TestData.BAD_GATEWAY
import com.demo.movies.testdata.TestData.BAD_REQUEST
import com.demo.movies.testdata.TestData.FORBIDDEN
import com.demo.movies.testdata.TestData.INTERNAL_ERROR
import com.demo.movies.testdata.TestData.MOCKED_RESPONSE
import com.demo.movies.testdata.TestData.MOVED
import com.demo.movies.testdata.TestData.NOT_FOUND
import com.demo.movies.testdata.TestData.OKHTTP
import com.demo.movies.testdata.TestData.UNAUTHORISED
import com.demo.movies.tests.allmovies.AllMoviesRobot.Companion.HTTP_OK
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
                    OKHTTP,
                    OkHttpProvider.getOkHttpClient()
                )
            )
        }
    }

    @Test
    fun shouldFetchAndStoreImagePath() {

        allMoviesRobot {
            // when
            launchActivity()

            // then
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
                responseBody = readJson(MOCKED_RESPONSE)
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
            getApiResponse(
                responseCode = MOVED,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(resource_moved_error_message))
        }
    }

    @Test
    fun testFailedUnauthorisedResponse() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = UNAUTHORISED,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(unauthorized_error_message))
        }
    }

    @Test
    fun testFailedResourceForbiddenResponse() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = FORBIDDEN,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(resource_forbidden_error_message))
        }
    }

    @Test
    fun testFailedNotFoundResponse() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = NOT_FOUND,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(not_found_error_message))
        }
    }

    @Test
    fun testBadRequestErrorResponse() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = BAD_REQUEST,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(bad_request_message))
        }
    }

    @Test
    fun testInternalErrorResponse() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = INTERNAL_ERROR,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(internal_error_message))
        }
    }

    @Test
    fun testBadResponseError() {

        allMoviesRobot {

            // given
            getApiResponse(
                responseCode = BAD_GATEWAY,
                responseBody = readJson(MOCKED_RESPONSE)
            )

            // when
            launchActivity()

            // then
            verifyHttpErrorToastIsDisplaying(getResourceString(bad_gateway_message))
        }
    }

    @After
    fun tearDown() {
        allMoviesRobot {
            stopServer()
        }
    }
}