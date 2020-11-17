package com.demo.movies.ui.allmovies

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
import com.demo.movies.util.PrefsHelper
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_allmovies.*
import javax.inject.Inject


class AllMoviesActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: AllMoviesViewModel
    @Inject lateinit var prefsHelper: PrefsHelper

    private lateinit var adapterAll: AllMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allmovies)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.activeNetworkState.observe(
            this,
            { isActive ->
                run {
                    if (isActive) {
                        viewModel.fetchConfiguration()
                        viewModel.getMoviesNowShowing()
                    } else {
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

    private fun showBadGatewayError() = setErrorViewState(R.string.bad_gateway_message)
    private fun showInternalError() = setErrorViewState(R.string.internal_error_message)
    private fun showBadRequestError() = setErrorViewState(R.string.bad_request_message)
    private fun showResourceNotFoundError() = setErrorViewState(R.string.not_found_error_message)
    private fun showUnauthorisedError() = setErrorViewState(R.string.unauthorized_error_message)
    private fun showResourceMovedError() = setErrorViewState(R.string.resource_moved_error_message)
    private fun showForbiddenNetworkError() = setErrorViewState(R.string.resource_forbidden_error_message)

    private fun showError(message: String) {
        progressBar.gone()
        toast(message)
    }

    private fun setErrorViewState(@StringRes message: Int) {
        progressBar.gone()
        moviesList.gone()
        toast(String.format(getString(message)))
    }

    private fun showMovies(movies: MoviesResponse) {

        progressBar.gone()
        moviesList.visible()

        adapterAll = AllMoviesAdapter(
            prefsHelper = prefsHelper,
            movieListener = { partItem: Movie -> onMovieSelected(partItem) }
        )
        moviesList.adapter = adapterAll

        moviesList.layoutManager = GridLayoutManager(this, 4)

        adapterAll.populate(movies.results as ArrayList<Movie>)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
    }

    private fun showNoConnectionSnack(isNetworkAvailable: Boolean) {
        if (!isNetworkAvailable) {
            progressBar.gone()
            getString(R.string.check_connection_message).let { root.snack(it) }
        }
    }

    private fun onMovieSelected(movie : Movie) {
        Toast.makeText(this, "Selected: ${movie.id}", Toast.LENGTH_LONG).show()
        launchMovieDetails(movieId = movie.id.toString())
    }

    private fun launchMovieDetails(movieId: String){
        val intent = Intent(this, MovieDetailsActivity::class.java)
            .putExtra("MOVIE_ID", movieId)
        startActivity(intent)
    }
}