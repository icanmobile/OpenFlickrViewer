package com.icanmobile.openflickrviewer.util

import android.util.Log
import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class EspressoIdlingResourceRule : TestWatcher(){

    private val idlingResource = EspressoIdlingResource.countingIdlingResource

    override fun finished(description: Description?) {
        Log.d("EspressoIdlingResourceRule", "FINISHED")
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }

    override fun starting(description: Description?) {
        Log.d("EspressoIdlingResourceRule", "STARTING")
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }
}
