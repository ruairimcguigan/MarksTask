package com.demo.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.movies.ui.allmovies.AllMoviesViewModel
import com.demo.movies.ui.moviedetails.MovieDetailsViewModel
import com.demo.movies.ui.viewmodel.ViewModelFactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AllMoviesViewModel::class)
    abstract fun bindAllMoviesViewModel(viewModel: AllMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsModel(viewmodel: MovieDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}