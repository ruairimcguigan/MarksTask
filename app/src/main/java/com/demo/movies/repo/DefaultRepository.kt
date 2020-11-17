package com.demo.movies.repo

import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.Companion.BAD_GATEWAY
import com.demo.movies.api.ApiResponse.Companion.BAD_REQUEST
import com.demo.movies.api.ApiResponse.Companion.FORBIDDEN
import com.demo.movies.api.ApiResponse.Companion.INTERNAL_ERROR
import com.demo.movies.api.ApiResponse.Companion.MOVED
import com.demo.movies.api.ApiResponse.Companion.NOT_FOUND
import com.demo.movies.api.ApiResponse.Companion.UNAUTHORISED
import com.demo.movies.api.ApiResponse.HttpErrors.*
import com.demo.movies.api.ApiResponse.Success
import com.demo.movies.api.MoviesService
import com.demo.movies.models.Configuration
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

    val configResponse = PublishSubject.create<ApiResponse>()
    val paymentResponse = PublishSubject.create<ApiResponse>()

    override fun getConfiguration(apiKey: String): PublishSubject<ApiResponse> {
        disposable.add(
            service.getConfiguration(apiKey)
                .doFinally { disposeOnComplete() }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableSingleObserver<Response<Configuration>>() {

                    override fun onSuccess(t: Response<Configuration>) {
                        if (t.isSuccessful){
                            configResponse.onNext(
                                Success(t))
                        } else {
                            handleHttpErrorResponse(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        TODO("Not yet implemented")
                    }

                }
        ))
        return configResponse
    }

    override fun getNowShowing(apiKey: String): PublishSubject<ApiResponse> {

        disposable.add(
            service.getNowShowing(apiKey)
                .doFinally { disposeOnComplete() }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableSingleObserver<Response<MoviesResponse>>() {
                    override fun onSuccess(response: Response<MoviesResponse>) {

                        if (response.isSuccessful) {
                            paymentResponse.onNext(Success(onSuccessResponse(response)))
                        } else {
                            handleHttpErrorResponse(response)
                        }
                    }
                    override fun onError(e: Throwable) = Timber.e(
                        "on payment response error: %s", e.localizedMessage
                    )
                })
        )

        return paymentResponse
    }

    private fun <T: Any>  handleHttpErrorResponse(response: Response<T>) {
        when (response.code()) {
            FORBIDDEN -> paymentResponse.onNext(
                Forbidden(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
            NOT_FOUND -> paymentResponse.onNext(
                ResourceNotFound(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
            UNAUTHORISED -> paymentResponse.onNext(
                Unauthorised(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
            INTERNAL_ERROR -> paymentResponse.onNext(
                InternalError(errorBody(response as Response<MoviesResponse>))
            )
            BAD_REQUEST -> paymentResponse.onNext(
                BadRequest(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
            BAD_GATEWAY -> paymentResponse.onNext(
                BadGateway(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
            MOVED -> paymentResponse.onNext(
                ResourceMoved(
                    errorBody(response as Response<MoviesResponse>)
                )
            )
        }
    }

    override fun getMovieForId(movieId: String): PublishSubject<ApiResponse> {
        TODO("Not yet implemented")
    }

    override fun getCollectionForId(collectionId: String): PublishSubject<ApiResponse> {
        TODO("Not yet implemented")
    }

    private fun onSuccessResponse(response: Response<MoviesResponse>) = response.body()!!

    private fun errorBody(response: Response<MoviesResponse>) =
        response.errorBody().toString()

    private fun disposeOnComplete(){
        disposable.dispose()
    }}