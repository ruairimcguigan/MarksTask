package com.demo.movies.test.moviedetail

import androidx.test.rule.ActivityTestRule
import com.demo.movies.robot.BaseRobot
import com.demo.movies.ui.moviedetails.MovieDetailsActivity
import org.junit.Rule

class MovieDetailsRobot: BaseRobot() {

    @get:Rule
    val activityRule = ActivityTestRule(
        MovieDetailsActivity::class.java,
        true,
        false
    )

}
