package com.demo.movies.ui.moviedetails

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.movies.R
import com.demo.movies.api.ApiResponse
import com.demo.movies.ext.gone
import com.demo.movies.ext.toast
import com.demo.movies.ext.visible
import com.demo.movies.models.Constituent
import com.demo.movies.models.Movie
import com.demo.movies.models.Part
import com.demo.movies.util.ImgPathBuilder
import com.demo.movies.util.PrefsHelper
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_allmovies.*
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.movie_detail_content.*
import javax.inject.Inject

class MovieDetailsActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: MovieDetailsViewModel
    @Inject lateinit var pathBuilder: ImgPathBuilder
    @Inject lateinit var prefsHelper: PrefsHelper

    private lateinit var collectionAdapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        initViews()
        showMoviesDetail()
        observeViewModel()
    }

    private fun showMoviesDetail() {
        val movie = intent.getParcelableExtra<Movie>(MOVIE_KEY)

        showMovieDetails(movie)
        observeViewModel()
        checkForCollections(movie)
    }

    private fun checkForCollections(movie: Movie) {
        viewModel.fetchCollectionDetails(movie)
    }

    private fun initViews() {
        collectionAdapter = CollectionAdapter(
            prefsHelper = prefsHelper,
            collectionListener = { constituent: Part -> viewSelectMovieFromCollection(constituent) },
            pathBuilder = pathBuilder
        )
        collectionsList.adapter = collectionAdapter
        collectionsList.layoutManager = GridLayoutManager(
            this,
            SPAN_COUNT
        )
    }

    private fun observeViewModel(){
        viewModel.collectionDetails.observe(
            this, {
                collectionTitle.visible()
                showCollection(it)
            }
        )
        viewModel.apiResponse.observe(this, { response ->
            when (response) {
                is ApiResponse.HttpErrors.Forbidden -> showForbiddenNetworkError()
                is ApiResponse.HttpErrors.ResourceNotFound -> showResourceNotFoundError()
                is ApiResponse.HttpErrors.Unauthorised -> showUnauthorisedError()
                is ApiResponse.HttpErrors.BadRequest -> showBadRequestError()
                is ApiResponse.HttpErrors.BadGateway -> showBadGatewayError()
                is ApiResponse.HttpErrors.InternalError -> showInternalError()
                is ApiResponse.HttpErrors.ResourceMoved -> showResourceMovedError()
            }
        })
    }

    private fun showMovieDetails(movie: Movie) {

        setMovieTitle(movie.originalTitle)
        setMovieDescription(movie.overview)

        Picasso.with(this)
            .load(pathBuilder.buildPosterPath(movie.posterPath))
            .into(movieBackdrop)
    }

    private fun setMovieTitle(movieTitle: String) {
        collapsing.title = movieTitle
        collapsing.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
        collapsing.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1)
        collapsing.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1)
    }

    private fun setMovieDescription(overview: String) {
        movieDescription.text = overview
    }

    private fun showCollection(constituent: Constituent)
            = collectionAdapter.populate(constituent)

    private fun viewSelectMovieFromCollection(part: Part) {
        setMovieTitle(part.title)
        setMovieDescription(part.overview)
        Picasso.with(this)
            .load(pathBuilder.buildPosterPath(part.posterPath))
            .into(movieBackdrop)
    }

    private fun showBadGatewayError() = setErrorViewState(R.string.bad_gateway_message)
    private fun showInternalError() = setErrorViewState(R.string.internal_error_message)
    private fun showBadRequestError() = setErrorViewState(R.string.bad_request_message)
    private fun showResourceNotFoundError() = setErrorViewState(R.string.not_found_error_message)
    private fun showUnauthorisedError() = setErrorViewState(R.string.unauthorized_error_message)
    private fun showResourceMovedError() = setErrorViewState(R.string.resource_moved_error_message)
    private fun showForbiddenNetworkError() = setErrorViewState(R.string.resource_forbidden_error_message)

    private fun setErrorViewState(@StringRes message: Int) {
        progressBar.gone()
        moviesList.gone()
        toast(String.format(getString(message)))
    }

    companion object {
        const val SPAN_COUNT = 4
        const val MOVIE_KEY = "MOVIE"
    }
}