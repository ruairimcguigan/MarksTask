package com.demo.movies.threading

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class DefaultSchedulerProvider : SchedulerProvider {

  override fun computation() = Schedulers.computation()
  override fun io() = Schedulers.io()
  override fun ui() = AndroidSchedulers.mainThread()
}