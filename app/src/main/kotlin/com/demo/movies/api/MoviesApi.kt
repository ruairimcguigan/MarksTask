package com.demo.movies.api

import com.demo.movies.models.Configuration
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.models.MoviesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    // https://api.themoviedb.org/3/configuration?api_key=<<api_key>>
    @GET("/3/configuration")
    fun getConfiguration(
        @Query("api_key") apiKey: String
    ): Single<Response<Configuration>>

    // https://api.themoviedb.org/3/movie/now_playing?api_key=<<api_key>>&language=en-US&page=1
    @GET("/3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String
    ): Single<Response<MoviesResponse>>

    // https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US
    @GET("/3/movie/{movie_id}")
    fun getMovieForId(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Single<Response<Movie>>

    // https://api.themoviedb.org/3/collection/10?api_key=3e817577f0f7d61c25e79d170c7e423e
    @GET("/3/collection/{collection_id}")
    fun getCollectionForId(
        @Path("collection_id") collectionId: String,
        @Query("api_key") apiKey: String
    ): Single<Response<Constituent>>
}