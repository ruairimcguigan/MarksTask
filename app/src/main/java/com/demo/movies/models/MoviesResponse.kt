package com.demo.movies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MoviesResponse(
    @SerializedName("dates")
    var dates: Dates = Dates(),
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("results")
    var results: List<Movie>? = listOf(),
    @SerializedName("total_results")
    var totalResults: Int = 0
)

@Parcelize
data class Dates(
    @SerializedName("maximum")
    val maximum: String = "",
    @SerializedName("minimum")
    val minimum: String = ""
): Parcelable