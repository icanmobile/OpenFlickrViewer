package com.icanmobile.openflickrviewer.di

import android.app.Application
import com.bumptech.glide.Glide
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.manager.GlideManagerImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object GlideModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(
        application: Application
    ): GlideManager {
        return GlideManagerImpl(
            Glide.with(application)
        )
    }
}