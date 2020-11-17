package com.demo.movies.test.allmovies

import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import com.demo.movies.R
import com.demo.movies.R.id.moviesErrorView
import com.demo.movies.R.id.moviesList
import com.demo.movies.robot.BaseTest
import com.demo.movies.test.allmovies.AllMoviesRobot.Companion.HTTP_OK
import com.demo.movies.util.ResponseReader.readJson
import org.junit.After
import org.junit.Before
import org.junit.Test

class AllMoviesTest: BaseTest() {

    private val allMoviesRobot = createRobotRunner(AllMoviesRobot::class)

    @Before
    override fun setup() {
        allMoviesRobot {
            startServer()
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

    @After
    fun tearDown() {
        allMoviesRobot {
            stopServer()
        }
    }
}