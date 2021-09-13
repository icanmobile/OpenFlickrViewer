package com.icanmobile.openflickrviewer.di

import com.icanmobile.openflickrviewer.repository.FlickrRepository
import com.icanmobile.openflickrviewer.repository.FlickrRepositoryImpl
import com.icanmobile.openflickrviewer.repository.api.FlickrApi
import com.icanmobile.openflickrviewer.repository.db.FlickrDAO
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RepositoryModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideFlickrApi(retrofit: Retrofit): FlickrApi {
        return retrofit.create(FlickrApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFlickrRepository(flickrApi: FlickrApi, flickrDao: FlickrDAO): FlickrRepository {
        return FlickrRepositoryImpl(flickrApi, flickrDao)
    }
}