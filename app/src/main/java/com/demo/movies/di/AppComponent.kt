package com.demo.movies.di

import android.app.Application
import com.demo.movies.MoviesApp
import com.demo.movies.api.ApiModule
import com.demo.movies.network.NetworkModule
import com.demo.movies.repo.RepoModule
import com.demo.movies.ui.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        ApiModule::class,
        NetworkModule::class,
        RepoModule::class
    ]
)
interface AppComponent : AndroidInjector<MoviesApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}