package com.demo.movies.di

import com.demo.movies.ui.allmovies.AllMoviesActivity
import com.demo.movies.ui.moviedetails.MovieDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributesAllMoviesActivity(): AllMoviesActivity

    @ContributesAndroidInjector
    abstract fun contributesMovieDetailsActivity(): MovieDetailsActivity
}