package com.icanmobile.openflickrviewer.ui

import com.icanmobile.openflickrviewer.TestOpenFlickrViewApplication
import com.icanmobile.openflickrviewer.di.TestAppComponent
import com.icanmobile.openflickrviewer.repository.FakeFlickrRepositoryImpl
import com.icanmobile.openflickrviewer.repository.api.FakeFlickrApi
import com.icanmobile.openflickrviewer.repository.db.FakeFlickrDAO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseMainActivityTests {

    fun configureFakeFlickrApi(
        flickrDataSource: String? = null,
        networkDelay: Long? = null,
        application: TestOpenFlickrViewApplication
    ): FakeFlickrApi {
        val flickrApi = (application.appComponent as TestAppComponent).flickrApi
        flickrDataSource?.let { flickrApi.flickrJsonFileName = it }
        networkDelay?.let { flickrApi.networkDelay = it }
        return flickrApi
    }

    fun configureFakeFlickrDao(
        flickrDataSource: String? = null,
        application: TestOpenFlickrViewApplication
    ): FakeFlickrDAO {
        val flickrDao = (application.appComponent as TestAppComponent).flickrDao
        flickrDataSource?.let { flickrDao.flickrJsonFileName = it }
        return flickrDao
    }

    fun configureFakeRepository(
        flickrApi: FakeFlickrApi,
        flickrDao: FakeFlickrDAO,
        application: TestOpenFlickrViewApplication
    ): FakeFlickrRepositoryImpl {
        val flickrRepository = (application.appComponent as TestAppComponent).flickrRepository
        flickrRepository.flickrApi = flickrApi
        flickrRepository.flickrDao = flickrDao
        return flickrRepository
    }

    abstract fun injectTest(application: TestOpenFlickrViewApplication)
}