package com.icanmobile.openflickrviewer.ui.detail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
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
import com.icanmobile.openflickrviewer.ui.BaseMainActivityTests
import com.icanmobile.openflickrviewer.ui.FakeFragmentFactory
import com.icanmobile.openflickrviewer.ui.UICommunicationListener
import com.icanmobile.openflickrviewer.ui.viewmodel.selectPhotoListItem
import com.icanmobile.openflickrviewer.util.Constants
import com.icanmobile.openflickrviewer.util.JsonUtil
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class PhotoDetailFragmentTest: BaseMainActivityTests() {

    @Inject lateinit var jsonUtil: JsonUtil
    @Inject lateinit var fragmentFactory: FakeFragmentFactory
    private val uiCommunicationListener = mockk<UICommunicationListener>()

    @Before
    fun init(){
        every { uiCommunicationListener.showStatusBar() } just runs
        every { uiCommunicationListener.expandAppBar() } just runs
        every { uiCommunicationListener.hideMenu() } just runs
    }

    @Test
    fun clickPhoto_InPhotoDetailFragment() {
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



        val scenario = launchFragmentInContainer<PhotoDetailFragment>(
            factory = fragmentFactory
        )

        val rawJson = jsonUtil.readJSONFromAsset(Constants.FLICKR_DATA_FILENAME)
        val flickrDto = Gson().fromJson<FlickrDTO>(
            rawJson,
            object : TypeToken<FlickrDTO>() {}.type
        )
        val selectedPhoto = flickrDto.toPhotoList()?.sortedByDescending{ it.id }[0]

        scenario.onFragment { fragment ->
            fragment.viewModel.selectPhotoListItem(selectedPhoto)
        }

        Espresso.onView(ViewMatchers.withId(R.id.photo_title))
            .check(ViewAssertions.matches(withText(selectedPhoto.title)))
    }

    override fun injectTest(application: TestOpenFlickrViewApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}