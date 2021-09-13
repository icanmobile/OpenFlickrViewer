package com.icanmobile.openflickrviewer.repository.api.interceptor

import io.mockk.MockKAnnotations
import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RetryOn429And503InterceptorTest {
    private lateinit var server: MockWebServer
    private lateinit var retryOn429And503Interceptor: RetryOn429And503Interceptor

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        retryOn429And503Interceptor = RetryOn429And503Interceptor()
    }

    @After
    fun teardown() {
        if (!::server.isInitialized) {
            throw IllegalStateException("Use the instance variable")
        }
        server.shutdown()
    }

    @Test
    fun intercept_whenNothingInPref_ResponseSuccessful() {
        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        assertNotNull(request.headers)
    }

    @Test
    fun intercept_when429CodeTooManyRequest_retryAfter2seconds() {
        val mock429Response = MockResponse().setResponseCode(CODE_TOO_MANY_REQUESTS).addHeader(HEADER_RETRY_AFTER, 2)
        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock429Response, mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        assertNotNull(request.headers)
    }

    @Test
    fun intercept_when429CodeTooManyRequest_retryAfterDefaultSeconds() {
        val mock429Response = MockResponse().setResponseCode(CODE_TOO_MANY_REQUESTS)
        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock429Response, mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        assertNotNull(request.headers)
    }

    @Test
    fun intercept_when503CodeServiceUnavailable_retryAfter2seconds() {
        val mock503Response = MockResponse().setResponseCode(CODE_SERVICE_UNAVAILABLE).addHeader(HEADER_RETRY_AFTER, 2)
        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock503Response, mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        assertNotNull(request.headers)
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
            .addInterceptor(retryOn429And503Interceptor).build()
        okHttpClient.newCall(Request.Builder().url(server.url("/")).build()).execute()
    }

    companion object {
        private const val HEADER_RETRY_AFTER = "Retry-After"
        private const val CODE_TOO_MANY_REQUESTS = 429
        private const val CODE_SERVICE_UNAVAILABLE = 503
    }
}