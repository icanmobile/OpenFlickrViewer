package com.icanmobile.openflickrviewer.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class PhotoListFragmentIntegrationTests : BaseMainActivityTests() {

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun emptyList_InPhotoListFragment() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestOpenFlickrViewApplication
        val flickrApi = configureFakeFlickrApi(
            flickrDataSource = Constants.EMPTY_FLICKR_LIST, // empty list of data
            networkDelay = 0L,
            application = app
        )
        val flickrDao = configureFakeFlickrDao(
            flickrDataSource = Constants.EMPTY_FLICKR_LIST, // empty list of data
            application = app
        )
        configureFakeRepository(flickrApi, flickrDao, app)
        injectTest(app)



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        val recyclerView = Espresso.onView(withId(R.id.recycler_view))

        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.no_data_textview))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun checkListData_withScrolling_InPhotoListFragment() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestOpenFlickrViewApplication
        val flickrApi = configureFakeFlickrApi(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )
        val flickrDao = configureFakeFlickrDao(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            application = app
        )
        configureFakeRepository(flickrApi, flickrDao, app)
        injectTest(app)



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        val recyclerView = Espresso.onView(withId(R.id.recycler_view))

        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(0)
        )
        Espresso.onView(ViewMatchers.withText("DSCN2014"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(8)
        )
        Espresso.onView(ViewMatchers.withText("365-253 - Let sleeping dogs lie"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(0)
        )
        Espresso.onView(ViewMatchers.withText("DSCN2014"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.no_data_textview))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun isInstanceStateSavedAndRestored_OnDestroyActivity_InPhotoListFragment() {
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestOpenFlickrViewApplication
        val flickrApi = configureFakeFlickrApi(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )
        val flickrDao = configureFakeFlickrDao(
            flickrDataSource = Constants.FLICKR_DATA_FILENAME,
            application = app
        )
        configureFakeRepository(flickrApi, flickrDao, app)
        injectTest(app)



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        Espresso.onView(withId(R.id.recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(8)
        )

        Espresso.onView(ViewMatchers.withText("365-253 - Let sleeping dogs lie"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        scenario.recreate()

        Espresso.onView(ViewMatchers.withText("365-253 - Let sleeping dogs lie"))
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

    override fun injectTest(application: TestOpenFlickrViewApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}