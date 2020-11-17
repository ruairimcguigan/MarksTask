package com.demo.movies.robot

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    private var context = InstrumentationRegistry.getInstrumentation().context

    internal fun <T : BaseRobot> createRobotRunner(cls: KClass<T>) = { func: T.() -> Unit ->
        cls.createInstance().apply {
            func()
        }
    }

    internal fun getString(stringId: Int) = context.resources.getString(stringId)

//    @Before
//    open fun setup() {
//        IdlingRegistry.getInstance().register(
//            OkHttp3IdlingResource.create(
//                "okhttp",
//                OkHttpProvider.getOkHttpClient()
//            )
//        )
//    }

}