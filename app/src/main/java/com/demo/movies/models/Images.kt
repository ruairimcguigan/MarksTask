package com.demo.movies.models

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("poster_sizes")
    val posterSizes: List<String>?,
    @SerializedName("secure_base_url")
    val secureBaseUrl: String = "",
    @SerializedName("backdrop_sizes")
    val backdropSizes: List<String>?,
    @SerializedName("base_url")
    val baseUrl: String = "",
    @SerializedName("logo_sizes")
    val logoSizes: List<String>?,
    @SerializedName("still_sizes")
    val stillSizes: List<String>?,
    @SerializedName("profile_sizes")
    val profileSizes: List<String>?
)