package com.demo.movies.threading

import io.reactivex.Scheduler

interface SchedulerProvider {
  fun computation(): Scheduler
  fun io(): Scheduler
  fun ui(): Scheduler
}