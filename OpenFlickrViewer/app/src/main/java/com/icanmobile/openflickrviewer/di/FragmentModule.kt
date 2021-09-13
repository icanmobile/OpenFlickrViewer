package com.icanmobile.openflickrviewer.di

import androidx.fragment.app.FragmentFactory
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.ui.FragmentFactoryImpl
import com.icanmobile.openflickrviewer.ui.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object FragmentModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainFragmentFactory(
        viewModelFactory: MainViewModelFactory,
        glideManager: GlideManager
    ): FragmentFactory {
        return FragmentFactoryImpl(viewModelFactory, glideManager)
    }
}