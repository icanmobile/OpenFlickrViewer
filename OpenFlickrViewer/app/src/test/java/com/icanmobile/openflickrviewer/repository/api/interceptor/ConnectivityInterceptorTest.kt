package com.icanmobile.openflickrviewer.repository.api.interceptor

import android.content.Context
import com.icanmobile.openflickrviewer.util.NetworkUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ConnectivityInterceptorTest {
    private lateinit var server: MockWebServer
    private lateinit var connectivityInterceptor: ConnectivityInterceptor

    @MockK lateinit var context: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        connectivityInterceptor = ConnectivityInterceptor(context)
    }

    @After
    fun teardown() {
        if (!::server.isInitialized) {
            throw IllegalStateException("Use the instance variable")
        }
        server.shutdown()
    }

    @Test
    fun interceptor_networkConnected_ResponseSuccess() {
        mockkObject(NetworkUtil)
        every { NetworkUtil.isInternetConnected(any()) } returns true

        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock200Response)
        makeNewCall()

        val request: RecordedRequest = server.takeRequest()
        assertNotNull(request)
    }

    @Test(expected = IOException::class)
    fun interceptor_noNetworkConnected_throwIoException() {
        mockkObject(NetworkUtil)
        every { NetworkUtil.isInternetConnected(any()) } returns false

        val mock200Response = MockResponse().setResponseCode(200)
        server = startServer(mock200Response)
        makeNewCall()
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
            .addInterceptor(connectivityInterceptor).build()
        okHttpClient.newCall(Request.Builder().url(server.url("/")).build()).execute()
    }
}