package com.demo.movies

class TestMoviesApp: MoviesApp() {
    override fun getBaseUrl(): String = "http://127.0.0.1:8080"

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object {
        private var sInstance: TestMoviesApp? = null

        @Synchronized
        fun getInstance(): TestMoviesApp {
            return sInstance!!
        }
    }
}