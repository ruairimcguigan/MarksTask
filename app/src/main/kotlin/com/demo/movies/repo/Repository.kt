package com.demo.movies.repo

import com.demo.movies.api.ApiResponse
import io.reactivex.subjects.PublishSubject

interface Repository {

    fun getConfiguration(apiKey: String)

    fun getNowShowing(
        apiKey: String
    ): PublishSubject<ApiResponse>

    fun getCollectionForId(
        apiKey: String,
        collectionId: String
    ): PublishSubject<ApiResponse>
}