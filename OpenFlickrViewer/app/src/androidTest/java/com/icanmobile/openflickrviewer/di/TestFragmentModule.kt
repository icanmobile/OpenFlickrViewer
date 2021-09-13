package com.icanmobile.openflickrviewer.di

import androidx.fragment.app.FragmentFactory
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.ui.FakeFragmentFactory
import com.icanmobile.openflickrviewer.ui.viewmodel.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestFragmentModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainFragmentFactory(
        viewModelFactory: FakeMainViewModelFactory,
        glideManager: GlideManager
    ): FragmentFactory {
        return FakeFragmentFactory(viewModelFactory, glideManager)
    }
}