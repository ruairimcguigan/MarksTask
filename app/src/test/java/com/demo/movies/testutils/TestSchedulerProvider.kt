package com.demo.movies.testutils

import com.demo.movies.threading.DefaultSchedulerProvider
import io.reactivex.schedulers.Schedulers.trampoline

class TestSchedulerProvider : DefaultSchedulerProvider() {
    override fun computation() = trampoline()
    override fun io() = trampoline()
    override fun ui() = trampoline()
}