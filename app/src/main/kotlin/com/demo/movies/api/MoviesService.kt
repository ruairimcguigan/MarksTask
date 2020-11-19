package com.demo.movies.api

import com.demo.movies.models.Configuration
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.models.MoviesResponse
import io.reactivex.Single
import retrofit2.Response

class MoviesService(private val moviesApi: MoviesApi) {

    fun getConfiguration(apiKey: String
    ): Single<Response<Configuration>> = moviesApi.getConfiguration(apiKey)

    fun getNowShowing(apiKey: String
    ): Single<Response<MoviesResponse>> = moviesApi.getNowPlaying(apiKey)

    fun getMovieForId(
        apiKey: String,
        movieId: String
    ): Single<Response<Movie>> = moviesApi.getMovieForId(
        apiKey = apiKey,
        movieId = movieId
    )

    fun getCollectionForId(
        apiKey: String,
        collectionId: String
    ): Single<Response<Constituent>> = moviesApi.getCollectionForId(
        apiKey = apiKey,
        collectionId = collectionId
    )
}