package com.demo.movies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Configuration(
    @SerializedName("images")
    val images: Images = Images(),
    @SerializedName("change_keys")
    val changeKeys: List<String> = listOf()
)

@Parcelize
data class Images(
    @SerializedName("poster_sizes")
    val posterSizes: List<String> = listOf(),
    @SerializedName("secure_base_url")
    val secureBaseUrl: String = "",
    @SerializedName("backdrop_sizes")
    val backdropSizes: List<String> = listOf(),
    @SerializedName("base_url")
    val baseUrl: String = "",
    @SerializedName("logo_sizes")
    val logoSizes: List<String> = listOf(),
    @SerializedName("still_sizes")
    val stillSizes: List<String> = listOf(),
    @SerializedName("profile_sizes")
    val profileSizes: List<String> = listOf()
): Parcelable