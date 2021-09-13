package com.icanmobile.openflickrviewer.di

import android.app.Application
import com.icanmobile.openflickrviewer.manager.GlideManager
import com.icanmobile.openflickrviewer.manager.FakeGlideRequestManager
import com.icanmobile.openflickrviewer.util.JsonUtil
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestAppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(): GlideManager {
        return FakeGlideRequestManager()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideJsonUtil(application: Application): JsonUtil {
        return JsonUtil(application)
    }
}