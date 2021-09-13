package com.icanmobile.openflickrviewer.repository.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter(API_KEY, API_KEY_VALUE)
            .build()

        return chain.proceed(chain.request().newBuilder().url(newUrl).build())
    }

    companion object {
        const val API_KEY = "api_key"
        const val API_KEY_VALUE = "c7be4672be4d4377db6b2ac262593a60"
    }
}