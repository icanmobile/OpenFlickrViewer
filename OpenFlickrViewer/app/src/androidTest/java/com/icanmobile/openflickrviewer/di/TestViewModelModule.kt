package com.icanmobile.openflickrviewer.di

import androidx.lifecycle.ViewModelProvider
import com.icanmobile.openflickrviewer.repository.FakeFlickrRepositoryImpl
import com.icanmobile.openflickrviewer.ui.viewmodel.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestViewModelModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideViewModelFactory(
        flickrRepository: FakeFlickrRepositoryImpl
    ): ViewModelProvider.Factory{
        return FakeMainViewModelFactory(flickrRepository)
    }
}