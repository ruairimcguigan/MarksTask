package com.demo.movies.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpProvider {

    private const val REQUEST_TIMEOUT = 3L

    private var okHttpClient: OkHttpClient? = null

    fun getOkHttpClient(): OkHttpClient = (if (okHttpClient == null) {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .build()
            OkHttpProvider.okHttpClient = okHttpClient
            okHttpClient
        } else {
        okHttpClient
        }) as OkHttpClient
}