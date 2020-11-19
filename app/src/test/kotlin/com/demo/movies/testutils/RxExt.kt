package com.demo.movies.testutils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable

fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner): Flowable<T> =
    Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))