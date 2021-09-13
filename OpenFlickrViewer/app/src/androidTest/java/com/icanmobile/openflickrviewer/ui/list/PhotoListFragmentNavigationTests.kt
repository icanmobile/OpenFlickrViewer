package com.icanmobile.openflickrviewer.ui.list

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
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
import com.icanmobile.openflickrviewer.ui.FakeFragmentFactory
import com.icanmobile.openflickrviewer.ui.UICommunicationListener
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.EspressoIdlingResourceRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class PhotoListFragmentNavigationTests : BaseMainActivityTests(){

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject lateinit var fragmentFactory: FakeFragmentFactory
    private val uiCommunicationListener = mockk<UICommunicationListener>()

    @Before
    fun init(){
        every { uiCommunicationListener.showStatusBar() } just runs
        every { uiCommunicationListener.expandAppBar() } just runs
        every { uiCommunicationListener.showMenu() } just runs
    }

    @Test
    fun navigation_fromPhotoListFragment_toPhotoDetailFragment() {
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

        fragmentFactory.uiCommunicationListener = uiCommunicationListener

        val navController = TestNavHostController(app)
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.photoListFragment)



        val scenario = launchFragmentInContainer<PhotoListFragment>(
            factory = fragmentFactory
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        scenario.onFragment { fragment ->
            fragment.newSearch("dog")
        }

        val recyclerView = Espresso.onView(withId(R.id.recycler_view))
        recyclerView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(5)
        )

        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<PhotoListAdapter.PhotoViewHolder>(5, ViewActions.click())
        )

        assertEquals(navController.currentDestination?.id, R.id.photoDetailFragment)
    }

    override fun injectTest(application: TestOpenFlickrViewApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}