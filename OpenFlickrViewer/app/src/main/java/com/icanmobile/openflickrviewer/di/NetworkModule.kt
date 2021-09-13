package com.icanmobile.openflickrviewer.di

import android.app.Application
import android.content.Context
import com.icanmobile.openflickrviewer.repository.api.FlickrApi
import com.icanmobile.openflickrviewer.repository.api.interceptor.ApiKeyInterceptor
import com.icanmobile.openflickrviewer.repository.api.interceptor.ConnectivityInterceptor
import com.icanmobile.openflickrviewer.repository.api.interceptor.RetryOn429And503Interceptor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object NetworkModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideConnectivityInterceptor(application: Application): ConnectivityInterceptor {
        return ConnectivityInterceptor(application.applicationContext)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetryOn429And503Interceptor(): RetryOn429And503Interceptor {
        return RetryOn429And503Interceptor()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        connectivityInterceptor: ConnectivityInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor,
        retryOn429And503Interceptor: RetryOn429And503Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(retryOn429And503Interceptor)
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FlickrApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}