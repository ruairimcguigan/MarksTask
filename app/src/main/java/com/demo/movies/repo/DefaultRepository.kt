package com.demo.movies.repo

import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.Companion.BAD_GATEWAY
import com.demo.movies.api.ApiResponse.Companion.BAD_REQUEST
import com.demo.movies.api.ApiResponse.Companion.FORBIDDEN
import com.demo.movies.api.ApiResponse.Companion.INTERNAL_ERROR
import com.demo.movies.api.ApiResponse.Companion.MOVED
import com.demo.movies.api.ApiResponse.Companion.NOT_FOUND
import com.demo.movies.api.ApiResponse.Companion.UNAUTHORISED
import com.demo.movies.api.ApiResponse.HttpErrors
import com.demo.movies.api.ApiResponse.HttpErrors.InternalError
import com.demo.movies.api.MoviesService
import com.demo.movies.models.MoviesResponse
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val service: MoviesService,
    private val disposable: RxDisposable,
    private val schedulerProvider: DefaultSchedulerProvider
) : Repository {

    val paymentResponse = PublishSubject.create<ApiResponse>()

    override fun getNowShowing(apiKey: String): PublishSubject<ApiResponse> {

        disposable.add(
            service.getNowShowing(apiKey)
                .doFinally { disposeOnComplete() }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableSingleObserver<Response<MoviesResponse>>() {
                    override fun onSuccess(response: Response<MoviesResponse>) {

                        if (response.isSuccessful) {
                            paymentResponse.onNext(ApiResponse.Success(
                                onSuccessResponse(response))
                            )
                        } else {
                            when (response.code()) {
                                FORBIDDEN -> paymentResponse.onNext(
                                    HttpErrors.Forbidden(
                                        errorBody(response)
                                    )
                                )
                                NOT_FOUND -> paymentResponse.onNext(
                                    HttpErrors.ResourceNotFound(
                                        errorBody(response)
                                    )
                                )
                                UNAUTHORISED -> paymentResponse.onNext(
                                    HttpErrors.Unauthorised(
                                        errorBody(response)
                                    )
                                )
                                INTERNAL_ERROR -> paymentResponse.onNext(
                                    InternalError(errorBody(response))
                                )
                                BAD_REQUEST -> paymentResponse.onNext(
                                    HttpErrors.BadRequest(
                                        errorBody(response)
                                    )
                                )
                                BAD_GATEWAY -> paymentResponse.onNext(
                                    HttpErrors.BadGateway(
                                        errorBody(response)
                                    )
                                )
                                MOVED -> paymentResponse.onNext(
                                    HttpErrors.ResourceMoved(
                                        errorBody(response)
                                    )
                                )
                            }
                        }
                    }
                    override fun onError(e: Throwable) = Timber.e(
                        "on payment response error: %s", e.localizedMessage
                    )
                })
        )

        return paymentResponse
    }

    override fun getMovieForId(movieId: String): PublishSubject<ApiResponse> {
        TODO("Not yet implemented")
    }

    override fun getCollectionForId(collectionId: String): PublishSubject<ApiResponse> {
        TODO("Not yet implemented")
    }

    private fun onSuccessResponse(response: Response<MoviesResponse>) =
        response.body()!!

    private fun errorBody(response: Response<MoviesResponse>) =
        response.errorBody().toString()

    private fun disposeOnComplete(){
        disposable.dispose()
    }}