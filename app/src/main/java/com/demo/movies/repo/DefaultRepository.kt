package com.demo.movies.repo

import android.app.Application
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
import com.demo.movies.util.PrefsHelper
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DefaultRepository @Inject constructor(
    private val application: Application,
    private val service: MoviesService,
    private val disposable: RxDisposable,
    private val schedulerProvider: DefaultSchedulerProvider,
    private val prefsHelper: PrefsHelper
) : Repository {

    val paymentResponse = PublishSubject.create<ApiResponse>()

    override fun getConfiguration(apiKey: String) {
        disposable.add(
            service.getConfiguration(apiKey)
                .doFinally { disposeOnComplete() }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableSingleObserver<Response<Configuration>>() {

                    override fun onSuccess(config: Response<Configuration>) {
                        if (config.isSuccessful){
                            storeConfiguration(config.body())
                        } else {
                            handleHttpErrorResponse(config)
                        }
                    }

                    override fun onError(e: Throwable) = Timber.e(
                        "on configuration response error: %s", e.localizedMessage
                    )

                }
        ))
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
                Forbidden(errorBody(response as Response<MoviesResponse>))
            )
            NOT_FOUND -> paymentResponse.onNext(
                ResourceNotFound(errorBody(response as Response<MoviesResponse>))
            )
            UNAUTHORISED -> paymentResponse.onNext(
                Unauthorised(errorBody(response as Response<MoviesResponse>))
            )
            INTERNAL_ERROR -> paymentResponse.onNext(
                InternalError(errorBody(response as Response<MoviesResponse>))
            )
            BAD_REQUEST -> paymentResponse.onNext(
                BadRequest(errorBody(response as Response<MoviesResponse>))
            )
            BAD_GATEWAY -> paymentResponse.onNext(
                BadGateway(errorBody(response as Response<MoviesResponse>))
            )
            MOVED -> paymentResponse.onNext(
                ResourceMoved(errorBody(response as Response<MoviesResponse>))
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

    private fun storeConfiguration(configuration: Configuration?){
        val baseUrl = configuration?.images?.secureBaseUrl
        val profile_size = configuration?.images?.stillSizes?.get(2)
        val imagePath = StringBuilder().append(baseUrl).append(profile_size).toString()


        prefsHelper.write(application, imagePath)
    }

    private fun disposeOnComplete(){
        disposable.dispose()
    }}