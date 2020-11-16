package com.demo.movies.network

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {
    @Provides
    fun provideNetworkStateCheck(app: Application): NetworkState = DefaultNetworkState(app)
}