package com.demo.movies.ui.moviedetails

import androidx.lifecycle.MutableLiveData
import com.demo.movies.BuildConfig.API_KEY
import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.HttpErrors
import com.demo.movies.api.ApiResponse.Loading
import com.demo.movies.models.Movie
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.ui.viewmodel.BaseViewModel
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val repo: Repository,
    private val schedulerProvider: DefaultSchedulerProvider,
    private val networkState: NetworkState,
    disposable: RxDisposable
) : BaseViewModel(
    disposable,
    networkState
) {
    internal val moviesDetails: MutableLiveData<ApiResponse> = MutableLiveData()

    internal fun getMovieForId(movieId: String): MutableLiveData<ApiResponse> {

        if (networkState.isAvailable()) {
            disposable.add(
                repo.getMovieForId(
                    apiKey = API_KEY,
                    movieId = movieId
                ).doOnSubscribe { moviesDetails.postValue(Loading) }
                    .subscribeOn(schedulerProvider.io())
                    .subscribeWith(object : DisposableObserver<ApiResponse>() {

                        override fun onNext(viewState: ApiResponse) {

                            when (viewState) {
                                is ApiResponse.Success<*> -> onMovieDetailsSuccess(viewState.data as Movie)
                                is ApiResponse.Error -> handleError(viewState)

                                is HttpErrors.Unauthorised -> handleError(viewState)
                                is HttpErrors.Forbidden -> handleError(viewState)
                                is HttpErrors.BadRequest -> handleError(viewState)
                                is HttpErrors.InternalError -> handleError(viewState)
                                is HttpErrors.BadGateway -> handleError(viewState)
                                is HttpErrors.ResourceMoved -> handleError(viewState)
                                is HttpErrors.ResourceNotFound -> handleError(viewState)
                            }
                        }

                        override fun onError(e: Throwable) {
                            Timber.e("Error retrieving response: %s", e.localizedMessage)
                        }

                        override fun onComplete() = disposable.dispose()
                    }
                    ))
        } else {
            _activeNetworkState.value = false
        }

        return moviesDetails
    }

    private fun onMovieDetailsSuccess(movies: Movie) {
        TODO("Not yet implemented")
    }

    private fun handleError(viewState: ApiResponse) {
        TODO("Not yet implemented")
    }
}