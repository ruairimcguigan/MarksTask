package com.demo.movies.test.moviedetail

import com.demo.movies.robot.BaseTest
import com.demo.movies.test.allmovies.AllMoviesRobot

class MovieDetailsTest: BaseTest() {

    private val movieDetailsRobot = createRobotRunner(MovieDetailsRobot::class)
}