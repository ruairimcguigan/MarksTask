package com.demo.movies.moviedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.movies.api.ApiResponse
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.models.Part
import com.demo.movies.network.NetworkState
import com.demo.movies.repo.Repository
import com.demo.movies.testutils.RxSchedulerRule
import com.demo.movies.testutils.TestValues.API_KEY
import com.demo.movies.testutils.TestValues.COLLECTION_ID
import com.demo.movies.threading.DefaultSchedulerProvider
import com.demo.movies.threading.RxDisposable
import com.demo.movies.threading.SchedulerProvider
import com.demo.movies.ui.moviedetails.MovieDetailsViewModel
import io.reactivex.subjects.PublishSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class MovieDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxSchedulerRule = RxSchedulerRule()

    @Mock private lateinit var repo: Repository
    @Mock private lateinit var disposable: RxDisposable
    @Mock private lateinit var networkState: NetworkState

    private lateinit var defaultSchedulerProvider: SchedulerProvider
    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        initMocks(this)

        defaultSchedulerProvider = DefaultSchedulerProvider()

        viewModel = MovieDetailsViewModel(
            repo = repo,
            disposable = disposable,
            networkState = networkState,
            schedulerProvider = defaultSchedulerProvider
        )
    }

    @Test
    fun onNetworkStateAvailable_setNetworkObservableValueTrue() {

        `when`(networkState.isAvailable()).thenReturn(true)
        viewModel.confirmNetworkState()
        viewModel.activeNetworkState.value?.let { Assert.assertTrue(it) }
    }

    @Test
    fun onNetworkStateNotAvailable_setNetworkObservableValueFalse() {

        `when`(networkState.isAvailable()).thenReturn(false)
        viewModel.confirmNetworkState()
        viewModel.activeNetworkState.value?.let { Assert.assertFalse(it) }
    }

    @Test
    fun onNetworkStateNotAvailable_ApiNotCalled() {

        // given
        `when`(networkState.isAvailable()).thenReturn(false)
        val movie = Movie(belongsToCollection = Constituent(parts = listOf(Part(), Part())))

        // when
        viewModel.fetchCollectionDetails(movie)
        // then
        verify(repo, never()).getCollectionForId(API_KEY, COLLECTION_ID)
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
            repo.getCollectionForId(
                apiKey = "3e817577f0f7d61c25e79d170c7e423e",
                collectionId = "123"
            )
        ).thenReturn(create)

        // when
        viewModel.fetchCollectionDetails(movie)

        // then
        verify(repo).getCollectionForId(apiKey = API_KEY, collectionId = COLLECTION_ID)
    }

    @Test
    fun onNoConstituentParts_ApiNotCalled() {

        // given
        `when`(networkState.isAvailable()).thenReturn(true)
        val constituent = Constituent(id = 123)
        val movie = Movie(id = 123, belongsToCollection = constituent)
        val create = PublishSubject.create<ApiResponse>()
        create.onNext(ApiResponse.Success(constituent))
        `when`(
            repo.getCollectionForId(
                apiKey = "3e817577f0f7d61c25e79d170c7e423e",
                collectionId = "123"
            )
        ).thenReturn(create)

        // when
        viewModel.fetchCollectionDetails(movie)

        // then
        verify(repo, never()).getCollectionForId(apiKey = API_KEY, collectionId = COLLECTION_ID)
    }
}