package com.demo.movies.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Collection(
    @SerializedName("page")
    var page: Int = 0,
    @SerializedName("results")
    var constituents: List<Constituent> = listOf(),
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("total_results")
    var totalResults: Int = 0
)

@Parcelize
data class Constituent(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("parts")
    var parts: List<Part> = listOf()
): Parcelable

@Parcelize
data class Part(
    @SerializedName("adult")
    var adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("genre_ids")
    var genreIds: List<Int> = listOf(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("original_language")
    var originalLanguage: String = "",
    @SerializedName("original_title")
    var originalTitle: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("popularity")
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("video")
    var video: Boolean = false,
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0
): Parcelable