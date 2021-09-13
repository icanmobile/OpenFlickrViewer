package com.icanmobile.openflickrviewer.di

import android.app.Application
import com.icanmobile.openflickrviewer.repository.FakeFlickrRepositoryImpl
import com.icanmobile.openflickrviewer.repository.api.FakeFlickrApi
import com.icanmobile.openflickrviewer.repository.db.FakeFlickrDAO
import com.icanmobile.openflickrviewer.ui.MainNavigationTests
import com.icanmobile.openflickrviewer.ui.detail.PhotoDetailFragmentTest
import com.icanmobile.openflickrviewer.ui.list.PhotoListFragmentErrorTests
import com.icanmobile.openflickrviewer.ui.list.PhotoListFragmentIntegrationTests
import com.icanmobile.openflickrviewer.ui.list.PhotoListFragmentNavigationTests
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(modules = [
    TestFragmentModule::class,
    TestViewModelModule::class,
    TestAppModule::class
])
interface TestAppComponent: AppComponent {

    val flickrApi: FakeFlickrApi
    val flickrDao: FakeFlickrDAO
    val flickrRepository: FakeFlickrRepositoryImpl

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): TestAppComponent
    }

    fun inject(mainNavigationTests: MainNavigationTests)

    fun inject(listFragmentIntegrationTests: PhotoListFragmentIntegrationTests)

    fun inject(listFragmentNavigationTests: PhotoListFragmentNavigationTests)

    fun inject(listFragmentErrorTests: PhotoListFragmentErrorTests)

    fun inject(detailFragmentTest: PhotoDetailFragmentTest)
}