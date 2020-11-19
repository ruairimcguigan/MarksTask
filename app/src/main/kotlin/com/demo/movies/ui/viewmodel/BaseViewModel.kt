package com.demo.movies.ui.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.movies.network.NetworkState
import com.demo.movies.threading.RxDisposable
import timber.log.Timber

abstract class BaseViewModel constructor(
    val disposable: RxDisposable,
    private val networkState: NetworkState
): ViewModel(), LifecycleObserver {

    private val _activeNetworkState: MutableLiveData<Boolean> = MutableLiveData()
    val activeNetworkState: LiveData<Boolean>
        get() = _activeNetworkState

    internal fun confirmNetworkState() {
        if (networkState.isAvailable()) {
            _activeNetworkState.value = true
        } else {
            _activeNetworkState.value = false
            Timber.i("No active network detected, check your connection")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}