package com.icanmobile.openflickrviewer.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }

    fun clear(): Boolean {
        return if (!countingIdlingResource.isIdleNow) {
            decrement()
            false
        } else{
            true
        }
    }
}