package com.demo.movies.ui.moviedetails

import android.os.Bundle
import com.demo.movies.R
import com.demo.movies.ext.toast
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MovieDetailsActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setSupportActionBar(findViewById(R.id.toolbar))

//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        val movieId = intent.getStringExtra("MOVIE_ID")
        if (movieId != null) {
            showMovieDetails(movieId)
        } else {
            toast("No movieId received, try again")
        }
    }

    // Receive movieId and trigger web call to get movie for id - (/movie/{movie_id})
    // On response,
    //      - check if the movie is part of a collection of movies
    //      - if yes -> show the other movies in the collection
    //      - on clicking on other movies, replace the current movie with the new movie - (/collection/{collection_id})


    private fun showMovieDetails(movieId: String) {

    }
}