package com.example.battleshipmobile.utils

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 *
 */
fun <A: Activity, T> ActivityScenario<A>.getFromActivity(supplier: Activity.() -> T): T{
    val countDownLatch = CountDownLatch(1)
    var result: T? = null

    onActivity { activity ->
        result = activity.supplier()
        countDownLatch.countDown()
    }

    countDownLatch.await(1, TimeUnit.SECONDS)
    return result ?: error("Result was not initialized")
}