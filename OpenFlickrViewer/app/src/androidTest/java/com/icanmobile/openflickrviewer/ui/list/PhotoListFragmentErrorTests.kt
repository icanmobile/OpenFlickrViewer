package com.icanmobile.openflickrviewer.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.TestOpenFlickrViewApplication
import com.icanmobile.openflickrviewer.di.TestAppComponent
import com.icanmobile.openflickrviewer.ui.BaseMainActivityTests
import com.icanmobile.openflickrviewer.ui.MainActivity
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.EspressoIdlingResourceRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class PhotoListFragmentErrorTests: BaseMainActivityTests() {

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun showErrorDialog_withUnknownError_InPhotoListFragment() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestOpenFlickrViewApplication
        val flickrApi = configureFakeFlickrApi(
            flickrDataSource = Constants.SERVER_ERROR_FILENAME,
            networkDelay = 0L,
            application = app
        )
        val flickrDao = configureFakeFlickrDao(
            flickrDataSource = Constants.SERVER_ERROR_FILENAME,
            application = app
        )
        configureFakeRepository(flickrApi, flickrDao, app)
        injectTest(app)



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        Espresso.onView(withText(R.string.text_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withSubstring(Constants.UNKNOWN_ERROR))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showErrorDialog_withNetworkTimeout_InPhotoListFragment() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestOpenFlickrViewApplication
        val flickrApi = configureFakeFlickrApi(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            networkDelay = 11000L,
            application = app
        )
        val flickrDao = configureFakeFlickrDao(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            application = app
        )
        configureFakeRepository(flickrApi, flickrDao, app)
        injectTest(app)



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        Espresso.onView(withText(R.string.text_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withSubstring(Constants.NETWORK_ERROR_TIMEOUT))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    private fun getSearchIntent(): Intent {
        val searchIntent = Intent(
            ApplicationProvider.getApplicationContext<Context>(),
            MainActivity::class.java
        )
        searchIntent.putExtra(SearchManager.QUERY, "dog")
        searchIntent.action = Intent.ACTION_SEARCH
        return searchIntent
    }

    override fun injectTest(application: TestOpenFlickrViewApplication){
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}