package com.demo.movies.repository

import android.app.Application
import com.demo.movies.api.MoviesService
import com.demo.movies.models.MoviesResponse
import com.demo.movies.repo.DefaultRepository
import com.demo.movies.repo.Repository
import com.demo.movies.testutils.TestSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.util.PrefsHelper
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Response

class RepositoryTest {

    @Mock lateinit var service: MoviesService
    @Mock lateinit var rxDisposable: RxDisposable
    @Mock lateinit var application: Application
    @Mock lateinit var prefsHelper: PrefsHelper

    private lateinit var repository: Repository

    private var schedulerProvider = TestSchedulerProvider()

    @Before
    fun setUp() {
        initMocks(this)

        repository = DefaultRepository(
            application = application,
            service = service,
            disposable = rxDisposable,
            schedulerProvider = schedulerProvider,
            prefsHelper = prefsHelper
        )
    }

    @Test
    fun onSuccessResponse() {

        //given
        val okResponse = Response.success(201, MoviesResponse())
        val expected = (repository as DefaultRepository).allMoviesResponse

        `when`(service.getNowShowing(apiKey = "abc")).thenReturn(Single.just(okResponse))

        //when
        val actual = repository.getNowShowing(apiKey = "abc")

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun onBadRequestResponse() {

        //given
        val aResponse: Response<MoviesResponse> = Response.error(
            400,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"key\":[\"somestuff\"]}"
            )
        )

        val expected = (repository as DefaultRepository).allMoviesResponse

        `when`(service.getNowShowing(apiKey = "abc")).thenReturn(Single.just(aResponse))
        //when
        val actual = repository.getNowShowing(apiKey = "abc")

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun onUnauthorisedResponse() {

        //given
        val aResponse: Response<MoviesResponse> = Response.error(
            401,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"key\":[\"somestuff\"]}"
            )
        )

        val expected = (repository as DefaultRepository).allMoviesResponse
        `when`(service.getNowShowing(apiKey = "abc")).thenReturn(Single.just(aResponse))

        //when
        val actual = repository.getNowShowing(apiKey = "abc")

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun onForbiddenResponse() {

        //given
        val aResponse: Response<MoviesResponse> = Response.error(
            403,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"key\":[\"somestuff\"]}"
            )
        )

        val expected = (repository as DefaultRepository).allMoviesResponse
        `when`(service.getNowShowing(apiKey = "abc")).thenReturn(Single.just(aResponse))

        //when
        val actual = repository.getNowShowing(apiKey = "abc")

        //then
        assertEquals(expected, actual)
    }

    @Test
    fun onNotFoundResponse() {

        //given
        val aResponse: Response<MoviesResponse> = Response.error(
            404,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"key\":[\"somestuff\"]}"
            )
        )

        val expected = (repository as DefaultRepository).allMoviesResponse
        `when`(service.getNowShowing(apiKey = "abc")).thenReturn(Single.just(aResponse))

        //when
        val actual = repository.getNowShowing(apiKey = "abc")

        //then
        assertEquals(expected, actual)
    }
}