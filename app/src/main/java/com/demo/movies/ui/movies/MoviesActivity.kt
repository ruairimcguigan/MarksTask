package com.demo.movies.ui.movies

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
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
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import javax.inject.Inject


class MoviesActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: MoviesViewModel

    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.activeNetworkState.observe(
            this,
            Observer { isActive ->
                run {
                    if (!isActive) {
                        showNoConnectionSnack(isActive)
                    }
                }
            }
        )
        viewModel.moviesState.observe(this, Observer { response ->
            when (response) {
                is ApiResponse.Loading -> progressBar.visible()
                is ApiResponse.Success -> showMovies(response.data)
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
        toast(String.format(getString(message)))
    }

    private fun showMovies(movies: MoviesResponse) {

        progressBar.gone()
        moviesList.visible()

        adapter = MoviesAdapter()
        val calculateNoOfColumns = calculateNoOfColumns(this, 110f)
        moviesList.layoutManager = GridLayoutManager(this, calculateNoOfColumns)

        adapter.populate(movies.results as ArrayList<Movie>)
        moviesList.adapter = adapter
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
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
}