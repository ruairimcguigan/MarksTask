package com.demo.movies.api

import com.demo.movies.models.MoviesResponse
import io.reactivex.Single
import retrofit2.Response

class MoviesService(private val moviesApi: MoviesApi) {

    fun getNowShowing(apiKey: String
    ): Single<Response<MoviesResponse>> = moviesApi.getNowPlaying(apiKey)

    fun getMovieForId(
        apiKey: String,
        movieId: String
    ): Single<Response<MoviesResponse>> = moviesApi.getMovieForId(
        apiKey = apiKey,
        movieId = movieId
    )

    fun getCollectionForId(
        apiKey: String,
        collectionId: String
    ): Single<Response<MoviesResponse>> = moviesApi.getCollectionForId(
        apiKey = apiKey,
        collectionId = collectionId
    )
}