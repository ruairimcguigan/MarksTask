package com.demo.movies.ui.allmovies

import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.demo.movies.BuildConfig.API_KEY
import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.Error
import com.demo.movies.api.ApiResponse.HttpErrors.*
import com.demo.movies.api.ApiResponse.Success
import com.demo.movies.models.MoviesResponse
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.threading.RxDisposable
import com.demo.movies.threading.SchedulerProvider
import com.demo.movies.ui.viewmodel.BaseViewModel
import com.demo.movies.util.PrefsHelper
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class AllMoviesViewModel@Inject constructor(
    private val repo: Repository,
    private val schedulerProvider: SchedulerProvider,
    private val networkState: NetworkState,
    private val prefsHelper: PrefsHelper,
    disposable: RxDisposable
) : BaseViewModel(
    disposable,
    networkState
) {
    private val _moviesState: MutableLiveData<ApiResponse> = MutableLiveData()
    val moviesState: LiveData<ApiResponse>
        get() = _moviesState

    init {
        confirmNetworkState()
    }

    /**
     * Temp measure to store image base path on first-time app start. On subsequent app starts,
     * this will be updated with live configuration data. To implement this trait of the api fully
     * would need to be driven by the requirements in a production setting
     * - https://developers.themoviedb.org/3/getting-started/images
     */
    @OnLifecycleEvent(ON_CREATE)
    private fun onFirstLoadStoreImgPath() = prefsHelper.write(IMG_PATH)
    private fun fetchConfiguration() = repo.getConfiguration(API_KEY)

    @OnLifecycleEvent(ON_START)
    internal fun getMoviesNowShowing() = if (networkState.isAvailable()) {
            disposable.add(
                repo.getNowShowing(API_KEY)
                    .doOnSubscribe { _moviesState.postValue(ApiResponse.Loading) }
                    .subscribeOn(schedulerProvider.io())
                    .subscribeWith(object : DisposableObserver<ApiResponse>() {

                        override fun onNext(viewState: ApiResponse) {

                            when (viewState) {
                                is Success<*> -> onRetrieveMoviesSuccess(
                                    viewState.data as MoviesResponse
                                )
                                is Error -> handleError(viewState)

                                is Unauthorised -> handleError(viewState)
                                is Forbidden -> handleError(viewState)
                                is BadRequest -> handleError(viewState)
                                is InternalError -> handleError(viewState)
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
            Timber.i("Check network connection")
        }

    private fun onRetrieveMoviesSuccess(response: MoviesResponse) {
        _moviesState.postValue(Success(response))
    }

    private fun handleError(response: ApiResponse) = when (response) {
        is Forbidden -> _moviesState.postValue(response)
        is ResourceNotFound -> _moviesState.postValue(response)
        is Unauthorised -> _moviesState.postValue(response)
        is InternalError -> _moviesState.postValue(response)
        is BadRequest -> _moviesState.postValue(response)
        is BadGateway -> _moviesState.postValue(response)
        is ResourceMoved -> _moviesState.postValue(response)
        else -> _moviesState.postValue(response)
    }

    @OnLifecycleEvent(ON_STOP)
    private fun onStop() {
        disposable.dispose()
    }

    companion object {
        const val IMG_PATH = "https://image.tmdb.org/t/p/w300"
    }
}