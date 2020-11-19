package com.demo.movies.testutils

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable

class RxSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { schedulerCallable: Callable<Scheduler?>? -> TrampolineScheduler.instance() }
                RxJavaPlugins.setIoSchedulerHandler { scheduler: Scheduler? -> TrampolineScheduler.instance() }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler: Scheduler? -> TrampolineScheduler.instance() }
                try {
                    base.evaluate()
                } finally {
                    RxAndroidPlugins.reset()
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}