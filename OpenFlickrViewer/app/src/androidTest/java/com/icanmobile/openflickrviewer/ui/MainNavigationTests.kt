package com.icanmobile.openflickrviewer.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.icanmobile.openflickrviewer.R
import com.icanmobile.openflickrviewer.TestOpenFlickrViewApplication
import com.icanmobile.openflickrviewer.di.TestAppComponent
import com.icanmobile.openflickrviewer.model.toPhotoList
import com.icanmobile.openflickrviewer.repository.api.FlickrDTO
import com.icanmobile.openflickrviewer.ui.list.PhotoListAdapter
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.EspressoIdlingResourceRule
import com.icanmobile.openflickrviewer.util.JsonUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
class MainNavigationTests : BaseMainActivityTests() {

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Test
    fun navigation_fragments(){
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


        val rawJson = jsonUtil.readJSONFromAsset(Constants.FLICKR_DATA_FILENAME)
        val flickrDto = Gson().fromJson<FlickrDTO>(
            rawJson,
            object : TypeToken<FlickrDTO>() {}.type
        )
        val selectedListIndex = 8 // chose 8 so the app has to scroll
        val selectedPhoto = flickrDto.toPhotoList()[selectedListIndex]



        val scenario = launchActivity<MainActivity>(getSearchIntent())

        Espresso.onView(withId(R.id.recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<PhotoListAdapter.PhotoViewHolder>(selectedListIndex)
        )

        // Nav DetailFragment
        Espresso.onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PhotoListAdapter.PhotoViewHolder>(
                selectedListIndex,
                ViewActions.click()
            )
        )

        Espresso.onView(withId(R.id.photo_title))
            .check(ViewAssertions.matches(withText(selectedPhoto.title)))

        // Nav PhotoFragment
        Espresso.onView(withId(R.id.photo_image)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.scaling_image_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Back to PhotoDetailFragment
        Espresso.pressBack()

        Espresso.onView(withId(R.id.photo_title))
            .check(ViewAssertions.matches(withText(selectedPhoto.title)))

        // Back to PhotoListFragment
        Espresso.pressBack()

        Espresso.onView(withId(R.id.recycler_view))
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