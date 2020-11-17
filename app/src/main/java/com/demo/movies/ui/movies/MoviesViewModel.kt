package com.demo.movies.ui.movies

import androidx.lifecycle.MutableLiveData
import com.demo.movies.BuildConfig.API_KEY
import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.*
import com.demo.movies.api.ApiResponse.HttpErrors.*
import com.demo.movies.models.MoviesResponse
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.ui.viewmodel.BaseViewModel
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class MoviesViewModel@Inject constructor(
    private val repo: Repository,
    private val schedulerProvider: DefaultSchedulerProvider,
    private val networkState: NetworkState,
    disposable: RxDisposable
) : BaseViewModel(
    disposable,
    networkState
) {
    internal val moviesState: MutableLiveData<ApiResponse> = MutableLiveData()

    init {
        getMoviesNowShowing()
    }

    private fun getMoviesNowShowing() {

        if (networkState.isAvailable()) {
            disposable.add(
                repo.getNowShowing(API_KEY)
                    .doOnSubscribe { moviesState.postValue(Loading) }
                    .subscribeOn(schedulerProvider.io())
                    .subscribeWith(object : DisposableObserver<ApiResponse>() {

                        override fun onNext(viewState: ApiResponse) {
                            when (viewState) {

                                is Success -> onRetrieveMoviesSuccess(viewState.data as MoviesResponse)
                                is Error -> handleError(viewState)

                                is Unauthorised -> handleError(viewState)
                                is Forbidden -> handleError(viewState)
                                is BadRequest -> handleError(viewState)
                                is java.lang.InternalError -> handleError(viewState)
                                is BadGateway -> handleError(viewState)
                                is ResourceMoved -> handleError(viewState)
                                is ResourceNotFound -> handleError(viewState)
                            }
                        }

                        override fun onError(e: Throwable) {
                            Timber.e("Error retrieving response: %s", e.localizedMessage)
                        }

                        override fun onComplete() {
                            disposable.dispose()
                        }
                    }
                    ))
        } else {
            _activeNetworkState.value = false
        }
    }

    private fun onRetrieveMoviesSuccess(response: MoviesResponse) {
        moviesState.postValue(response.let { Success(it) })
    }

    private fun handleError(response: ApiResponse) = when (response) {
        is Forbidden -> moviesState.postValue(response)
        is ResourceNotFound -> moviesState.postValue(response)
        is Unauthorised -> moviesState.postValue(response)
        is InternalError -> moviesState.postValue(response)
        is BadRequest -> moviesState.postValue(response)
        is BadGateway -> moviesState.postValue(response)
        is ResourceMoved -> moviesState.postValue(response)
        else -> moviesState.postValue(response)
    }
}