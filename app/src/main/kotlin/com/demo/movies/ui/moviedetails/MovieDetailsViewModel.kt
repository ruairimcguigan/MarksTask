package com.demo.movies.ui.moviedetails

import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.demo.movies.BuildConfig.API_KEY
import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.HttpErrors
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.threading.RxDisposable
import com.demo.movies.threading.SchedulerProvider
import com.demo.movies.ui.viewmodel.BaseViewModel
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val repo: Repository,
    private val schedulerProvider: SchedulerProvider,
    private val networkState: NetworkState,
    disposable: RxDisposable
) : BaseViewModel(
    disposable,
    networkState
) {
    internal val apiResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    internal val collectionDetails: MutableLiveData<Constituent> = MutableLiveData()

    internal fun fetchCollectionDetails(movie: Movie) {
        if (movie.belongsToCollection.parts.isNotEmpty()) {
            getCollection(movie.belongsToCollection.id.toString())
        }
    }

    private fun getCollection(collectionId: String) {
        if (networkState.isAvailable()) {

            disposable.add(
                repo.getCollectionForId(
                    apiKey = API_KEY,
                    collectionId = collectionId
                ).subscribeOn(schedulerProvider.io())
                    .subscribeWith(object : DisposableObserver<ApiResponse>() {

                        override fun onNext(collectionViewState: ApiResponse) {
                            when (collectionViewState) {
                                is ApiResponse.Success<*> -> {
                                    collectionDetails.postValue(
                                        collectionViewState.data as Constituent
                                    )
                                }
                                is ApiResponse.Error -> handleError(collectionViewState)

                                is HttpErrors.Unauthorised -> handleError(collectionViewState)
                                is HttpErrors.Forbidden -> handleError(collectionViewState)
                                is HttpErrors.BadRequest -> handleError(collectionViewState)
                                is HttpErrors.InternalError -> handleError(collectionViewState)
                                is HttpErrors.BadGateway -> handleError(collectionViewState)
                                is HttpErrors.ResourceMoved -> handleError(collectionViewState)
                                is HttpErrors.ResourceNotFound -> handleError(collectionViewState)
                            }
                        }

                        override fun onError(e: Throwable) = Timber.e(
                            "Error retrieving collection response: %s", e.localizedMessage
                        )

                        override fun onComplete() = disposable.dispose()
                    })
            )
        } else {
            Timber.i("Check network connection")
        }
    }

    private fun handleError(response: ApiResponse) = when (response) {
        is HttpErrors.Forbidden -> apiResponse.postValue(response)
        is HttpErrors.ResourceNotFound -> apiResponse.postValue(response)
        is HttpErrors.Unauthorised -> apiResponse.postValue(response)
        is HttpErrors.InternalError -> apiResponse.postValue(response)
        is HttpErrors.BadRequest -> apiResponse.postValue(response)
        is HttpErrors.BadGateway -> apiResponse.postValue(response)
        is HttpErrors.ResourceMoved -> apiResponse.postValue(response)
        else -> apiResponse.postValue(response)
    }

    @OnLifecycleEvent(ON_STOP)
    private fun onStop() {
        disposable.dispose()
    }
}