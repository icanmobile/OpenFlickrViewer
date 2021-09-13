package com.icanmobile.openflickrviewer.repository.api.interceptor

import com.icanmobile.openflickrviewer.repository.api.interceptor.ApiKeyInterceptor.Companion.API_KEY
import com.icanmobile.openflickrviewer.repository.api.interceptor.ApiKeyInterceptor.Companion.API_KEY_VALUE
import io.mockk.MockKAnnotations
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApiKeyInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var apiKeyInterceptor: ApiKeyInterceptor

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        apiKeyInterceptor = ApiKeyInterceptor()
    }

    @After
    fun teardown() {
        if (!::server.isInitialized) {
            throw IllegalStateException("Use the instance variable")
        }
        server.shutdown()
    }

    @Test
    fun intercept_addApiKey_ResponseSuccessful() {
        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        val apiKey = request.requestUrl.queryParameter(API_KEY)
        assertEquals(API_KEY_VALUE, apiKey)
    }

    private fun startServer(vararg mockResponses: MockResponse): MockWebServer {
        val server = MockWebServer()
        for (mockResponse in mockResponses) {
            server.enqueue(mockResponse)
        }
        server.start()
        return server
    }

    private fun makeNewCall() {
        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(apiKeyInterceptor).build()
        okHttpClient.newCall(Request.Builder().url(server.url("/")).build()).execute()
    }
}