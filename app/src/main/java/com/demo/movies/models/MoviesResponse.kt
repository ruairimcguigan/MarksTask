package com.demo.movies.models

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<Movie>?,
    @SerializedName("total_results")
    val totalResults: Int
)