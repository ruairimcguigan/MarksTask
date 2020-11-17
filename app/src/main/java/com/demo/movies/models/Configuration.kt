package com.demo.movies.models

import android.provider.MediaStore
import com.google.gson.annotations.SerializedName

data class Configuration(
    @SerializedName("images")
    val images: MediaStore.Images,
    @SerializedName("change_keys")
    val changeKeys: List<String>?
)