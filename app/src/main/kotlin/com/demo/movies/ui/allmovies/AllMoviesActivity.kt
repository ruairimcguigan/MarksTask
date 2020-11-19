package com.demo.movies.ui.allmovies

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.movies.R
import com.demo.movies.api.ApiResponse
import com.demo.movies.api.ApiResponse.HttpErrors
import com.demo.movies.ext.gone
import com.demo.movies.ext.snack
import com.demo.movies.ext.toast
import com.demo.movies.ext.visible
import com.demo.movies.models.Movie
import com.demo.movies.models.MoviesResponse
import com.demo.movies.ui.moviedetails.MovieDetailsActivity
import com.demo.movies.util.ImgPathBuilder
import com.demo.movies.util.PrefsHelper
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_allmovies.*
import javax.inject.Inject

class AllMoviesActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: AllMoviesViewModel
    @Inject lateinit var prefsHelper: PrefsHelper
    @Inject lateinit var pathBuilder: ImgPathBuilder

    private lateinit var adapterAll: AllMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allmovies)

        observeViewModel()
    }

    private fun observeViewModel() {

        lifecycle.addObserver(viewModel)

        viewModel.activeNetworkState.observe(
            this,
            { isActive ->
                run {
                    if (!isActive) {
                        showNoConnectionSnack(isActive)
                    }
                }
            }
        )
        viewModel.moviesState.observe(this, { response ->
            when (response) {
                is ApiResponse.Loading -> progressBar.visible()
                is ApiResponse.Success<*> -> showMovies(response.data as MoviesResponse)
                is ApiResponse.Error -> showError(response.error)

                is HttpErrors.Forbidden -> showForbiddenNetworkError()
                is HttpErrors.ResourceNotFound -> showResourceNotFoundError()
                is HttpErrors.Unauthorised -> showUnauthorisedError()
                is HttpErrors.BadRequest -> showBadRequestError()
                is HttpErrors.BadGateway -> showBadGatewayError()
                is HttpErrors.InternalError -> showInternalError()
                is HttpErrors.ResourceMoved -> showResourceMovedError()
            }
        })
    }

    private fun showBadGatewayError() = setErrorState(R.string.bad_gateway_message)
    private fun showInternalError() = setErrorState(R.string.internal_error_message)
    private fun showBadRequestError() = setErrorState(R.string.bad_request_message)
    private fun showResourceNotFoundError() = setErrorState(R.string.not_found_error_message)
    private fun showUnauthorisedError() = setErrorState(R.string.unauthorized_error_message)
    private fun showResourceMovedError() = setErrorState(R.string.resource_moved_error_message)
    private fun showForbiddenNetworkError() = setErrorState(R.string.resource_forbidden_error_message)

    private fun showError(message: String) {
        progressBar.gone()
        toast(message)
    }

    private fun setErrorState(@StringRes message: Int) {
        progressBar.gone()
        moviesList.gone()
        toast(String.format(getString(message)))
    }

    private fun showMovies(movies: MoviesResponse) {

        progressBar.gone()
        moviesList.visible()

        adapterAll = AllMoviesAdapter(
            prefsHelper = prefsHelper,
            movieListener = { movie: Movie -> launchMovieDetails(movie) },
            pathBuilder = pathBuilder
        )
        moviesList.adapter = adapterAll
        moviesList.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        adapterAll.populate(movies.results as ArrayList<Movie>)

    }

    private fun showNoConnectionSnack(isNetworkAvailable: Boolean) {
        if (!isNetworkAvailable) {
            progressBar.gone()
            getString(R.string.check_connection_message).let { root.snack(it) }
        }
    }

    private fun launchMovieDetails(movie: Movie){
        val intent = Intent(this, MovieDetailsActivity::class.java)
            .putExtra(MOVIE_PARCEL, movie)
        startActivity(intent)
    }

    companion object {
        const val SPAN_COUNT = 4
        const val MOVIE_PARCEL = "MOVIE"
    }
}