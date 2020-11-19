package com.demo.movies.allmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.movies.BuildConfig.API_KEY
import com.demo.movies.api.ApiResponse
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.models.Part
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.testutils.RxSchedulerRule
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.threading.SchedulerProvider
import com.demo.movies.ui.allmovies.AllMoviesViewModel
import com.demo.movies.util.PrefsHelper
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks

class AllMoviesViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxSchedulerRule = RxSchedulerRule()

    @Mock private lateinit var repo: Repository
    @Mock private lateinit var disposable: RxDisposable
    @Mock private lateinit var networkState: NetworkState
    @Mock private lateinit var prefsHelper: PrefsHelper

    private lateinit var defaultSchedulerProvider: SchedulerProvider
    private lateinit var viewModel: AllMoviesViewModel

    @Before
    fun setUp() {
        initMocks(this)

        defaultSchedulerProvider = DefaultSchedulerProvider()

        viewModel = AllMoviesViewModel(
            repo = repo,
            disposable = disposable,
            networkState = networkState,
            schedulerProvider = defaultSchedulerProvider,
            prefsHelper = prefsHelper
        )
    }

    @Test
    fun onNetworkStateAvailable_setNetworkObservableValueTrue() {

        `when`(networkState.isAvailable()).thenReturn(true)
        viewModel.confirmNetworkState()
        viewModel.activeNetworkState.value?.let { assertTrue(it) }
    }

    @Test
    fun onNetworkStateNotAvailable_setNetworkObservableValueFalse() {

        `when`(networkState.isAvailable()).thenReturn(false)
        viewModel.confirmNetworkState()
        viewModel.activeNetworkState.value?.let { assertFalse(it) }
    }

    @Test
    fun onNetworkStateNotAvailable_ApiNotCalled() {

        // given
        `when`(networkState.isAvailable()).thenReturn(false)
        val movie = Movie(belongsToCollection = Constituent(parts = listOf(Part(), Part())))

        // when
        viewModel.getMoviesNowShowing()
        // then
        verify(repo, never()).getNowShowing(API_KEY)
    }

    @Test
    fun onNetworkStateAvailable_ApiCalled() {

        // given
        `when`(networkState.isAvailable()).thenReturn(true)
        val constituent = Constituent(id = 123, parts = listOf(Part(), Part()))
        val movie = Movie(id = 123, belongsToCollection = constituent)
        val create = PublishSubject.create<ApiResponse>()
        create.onNext(ApiResponse.Success(constituent))
        `when`(
            repo.getNowShowing(API_KEY)).thenReturn(create)

        // when
        viewModel.getMoviesNowShowing()

        // then
        verify(repo).getNowShowing(API_KEY)
    }
}